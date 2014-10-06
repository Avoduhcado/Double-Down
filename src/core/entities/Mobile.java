package core.entities;

import java.awt.geom.Point2D;

import core.Theater;

public abstract class Mobile extends Entity {

	protected float speed = 2f;
	protected float speedMod = 1f;
	protected float dx, dy;
	protected float distance;

	public void move() {
		followPath();
		
		x += Theater.getDeltaSpeed(dx);
		y += Theater.getDeltaSpeed(dy);

		updateBox();
	}

	public void followPath() {
		if(distance > 0) {
			if((float) Math.sqrt(Math.pow(Theater.getDeltaSpeed(dx), 2) + Math.pow(Theater.getDeltaSpeed(dy), 2)) > distance) {
				float tempDistance = distance / (float) Math.sqrt(Math.pow(Theater.getDeltaSpeed(dx), 2) + Math.pow(Theater.getDeltaSpeed(dy), 2));
				dx = dx * tempDistance;
				dy = dy * tempDistance;
			}
			distance -= (float) Math.sqrt(Math.pow(Theater.getDeltaSpeed(dx), 2) + Math.pow(Theater.getDeltaSpeed(dy), 2));
		}
	}

	public void setMovement(Point2D destination) {
		if(destination.getX() == x) {
			float theta = (float) Math.atan2(destination.getX() - x, destination.getY() - y);
			dx = 0f;
			dy = (float) Math.cos(theta) * getFullSpeed();
			distance = (float) Math.sqrt(Math.pow(x - destination.getX(), 2) + Math.pow(y - destination.getY(), 2));
		} else if(destination.getY() == y) {
			float theta = (float) Math.atan2(destination.getX() - x, destination.getY() - y);
			dx = (float) Math.sin(theta) * getFullSpeed();
			dy = 0f;
			distance = (float) Math.sqrt(Math.pow(x - destination.getX(), 2) + Math.pow(y - destination.getY(), 2));
		} else {
			float theta = (float) Math.atan2(destination.getX() - x, destination.getY() - y);
			dx = (float) Math.sin(theta) * getFullSpeed();
			dy = (float) Math.cos(theta) * getFullSpeed();
			distance = (float) Math.sqrt(Math.pow(x - destination.getX(), 2) + Math.pow(y - destination.getY(), 2));
		}
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getFullSpeed() {
		return speed * speedMod;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeedMod() {
		return speedMod;
	}

	public void setSpeedMod(float speedMod) {
		this.speedMod = speedMod;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

}
