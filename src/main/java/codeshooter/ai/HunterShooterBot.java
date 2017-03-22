package codeshooter.ai;

import java.awt.event.KeyEvent;
import java.util.Random;

import codeshooter.model.Sensor;
import codeshooter.model.Shooter;

public class HunterShooterBot extends ShooterBot {
	private static final long KEY_DURATION_IN_MS = 50;
	private static final long KEY_WAIT_IN_MS = 50;

	private static final int EXPLORE_MAX = 50;
	private static final int SEARCH_MAX = 20;

	private static int[] outputKeys = { KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT,
			KeyEvent.VK_UP,
			KeyEvent.VK_UP,
			KeyEvent.VK_UP,
			KeyEvent.VK_UP
		};

	private static enum BotState { EXPLORING, SEARCHING, LOCKED_ON, CHASING }

	public HunterShooterBot(Shooter shooter) {
		this.shooter = shooter;
		this.thread = new Thread() {
			@Override
			public void run() {
				BotState state = BotState.SEARCHING;

				int midIndex = shooter.getSensor().getNumReadings()/2;

				int exploreCount = 1;
				int searchCount = 1;

				while ( shooter.getHealth() > 0 ) {
					int shooterIndex = getShooterIndex();
					int key = KeyEvent.VK_LEFT;

					switch (state) {
						case EXPLORING:
							if ( Math.abs(shooterIndex - midIndex) <= 1 ) {
								key = KeyEvent.VK_UP;
								state = BotState.LOCKED_ON;
							} else if ( shooterIndex == -1 ) {
								int randomIndex = (new Random()).nextInt(outputKeys.length);
								key = outputKeys[randomIndex];

								if ( exploreCount == EXPLORE_MAX ) {
									exploreCount = 1;
									state = BotState.SEARCHING;
								}

								exploreCount++;
							} else if ( shooterIndex < midIndex ) {
								key = KeyEvent.VK_LEFT;
								state = BotState.CHASING;
							} else if ( shooterIndex > midIndex ) {
								key = KeyEvent.VK_RIGHT;
								state = BotState.CHASING;
							}

							break;
						case SEARCHING:
							if ( Math.abs(shooterIndex - midIndex) <= 1 ) {
								key = KeyEvent.VK_UP;
								state = BotState.LOCKED_ON;
							} else if ( shooterIndex == -1 ) {
								key = KeyEvent.VK_LEFT;

								if ( searchCount == SEARCH_MAX ) {
									searchCount = 1;
									state = BotState.EXPLORING;
								}

								searchCount++;
							} else if ( shooterIndex < midIndex ) {
								key = KeyEvent.VK_LEFT;
								state = BotState.CHASING;
							} else if ( shooterIndex > midIndex ) {
								key = KeyEvent.VK_RIGHT;
								state = BotState.CHASING;
							}

							break;
						case LOCKED_ON:
							if ( Math.abs(shooterIndex - midIndex) <= 1 ) {
								key = KeyEvent.VK_UP;
								state = BotState.CHASING;
							} else if ( shooterIndex == -1 ) {
								state = BotState.SEARCHING;
							}

							break;
						case CHASING:
							if ( Math.abs(shooterIndex - midIndex) <= 1 ) {
								key = KeyEvent.VK_SPACE;
								state = BotState.LOCKED_ON;
							} else if ( shooterIndex == -1 ) {
								state = BotState.SEARCHING;
							}

							break;
						default:
							key = KeyEvent.VK_LEFT;
					}

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

	private int getShooterIndex() {
		for ( int i = 0; i < shooter.getSensor().getNumReadings(); i++ ) {
			if ( shooter.getSensor().getType(i) == Sensor.ReadingType.SHOOTER ) {
				return i;
			}
		}

		return -1;
	}
}
