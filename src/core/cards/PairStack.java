package core.cards;

public class PairStack extends CardStack {

	public PairStack(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void draw() {
		blankSpace.draw((float) position.getX(), (float) position.getY());
		if(!cards.isEmpty()) {
			for(Card c : cards)
				c.draw();
		}
	}
	
	@Override
	public void addCard(Card card) {
		card.setSpeed(15f);
		card.setMovement(this.position);
		if(!cards.contains(card))
			cards.add(card);
	}

}
