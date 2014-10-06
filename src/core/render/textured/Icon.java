package core.render.textured;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import core.Camera;
import core.Theater;

public class Icon extends Sprite {

	private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
	private Vector4f rotation = new Vector4f(0f, 0f, 0f, 0f);
	private Vector3f scale = new Vector3f(1f, 1f, 1f);
	
	private float rotateSpeed = 0f;
	// Temp feature
	//private float maxSpeed;
		
	public Icon(String ref) {
		super("/icons/" + ref);
	}
	
	@Override
	public void draw(float x, float y) {
		if(Float.isNaN(x))
			x = Camera.get().getDisplayWidth(2f) - (getWidth() / 2f);
		if(Float.isNaN(y))
			y = Camera.get().getDisplayHeight(2f) - (getHeight() / 2f);
		
		texture.bind();
		
		updateTextureOffsets();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(x, y, 0f);
		GL11.glColor3f(color.x, color.y, color.z);
		GL11.glRotatef(rotation.w, rotation.x, rotation.y, rotation.z);
		if(rotateSpeed != 0f)
			GL11.glTranslatef(-getWidth() / 2f, -getHeight() / 2f, 0f);
		GL11.glScalef(scale.x, scale.y, scale.z);
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
	
	@Override
	public void animate() {
		super.animate();
		
		if(rotateSpeed != 0f) {
			rotation.w += Theater.getDeltaSpeed(0.025f) * rotateSpeed;
			if(rotation.w > 360f)
				rotation.w = 0f;
			else if(rotation.w < 0f)
				rotation.w = 360f;
			
			/*if(maxSpeed > 0) {
				rotateSpeed += Theater.getDeltaSpeed(0.25f) * (rotateSpeed < 0 ? 3f : 1f);
				if(rotateSpeed == 0f)
					rotateSpeed += Theater.getDeltaSpeed(1f);
				if(rotateSpeed >= maxSpeed)
					maxSpeed = -maxSpeed;
			} else {
				rotateSpeed -= Theater.getDeltaSpeed(0.25f) * (rotateSpeed > 0 ? 3f : 1f);
				if(rotateSpeed == 0f)
					rotateSpeed -= Theater.getDeltaSpeed(1f);
				if(rotateSpeed <= maxSpeed)
					maxSpeed = -maxSpeed;
			}*/
		}
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public Vector4f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector4f rotation, float speed) {
		this.rotation = rotation;
		setRotateSpeed(speed);
	}
	
	public float getRotateSpeed() {
		return rotateSpeed;
	}
	
	public void setRotateSpeed(float speed) {
		this.rotateSpeed = speed;
		//this.maxSpeed = speed;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

}
