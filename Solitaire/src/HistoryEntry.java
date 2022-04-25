/* Program: HistoryEntry.java
 * Date: 24 April 2022
 * Desc: This class stores information about a move made in the game. It is used with HistoryList
 */

public class HistoryEntry {
	public int[] prevIndex;
	public int[] postIndex;
	public Container prevContainer;
	public Container postContainer;
	public boolean cardFlipped = false;
	
	public enum Container {
		TABLEAU,
		STOCKPILE,
		ACEPILE;
	}
	
	public HistoryEntry(int[] prevIndex, Container prevContainer, int[] postIndex, Container postContainer) {
		this.prevIndex = prevIndex;
		this.prevContainer = prevContainer;
		this.postIndex = postIndex;
		this.postContainer = postContainer;
	}
	
	//alternate constructor that sets cardFlipped to true, indicating a card was
	//flipped face-up by this move
	public HistoryEntry(int[] prevIndex, Container prevContainer, int[] postIndex, Container postContainer, 
			boolean cardFlipped) {
		this(prevIndex, prevContainer, postIndex, postContainer);
		this.cardFlipped = cardFlipped;
	}
}