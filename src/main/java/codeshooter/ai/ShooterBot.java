package codeshooter.ai;

import codeshooter.model.Shooter;
import codeshooter.model.ShooterState;

public abstract class ShooterBot {
	protected Runnable runnable;

	protected class ShooterBotRunnable implements Runnable {
		protected static final long KEY_DURATION_IN_MS = 50;
		protected static final long KEY_WAIT_IN_MS = 50;

		private Shooter shooter;

		public ShooterBotRunnable(Shooter shooter) {
			this.shooter = shooter;
		}

		@Override
		public void run() {
			while ( true ) {
				ShooterState shooterState = new ShooterState(shooter.getHealth(),
						shooter.getSensor().getReadings(),
						shooter.getSensor().getTypes());

				int key = getKey(shooterState);

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
	}

	public void start(Shooter shooter) {
		runnable = new ShooterBotRunnable(shooter);
		(new Thread(runnable)).start();
	}

	public abstract int getKey(ShooterState shooterState);
}
