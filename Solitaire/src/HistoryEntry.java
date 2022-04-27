/* Program: HistoryEntry.java
 * Date: 24 April 2022
 * Desc: This class stores information about a move made in the game. It is used with HistoryList
 */

public class HistoryEntry {
	public int[] prevIndex;				//pre-move index of cards
	public int[] postIndex;				//post-move index of cards
	public Container prevContainer;		//pre-move container of cards
	public Container postContainer;		//post-move container of cards
	public boolean cardFlipped = false;	//whether this move caused the tableau to flip a card over
	
	//game components that hold cards
	public enum Container {
		TABLEAU,
		STOCKPILE,
		ACEPILE;
	}
	
	public HistoryEntry(int[] prevIndex, Container prevContainer, int[] postIndex, Container postContainer, 
			boolean cardFlipped) {
		this.prevIndex = prevIndex;
		this.prevContainer = prevContainer;
		this.postIndex = postIndex;
		this.postContainer = postContainer;
		this.cardFlipped = cardFlipped;
	}
}
