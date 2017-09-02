package codeshooter.arena;

import codeshooter.game.Game;
import codeshooter.model.Circle;
import codeshooter.model.Pillar;
import codeshooter.model.Shape;
import codeshooter.utils.Geometry;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircleArena extends Arena {
  private static final long serialVersionUID = 1L;

  private Shape shape;
  private Color color;

  protected List<Pillar> pillars = new ArrayList<>();

  public CircleArena(Game game, double x, double y, double radius, Color color) {
    super(game);

    this.shape = new Circle(x, y, radius);
    this.color = color;

    setPreferredSize(new Dimension((int) radius * 2, (int) radius * 2));

    init();
  }

  public Shape getShape() {
    return shape;
  }

  public List<Pillar> getPillars() {
    return pillars;
  }

  public void addPillar(Pillar pillar) {
    pillars.add(pillar);
  }

  @Override
  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    RenderingHints rh =
        new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setRenderingHints(rh);

    shape.draw(color, g2d);

    for (Pillar pillar : pillars) {
      pillar.draw(g);
    }

    game.draw(g);
  }

  @Override
  public boolean goodToMove(Shape mShape, double dx, double dy) {
    if (Geometry.containsEntirely((Circle) shape, (Circle) mShape, dx, dy)) {
      for (Pillar pillar : pillars) {
        if (Geometry.isColliding((Circle) pillar.getShape(), (Circle) mShape, dx, dy)) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }
}
