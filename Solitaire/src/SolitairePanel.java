import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

/* Program: SolitairePanel.java
 * Date: 8 April 2022
 * Desc: This file sets up the game panel component of the Solitaire program.
 */

public class SolitairePanel extends JPanel {
	private static final long serialVersionUID = 2442334202593446089L;
	public static final int PANEL_WIDTH = 1000;
	public static final int PANEL_HEIGHT = 400;
	private ArrayList<Card> deck;		//List of all cards. Used only for random selection/distribution
	private ArrayList<ArrayList<Card>> columns;	//columns used for the game, represented as a 2D ArrayList.
	
	public SolitairePanel() {
		this.setBackground(Color.GREEN);
		
		//create 52 cards in deck, one of every number for each suit.
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
		for (int i = 1; i < 7; i++) {
			for (int j = i; j > 0; j--) {
				int index = r.nextInt(0,deck.size());
				
				//set location of card based on its index, before adding to column
				deck.get(index).setLocation(new Point(0 + (170 * i), 50 + (50 * j)));
				
				columns.get(i).add(deck.get(index));
				deck.remove(index);
				
			}
			columns.get(i).get(0).flipOver();
		}
		
		//randomly add remaining cards into stock pile
		ArrayList<Card> stockPile = new ArrayList<Card>();
		while (!deck.isEmpty()) {
			int index = r.nextInt(0,deck.size());
			stockPile.add(deck.get(index));
			deck.remove(index);
		}
		
		//create ace piles
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//draw cards in columns
		for (ArrayList<Card> col: columns) {
			for (int i = col.size() - 1; i >= 0; i--) {
				Card c = col.get(i);
				try {
					g.drawImage(c.getImage(), c.getLocation().x, c.getLocation().y, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//draw stock pile
		
		//draw ace piles
	}
}
