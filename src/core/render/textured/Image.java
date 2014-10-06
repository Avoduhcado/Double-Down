package core.render.textured;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import core.Camera;
import core.render.TextureLoader;

public class Image {

	private Texture texture;
	
	public Image(String ref) {
		this.texture = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/images/" + ref + ".png");
	}
	
	public void draw(float x, float y) {
		if(Float.isNaN(x))
			x = Camera.get().getDisplayWidth(2f) - (getWidth() / 2f);
		if(Float.isNaN(y))
			y = Camera.get().getDisplayHeight(2f) - (getHeight() / 2f);
		
		texture.bind();
				
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, 0f);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glScalef(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(texture.getWidth(), 0);
			GL11.glVertex2f(getWidth(), 0);
			GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
			GL11.glVertex2f(getWidth(), getHeight());
			GL11.glTexCoord2f(0, texture.getHeight());
			GL11.glVertex2f(0, getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public float getWidth() {
		return texture.getImageWidth();
	}
	
	public float getHeight() {
		return texture.getImageHeight();
	}

}
