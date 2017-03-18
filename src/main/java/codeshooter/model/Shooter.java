package codeshooter.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import codeshooter.arena.Arena;
import codeshooter.utils.Geometry;
import codeshooter.utils.Heading;

public class Shooter extends Entity {
	private Shape shape;
	private Heading heading;

	private double dx;
	private double dy;
	private double dh;

	private Color color;
	private Color dirColor;

	private double turnIncInRadians;

	private double health;

	private List<Projectile> projectiles = new ArrayList<>();

	public Shooter(double x, double y, int radius, Color color, Color dirColor, double turnIncInRadians, double health) {
		this.shape = new Circle(x, y, radius);
		this.heading = new Heading();

		this.color = color;
		this.dirColor = dirColor;

		this.turnIncInRadians = turnIncInRadians;

		this.health = health;
	}

	public Shape getShape() {
		return shape;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public void fire() {
		Circle shooterShape = (Circle) shape;

		int projectileRadius = 4;
		int projectileDamage = 10;
		int projectileSpeed = 10;

		double projectileX = shooterShape.getCentreX() - projectileRadius + shooterShape.getRadius() * heading.getX();
		double projectileY = shooterShape.getCentreY() - projectileRadius + shooterShape.getRadius() * heading.getY();

		projectiles.add(new Projectile(projectileX,
										projectileY,
										projectileRadius,
										Color.YELLOW,
										projectileDamage,
										heading,
										projectileSpeed));
	}

	public double getHealth() {
		return health;
	}

	public void changeHealth(double delta) {
		health += delta;

		if ( health <= 0 ) {
			setVisible(false);
		}
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

	public void move(Arena arena) {
		if ( arena.goodToMove(shape, dx, dy) ) {
			for ( Target target : arena.getGame().getTargets() ) {
				if ( Geometry.isColliding((Circle) target.getShape(), (Circle) shape, dx, dy) ) {
					return;
				}
			}

			for ( Shooter shooter : arena.getGame().getShooters() ) {
				if ( this != shooter && Geometry.isColliding((Circle) shooter.getShape(), (Circle) shape, dx, dy) ) {
					return;
				}
			}

			shape.updateX(dx);
			shape.updateY(dy);
			heading.change(dh);
		}
	}

	public void processPressKeyCode(int key) {
		if ( key == KeyEvent.VK_LEFT ) {
			dh = -turnIncInRadians;
		}

		if ( key == KeyEvent.VK_RIGHT ) {
			dh = turnIncInRadians;
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

	public void processReleaseKeyCode(int key) {
		if ( key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT ) {
			dh = 0;
		}

		if ( key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN ) {
			dx = 0;
			dy = 0;
		}
	}

	public void keyPressed(KeyEvent e) {
		processPressKeyCode(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		processReleaseKeyCode(e.getKeyCode());
	}
}
