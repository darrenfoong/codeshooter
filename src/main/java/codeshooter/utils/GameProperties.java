package codeshooter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class GameProperties extends Properties {
  private static final long serialVersionUID = 1L;

  private static GameProperties instance = null;

  public static final String ARENA_RADIUS = "arena.radius";
  public static final String ARENA_COLOR = "arena.color";

  public static final String SHOOTER_RADIUS = "shooter.radius";
  public static final String SHOOTER_COLOR = "shooter.color";
  public static final String SHOOTER_DIR_COLOR = "shooter.color.dir";
  public static final String SHOOTER_INFO_COLOR = "shooter.color.info";
  public static final String SHOOTER_SENSOR_COLOR = "shooter.color.sensor";
  public static final String SHOOTER_HEALTH = "shooter.health";
  public static final String SHOOTER_TURN_INC_IN_RADIANS = "shooter.turnIncInRadians";
  public static final String SHOOTER_SENSOR_ANGLE_IN_DEGREES = "shooter.sensor.angleInDegrees";
  public static final String SHOOTER_SENSOR_RANGE = "shooter.sensor.range";
  public static final String SHOOTER_SENSOR_NUM_READINGS = "shooter.sensor.numReadings";

  public static final String PROJECTILE_RADIUS = "projectile.radius";
  public static final String PROJECTILE_COLOR = "projectile.color";
  public static final String PROJECTILE_DAMAGE = "projectile.damage";
  public static final String PROJECTILE_SPEED = "projectile.speed";

  public static final String BOT_COLOR = "bot.color";
  public static final String BOT_DIR_COLOR = "bot.color.dir";
  public static final String BOT_HEALTH = "bot.health";
  public static final String BOT_KEY_DURATION_IN_MS = "bot.keyDurationInMs";
  public static final String BOT_KEY_WAIT_IN_MS = "bot.keyWaitInMs";

  public static final String SIDEBAR_WIDTH = "sidebar.width";
  public static final String SIDEBAR_UPDATE_INTERVAL_IN_MS = "sidebar.updateIntervalInMs";

  public static final String TOPBAR_HEIGHT = "topbar.height";
  public static final String TOPBAR_UPDATE_INTERVAL_IN_MS = "topbar.updateIntervalInMs";

  public static final String PORT = "port";

  private static Logger LOGGER = Logger.getLogger(GameProperties.class.getName());

  public static synchronized GameProperties getInstance() {
    if (instance == null) {
      try (InputStream in =
          GameProperties.class.getClassLoader().getResourceAsStream("codeshooter.properties")) {
        instance = new GameProperties();
        instance.load(in);
      } catch (IOException e) {
        LOGGER.severe("IOException");
        e.printStackTrace();
        return null;
      }
    }

    return instance;
  }
}
