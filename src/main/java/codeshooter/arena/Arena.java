package codeshooter.arena;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.Timer;

import codeshooter.game.Game;
import codeshooter.model.Shape;

public abstract class Arena extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	protected Game game;

	protected Timer timer;
	protected int DELAY_IN_MS = 10;

	private static Logger LOGGER = Logger.getLogger(Arena.class.getName());

	public Arena(Game game) {
		this.game = game;
		game.setArena(this);
	}

	protected void init() {
		addKeyListener(game.getKeyAdapter());
		setFocusable(true);

		timer = new Timer(DELAY_IN_MS, this);
		timer.start();

		LOGGER.fine("Started timer of " + DELAY_IN_MS + " ms");
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		draw(g);
	}

	protected abstract void draw(Graphics g);

	public abstract boolean goodToMove(Shape mShape, double dx, double dy);

	@Override
	public void actionPerformed(ActionEvent e) {
		game.refresh();

		repaint();
	}
}
