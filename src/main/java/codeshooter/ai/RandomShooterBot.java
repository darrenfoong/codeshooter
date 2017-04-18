package codeshooter.ai;

import java.awt.event.KeyEvent;
import java.util.Random;

import codeshooter.model.ShooterState;

public class RandomShooterBot extends ShooterBot {
	private static int[] outputKeys = { KeyEvent.VK_LEFT,
									KeyEvent.VK_RIGHT,
									KeyEvent.VK_UP,
									KeyEvent.VK_DOWN,
									KeyEvent.VK_SPACE
								};

	@Override
	public int getKey(ShooterState shooterState) {
		int randomIndex = (new Random()).nextInt(outputKeys.length);
		return outputKeys[randomIndex];
	}
}
