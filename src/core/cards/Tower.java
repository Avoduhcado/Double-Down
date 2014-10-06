package core.cards;

public class Tower extends CardStack {

	public Tower(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void draw() {
		if(cards.size() > 1)
			cards.get(cards.size() - 2).draw((float) position.getX() - 3f, (float) position.getY() - 3f);
		else
			blankSpace.draw((float) position.getX(), (float) position.getY());
		super.draw();
	}

}
