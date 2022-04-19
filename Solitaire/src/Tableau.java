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
	public int[] getIndexAtPoint(Point p) {
		for (int i = 0; i < 7; i++) {
			//if column is empty, check if marker area was clicked
			if (get(i).size() == 0) {
				int[] xCoords = {25 + (100 * i), 100 + (100 * i), 100 + (100 * i), 25 + (100 * i)};
				int[] yCoords = {150, 150, 250, 250};
				Polygon markerArea = new Polygon(xCoords, yCoords, 4);
				if (markerArea.contains(p)) {
					int[] result = {i,0};
					return result;
				}
			//if column is not empty
			} else {
				for (int j = get(i).size() - 1; j >= 0; j--) {
					Card c = get(i).get(j);
					if (c.faceUp) {
						if (c.area.contains(p)) {
							int[] result = {i,j};
							return result;
						}
					}
				}
			}
		}
		return null;
	}
	
	//adds selected cards to given column if move is legal, and clear SelectedCards
	//returns true if successful, false if not.
	public boolean addCards(SelectedCards stack, int colNum, int index) {
		Card stackCard = stack.get(0);				//bottom-most card in stack to be moved
		ArrayList<Card> col = get(colNum);
		
		//if column is not currently empty
		if (col.size() > 0) {
			Card colCard = col.get(col.size() - 1);	//topmost card on destination column
			//check legality before moving stack onto destination column and setting new locations
			if (colCard.isRed() != stackCard.isRed() && colCard.getNumber() == stackCard.getNumber() + 1) {
				col.addAll(stack);
				for (int i = 0; i < col.size(); i++) {
					col.get(i).setLocation(new Point(25 + (100 * colNum), 150 + (30 * i)));
				}
				stack.clear();
				return true;
			}
		//column is currently empty (only a king can start this column)
		} else if (stackCard.getNumber() == 13) {
			col.addAll(stack);
			for (int i = 0; i < col.size(); i++) {
				col.get(i).setLocation(new Point(25 + (100 * colNum), 150 + (30 * i)));
			}
			stack.clear();
			return true;
		}
		return false;
	}
	
	public void replaceCards(SelectedCards stack, int colNum) {
		ArrayList<Card> col = get(colNum); 
		col.addAll(stack);
		for (int i = 0; i < col.size(); i++) {
			col.get(i).setLocation(new Point(25 + (100 * colNum), 150 + (30 * i)));
		}
	}
	
	//removes and returns a stack of cards from the given column, starting with the given card
	public ArrayList<Card> removeCards(int colNum, int index) {
		ArrayList<Card> col = get(colNum);
		ArrayList<Card> returnStack = new ArrayList<Card>();
		while (col.size() - 1 >= index) {
			returnStack.add(col.get(index));
			col.remove(index);
		}
		return returnStack;
	}
	
	public void flipLast (int colNum) {
		if (get(colNum).size() != 0) {
			get(colNum).get(get(colNum).size() - 1).faceUp = true;
		}
	}
}
