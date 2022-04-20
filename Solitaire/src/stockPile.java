import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class stockPile extends SolitairePanel {

    public stockPile(int x, int y) {
        super();
        
	Card.deck = new ArrayList<Card>();
	for (int i = 1; i <= 13; i++) {
		Card.deck.add(new Card(Card.suits.CLUB, i));
		Card.deck.add(new Card(Card.suits.SPADE, i));
		Card.deck.add(new Card(Card.suits.HEART, i));
		Card.deck.add(new Card(Card.suits.DIAMOND, i));
	}
    }

}