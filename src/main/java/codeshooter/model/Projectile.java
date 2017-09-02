package codeshooter.model;

import codeshooter.arena.Arena;
import codeshooter.utils.Geometry;
import codeshooter.utils.Heading;
import java.awt.*;

public class Projectile extends Entity {
  private Shape shape;

  private double dx;
  private double dy;

  private Color color;

  private double damage;
  private Shooter originShooter;

  public Projectile(
      double x,
      double y,
      int radius,
      Color color,
      double damage,
      Shooter originShooter,
      Heading heading,
      double speed) {
    this.shape = new Circle(x, y, radius);

    this.dx = heading.getX() * speed;
    this.dy = heading.getY() * speed;

    this.color = color;

    this.damage = damage;

    this.originShooter = originShooter;
  }

  public void draw(Graphics g) {
    shape.draw(color, g);
  }

  public void move(Arena arena) {
    if (arena.goodToMove(shape, dx, dy)) {
      shape.updateX(dx);
      shape.updateY(dy);

      for (Target target : arena.getGame().getTargets()) {
        if (Geometry.isColliding((Circle) target.getShape(), (Circle) shape)) {
          target.changeHealth(-damage);
          setVisible(false);
          return;
        }
      }

      for (Shooter shooter : arena.getGame().getShooters()) {
        if (Geometry.isColliding((Circle) shooter.getShape(), (Circle) shape)) {
          shooter.changeHealth(-damage, originShooter);
          setVisible(false);
          return;
        }
      }

    } else {
      setVisible(false);
    }
  }
}
