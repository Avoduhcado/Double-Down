package core.cards;

import java.awt.geom.Rectangle2D;

import core.entities.Mobile;
import core.render.textured.FixedSprite;

public class Card extends Mobile {

	private int rank;
	private Suit suit;
	
	public Card(Suit suit, int rank) {
		this.setRank(rank);
		this.setSuit(suit);
		this.box = new Rectangle2D.Double(0, 0, 72, 100);
		this.sprite = new FixedSprite("/sprites/" + suit.toString() + rank, (float) box.getWidth(), (float) box.getHeight());
	}
	
	public String toString() {
		switch(rank) {
		case(0):
			return "Ace of " + suit;
		case(10):
			return "Jack of " + suit;
		case(11):
			return "Queen of " + suit;
		case(12):
			return "King of " + suit;
		default:
			return (rank + 1) + " of " + suit;
		}
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	
}
