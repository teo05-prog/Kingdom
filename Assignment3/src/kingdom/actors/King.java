package kingdom.actors;

import kingdom.treasureroom.TreasureRoom;
import kingdom.treasureroom.TreasureRoomDoor;
import kingdom.utils.Logger;
import kingdom.valuables.Valuable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class King implements Runnable
{
  private final String name;
  private final int minPartyValue;
  private final int maxPartyValue;
  private final int gatheringTime;
  private final int partyDuration;
  private final int restingPeriod;
  private final TreasureRoomDoor treasureRoomDoor;
  private final Random random;
  private volatile boolean running;
  private final Logger logger;

  public King(String name, int minPartyValue, int maxPartyValue,
      int gatheringTime, int partyDuration, int restingPeriod,
      TreasureRoomDoor treasureRoomDoor)
  {
    this.name = name;
    this.minPartyValue = minPartyValue;
    this.maxPartyValue = maxPartyValue;
    this.gatheringTime = gatheringTime;
    this.partyDuration = partyDuration;
    this.restingPeriod = restingPeriod;
    this.treasureRoomDoor = treasureRoomDoor;
    this.random = new Random();
    this.running = true;
    this.logger = Logger.getInstance();
  }

  public void throwParty(int totalValue)
  {
    try {
      logger.logAction(name, "PARTY", "Throwing a lavish party worth " + totalValue);
      Thread.sleep(partyDuration);
      logger.logAction(name, "PARTY_OVER", "Party has ended, valuables spent");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.logAction(name, "INTERRUPTED", "Party was interrupted");
    }
  }

  @Override public void run()
  {
    logger.logAction(name, "STARTED", "King is ready to throw parties");

    while (running)
    {
      try
      {
        // Generate random target value for the party
        int targetValue = random.nextInt(maxPartyValue - minPartyValue + 1) + minPartyValue;
        logger.logAction(name, "PLANNING", "Planning party with budget of " + targetValue);

        // Acquire write access to the treasure room
        logger.logAction(name, "REQUESTING", "Requesting access to the treasure room");
        TreasureRoom treasureRoom = treasureRoomDoor.acquireWriteAccess();
        logger.logAction(name, "ACCESS", "Gained access to the treasure room");

        // Retrieve valuables
        List<Valuable> retrievedValuables = new ArrayList<>();
        int collectedValue = 0;
        boolean canAffordParty = true;

        while (collectedValue < targetValue)
        {
          // Simulate time to select a valuable
          Thread.sleep(gatheringTime / 10);

          Valuable valuable = treasureRoom.retrieveValuable();
          if (valuable == null)
          {
            logger.logAction(name, "INSUFFICIENT", "Not enough valuables for the party");
            canAffordParty = false;
            break;
          }

          retrievedValuables.add(valuable);
          collectedValue += valuable.getValue();
          logger.logAction(name, "RETRIEVED", "Retrieved " + valuable + ", total so far: " + collectedValue);
        }

        // Check if target was met
        if (canAffordParty && collectedValue >= targetValue)
        {
          logger.logAction(name, "SUCCESS", "Collected enough valuables worth " + collectedValue);
          treasureRoomDoor.releaseWriteAccess();
          throwParty(collectedValue);
        }
        else
        {
          // Return valuables if target not met
          logger.logAction(name, "RETURNING", "Returning valuables to the treasure room");
          for (Valuable valuable : retrievedValuables)
          {
            Thread.sleep(gatheringTime / 15); // Takes less time to put back
            treasureRoom.addValuable(valuable);
            logger.logAction(name, "RETURNED", "Returned " + valuable);
          }
          treasureRoomDoor.releaseWriteAccess();
          logger.logAction(name, "CANCELLED", "Party cancelled");
        }

        // Rest before planning next party
        logger.logAction(name, "RESTING", "Resting before next party");
        Thread.sleep(restingPeriod);
      }
      catch (InterruptedException e)
      {
        logger.logAction(name, "INTERRUPTED", "King's activities were interrupted");
        Thread.currentThread().interrupt();
        break;
      }
    }

    logger.logAction(name, "STOPPED", "King is no longer throwing parties");
  }

  public void stop()
  {
    running = false;
  }

  public String getName()
  {
    return name;
  }
}