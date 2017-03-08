package codeshooter.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import codeshooter.ui.CircleArena;
import codeshooter.utils.Heading;

public class Shooter {
	private double x;
	private double y;
	private int radius;
	private Heading heading;

	private double dx;
	private double dy;

	private Color color;
	private Color dirColor;

	private double turnIncInRadians;

	private List<Projectile> projectiles = new ArrayList<>();

	public Shooter(double x, double y, int radius, Color color, Color dirColor, double turnIncInRadians) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.heading = new Heading();

		this.color = color;
		this.dirColor = dirColor;

		this.turnIncInRadians = turnIncInRadians;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public void fire() {
		int projectileRadius = 4;
		int projectileSpeed = 10;
		projectiles.add(new Projectile(x+radius-projectileRadius,
										y+radius-projectileRadius,
										projectileRadius,
										Color.YELLOW,
										heading,
										projectileSpeed));
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Ellipse2D circle = new Ellipse2D.Double(x, y, radius*2, radius*2);
		g2d.setColor(color);
		g2d.fill(circle);

		Line2D dirLine = new Line2D.Double(x+radius,
											y+radius,
											x+radius+(radius*heading.getX()),
											y+radius+(radius*heading.getY()));
		g2d.setColor(dirColor);
		g2d.draw(dirLine);

		for ( Projectile projectile : projectiles ) {
			projectile.draw(g);
		}
	}

	public void move(CircleArena arena) {
		double nx = x + dx;
		double ny = y + dy;

		double ncx = nx + radius;
		double ncy = ny + radius;

		if ( arena.containsEntirely(ncx, ncy, radius) ) {
			x = nx;
			y = ny;
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if ( key == KeyEvent.VK_LEFT ) {
			heading.change(-turnIncInRadians);
		}

		if ( key == KeyEvent.VK_RIGHT ) {
			heading.change(turnIncInRadians);
		}

		if ( key == KeyEvent.VK_UP ) {
			dx = heading.getX();
			dy = heading.getY();
		}

		if ( key == KeyEvent.VK_DOWN ) {
			dx = -heading.getX();
			dy = -heading.getY();
		}

		if ( key == KeyEvent.VK_SPACE ) {
			fire();
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if ( key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN ) {
			dx = 0;
			dy = 0;
		}
	}
}
