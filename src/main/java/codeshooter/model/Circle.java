package codeshooter.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Circle extends Shape {
	private double radius;

	public Circle(double x, double y, double radius) {
		super(x, y);
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	public double getCentreX() {
		return getX() + radius;
	}

	public double getCentreY() {
		return getY() + radius;
	}

	@Override
	public void draw(Color color, Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Ellipse2D circle = new Ellipse2D.Double(getX(), getY(), radius*2, radius*2);
		g2d.setColor(color);
		g2d.fill(circle);
	}
}
