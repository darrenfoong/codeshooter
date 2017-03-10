package codeshooter.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import codeshooter.model.Circle;
import codeshooter.model.Entity;
import codeshooter.model.Pillar;
import codeshooter.model.Projectile;
import codeshooter.model.Shape;
import codeshooter.model.Shooter;
import codeshooter.model.Target;

public class CircleArena extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Shape shape;
	private Color color;

	private List<Pillar> pillars = new ArrayList<>();

	private Shooter mainShooter;
	private List<Target> targets = new ArrayList<>();

	private Timer timer;
	private int DELAY_IN_MS = 10;

	public CircleArena(double x, double y, double radius, Color color) {
		super();

		this.shape = new Circle(x, y, radius);
		this.color = color;

		init();
	}

	private void init() {
		addKeyListener(new TAdapter());
		setFocusable(true);

		timer = new Timer(DELAY_IN_MS, this);
		timer.start();
	}

	public Shape getShape() {
		return shape;
	}

	public List<Pillar> getPillars() {
		return pillars;
	}

	public void addPillar(Pillar pillar) {
		pillars.add(pillar);
	}

	public void setShooter(Shooter shooter) {
		this.mainShooter = shooter;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void addTarget(Target target) {
		targets.add(target);
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

		shape.draw(color, g2d);

		mainShooter.draw(g2d);

		for ( Target target : targets ) {
			target.draw(g);
		}

		for ( Pillar pillar : pillars ) {
			pillar.draw(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainShooter.move(this);

		filter(mainShooter.getProjectiles());

		for ( Projectile projectile : mainShooter.getProjectiles() ) {
			projectile.move(this);
		}

		filter(targets);

		repaint();
	}

	private <T extends Entity> void filter(List<T> entities) {
		Iterator<T> iter = entities.iterator();
		while ( iter.hasNext() ) {
			T entity = iter.next();

			if ( !entity.isVisible() ) {
				iter.remove();
			}
		}
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
