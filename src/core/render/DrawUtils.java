package core.render;

import java.awt.geom.Rectangle2D;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import core.Camera;

public class DrawUtils {
	
	/** Color of object to be drawn */
	private static Vector3f color = new Vector3f(0f, 0f, 0f);

	/**
	 * Set new drawing color.
	 * 
	 * @param color to draw with
	 */
	public static void setColor(Vector3f color) {
		DrawUtils.color = color;
	}
	
	/**
	 * Draw a line to the screen.
	 * 
	 * @param x1 of line
	 * @param y1 of line
	 * @param x2 of line
	 * @param y2 of line
	 */
	public static void drawLine(double x1, double y1, double x2, double y2) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor3f(color.x, color.y, color.z);
		GL11.glLineWidth(2.0f);
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2d(x1, y1);
			GL11.glVertex2d(x2, y2);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Reset color
		color.set(0f, 0f, 0f);
	}
	
	/**
	 * Draw a rectangle to the screen.
	 * 
	 * @param x of rectangle
	 * @param y of rectangle
	 * @param rect to be drawn
	 */
	public static void drawRect(float x, float y, Rectangle2D rect) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor3f(color.x, color.y, color.z);
		GL11.glLineWidth(2.0f);
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + rect.getWidth(), y);
			GL11.glVertex2d(x + rect.getWidth(), y + rect.getHeight());
			GL11.glVertex2d(x, y + rect.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Reset color
		color.set(0f, 0f, 0f);
	}
	
	/**
	 * Draws a single color over the entire screen.
	 * 
	 * @param r Red value of color
	 * @param g Green value of color
	 * @param b Blue value of color
	 * @param a Transparency
	 */
	public static void fillColor(float r, float g, float b, float a) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor4f(r, g, b, a);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(Camera.get().frame.getWidth(), 0);
			GL11.glVertex2d(Camera.get().frame.getWidth(), Camera.get().frame.getHeight());
			GL11.glVertex2d(0, Camera.get().frame.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
}
