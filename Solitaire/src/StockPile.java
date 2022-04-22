/* use an array list for stockpile and discardpile */
/* use field class for deck */
/* for discardPile use a list */
/*Connect stockpile to discardpile and tableau */
/* only change the stockpile and discardpile */
/* do not overcomplicate/make it simple*/
/* do not need external deck*/
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class StockPile extends ArrayList <Card>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3773057250436319588L;
	public ArrayList <Card> discardPile;
	public Polygon stockSelectableArea;
	public Polygon discardSelectableArea;

	public StockPile(ArrayList<Card> deck) {
//randomly fill stockpile
		Random r = new Random();
		while (!deck.isEmpty()) {
			int index = r.nextInt(deck.size());
			add(deck.get(index));
			deck.remove(index);
		}
		discardPile = new ArrayList<Card>();
		for (Card c : this) {
			c.location = new Point(25,25);
		}
//create selectable areas
		int[] xCoords = new int[] {25,100,100,25};
		int[] yCoords = new int[] {25,25,125,125};
		stockSelectableArea = new Polygon(xCoords,yCoords,4);
		for (int i = 0; i < 4; i++) {
			xCoords[i] += 100;
		}
		discardSelectableArea = new Polygon(xCoords,yCoords,4);
	}	
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
	public void reset() {
		addAll(discardPile);
		discardPile.clear();
		for (Card c : this) {
			c.faceUp = false;
			c.location = new Point(25,25);
		}
	}
	public ArrayList<Card> removeLast(){
		if (!discardPile.isEmpty()) {
			ArrayList<Card> result = new ArrayList<Card>();
			result.add(discardPile.get(discardPile.size() - 1));
			discardPile.remove(discardPile.size() - 1);
			return result;
		}
		return null;
	}
	public void replaceCards(SelectedCards stack, int[]origIndex) {
		discardPile.add(stack.get(0));
		discardPile.get(discardPile.size() - 1).location = new Point(125,25);
	}
}
    		
    	