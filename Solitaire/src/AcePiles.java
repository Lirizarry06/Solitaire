import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/* Program: AcePiles.java
   Date: April 18, 2022
   Desc: This class represents the piles in the top-left that must be filled to win the game.
*/

public class AcePiles extends ArrayList<ArrayList<Card>> {
	
	private static final long serialVersionUID = 1260372991802134488L; 
	
	public AcePiles() {
		//create columns
		 for (int i = 0; i < 4; i++){
				this.add(new ArrayList<Card>());	
		 }
	}
	
	public boolean addCards(SelectedCards stack, int colNum) {
		if (stack.size() == 1) {
			Card stackCard = stack.get(0);				//bottom-most card in stack to be moved
			ArrayList<Card> col = get(colNum);
		
			//if column is not currently empty
			if (col.size() > 0) {
				Card colCard = col.get(col.size() - 1);	//topmost card on destination column
				//check legality before moving stack onto destination column and setting new locations
				
				if (colCard.Suit == stackCard.Suit && colCard.number == stackCard.number - 1) {
					stackCard.setLocation(new Point(325 + (100 * colNum), 25));
					col.addAll(stack);	
					stack.clear();
					return true;
				}
				//column is currently empty (only an ace can start this column)
			} else if (stackCard.number == 1) {
				stackCard.setLocation(new Point(325 + (100 * colNum), 25));
				col.addAll(stack);	
				stack.clear();
				return true;
			}
		}
		return false;
	}
	
	//returns whether there are kings in every pile
	public boolean Solved() {
		for (int i = 0; i < 4; i++) {
			if (get(i).size() != 0) {
				if (get(i).get(get(i).size() - 1).number != 13) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	//returns the index of the pile at the given Point
	public int[] getIndexAtPoint(Point p) {
		for (int i = 0; i < 4; i++) {
			int[] xCoords = new int[] {325 + (100 * i), 425 + (100 * i), 425 + (100 * i), 325 + (100 * i)};
			int[] yCoords = new int[] {25, 25, 125, 125};
			Polygon pileArea = new Polygon(xCoords, yCoords, 4);
			if (pileArea.contains(p)) {
				return new int[] {i};
			}
		}
		return null;
	}
}



