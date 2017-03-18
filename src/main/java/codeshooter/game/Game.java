package codeshooter.game;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import codeshooter.arena.Arena;
import codeshooter.model.Entity;
import codeshooter.model.Projectile;
import codeshooter.model.Shooter;
import codeshooter.model.Target;

public class Game {
	private Arena arena;
	private KeyAdapter keyAdapter;

	private Shooter mainShooter;
	private List<Target> targets = new ArrayList<>();
	private List<Shooter> shooters = new ArrayList<>();

	public Game() {
		this.keyAdapter = new TAdapter();
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public KeyAdapter getKeyAdapter() {
		return keyAdapter;
	}

	public Shooter getShooter() {
		return mainShooter;
	}

	public void setShooter(Shooter shooter) {
		this.mainShooter = shooter;
		shooters.add(shooter);
	}

	public List<Shooter> getShooters() {
		return shooters;
	}

	public void addShooter(Shooter shooter) {
		shooters.add(shooter);
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void addTarget(Target target) {
		targets.add(target);
	}

	public void draw(Graphics g) {
		for ( Shooter shooter : shooters ) {
			shooter.draw(g);
		}

		for ( Target target : targets ) {
			target.draw(g);
		}
	}

	public void refresh() {
		for ( Shooter shooter : shooters ) {
			shooter.move(arena);

			filter(shooter.getProjectiles());

			for ( Projectile projectile : shooter.getProjectiles() ) {
				projectile.move(arena);
			}
		}

		filter(shooters);

		filter(targets);
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
