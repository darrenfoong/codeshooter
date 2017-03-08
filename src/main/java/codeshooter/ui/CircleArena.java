package codeshooter.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;

import codeshooter.model.Projectile;
import codeshooter.model.Shooter;

public class CircleArena extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private double x;
	private double y;
	private int radius;

	private Color color;

	private Shooter mainShooter;

	private Timer timer;
	private final int DELAY_IN_MS = 10;

	public CircleArena(double x, double y, int radius, Color color) {
		super();

		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;

		init();
	}

	private void init() {
		addKeyListener(new TAdapter());
		setFocusable(true);

		timer = new Timer(DELAY_IN_MS, this);
		timer.start();
	}

	public void setShooter(Shooter shooter) {
		this.mainShooter = shooter;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		draw(g);
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHints(rh);

		Ellipse2D circle = new Ellipse2D.Double(x, y, radius*2, radius*2);
		g2d.setColor(color);
		g2d.fill(circle);

		mainShooter.draw(g2d);
	}

	public boolean containsEntirely(double ox, double oy, double oradius) {
		if ( oradius > radius ) {
			return false;
		} else {
			return (ox-radius)*(ox-radius) + (oy-radius)*(oy-radius) <= (radius-oradius)*(radius-oradius);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainShooter.move(this);

		Iterator<Projectile> iter = mainShooter.getProjectiles().iterator();
		while ( iter.hasNext() ) {
			Projectile projectile = iter.next();

			if ( projectile.isVisible() ) {
				projectile.move(this);
			} else {
				iter.remove();
			}
		}

		repaint();
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			mainShooter.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			mainShooter.keyPressed(e);
		}
	}
}
