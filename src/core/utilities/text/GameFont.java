package core.utilities.text;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.GlyphPage;
import org.newdawn.slick.font.effects.ColorEffect;

public class GameFont implements Cloneable {

	private Font font = new Font("Times New Roman", Font.PLAIN, 16);
	private UnicodeFont unifont;
	
	private String fontName;
	private float size;
	private Color color;
	
	public GameFont(String fontName, float size, Color color) {
		this.fontName = fontName;
		this.size = size;
		this.color = color;
		
		try {
			font = Font.createFont(Font.PLAIN, new FileInputStream(System.getProperty("resources") + "/fonts/" + fontName));
			font = font.deriveFont(size);
		} catch (FontFormatException e) {
			System.err.println("Invalid font format");
		} catch (IOException e) {
			System.err.println("Could not find font: " + fontName);
		}
		
		updateUnifont();
	}
	
	@Override
	public GameFont clone() {
		return new GameFont(this.fontName, this.size, this.color);
	}
	
	@SuppressWarnings("unchecked")
	public void updateUnifont() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		unifont = new UnicodeFont(font);
		unifont.getEffects().add(new ColorEffect());
		unifont.addAsciiGlyphs();
		try {
			unifont.loadGlyphs();
		} catch(SlickException e) {
			System.err.println("Could not load ASCII Glyphs");
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void changeSize(float size) {
		font = font.deriveFont(size);
		this.size = size;
		updateUnifont();
	}
	
	public void changeColor(Color color) {
		this.color = color;
	}
	
	public void changeAliasing(boolean antiAlias) {
		java.awt.Graphics g = GlyphPage.getScratchGraphics();
		
		if (g!=null && g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}
		
		updateUnifont();
	}
	
	public void drawString(String text, float x, float y, Color color) {
		unifont.drawString(x + 2, y + 2, text, Color.black);
		unifont.drawString(x, y, text, (color == null ? this.color : color));
	}
	
	public void drawString(String text, float x, float y, Color color, int start, int end) {
		unifont.drawString(x + 2, y + 2, text, Color.black, start, end);
		unifont.drawString(x, y, text, (color == null ? this.color : color), start, end);
	}
	
	public void drawCenteredString(String text, float x, float y, Color color) {
		unifont.drawString((x + 2) - (unifont.getWidth(text) / 2f), y + 2, text, Color.black);
		unifont.drawString(x - (unifont.getWidth(text) / 2f), y, text, (color == null ? this.color : color));
	}
	
	public float getWidth(String text) {
		return unifont.getWidth(text);
	}
	
	public float getHeight(String text) {
		return unifont.getHeight(text);
	}
	
	public UnicodeFont getUnifont() {
		return unifont;
	}
	
	public float getSize() {
		return size;
	}
	
	public Color getColor() {
		return color;
	}
	
}
