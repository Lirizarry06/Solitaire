import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

/* Program: StockPile.java
 * Date: 21 April 2022
 * Desc: This class represents the extra cards in the top-left, which can be cycled through and placed at any time. 
 */

public class StockPile extends ArrayList <Card>{
	
	private static final long serialVersionUID = -3773057250436319588L;
	public ArrayList <Card> discardPile;
	public Polygon stockSelectableArea;
	public Polygon discardSelectableArea;

	public StockPile(ArrayList<Card> deck) {
		
		//randomly fill stock pile
		Random r = new Random();
		while (!deck.isEmpty()) {
			int index = r.nextInt(deck.size());
			add(deck.get(index));
			deck.remove(index);
		}
		
		//initialize locations of stock pile cards
		for (Card c : this) {
			c.location = new Point(25,25);
		}
		
		//initialize empty discard pile
		discardPile = new ArrayList<Card>();
		
		//create selectable areas
		int[] xCoords = new int[] {25,100,100,25};
		int[] yCoords = new int[] {25,25,125,125};
		stockSelectableArea = new Polygon(xCoords,yCoords,4);
		for (int i = 0; i < 4; i++) {
			xCoords[i] += 100;
		}
		discardSelectableArea = new Polygon(xCoords,yCoords,4);
	}	
	
	//moves one card from the stock pile to the discard pile, and resets cycle if stock pile is empty
	public void cycleCard() {
		if (!isEmpty()) {
			discardPile.add(get(size()-1));
			remove(size()-1);
			discardPile.get(discardPile.size() - 1).faceUp = true;
			discardPile.get(discardPile.size() - 1).location = new Point(125,25);
		} else {
			reset();
		}
	}
	
	//moves all cards from discard pile back into stock pile
	public void reset() {
		for (int i = discardPile.size() - 1; i >= 0; i--) {
			add(discardPile.get(i));
		}
		discardPile.clear();
		for (Card c : this) {
			c.faceUp = false;
			c.location = new Point(25,25);
		}
	}
	
	//removes and returns the last card in the discard pile
	public ArrayList<Card> removeLast(){
		if (!discardPile.isEmpty()) {
			ArrayList<Card> result = new ArrayList<Card>();
			result.add(discardPile.get(discardPile.size() - 1));
			discardPile.remove(discardPile.size() - 1);
			return result;
		}
		return null;
	}
	
	//adds cards from a selected cards object back into discard pile for snap-back behavior
	public void replaceCards(SelectedCards stack, int[]origIndex) {
		discardPile.add(stack.get(0));
		discardPile.get(discardPile.size() - 1).location = new Point(125,25);
	}
}
    		
    	