package codeshooter.ui;

import codeshooter.ai.RemoteProxyBot;
import codeshooter.arena.CircleArena;
import codeshooter.game.Game;
import codeshooter.model.Pillar;
import codeshooter.model.Shooter;
import codeshooter.utils.Properties;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class Application extends JFrame {
  private static final long serialVersionUID = 1L;

  private static Properties PROPERTIES = Properties.getInstance();

  private static final int WIDTH =
      Integer.parseInt(PROPERTIES.getProperty(Properties.ARENA_RADIUS)) * 2;
  private static final int HEIGHT =
      Integer.parseInt(PROPERTIES.getProperty(Properties.ARENA_RADIUS)) * 2;

  private static final Color ARENA_COLOR =
      Color.decode(PROPERTIES.getProperty(Properties.ARENA_COLOR));

  private static final int SHOOTER_RADIUS =
      Integer.parseInt(PROPERTIES.getProperty(Properties.SHOOTER_RADIUS));
  private static final Color SHOOTER_COLOR =
      Color.decode(PROPERTIES.getProperty(Properties.SHOOTER_COLOR));
  private static final Color SHOOTER_DIR_COLOR =
      Color.decode(PROPERTIES.getProperty(Properties.SHOOTER_DIR_COLOR));
  private static final double SHOOTER_HEALTH =
      Double.parseDouble(PROPERTIES.getProperty(Properties.SHOOTER_HEALTH));
  private static final double SHOOTER_TURN_INC_RADIANS =
      Double.parseDouble(PROPERTIES.getProperty(Properties.SHOOTER_TURN_INC_IN_RADIANS));
  private static final double SHOOTER_SENSOR_ANGLE_IN_DEGREES =
      Double.parseDouble(PROPERTIES.getProperty(Properties.SHOOTER_SENSOR_ANGLE_IN_DEGREES));
  private static final double SHOOTER_SENSOR_RANGE =
      Double.parseDouble(PROPERTIES.getProperty(Properties.SHOOTER_SENSOR_RANGE));
  private static final int SHOOTER_SENSOR_NUM_READINGS =
      Integer.parseInt(PROPERTIES.getProperty(Properties.SHOOTER_SENSOR_NUM_READINGS));

  private static final Color BOT_COLOR = Color.decode(PROPERTIES.getProperty(Properties.BOT_COLOR));
  private static final Color BOT_DIR_COLOR =
      Color.decode(PROPERTIES.getProperty(Properties.BOT_DIR_COLOR));
  private static final double BOT_HEALTH =
      Double.parseDouble(PROPERTIES.getProperty(Properties.BOT_HEALTH));

  private static final int PORT = Integer.parseInt(PROPERTIES.getProperty(Properties.PORT));

  private Game game;
  private JPanel container;
  private Topbar topbar;
  private Sidebar sidebar;

  private static Logger LOGGER = Logger.getLogger(Application.class.getName());

  public Application() {
    LOGGER.info("Starting Codeshooter");

    game = new Game();

    LOGGER.info("Loading CircleArena");
    CircleArena arena = new CircleArena(game, 0, 0, (double) WIDTH / 2, ARENA_COLOR);

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

    topbar.start();
    sidebar.start();

    LOGGER.info("Loading UI");
    initUI();

    game.setShooter(
        new Shooter(
            0,
            "Player",
            WIDTH / 2 - SHOOTER_RADIUS,
            WIDTH / 2 - SHOOTER_RADIUS,
            SHOOTER_RADIUS,
            SHOOTER_COLOR,
            SHOOTER_DIR_COLOR,
            SHOOTER_TURN_INC_RADIANS,
            SHOOTER_HEALTH,
            SHOOTER_SENSOR_ANGLE_IN_DEGREES,
            SHOOTER_SENSOR_RANGE,
            SHOOTER_SENSOR_NUM_READINGS));

    Shooter enemyShooterA =
        new Shooter(
            1,
            "BotA",
            (double) WIDTH / 2,
            10,
            SHOOTER_RADIUS,
            BOT_COLOR,
            BOT_DIR_COLOR,
            SHOOTER_TURN_INC_RADIANS,
            BOT_HEALTH,
            SHOOTER_SENSOR_ANGLE_IN_DEGREES,
            SHOOTER_SENSOR_RANGE,
            SHOOTER_SENSOR_NUM_READINGS);

    Shooter enemyShooterB =
        new Shooter(
            2,
            "BotB",
            (double) WIDTH / 2,
            WIDTH - SHOOTER_RADIUS * 2 - 10,
            SHOOTER_RADIUS,
            BOT_COLOR,
            BOT_DIR_COLOR,
            SHOOTER_TURN_INC_RADIANS,
            BOT_HEALTH,
            SHOOTER_SENSOR_ANGLE_IN_DEGREES,
            SHOOTER_SENSOR_RANGE,
            SHOOTER_SENSOR_NUM_READINGS);

    game.addShooter(enemyShooterA);
    game.addShooter(enemyShooterB);

    RemoteProxyBot enemyShooterBotA = new RemoteProxyBot(PORT + 1);
    RemoteProxyBot enemyShooterBotB = new RemoteProxyBot(PORT + 2);
    enemyShooterBotA.start(enemyShooterA);
    enemyShooterBotB.start(enemyShooterB);
  }

  private void initUI() {
    getContentPane().add(container);
    pack();

    setTitle("Codeshooter");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            try {
              for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                  UIManager.setLookAndFeel(info.getClassName());
                  break;
                }
              }
            } catch (Exception e) {
              LOGGER.warning("Nimbus look and feel not available");
            }

            Application ex = new Application();
            ex.setVisible(true);
          }
        });
  }
}
