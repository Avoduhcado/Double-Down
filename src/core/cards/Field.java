package core.cards;

import core.Theater;
import core.render.textured.Image;

public class Field extends CardStack {

	private final Image cardback = new Image("Extras3");
	
	public Field(float x, float y) {
		super(x, y);
		if(Theater.get().stoneField) {
			this.blankSpace = new Image("Extras1");
		}
	}
	
	@Override
	public void draw() {
		if(cards.size() > 1)
			cardback.draw((float) position.getX() - 3f, (float) position.getY() - 3f);
		else
			blankSpace.draw((float) position.getX(), (float) position.getY());
		super.draw();
	}
	
}
