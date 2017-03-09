package codeshooter.model;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape {
	private double x;
	private double y;

	public Shape(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void updateX(double dx) {
		x += dx;
	}

	public void updateY(double dy) {
		y += dy;
	}

	public abstract void draw(Color color, Graphics g);
}
