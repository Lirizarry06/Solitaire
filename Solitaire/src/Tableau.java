import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Program: Tableau.java
 * Date: 16 April 2022
 * Desc: This class represents the main game area containing columns of cards.
 */

public class Tableau extends ArrayList<ArrayList<Card>> {
	
	private static final long serialVersionUID = -2141294414506828145L;

	//pre: The passed deck contains at least 28 cards
	//post: creates a randomly generated tableau from the passed deck of cards
	public Tableau(ArrayList<Card> deck) {
		
		//create columns
		for (int i = 0; i < 7; i++) {
			this.add(new ArrayList<Card>());
		}
		
		//randomly add cards from deck into columns
		Random r = new Random();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < i + 1; j++) {
				int index = r.nextInt(deck.size());
				
				//set location of card based on its index, before adding to column
				deck.get(index).setLocation(new Point(25 + (100 * i), 150 + (30 * j)));
				get(i).add(deck.get(index));
				deck.remove(index);
			}
			//flip over topmost card in each column
			get(i).get(get(i).size() - 1).flipOver();
		}
	}
	
	//returns the index and column number of the topmost card that contains the given Point
	//(or of the empty column if Point is within empty column marker)
	public int[] selectIndex(Point p) {
		for (int i = 0; i < 7; i++) {
			int[] xCoords = {25 + (100 * i), 100 + (100 * i), 100 + (100 * i), 25 + (100 * i)};
			int[] yCoords = {150, 150, 250, 250};
			Polygon cardArea = new Polygon(xCoords, yCoords, 4);
			//if column is empty
			if (get(i).size() == 0) {
				if (cardArea.contains(p)) {
					int[] result = {i,0};
					return result;
				}
			//if column is not empty
			} else {
				for (int j = get(i).size() - 1; j >= 0; j--) {
					Card c = get(i).get(j);
					cardArea = new Polygon(new int[] {c.getLocation().x, c.getLocation().x + 75, c.getLocation().x + 75, c.getLocation().x},
							new int[] {c.getLocation().y, c.getLocation().y, c.getLocation().y + 100, c.getLocation().y + 100}, 4);
					if (c.faceUp) {
						if (cardArea.contains(p)) {
							int[] result = {i,j};
							return result;
						}
					}
				}
			}
		}
		return null;
	}
	
	//gets the stack of cards starting at the given indexes
	public List<Card> getStack(int[] indexes) {
		if (indexes == null) {
			return null;
		} else {
			return get(indexes[0]).subList(indexes[1], get(indexes[0]).size());
		}
	}
	
	//adds a stack of cards to given column if move is legal
	public void addCards(int colNum, int index, int destColNum) {
		ArrayList<Card> destCol = get(destColNum);			//destination column
		List<Card> stack = getStack(new int[] {colNum, index});
		Card bottomStackCard = stack.get(0);				//bottom-most card in stack to be moved
		
		//if column is not currently empty
		if (destCol.size() > 0) {
			Card destColCard = destCol.get(destCol.size() - 1);	//topmost card on destination column
			//check legality before moving stack onto destination column and setting new locations
			if (destColCard.isRed() != bottomStackCard.isRed() && destColCard.getNumber() == bottomStackCard.getNumber() + 1) {
				destCol.addAll(stack);
				for (int i = 0; i < destCol.size(); i++) {
					destCol.get(i).setLocation(new Point(25 + (100 * destColNum), 150 + (30 * i)));
				}
				removeCards(colNum, index);
			}
		//column is currently empty (only a king can start this column)
		} else if (bottomStackCard.getNumber() == 13) {
			destCol.addAll(stack);
			for (int i = 0; i < destCol.size(); i++) {
				destCol.get(i).setLocation(new Point(25 + (100 * destColNum), 150 + (30 * i)));
			}
			removeCards(colNum, index);
		}
	}
	
	//removes a stack of cards from the given column, starting with the given card
	public void removeCards(int colNum, int index) {
		ArrayList<Card> col = get(colNum);
		while (col.size() - 1 >= index) {
			col.remove(col.size() - 1);
		}
		if (col.size() != 0 && !col.get(col.size() - 1).faceUp) {
			col.get(col.size() - 1).flipOver();
		}
	}
}
