package codeshooter.ai;

import codeshooter.model.Shooter;
import codeshooter.model.ShooterState;
import codeshooter.utils.Properties;

public abstract class ShooterBot {
  private static Properties PROPERTIES = Properties.getInstance();

  protected static final long KEY_DURATION_IN_MS =
      Long.parseLong(PROPERTIES.getProperty(Properties.BOT_KEY_DURATION_IN_MS));
  protected static final long KEY_WAIT_IN_MS =
      Long.parseLong(PROPERTIES.getProperty(Properties.BOT_KEY_WAIT_IN_MS));
  protected Runnable runnable;

  protected class ShooterBotRunnable implements Runnable {
    private Shooter shooter;

    public ShooterBotRunnable(Shooter shooter) {
      this.shooter = shooter;
    }

    @Override
    public void run() {
      while (true) {
        ShooterState shooterState =
            new ShooterState(
                shooter.getHealth(),
                shooter.getSensor().getReadings(),
                shooter.getSensor().getTypes());

        int key = getKey(shooterState);

        try {
          shooter.processPressKeyCode(key);

          Thread.sleep(KEY_DURATION_IN_MS);

          shooter.processReleaseKeyCode(key);

          Thread.sleep(KEY_WAIT_IN_MS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void start(Shooter shooter) {
    runnable = new ShooterBotRunnable(shooter);
    (new Thread(runnable)).start();
  }

  public abstract int getKey(ShooterState shooterState);
}
