import java.awt.Point;
import java.util.ArrayList;

/* Program: SelectedCards.java
 * Date: 18 April 2022
 * Desc: This class represents the cards currently selected to be moved.
 */

public class SelectedCards extends ArrayList<Card> {

	private static final long serialVersionUID = -8711653155556335558L;
	public int[] origIndex;		//original index of the cards in their previous container
	public ArrayList<Point> locations = new ArrayList<>();	//list of original locations of cards
	public int deltaX;			//used to calculate offset when updating card location on mouse drag
	public int deltaY;			//
	public Boolean fromTableau; //whether this card's previous container was the tableau
	
	//adds the cards from the given ArrayList to this list and stores their initial locations
	public void pickUpCards(ArrayList<Card> stack, int[] origIndex) {
		this.origIndex = origIndex;
		addAll(stack);
		for (int i = 0; i < size(); i++) {
			locations.add(get(i).location);
		}
	}
	
	//empties this list and resets all cards' locations to their original locations 
	public void releaseCards() {
		if (!isEmpty()) {
			for (int i = 0; i < size(); i++) {
				get(i).setLocation(locations.get(i));
			}
		}
		clear();
	}
	
	//changes the locations of the cards in this list according to the given point
	public void updateLocations(Point p) {
		for (int i = 0; i < size(); i++) {
			get(i).location = (new Point(p.x - deltaX, p.y - deltaY + i * 30));
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		deltaX = 0;
		deltaY = 0;
		locations.clear();
	}
}
