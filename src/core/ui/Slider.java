package core.ui;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import core.render.textured.Icon;
import core.utilities.mouse.MouseInput;

public class Slider extends UIElement {

	private float value;
	private Rectangle2D valueBox;
	private boolean held;
	
	private Icon background;
	private Icon valueIcon;
	
	public Slider(float x, float y, float scale, float value, String background, String valueIcon) {
		super(x, y, null);
		
		this.value = value;
		this.box = new Rectangle2D.Double(x, y, 100f * scale, 15f * scale);
		valueBox = new Rectangle2D.Double(x + (float) (this.box.getWidth() * value) - 5f, y, 10f * scale, 15f * scale);
		
		this.background = new Icon(background);
		this.background.setScale(new Vector3f(scale, scale, 1f));
		this.valueIcon = new Icon(valueIcon);
		this.valueIcon.setScale(new Vector3f(scale, scale, 1f));
	}
	
	public void update() {
		if(isClicked() || held) {
			held = true;
			value = (float) ((MouseInput.getMouseX() - this.x) / (this.box.getMaxX() - this.x));
			if(value < 0)
				value = 0f;
			else if(value > 1)
				value = 1f;
			valueBox.setFrame(x + (float) (this.box.getWidth() * value) - (float) (valueBox.getWidth() / 2f), valueBox.getY(), valueBox.getWidth(), valueBox.getHeight());
		}
		
		if(!Mouse.isButtonDown(0)) {
			held = false;
		}
	}
	
	@Override
	public void draw() {
		background.draw(x, y);
		valueIcon.draw((float) valueBox.getX(), (float) valueBox.getY());
	}

	@Override
	public boolean isClicked() {
		return box.contains(MouseInput.getMouse()) && Mouse.isButtonDown(0);
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
}
