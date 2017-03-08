package codeshooter.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import codeshooter.ui.CircleArena;
import codeshooter.utils.Heading;

public class Projectile {
	private double x;
	private double y;
	private int radius;

	private double dx;
	private double dy;

	private Color color;

	private boolean visible;

	public Projectile(double x, double y, int radius, Color color, Heading heading, double speed) {
		this.x = x;
		this.y = y;
		this.radius = radius;

		this.dx = heading.getX() * speed;
		this.dy = heading.getY() * speed;

		this.color = color;

		this.visible = true;
	}

	public boolean isVisible() {
		return visible;
	}

	public void getVisible(boolean visible) {
		this.visible = visible;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Ellipse2D circle = new Ellipse2D.Double(x, y, radius*2, radius*2);
		g2d.setColor(color);
		g2d.fill(circle);
	}

	public void move(CircleArena arena) {
		double nx = x + dx;
		double ny = y + dy;

		double ncx = nx + radius;
		double ncy = ny + radius;

		if ( arena.containsEntirely(ncx, ncy, radius) ) {
			x = nx;
			y = ny;
		} else {
			visible = false;
		}
	}
}
