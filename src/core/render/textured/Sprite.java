package core.render.textured;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import core.Camera;
import core.Theater;
import core.render.TextureLoader;

public class Sprite {
	
	protected Texture texture;

	protected float width;
	protected float height;
	protected float textureX;
	protected float textureY;
	protected float textureXWidth;
	protected float textureYHeight;
	
	protected int maxDirection = 1;
	protected int maxFrame = 1;
	protected int direction = 0;
	protected int frame = 0;
	private float animStep;
	
	public Sprite(String ref) {
		this.texture = TextureLoader.get().getSlickTexture(System.getProperty("resources") + ref + ".png");
		
		width = texture.getWidth() / maxFrame;
		height = texture.getHeight() / maxDirection;
	}
	
	/*public static Sprite loadSprite(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/sprites"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			Sprite sprite = new Sprite(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
	    			
	    			reader.close();
	    			return sprite;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The sprite database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Sprite database failed to load!");
	    	e.printStackTrace();
	    }
		
		System.out.println("Failed to load sprite: " + ref);
		return new Sprite(null, 0, 0);
	}*/
	
	public void draw(float x, float y) {
		if(Float.isNaN(x))
			x = Camera.get().getDisplayWidth(2f) - (getWidth() / 2f);
		if(Float.isNaN(y))
			y = Camera.get().getDisplayHeight(2f) - (getHeight() / 2f);
		
		texture.bind();
		
		updateTextureOffsets();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, 0f);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glScalef(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(textureX, textureY);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(textureXWidth, textureY);
			GL11.glVertex2f(getWidth(), 0);
			GL11.glTexCoord2f(textureXWidth, textureYHeight);
			GL11.glVertex2f(getWidth(), getHeight());
			GL11.glTexCoord2f(textureX, textureYHeight);
			GL11.glVertex2f(0, getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public void animate() {
		if(maxFrame > 1) {
			animStep += Theater.getDeltaSpeed(0.025f);
			if (animStep >= 0.16f) {
				animStep = 0f;
				frame++;
				if (frame >= maxFrame) {
					frame = 0;
				}
			}
		}
	}
	
	/*public void drawHover(float x, float y) {
		texture.bind();

		width = texture.getWidth() / maxFrame;
		height = texture.getHeight() / maxDir;
		float xoffset = ((texture.getImageWidth() * 1.1f) - texture.getImageWidth()) / 2f;
		float yoffset = ((texture.getImageHeight() * 1.1f) - texture.getImageHeight()) / 2f;

		textureX = 0;
		textureY = 0;
		textureXWidth = width;
		textureYHeight = height;
		textureX = width * frame;
		textureXWidth = (width * frame) + width;
		if(maxDir > 1) {
			textureY = height * dir;
			textureYHeight = (height * dir) + height;
		}
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef((x - xoffset) / Screen.get().getCameraXScale(), (y - yoffset) / Screen.get().getCameraYScale(), 0f);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glScalef(1.1f, 1.1f, 1f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(textureX, textureY);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(textureXWidth, textureY);
			GL11.glVertex2f(getWidth(), 0);
			GL11.glTexCoord2f(textureXWidth, textureYHeight);
			GL11.glVertex2f(getWidth(), getHeight());
			GL11.glTexCoord2f(textureX, textureYHeight);
			GL11.glVertex2f(0, getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}*/
	
	public void updateTextureOffsets() {
		textureX = width * frame;
		textureY = height * direction;
		textureXWidth = (width * frame) + width;
		textureYHeight = (height * direction) + height;
	}
	
	public void setTexture(String ref) {
		this.texture = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/sprites/" + ref + ".png");
	}
	
	public int getFrame() {
		return frame;
	}
	
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getMaxFrame() {
		return maxFrame;
	}

	/**
	 * Actual image width!!
	 * 
	 * @return Width of single frame of image
	 */
	public float getWidth() {
		return texture.getImageWidth() / maxFrame;
	}
	
	/**
	 * Actual image height!!
	 * 
	 * @return Height of single frame of image
	 */
	public float getHeight() {
		return texture.getImageHeight() / maxDirection;
	}
	
}
