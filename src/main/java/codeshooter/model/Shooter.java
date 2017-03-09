package codeshooter.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import codeshooter.ui.CircleArena;
import codeshooter.utils.Geometry;
import codeshooter.utils.Heading;

public class Shooter extends Entity {
	private Shape shape;
	private Heading heading;

	private double dx;
	private double dy;

	private Color color;
	private Color dirColor;

	private double turnIncInRadians;

	private List<Projectile> projectiles = new ArrayList<>();

	public Shooter(double x, double y, int radius, Color color, Color dirColor, double turnIncInRadians) {
		this.shape = new Circle(x, y, radius);
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
		int projectileDamage = 10;
		int projectileSpeed = 10;

		Circle shooterShape = (Circle) shape;
		projectiles.add(new Projectile(shooterShape.getCentreX()-projectileRadius,
										shooterShape.getCentreY()-projectileRadius,
										projectileRadius,
										Color.YELLOW,
										projectileDamage,
										heading,
										projectileSpeed));
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		shape.draw(color, g2d);

		Circle shooterShape = (Circle) shape;
		Line2D dirLine = new Line2D.Double(shooterShape.getCentreX(),
											shooterShape.getCentreY(),
											shooterShape.getCentreX()+(shooterShape.getRadius()*heading.getX()),
											shooterShape.getCentreY()+(shooterShape.getRadius()*heading.getY()));
		g2d.setColor(dirColor);
		g2d.draw(dirLine);

		for ( Projectile projectile : projectiles ) {
			projectile.draw(g);
		}
	}

	public void move(CircleArena arena) {
		if ( Geometry.containsEntirely((Circle) arena.getShape(), (Circle) shape, dx, dy) ) {
			for ( Target target : arena.getTargets() ) {
				if ( Geometry.isColliding((Circle) target.getShape(), (Circle) shape, dx, dy) ) {
					return;
				}
			}

			shape.updateX(dx);
			shape.updateY(dy);
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
