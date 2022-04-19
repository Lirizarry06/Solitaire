import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/* Program: SelectedCards.java
 * Date: 18 April 2022
 * Desc: This class represents the cards currently selected to be moved.
 */

public class SelectedCards extends ArrayList<Card> {

	private static final long serialVersionUID = -8711653155556335558L;
	public List<Card> origin;	//the original structure from which the cards were selected
	public int[] origIndex;		//the location of the selected cards within origin
								//origIndex[0] is start index of stack, origIndex[1] is end index, exclusive
	public ArrayList<Point> locations = new ArrayList<>();
	public int deltaX;
	public int deltaY;
	
	//adds the cards from the given ArrayList at the given indexes to this list
	//(cards were clicked)
	public void pickUpCards(ArrayList<Card> origin, int[] origIndex) {
		this.origin = origin;
		this.origIndex = origIndex;
		addAll(origin);
		for (int i = 0; i < size(); i++) {
			locations.add(get(i).getLocation());
		}
		//remove from origin
		for (int i = origIndex[0]; i < origIndex[1]; i++) {
			origin.remove(i);
		}
	}
	
	//returns the cards in this list to their original locations 
	//(cards were dragged but not placed in a legal spot)
	public void releaseCards() {
		for (int i = 0; i < size(); i++) {
			get(i).setLocation(locations.get(i));
			origin.addAll(this);
		}
		this.clear();
	}
	
	//updates the locations of the cards in this list according to the given point
	//(cards were dragged)
	public void updateLocations(Point p) {
		for (int i = 0; i < size(); i++) {
			get(i).location = (new Point(p.x - deltaX, p.y - deltaY + i * 40));
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
