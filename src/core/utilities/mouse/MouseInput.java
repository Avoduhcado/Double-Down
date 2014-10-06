package core.utilities.mouse;

import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;

import core.Camera;

public class MouseInput {

	/**
	 * @return Mouse X scaled to any screen resizing
	 */
	public static float getMouseX() {
		return Mouse.getX() / Camera.get().getFrameXScale();
	}
	
	/**
	 * @return Mouse Y scaled to any screen resizing
	 */
	public static float getMouseY() {
		// Invert Mouse Y for some odd reason
		return -(Mouse.getY() - (float)Camera.get().frame.getHeight()) / Camera.get().getFrameYScale();
	}
	
	/**
	 * @return Mouse as a Point2D
	 */
	public static Point2D getMouse() {
		return new Point2D.Double(getMouseX(), getMouseY());
	}
	
}
