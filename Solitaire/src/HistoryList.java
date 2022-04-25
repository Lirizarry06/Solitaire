import java.awt.Point;
import java.util.ArrayList;

/* Program: HistoryList
 * Date: 24 April 2022
 * Desc: This class stores up to 100 history entries that are used for the undo/redo functionality.
 */

public class HistoryList extends ArrayList<HistoryEntry> {

	private static final long serialVersionUID = -6172529883214311096L;
	public final int MAX_LENGTH = 100;	//maximum number of history entries that will be stored
	public int currentIndex = 0;		//position within the list of the next move to be made
	public Tableau tableau;
	public AcePiles acePiles;
	public StockPile stockPile;
	
	public HistoryList(Tableau tableau, AcePiles acePiles, StockPile stockPile) {
		this.tableau = tableau;
		this.acePiles = acePiles;
		this.stockPile = stockPile;
	}
	
	//adds a history entry to the end of the list, removing the earliest entry if 
	//the list has reached 100 entries
	@Override
	public boolean add(HistoryEntry entry) {
		//if size will exceed 100, remove the earliest entry
		if (size() + 1 > 100) {
			remove(0);
			currentIndex -= 1;
		}
		
		//if an entry is added after an undo has occurred, remove all subsequent entries
		//before adding the new one
		while (currentIndex < size()) {
			remove(size() - 1);
		}
		
		add(currentIndex, entry);
		currentIndex += 1;
		return true;
	}
	
	//reverts the last move made
	public void undo() {
		if (currentIndex > 0) {
			currentIndex -= 1;
			HistoryEntry entry = get(currentIndex);
			ArrayList<Card> stack = getCards(entry.postContainer, entry.postIndex);
			if (entry.cardFlipped) {
				if (tableau.get(entry.prevIndex[0]).size() > 0) {
					tableau.get(entry.prevIndex[0]).get(entry.prevIndex[1] - 1).faceUp = false;
				}
			}
			replaceCards(stack, entry.prevContainer, entry.prevIndex);
		}
	}
	
	//undoes a previous undo
	public void redo() {
		if (currentIndex < size()) {
			HistoryEntry entry = get(currentIndex);
			ArrayList<Card> stack = getCards(entry.prevContainer, entry.prevIndex);
			if (entry.cardFlipped) {
				if (tableau.get(entry.prevIndex[0]).size() > 0) {
					tableau.get(entry.prevIndex[0]).get(tableau.get(entry.prevIndex[0]).size() - 1).faceUp = true;
				}
			}
			replaceCards(stack, entry.postContainer, entry.postIndex);
			currentIndex += 1;
		}
	}
	
	//returns the cards specified by the given index and container and removes them from that container
	public ArrayList<Card> getCards(HistoryEntry.Container container, int[] index) {
		ArrayList<Card> stack = new ArrayList<>();
		
		//get ace pile cards
		if (container == HistoryEntry.Container.ACEPILE) {
			stack.add(acePiles.get(index[0]).get(acePiles.get(index[0]).size() - 1));
			acePiles.get(index[0]).remove(acePiles.get(index[0]).size() - 1);
			return stack;
			
		// get stock pile cards
		} else if (container == HistoryEntry.Container.STOCKPILE) {
			while (stockPile.discardPile.size() - 1 != index[0]) {
				stockPile.cycleCard();
			}
			stack.add(stockPile.discardPile.get(index[0]));
			stockPile.discardPile.remove(index[0]);
			return stack;
			
		//get tableau cards
		} else {
			for (int i = index[1]; i < tableau.get(index[0]).size(); i++) {
				stack.add(tableau.get(index[0]).get(i));
			}
			while (tableau.get(index[0]).size() > index[1]) {
				tableau.get(index[0]).remove(index[1]);
			}
			return stack;
		}
	}
	
	//places the given stack into the given container at the given index, and updates card locations
	public void replaceCards(ArrayList<Card> stack, HistoryEntry.Container container, int[] index) {
		//place into ace pile
		if (container == HistoryEntry.Container.ACEPILE) {
			stack.get(0).location = (new Point(325 + (100 * index[0]), 25));
			acePiles.get(index[0]).add(stack.get(0));
			
		//place into stock pile
		} else if (container == HistoryEntry.Container.STOCKPILE) {
			while (stockPile.discardPile.size() != index[0]) {
				stockPile.cycleCard();
			}
			stack.get(0).location = (new Point(125, 25));
			stockPile.discardPile.add(index[0], stack.get(0));
			
		//place into tableau
		} else {
			tableau.get(index[0]).addAll(stack);
			for (int i = 0; i < tableau.get(index[0]).size(); i++) {
				tableau.get(index[0]).get(i).setLocation(new Point(25 + (100 * index[0]), 150 + (30 * i)));
			}
		}
	}
}
