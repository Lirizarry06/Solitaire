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
	private ArrayList<Card> deck;		//List of all cards. Used only for random selection/distribution
	private Tableau tableau;			//Main game area in which columns of cards are moved around
	private ArrayList<Card> stockPile = new ArrayList<Card>(); 	//extra cards located in the top-left
	private SelectedCards selected = new SelectedCards();		//currently selected cards
	
	public SolitairePanel() {
		newGame();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	//creates a random tableau and stock pile and an empty ace pile.
	public void newGame() {
		
		//create 52-card deck
		deck = new ArrayList<Card>();
		for (int i = 1; i <= 13; i++) {
			deck.add(new Card(Card.suits.CLUB, i));
			deck.add(new Card(Card.suits.SPADE, i));
			deck.add(new Card(Card.suits.HEART, i));
			deck.add(new Card(Card.suits.DIAMOND, i));
		}
		
		//create random tableau using deck
		tableau = new Tableau(deck);
		
		//randomly add remaining cards into stock pile
		Random r = new Random();
		stockPile = new ArrayList<Card>();
		while (!deck.isEmpty()) {
			int index = r.nextInt(deck.size());
			stockPile.add(deck.get(index));
			deck.remove(index);
		}
		selected.clear();
		repaint();
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
		
		//draw stock pile
		if (!stockPile.isEmpty()) {
			try {
				g.drawImage(stockPile.get(stockPile.size() - 1).getImage(), 25, 25, this);
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
		}
		
		//pick up stock pile card if valid stock pile location selected
	}

	//Mouse release events (drop cards)
	public void mouseReleased(MouseEvent e) {
		Point mouseLocation = new Point(e.getX(), e.getY());
		
		if (!selected.isEmpty()) {
			
			//check if dropped in tableau
			int[] tableauIndex = tableau.getIndexAtPoint(mouseLocation);
			if (tableauIndex != null) {
				if (tableau.addCards(selected, tableauIndex[0], tableauIndex[1])) {
					tableau.flipLast(selected.origIndex[0]);
				}
			}
			tableau.replaceCards(selected, selected.origIndex[0]);
			
			//check if dropped in ace piles
			
			selected.releaseCards();
			repaint();
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