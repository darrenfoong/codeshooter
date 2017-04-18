package codeshooter.ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import codeshooter.game.Game;
import codeshooter.model.Shooter;

public class Sidebar extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 300;
	private static final long UPDATE_INTERVAL_IN_MS = 10;

	private Game game;

	private Thread sidebarThread = new Thread() {
		@Override
		public void run() {
			while ( true ) {
				update();

				try {
					Thread.sleep(UPDATE_INTERVAL_IN_MS);
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
			}
		}
	};

	public Sidebar(Game game) {
		this.game = game;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(WIDTH, 0));
	}

	private void update() {
		removeAll();

		for ( Shooter shooter : game.getShooters() ) {
			add(new JLabel("[" + shooter.getID() + "] " + shooter.getName() + ": (" + shooter.getHealth() + ") (" + shooter.getNumKills() + ")"));
		}

		revalidate();
		repaint();
	}

	public void start() {
		sidebarThread.start();
	}
}
