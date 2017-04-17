package codeshooter.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import codeshooter.ai.RemoteProxyBot;
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
	private static final double SHOOTER_SENSOR_ANGLE_IN_DEGREES = 30;
	private static final double SHOOTER_SENSOR_RANGE = 200;
	private static final int SHOOTER_SENSOR_NUM_READINGS = 7;

	private Game game;
	private JPanel container;
	private Topbar topbar;
	private Sidebar sidebar;

	public Application() {
		game = new Game();

		CircleArena arena = new CircleArena(game, 0, 0, WIDTH/2, ARENA_COLOR);

		arena.addPillar(new Pillar(300, 100, 40, Color.DARK_GRAY));
		arena.addPillar(new Pillar(100, 300, 40, Color.DARK_GRAY));
		arena.addPillar(new Pillar(10, 160, 40, Color.DARK_GRAY));

		topbar = new Topbar();
		sidebar = new Sidebar(game);

		container = new JPanel(new BorderLayout());
		add(container);

		container.add(arena, BorderLayout.CENTER);
		container.add(topbar, BorderLayout.PAGE_START);
		container.add(sidebar, BorderLayout.LINE_END);

		initUI();

		game.setShooter(new Shooter(0,
				"Player",
				WIDTH/2 - SHOOTER_RADIUS,
				WIDTH/2 - SHOOTER_RADIUS,
				SHOOTER_RADIUS,
				SHOOTER_COLOR,
				SHOOTER_DIR_COLOR,
				SHOOTER_TURN_INC_RADIANS,
				100,
				SHOOTER_SENSOR_ANGLE_IN_DEGREES,
				SHOOTER_SENSOR_RANGE,
				SHOOTER_SENSOR_NUM_READINGS));

		Shooter enemyShooterA = new Shooter(1,
				"HunterShooterBotA",
				WIDTH/2,
				10,
				SHOOTER_RADIUS,
				Color.WHITE,
				Color.RED,
				SHOOTER_TURN_INC_RADIANS,
				100,
				SHOOTER_SENSOR_ANGLE_IN_DEGREES,
				SHOOTER_SENSOR_RANGE,
				SHOOTER_SENSOR_NUM_READINGS);

		Shooter enemyShooterB = new Shooter(2,
				"HunterShooterBotB",
				WIDTH/2,
				WIDTH - SHOOTER_RADIUS*2 - 10,
				SHOOTER_RADIUS,
				Color.WHITE,
				Color.RED,
				SHOOTER_TURN_INC_RADIANS,
				100,
				SHOOTER_SENSOR_ANGLE_IN_DEGREES,
				SHOOTER_SENSOR_RANGE,
				SHOOTER_SENSOR_NUM_READINGS);

		game.addShooter(enemyShooterA);
		game.addShooter(enemyShooterB);

		RemoteProxyBot enemyShooterBotA = new RemoteProxyBot(enemyShooterA, 5781);
		RemoteProxyBot enemyShooterBotB = new RemoteProxyBot(enemyShooterB, 5782);
		enemyShooterBotA.start();
		enemyShooterBotB.start();

		topbar.start();
		sidebar.start();
	}

	private void initUI() {
		getContentPane().add(container);
		pack();

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
