package core.cards;

public enum Suit {

	HEARTS,
	DIAMONDS,
	SPADES,
	CLUBS;
	
	public boolean areOppositeSuits(Suit suit) {
		if((this.isBlack() && suit.isRed()) || (this.isRed() && suit.isBlack()))
			return true;
		return false;
	}
	
	public boolean isRed() {
		if(this.equals(DIAMONDS) || this.equals(HEARTS))
			return true;
		return false;
	}
	
	public boolean isBlack() {
		if(this.equals(CLUBS) || this.equals(SPADES))
			return true;
		return false;
	}
	
	public String toString() {
		switch(ordinal()) {
		case(0):
			return "Hearts";
		case(1):
			return "Diamonds";
		case(2):
			return "Spades";
		case(3):
			return "Clubs";
		default:
			return "Extras";
		}
	}
	
}
