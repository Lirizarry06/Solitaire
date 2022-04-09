import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/* Program: Card.java
 * Date: 8 April 2022
 * Desc: This class represents a playing card.
 */

public class Card {

	private suits Suit;			//may be Spade, Club, Heart, Diamond
	private int number; 		//may be 1-13. Aces are 1. Jacks, Queens, and Kings are 11, 12, and 13.
	private boolean faceUp = false;		//stores whether or not the card's suit and number are visible
	private Point location = new Point();		//location of the top-left corner of the card
	
	//Enumerates four possible suits.
	enum suits {
		SPADE,
		CLUB,
		HEART,
		DIAMOND;
	}
	
	//Constructs a new card with the given suit and number, face-down by default
	public Card(suits suit, int number) {
		this.Suit = suit;
		this.number = number;
	}
	
	//Flip card over. If face-up, turn face-down and vice versa.
	public void flipOver() {
		faceUp = !faceUp;
	}
	
	//Returns true if this card is a red card
	public boolean isRed() {
		return Suit == suits.HEART || Suit == suits.DIAMOND;
	}
	
	//Returns whether this card can legally be put on top of the given other card
	public boolean isStackable(Card other) {
		return this.number == other.number - 1;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point newLocation) {
		this.location = newLocation;
	}
	
	//Returns the image file of this card
	public BufferedImage getImage() throws IOException {
		if (faceUp) {
			return ImageIO.read(new File("card-images/" + this.number + "_" + this.Suit + ".png"));
		} else {
			return ImageIO.read(new File("card-images/back.png"));
		}
	}
}
