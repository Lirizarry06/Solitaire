import java.awt.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

/* Program: SolitairePanel.java
 * Date: 8 April 2022
 * Desc: This file sets up the game panel component of the Solitaire program.
 */

public class SolitairePanel extends JPanel {
	private static final long serialVersionUID = 2442334202593446089L;
	public static final int PANEL_WIDTH = 750;
	public static final int PANEL_HEIGHT = 600;
	private ArrayList<Card> deck;		//List of all cards. Used only for random selection/distribution
	private ArrayList<ArrayList<Card>> columns;	//columns used for the game, represented as a 2D ArrayList.
	private ArrayList<Card> stockPile = new ArrayList<Card>(); 	//extra cards located in the top-left
	
	public SolitairePanel() {
		newGame();
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
		
		//create columns
		columns = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < 7; i++) {
			columns.add(new ArrayList<Card>());
		}
		
		//randomly add cards from deck into columns
		Random r = new Random();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < i + 1; j++) {
				int index = r.nextInt(deck.size());
				
				//set location of card based on its index, before adding to column
				deck.get(index).setLocation(new Point(25 + (100 * i), 150 + (30 * j)));
				columns.get(i).add(deck.get(index));
				deck.remove(index);
			}
			//flip over topmost card in each column
			columns.get(i).get(columns.get(i).size() - 1).flipOver();
		}
		
		//randomly add remaining cards into stock pile
		stockPile = new ArrayList<Card>();
		while (!deck.isEmpty()) {
			int index = r.nextInt(deck.size());
			stockPile.add(deck.get(index));
			deck.remove(index);
		}
		
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
		
		//draw cards in columns
		for (ArrayList<Card> col: columns) {
			for (int i = 0; i < col.size(); i++) {
				Card c = col.get(i);
				try {
					g.drawImage(c.getImage(), c.getLocation().x, c.getLocation().y, this);
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
		
	}
}
