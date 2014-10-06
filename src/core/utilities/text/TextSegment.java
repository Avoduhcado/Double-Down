package core.utilities.text;

import java.lang.reflect.Field;

import org.newdawn.slick.Color;

public class TextSegment {

	private GameFont font;
	
	private String segment;
	
	public TextSegment(String segment) {
		if(segment.contains(">")) {
			String[] temp = segment.substring(0, segment.indexOf('>')).split(",");
			this.segment = segment.substring(segment.indexOf('>') + 1, segment.length());
			font = Text.getFont("SYSTEM").clone();
			
			for(int x = 0; x<temp.length; x++) {
				if(temp[x].startsWith("s")) {
					font.changeSize(Float.parseFloat(temp[x].substring(1)));
				} else if(temp[x].startsWith("c")) {
					try {
						if(temp[x].contains("#")) {
							font.changeColor(Color.decode(temp[x].substring(1)));
						} else {
							Field f = Color.class.getField(temp[x].substring(1).toLowerCase());
							font.changeColor((Color) f.get(null));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			this.segment = segment;
			font = Text.getFont("SYSTEM");
		}
	}
	
	public GameFont getFont() {
		return font;
	}
	
	public void setFont(GameFont font) {
		this.font = font;
	}
	
	public String getSegment() {
		return segment;
	}
	
	public float getWidth() {
		return this.font.getWidth(segment);
	}
	
	public float getHeight() {
		return this.font.getHeight(segment);
	}
	
}
