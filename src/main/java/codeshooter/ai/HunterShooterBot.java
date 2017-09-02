package codeshooter.ai;

import codeshooter.model.Sensor;
import codeshooter.model.ShooterState;
import java.awt.event.KeyEvent;
import java.util.Random;

public class HunterShooterBot extends ShooterBot {
  private static enum BotState {
    EXPLORING,
    SEARCHING,
    LOCKED_ON,
    CHASING
  }

  private BotState state = BotState.SEARCHING;

  private static final int EXPLORE_MAX = 50;
  private static final int SEARCH_MAX = 20;

  private int exploreCount = 1;
  private int searchCount = 1;

  private static int[] outputKeys = {
    KeyEvent.VK_LEFT,
    KeyEvent.VK_RIGHT,
    KeyEvent.VK_UP,
    KeyEvent.VK_UP,
    KeyEvent.VK_UP,
    KeyEvent.VK_UP
  };

  @Override
  public int getKey(ShooterState shooterState) {
    int shooterIndex = getShooterIndex(shooterState);
    int midIndex = shooterState.getReadings().length / 2;
    int key = KeyEvent.VK_LEFT;

    switch (state) {
      case EXPLORING:
        if (Math.abs(shooterIndex - midIndex) <= 1) {
          key = KeyEvent.VK_UP;
          state = BotState.LOCKED_ON;
        } else if (shooterIndex == -1) {
          int randomIndex = (new Random()).nextInt(outputKeys.length);
          key = outputKeys[randomIndex];

          if (exploreCount == EXPLORE_MAX) {
            exploreCount = 1;
            state = BotState.SEARCHING;
          }

          exploreCount++;
        } else if (shooterIndex < midIndex) {
          key = KeyEvent.VK_LEFT;
          state = BotState.CHASING;
        } else if (shooterIndex > midIndex) {
          key = KeyEvent.VK_RIGHT;
          state = BotState.CHASING;
        }

        break;
      case SEARCHING:
        if (Math.abs(shooterIndex - midIndex) <= 1) {
          key = KeyEvent.VK_UP;
          state = BotState.LOCKED_ON;
        } else if (shooterIndex == -1) {
          key = KeyEvent.VK_LEFT;

          if (searchCount == SEARCH_MAX) {
            searchCount = 1;
            state = BotState.EXPLORING;
          }

          searchCount++;
        } else if (shooterIndex < midIndex) {
          key = KeyEvent.VK_LEFT;
          state = BotState.CHASING;
        } else if (shooterIndex > midIndex) {
          key = KeyEvent.VK_RIGHT;
          state = BotState.CHASING;
        }

        break;
      case LOCKED_ON:
        if (Math.abs(shooterIndex - midIndex) <= 1) {
          key = KeyEvent.VK_UP;
          state = BotState.CHASING;
        } else if (shooterIndex == -1) {
          state = BotState.SEARCHING;
        }

        break;
      case CHASING:
        if (Math.abs(shooterIndex - midIndex) <= 1) {
          key = KeyEvent.VK_SPACE;
          state = BotState.LOCKED_ON;
        } else if (shooterIndex == -1) {
          state = BotState.SEARCHING;
        }

        break;
      default:
        key = KeyEvent.VK_LEFT;
    }

    return key;
  }

  private int getShooterIndex(ShooterState shooterState) {
    for (int i = 0; i < shooterState.getReadings().length; i++) {
      if (shooterState.getTypes()[i] == Sensor.ReadingType.SHOOTER) {
        return i;
      }
    }

    return -1;
  }
}
