package core.cards;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import core.render.textured.Image;

public class CardStack {

	protected Image blankSpace = new Image("Extras8");
	protected LinkedList<Card> cards;
	protected Point2D position;
	
	public CardStack(float x, float y) {
		this.cards = new LinkedList<Card>();
		position = new Point2D.Float(x, y);
	}
	
	public void update() {
		for(Card c : cards) {
			if(c.getDistance() > 0) {
				c.move();
			}
		}
	}
	
	public void draw() {
		if(!cards.isEmpty()) {
			cards.getLast().draw();
		} else {
			blankSpace.draw((float) position.getX(), (float) position.getY());
		}
	}
	
	public LinkedList<Card> getCards() {
		return cards;
	}
	
	public Card getTopCard() {
		if(cards.isEmpty())
			return null;
		return cards.getLast();
	}
	
	public void addCard(Card card) {
		card.setPosition((float) position.getX(), (float) position.getY());
		if(!cards.contains(card))
			cards.add(card);
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	public Rectangle2D getBox() {
		return new Rectangle2D.Double(position.getX(), position.getY(), blankSpace.getWidth(), blankSpace.getHeight());
	}
	
}
