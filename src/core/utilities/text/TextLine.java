package core.utilities.text;

import java.util.ArrayList;

public class TextLine {

	private ArrayList<TextSegment> line = new ArrayList<TextSegment>();
	private String fullLine = "";
	
	public TextLine(String line) {
		String[] temp = line.split("<");
		for(int x = 0; x<temp.length; x++) {
			this.line.add(new TextSegment(temp[x]));
			fullLine += this.line.get(x).getSegment();
		}
	}
	
	public void draw(float x, float y) {
		for(int i = 0; i<line.size(); i++) {
			line.get(i).getFont().drawString(fullLine, x, y, null, getCurrentIndex(i), getCurrentIndex(i) + line.get(i).getSegment().length());
		}
	}
	
	public int getCurrentIndex(int index) {
		int temp = 0;
		
		if(index > 0) {
			for(int x = 0; x<index; x++) {
				temp += line.get(x).getSegment().length();
			}
		}
		
		return temp;
	}
	
	public void setFont(GameFont font) {
		for(TextSegment t : line)
			t.setFont(font);
	}
	
	public void setFont(GameFont font, int index) {
		line.get(index).setFont(font);
	}
	
	public float getHeight() {
		float height = 0f;
		for(TextSegment t : line) {
			if(t.getHeight() > height)
				height = t.getHeight();
		}
		
		return height;
	}
	
	public float getWidth() {
		float width = 0f;
		for(TextSegment t : line) {
			width += t.getWidth();
		}
		
		return width;
	}
	
}
