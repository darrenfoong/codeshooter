package codeshooter.ui;

import codeshooter.utils.GameProperties;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;

public class Topbar extends JPanel {
  private static final long serialVersionUID = 1L;

  private static GameProperties PROPERTIES = GameProperties.getInstance();

  private static final int HEIGHT =
      Integer.parseInt(PROPERTIES.getProperty(GameProperties.TOPBAR_HEIGHT));
  private static final long UPDATE_INTERVAL_IN_MS =
      Long.parseLong(PROPERTIES.getProperty(GameProperties.TOPBAR_UPDATE_INTERVAL_IN_MS));

  private int secondsElapsed = 0;
  private JLabel timeLabel = new JLabel();

  private static Logger LOGGER = Logger.getLogger(Topbar.class.getName());

  private transient Thread topbarThread =
      new Thread() {
        @Override
        public void run() {
          while (true) {
            update();

            try {
              Thread.sleep(UPDATE_INTERVAL_IN_MS);
            } catch (InterruptedException e) {
              LOGGER.severe("Thread interrupted");
              e.printStackTrace();
            }
          }
        }
      };

  public Topbar() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setPreferredSize(new Dimension(0, HEIGHT));

    add(timeLabel);
    timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
  }

  private void update() {
    secondsElapsed++;
    timeLabel.setText(Integer.toString(secondsElapsed));
  }

  public void start() {
    topbarThread.start();
  }
}
