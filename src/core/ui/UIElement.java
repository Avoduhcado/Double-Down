package core.ui;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;

import core.Camera;
import core.Input;
import core.render.textured.UIFrame;
import core.utilities.mouse.MouseInput;

public abstract class UIElement {

	protected float x, y;
	protected UIFrame frame;
	protected Rectangle2D box;
	protected boolean enabled = true;
	
	public UIElement(float x, float y, String image) {
		if(image != null) {
			this.frame = new UIFrame(image);
		}
		
		if(Float.isNaN(x))
			this.x = Camera.get().getDisplayWidth(2f);
		else
			this.x = x;
		this.y = y;
		
		box = new Rectangle2D.Double(x, y, 0, 0);
	}
	
	public void update() {
		
	}
	
	public void draw() {
		if(frame != null)
			frame.draw(x, y, box);
	}
	
	public void draw(float x, float y) {
		if(frame != null)
			frame.draw(x, y, box);
	}
	
	public void updateBox() {
		box = new Rectangle2D.Double(x, y, box.getWidth(), box.getHeight());
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
	public float getOpacity() {
		if(frame != null)
			return frame.getOpacity();
		return 0;
	}
	
	public void setOpacity(float opacity) {
		if(frame != null)
			this.frame.setOpacity(opacity);
	}
	
	/**
	 * @return true if element is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Enable or disable this input.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
