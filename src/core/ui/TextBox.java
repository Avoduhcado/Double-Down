package core.ui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import core.utilities.text.Text;
import core.utilities.text.TextLine;

public class TextBox extends UIElement {

	private ArrayList<TextLine> text = new ArrayList<TextLine>();
	
	public TextBox(String text, String fontName, float x, float y, String image) {
		super(x, y, image);
		
		String[] temp = text.split(";");
		for(String t : temp)
			this.text.add(new TextLine(t));
		if(fontName != null) {
			for(TextLine t : this.text)
				t.setFont(Text.getFont(fontName).clone());
		}
		this.box = new Rectangle2D.Double(x, y, getWidth(), getHeight());
	}
	
	@Override
	public void draw() {
		if(frame != null)
			frame.draw(x, y, box);
		for(int i = 0; i<text.size(); i++)
			text.get(i).draw(x, y + getCurrentHeight(i));
	}
	
	@Override
	public void draw(float x, float y) {
		if(frame != null)
			frame.draw(x, y, box);
		for(int i = 0; i<text.size(); i++)
			text.get(i).draw(x, y + getCurrentHeight(i));
	}
	
	public float getWidth() {
		float width = 0;
		for(TextLine t : text) {
			if(t.getWidth() > width) {
				width = t.getWidth();
			}
		}
		
		return width;
	}
	
	public float getHeight() {
		float height = 0;
		for(TextLine t : text) {
			height += t.getHeight();
		}
		
		return height;
	}
	
	public float getCurrentHeight(int index) {
		float height = 0;
		for(int x = 0; x<index; x++) {
			height += text.get(x).getHeight();
		}
		
		return height;
	}
}
