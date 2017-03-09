package codeshooter.ui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

import codeshooter.model.Shooter;
import codeshooter.model.Target;

public class Application extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private static final Color ARENA_COLOR = Color.LIGHT_GRAY;

	private static final int SHOOTER_RADIUS = 20;
	private static final Color SHOOTER_COLOR = Color.BLUE;
	private static final Color SHOOTER_DIR_COLOR = Color.RED;
	private static final double SHOOTER_TURN_INC_RADIANS = 0.1;

	public Application() {
		initUI();

		CircleArena arena = new CircleArena(0, 0, WIDTH/2, ARENA_COLOR);
		add(arena);

		arena.setShooter(new Shooter(WIDTH/2 - SHOOTER_RADIUS,
									WIDTH/2 - SHOOTER_RADIUS,
									SHOOTER_RADIUS,
									SHOOTER_COLOR,
									SHOOTER_DIR_COLOR,
									SHOOTER_TURN_INC_RADIANS));

		arena.addTarget(new Target(100, 100, 10, Color.GREEN, 100));
		arena.addTarget(new Target(300, 300, 10, Color.GREEN, 100));
	}

	private void initUI() {
		setSize(WIDTH, HEIGHT);
		setTitle("Code Shooter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application ex = new Application();
				ex.setVisible(true);
			}
		});
	}
}
