package core.entities;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;

import core.Input;
import core.render.textured.Sprite;
import core.utilities.mouse.MouseInput;

public abstract class Entity {

	protected float x, y;
	protected Sprite sprite;
	protected Rectangle2D box;
		
	public void update() {
		
	}
	
	public void draw() {
		sprite.draw(x, y);
	}
	
	public void draw(float x, float y) {
		sprite.draw(x, y);
	}
	
	public void updateBox() {
		box = new Rectangle2D.Double(x, y, box.getWidth(), box.getHeight());
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		updateBox();
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public boolean isClicked() {
		return box.contains(MouseInput.getMouse()) && Mouse.isButtonDown(0) && !Input.mouseHeld;
	}
	
	public boolean isHovering() {
		return box.contains(MouseInput.getMouse());
	}
	
}
