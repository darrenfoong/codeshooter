package codeshooter.model;

import java.awt.*;

public class Target extends Entity {
  private Shape shape;

  private Color color;

  private double health;

  public Target(double x, double y, int radius, Color color, double health) {
    this.shape = new Circle(x, y, radius);

    this.color = color;

    this.health = health;
  }

  public Shape getShape() {
    return shape;
  }

  public void changeHealth(double delta) {
    health += delta;

    if (health <= 0) {
      setVisible(false);
    }
  }

  public void draw(Graphics g) {
    shape.draw(color, g);
  }
}
