package codeshooter.model;

import codeshooter.arena.Arena;
import codeshooter.arena.CircleArena;
import codeshooter.model.Sensor.ReadingType;
import codeshooter.utils.Geometry;
import codeshooter.utils.Heading;
import codeshooter.utils.Properties;
import codeshooter.utils.Text;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Shooter extends Entity {
  private static Properties PROPERTIES = Properties.getInstance();

  private int id;
  private String name;
  private int numKills;

  private Shape shape;
  private Heading heading;

  private int displacementDirection;
  private int rotationDirection;

  private Color color;
  private Color dirColor;
  private static final Color INFO_COLOR =
      Color.decode(PROPERTIES.getProperty(Properties.SHOOTER_INFO_COLOR));
  private static final Color SENSOR_COLOR =
      Color.decode(PROPERTIES.getProperty(Properties.SHOOTER_SENSOR_COLOR));

  private double turnIncInRadians;

  private double health;

  private Sensor sensor;

  private List<Projectile> projectiles = new ArrayList<>();

  public Shooter(
      int id,
      String name,
      double x,
      double y,
      int radius,
      Color color,
      Color dirColor,
      double turnIncInRadians,
      double health,
      double sensorAngleInDegrees,
      double sensorRange,
      int sensorNumReadings) {
    this.id = id;
    this.name = name;
    this.numKills = 0;

    this.shape = new Circle(x, y, radius);
    this.heading = new Heading();

    this.color = color;
    this.dirColor = dirColor;

    this.turnIncInRadians = turnIncInRadians;

    this.health = health;

    this.sensor = new Sensor(sensorAngleInDegrees, sensorRange, sensorNumReadings);
  }

  public int getID() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getNumKills() {
    return numKills;
  }

  public void incrementNumKills() {
    this.numKills++;
  }

  public Shape getShape() {
    return shape;
  }

  public List<Projectile> getProjectiles() {
    return projectiles;
  }

  public void fire() {
    Circle shooterShape = (Circle) shape;

    int projectileRadius = Integer.parseInt(PROPERTIES.getProperty(Properties.PROJECTILE_RADIUS));
    int projectileDamage = Integer.parseInt(PROPERTIES.getProperty(Properties.PROJECTILE_DAMAGE));
    int projectileSpeed = Integer.parseInt(PROPERTIES.getProperty(Properties.PROJECTILE_SPEED));

    double projectileX =
        shooterShape.getCentreX() - projectileRadius + shooterShape.getRadius() * heading.getX();
    double projectileY =
        shooterShape.getCentreY() - projectileRadius + shooterShape.getRadius() * heading.getY();

    projectiles.add(
        new Projectile(
            projectileX,
            projectileY,
            projectileRadius,
            Color.decode(PROPERTIES.getProperty(Properties.PROJECTILE_COLOR)),
            projectileDamage,
            this,
            heading,
            projectileSpeed));
  }

  public double getHealth() {
    return health;
  }

  public void changeHealth(double delta) {
    health += delta;

    if (health <= 0) {
      setVisible(false);
    }
  }

  public void changeHealth(double delta, Shooter originShooter) {
    health += delta;

    if (health <= 0) {
      setVisible(false);
      originShooter.incrementNumKills();
    }
  }

  public Sensor getSensor() {
    return sensor;
  }

  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    Circle shooterShape = (Circle) shape;

    Arc2D sensorArc =
        new Arc2D.Double(
            shooterShape.getCentreX() - sensor.getRange(),
            shooterShape.getCentreY() - sensor.getRange(),
            2 * sensor.getRange(),
            2 * sensor.getRange(),
            heading.getSwingDegrees() - sensor.getAngle() / 2,
            sensor.getAngle(),
            Arc2D.PIE);

    g2d.setColor(SENSOR_COLOR);
    g2d.draw(sensorArc);

    shape.draw(color, g2d);

    Line2D dirLine =
        new Line2D.Double(
            shooterShape.getCentreX(),
            shooterShape.getCentreY(),
            shooterShape.getCentreX() + (shooterShape.getRadius() * heading.getX()),
            shooterShape.getCentreY() + (shooterShape.getRadius() * heading.getY()));
    g2d.setColor(dirColor);
    g2d.draw(dirLine);

    Text.drawCentreString(
        g2d,
        name,
        INFO_COLOR,
        10f,
        (int) shooterShape.getX(),
        (int) (shooterShape.getY() + shooterShape.getRadius() * 2 + 10),
        (int) shooterShape.getRadius());

    Text.drawCentreString(
        g2d,
        Double.toString(health),
        INFO_COLOR,
        10f,
        (int) shooterShape.getX(),
        (int) (shooterShape.getY() + shooterShape.getRadius() * 2 - 10),
        (int) shooterShape.getRadius());

    for (Projectile projectile : projectiles) {
      projectile.draw(g);
    }
  }

  public void move(Arena arena) {
    heading.change(rotationDirection * turnIncInRadians);
    double dx = displacementDirection * heading.getX();
    double dy = displacementDirection * heading.getY();

    if (arena.goodToMove(shape, dx, dy)) {
      for (Target target : arena.getGame().getTargets()) {
        if (Geometry.isColliding((Circle) target.getShape(), (Circle) shape, dx, dy)) {
          return;
        }
      }

      for (Shooter shooter : arena.getGame().getShooters()) {
        if (this != shooter
            && Geometry.isColliding((Circle) shooter.getShape(), (Circle) shape, dx, dy)) {
          return;
        }
      }

      shape.updateX(dx);
      shape.updateY(dy);
    }

    updateSensor(arena);
  }

  public void updateSensor(Arena arena) {
    Circle shooterShape = (Circle) shape;
    CircleArena circleArena = (CircleArena) arena;

    for (int i = 0; i < sensor.getNumReadings(); i++) {
      Heading currentHeading = new Heading(heading.get());
      currentHeading.change(
          Math.toRadians(-sensor.getAngle() / 2 + i * sensor.getReadingInterval()));

      double arenaRange =
          Geometry.getRangeInsideCircle(
              shooterShape.getCentreX(),
              shooterShape.getCentreY(),
              currentHeading,
              (Circle) circleArena.getShape());
      double objectRange = Double.POSITIVE_INFINITY;

      ReadingType objectType = ReadingType.EMPTY;

      for (Pillar pillar : circleArena.getPillars()) {
        double pillarRange =
            Geometry.getRangeOutsideCircle(
                shooterShape.getCentreX(),
                shooterShape.getCentreY(),
                currentHeading,
                (Circle) pillar.getShape());
        if (0 <= pillarRange && pillarRange < objectRange) {
          objectRange = pillarRange;
          objectType = ReadingType.WALL;
        }
      }

      for (Shooter shooter : arena.getGame().getShooters()) {
        if (this != shooter) {
          double shooterRange =
              Geometry.getRangeOutsideCircle(
                  shooterShape.getCentreX(),
                  shooterShape.getCentreY(),
                  currentHeading,
                  (Circle) shooter.getShape());
          if (0 <= shooterRange && shooterRange < objectRange) {
            objectRange = shooterRange;
            objectType = ReadingType.SHOOTER;
          }
        }
      }

      double reading;
      ReadingType type;

      if (arenaRange < objectRange) {
        reading = arenaRange;
        type = ReadingType.WALL;
      } else {
        reading = objectRange;
        type = objectType;
      }

      if (reading > sensor.getRange()) {
        reading = sensor.getRange();
        type = ReadingType.EMPTY;
      }

      sensor.updateReading(i, reading, type);
    }
  }

  public void processPressKeyCode(int key) {
    if (key == KeyEvent.VK_LEFT) {
      rotationDirection = -1;
    }

    if (key == KeyEvent.VK_RIGHT) {
      rotationDirection = 1;
    }

    if (key == KeyEvent.VK_UP) {
      displacementDirection = 1;
    }

    if (key == KeyEvent.VK_DOWN) {
      displacementDirection = -1;
    }

    if (key == KeyEvent.VK_SPACE) {
      fire();
    }
  }

  public void processReleaseKeyCode(int key) {
    if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
      rotationDirection = 0;
    }

    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
      displacementDirection = 0;
    }
  }

  public void keyPressed(KeyEvent e) {
    processPressKeyCode(e.getKeyCode());
  }

  public void keyReleased(KeyEvent e) {
    processReleaseKeyCode(e.getKeyCode());
  }
}
