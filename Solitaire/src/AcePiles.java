import java.awt.Point;
import java.util.ArrayList;

//Date: April 18, 2022

//public class AcePiles {
//	public static void main(String[] args) {
	//	acepiles = new AcePiles[numOfAcePiles];
	//		for(int a = 0; a < 4; a++) {
		//		acepiles[a] = new AcePiles(tableau[col.size - a - 1].getx()), int y, deck.Width;
	//		}
//}


public class AcePiles extends ArrayList<ArrayList<Card>> {
	private static final long serialVersionUID = 1260372991802134488L; 
	//create columns
	public AcePiles() {
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
	public boolean Solved() {
		for (int i = 0; i < 4; i++) {
			if (get(i).get(get(i).size() - 1).number != 13) {
				return false;
			}
		}
		return true;
	}
}



