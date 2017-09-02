package codeshooter.model;

import java.awt.*;

public class Pillar {
  private Shape shape;

  private Color color;

  public Pillar(double x, double y, int radius, Color color) {
    this.shape = new Circle(x, y, radius);

    this.color = color;
  }

  public Shape getShape() {
    return shape;
  }

  public void draw(Graphics g) {
    shape.draw(color, g);
  }
}
