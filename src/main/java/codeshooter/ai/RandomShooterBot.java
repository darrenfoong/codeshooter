package codeshooter.ai;

import java.awt.event.KeyEvent;
import java.util.Random;

import codeshooter.model.Shooter;

public class RandomShooterBot extends ShooterBot {
	private static final long KEY_DURATION_IN_MS = 50;
	private static final long KEY_WAIT_IN_MS = 50;

	private static int[] outputKeys = { KeyEvent.VK_LEFT,
									KeyEvent.VK_RIGHT,
									KeyEvent.VK_UP,
									KeyEvent.VK_DOWN,
									KeyEvent.VK_SPACE
								};

	public RandomShooterBot(Shooter shooter) {
		this.shooter = shooter;
		this.thread = new Thread() {
			@Override
			public void run() {
				while ( shooter.getHealth() > 0 ) {
					int randomIndex = (new Random()).nextInt(outputKeys.length);
					int key = outputKeys[randomIndex];

					try {
						shooter.processPressKeyCode(key);

						Thread.sleep(KEY_DURATION_IN_MS);

						shooter.processReleaseKeyCode(key);

						Thread.sleep(KEY_WAIT_IN_MS);
					} catch ( InterruptedException e ) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
