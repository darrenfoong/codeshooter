package codeshooter.ui;

import codeshooter.game.Game;
import codeshooter.model.Shooter;
import codeshooter.utils.Properties;
import java.awt.*;
import javax.swing.*;

public class Sidebar extends JPanel {
  private static final long serialVersionUID = 1L;

  private static Properties PROPERTIES = Properties.getInstance();

  private static final int WIDTH =
      Integer.parseInt(PROPERTIES.getProperty(Properties.SIDEBAR_WIDTH));
  private static final long UPDATE_INTERVAL_IN_MS =
      Long.parseLong(PROPERTIES.getProperty(Properties.SIDEBAR_UPDATE_INTERVAL_IN_MS));

  private Game game;

  private Thread sidebarThread =
      new Thread() {
        @Override
        public void run() {
          while (true) {
            update();

            try {
              Thread.sleep(UPDATE_INTERVAL_IN_MS);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      };

  public Sidebar(Game game) {
    this.game = game;

    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setPreferredSize(new Dimension(WIDTH, 0));
  }

  private void update() {
    removeAll();

    for (Shooter shooter : game.getShooters()) {
      add(
          new JLabel(
              "["
                  + shooter.getID()
                  + "] "
                  + shooter.getName()
                  + ": ("
                  + shooter.getHealth()
                  + ") ("
                  + shooter.getNumKills()
                  + ")"));
    }

    revalidate();
    repaint();
  }

  public void start() {
    sidebarThread.start();
  }
}
