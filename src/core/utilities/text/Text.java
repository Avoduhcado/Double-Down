package core.utilities.text;

import java.util.HashMap;

import org.newdawn.slick.Color;

public class Text {

	private static HashMap<String, GameFont> fonts = new HashMap<String, GameFont>();
	
	public static void loadFonts() {
		
	}
	
	public static void addFont(String key, GameFont font) {
		if(!fonts.containsKey(key))
			fonts.put(key, font);
		
		if(fonts.size() == 1)
			fonts.put("DEFAULT", font.clone());
	}

	public static GameFont getFont(String font) {
		if(fonts.get(font) != null)
			return fonts.get(font);
		
		return fonts.get("DEFAULT");
	}
	
	public static void drawCenteredString(String text, float x, float y, Color color) {
		fonts.get("DEFAULT").drawCenteredString(text, x, y, color);
	}
	
	public static void drawString(String text, float x, float y, Color color) {
		fonts.get("DEFAULT").drawString(text, x, y, color);
	}
	
	public static void drawString(String text, float x, float y, Color color, int start, int end) {
		fonts.get("DEFAULT").drawString(text, x, y, color, start, end);
	}
	
}
