package codeshooter.ui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

import codeshooter.ai.RandomShooterBot;
import codeshooter.arena.CircleArena;
import codeshooter.game.Game;
import codeshooter.model.Pillar;
import codeshooter.model.Shooter;

public class Application extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private static final Color ARENA_COLOR = Color.LIGHT_GRAY;

	private static final int SHOOTER_RADIUS = 20;
	private static final Color SHOOTER_COLOR = Color.BLUE;
	private static final Color SHOOTER_DIR_COLOR = Color.RED;
	private static final double SHOOTER_TURN_INC_RADIANS = 0.05;

	private Game game;

	public Application() {
		initUI();

		game = new Game();

		CircleArena arena = new CircleArena(game, 0, 0, WIDTH/2, ARENA_COLOR);
		add(arena);

		arena.addPillar(new Pillar(300, 100, 40, Color.DARK_GRAY));
		arena.addPillar(new Pillar(100, 300, 40, Color.DARK_GRAY));
		arena.addPillar(new Pillar(10, 160, 40, Color.DARK_GRAY));

		game.setShooter(new Shooter(WIDTH/2 - SHOOTER_RADIUS,
				WIDTH/2 - SHOOTER_RADIUS,
				SHOOTER_RADIUS,
				SHOOTER_COLOR,
				SHOOTER_DIR_COLOR,
				SHOOTER_TURN_INC_RADIANS,
				100));

		Shooter enemyShooterA = new Shooter(WIDTH/2,
				10,
				SHOOTER_RADIUS,
				Color.WHITE,
				Color.RED,
				SHOOTER_TURN_INC_RADIANS,
				100);

		Shooter enemyShooterB = new Shooter(WIDTH/2,
				WIDTH - SHOOTER_RADIUS*2 - 10,
				SHOOTER_RADIUS,
				Color.WHITE,
				Color.RED,
				SHOOTER_TURN_INC_RADIANS,
				100);

		game.addShooter(enemyShooterA);
		game.addShooter(enemyShooterB);

		RandomShooterBot enemyShooterBotA = new RandomShooterBot(enemyShooterA);
		RandomShooterBot enemyShooterBotB = new RandomShooterBot(enemyShooterB);

		enemyShooterBotA.start();
		enemyShooterBotB.start();
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
