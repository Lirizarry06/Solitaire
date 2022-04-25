import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

/* Program: SolitairePanel.java
 * Date: 8 April 2022
 * Desc: This file sets up the game panel component of the Solitaire program.
 */

public class SolitairePanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 2442334202593446089L;
	public static final int PANEL_WIDTH = 750;
	public static final int PANEL_HEIGHT = 600;
	private Tableau tableau;			//main game area in which columns of cards are moved around
	private StockPile stockPile; 		//extra cards located in the top-left
	private AcePiles acePiles; 			//piles in top-right that build up from ace to king
	private SelectedCards selected;		//currently selected cards
	public HistoryList history;			//keeps track of moves for undo/redo
	
	public SolitairePanel() {
		newGame();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	//creates a random tableau and stock pile and an empty ace pile.
	public void newGame() {
		
		//create 52-card deck to be used for random distribution
		ArrayList<Card> deck = new ArrayList<Card>();
		for (int i = 1; i <= 13; i++) {
			deck.add(new Card(Card.suits.CLUB, i));
			deck.add(new Card(Card.suits.SPADE, i));
			deck.add(new Card(Card.suits.HEART, i));
			deck.add(new Card(Card.suits.DIAMOND, i));
		}
		
		//initialize all game components
		tableau = new Tableau(deck);
		stockPile = new StockPile(deck);
		acePiles = new AcePiles();
		history = new HistoryList(tableau, acePiles, stockPile);
		selected = new SelectedCards();
	}
	
	@Override
	//renders everything in the panel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//draw empty column markers
		g.setColor(Color.DARK_GRAY);
		for (int i = 0; i < 7; i++) {
			g.drawRoundRect(25 + (100 * i), 150, 74, 99, 5, 5);
		}
		
		//draw tableau
		for (ArrayList<Card> col: tableau) {
			for (int i = 0; i < col.size(); i++) {
				Card c = col.get(i);
				try {
					g.drawImage(c.getImage(), c.location.x, c.location.y, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//draw stock pile, or stock pile reset icon if stock pile is empty
		if (!stockPile.isEmpty()) {
			try {
				g.drawImage(stockPile.get(stockPile.size() - 1).getImage(), 25, 25, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			g.setColor(Color.WHITE);
			g.fillOval(37, 50, 50, 50);
			g.setColor(Color.GREEN);
			g.fillOval(45, 57, 35, 35);
		}
		
		//draw discardPile
		if (!stockPile.discardPile.isEmpty()) {
			try {
				g.drawImage(stockPile.discardPile.get(stockPile.discardPile.size() - 1).getImage(), 125, 25, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//draw empty ace pile markers
		g.setColor(Color.DARK_GRAY);
		for (int i = 0; i < 4; i++) {
			g.drawRoundRect(325 + (100 * i), 25, 74, 99, 5, 5);
		}
		
		//draw ace piles
		for (int i = 0; i < 4; i++) {
			if (!acePiles.get(i).isEmpty()) {
				ArrayList<Card> col = acePiles.get(i);
				Card c = col.get(col.size() - 1);
				try {
					g.drawImage(c.getImage(), c.location.x, c.location.y, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
		//draw selected cards
		if (!selected.isEmpty()) {
			for (int i = 0; i < selected.size(); i++) {
				Card c = selected.get(i);
				try {
					g.drawImage(c.getImage(), c.location.x, c.location.y, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Mouse press events (select cards)
	public void mousePressed(MouseEvent e) {
		Point mouseLocation = new Point(e.getX(), e.getY());
		
		//pick up tableau cards if valid tableau location selected
		int[] tableauIndex = tableau.getIndexAtPoint(mouseLocation);
		if (tableauIndex != null && !tableau.get(tableauIndex[0]).isEmpty()) {
			selected.pickUpCards(tableau.removeCards(tableauIndex[0], tableauIndex[1]), tableauIndex);
			selected.deltaX = mouseLocation.x - selected.get(0).location.x;
			selected.deltaY = mouseLocation.y - selected.get(0).location.y;
			selected.fromTableau = true;
		}
		
		//pick up discard pile card if valid discard pile selected
		if (stockPile.discardSelectableArea.contains(mouseLocation) && !stockPile.discardPile.isEmpty()){
			selected.pickUpCards(stockPile.removeLast(), new int[] {Math.max(0,stockPile.discardPile.size() - 1)});
			selected.deltaX = mouseLocation.x - selected.get(0).location.x;
			selected.deltaY = mouseLocation.y - selected.get(0).location.y;
			selected.fromTableau = false;
		}
		
		//cycle stock pile cards if stock pile selected
		if (stockPile.stockSelectableArea.contains(mouseLocation)){
			stockPile.cycleCard();
		}
				
		repaint();
	}

	//Mouse release events (drop cards)
	public void mouseReleased(MouseEvent e) {
		Point mouseLocation = new Point(e.getX(), e.getY());
		
		if (!selected.isEmpty()) {
			
			int[] tableauIndex = tableau.getIndexAtPoint(mouseLocation);
			int[] acePilesIndex = acePiles.getIndexAtPoint(mouseLocation);
			
			//check if dropped in tableau
			if (tableauIndex != null) {
				if (tableau.addCards(selected, tableauIndex[0], tableauIndex[1])) {
					//create history entry
					if (selected.fromTableau == true && selected.origIndex != tableauIndex) {
						if (tableau.flipLast(selected.origIndex[0])) {
							history.add(new HistoryEntry(selected.origIndex, HistoryEntry.Container.TABLEAU, 
									new int[] {tableauIndex[0], tableauIndex[1] + 1}, HistoryEntry.Container.TABLEAU, true));
						} else {
							history.add(new HistoryEntry(selected.origIndex, HistoryEntry.Container.TABLEAU, 
									new int[] {tableauIndex[0], tableauIndex[1] + 1}, HistoryEntry.Container.TABLEAU));
						}
						
					} else {
						history.add(new HistoryEntry(selected.origIndex, HistoryEntry.Container.STOCKPILE,
								new int[] {tableauIndex[0], tableauIndex[1] + 1}, HistoryEntry.Container.TABLEAU));
					}
				//non-valid tableau drop location selected
				} else if (selected.fromTableau == true) {	
					tableau.replaceCards(selected, selected.origIndex[0]);
				} else {
					stockPile.replaceCards(selected, selected.origIndex);
				}
			
			//check if dropped in ace piles
			} else if (acePilesIndex != null) {
				if (acePiles.addCards(selected, acePilesIndex[0])) {
					//create history entry
					if (selected.fromTableau == true) {
						if (tableau.flipLast(selected.origIndex[0])) {
							history.add(new HistoryEntry(selected.origIndex, HistoryEntry.Container.TABLEAU, 
									acePilesIndex, HistoryEntry.Container.ACEPILE, true));
						} else {
							history.add(new HistoryEntry(selected.origIndex, HistoryEntry.Container.TABLEAU, 
									acePilesIndex, HistoryEntry.Container.ACEPILE));
						}
					} else {
						history.add(new HistoryEntry(selected.origIndex, HistoryEntry.Container.STOCKPILE,
								acePilesIndex, HistoryEntry.Container.ACEPILE));
					}
				//non-valid ace pile drop location selected
				} else if (selected.fromTableau == true) {	
					tableau.replaceCards(selected, selected.origIndex[0]);
				} else {
					stockPile.replaceCards(selected, selected.origIndex);
				}
			
			//non-valid, empty drop location selected
			} else if (selected.fromTableau == true) {	
				tableau.replaceCards(selected, selected.origIndex[0]);
			} else {
				stockPile.replaceCards(selected, selected.origIndex);
			}
			
			selected.releaseCards();
			repaint();
			
			//display message if game is solved
			if (acePiles.checkForSolved()) {
				JOptionPane.showMessageDialog(this, "Congratulations, you won!");
			}
		}
	}
	
	//Mouse dragged events (animate card movement)
	public void mouseDragged(MouseEvent e) {
		Point mouseLocation = new Point(e.getX(), e.getY());
		if (!selected.isEmpty()) {
			selected.updateLocations(mouseLocation);
			repaint();
		}
	}
	
	//unused methods of implemented interfaces
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}
