package kingdom.actors;

import kingdom.deposit.Deposit;
import kingdom.utils.Logger;
import kingdom.valuables.Valuable;
import kingdom.valuables.GoldNugget;
import kingdom.valuables.Ruby;
import kingdom.valuables.Diamond;

import java.util.Random;

public class ValuableMiner implements Runnable
{
  private final String name;
  private final Deposit deposit;
  private final int minValue;
  private final int maxValue;
  private final int miningInterval;
  private final Logger logger;
  private final Random random;
  private volatile boolean running;

  public ValuableMiner(String name, Deposit deposit, int minValue, int maxValue, int miningInterval)
  {
    this.name = name;
    this.deposit = deposit;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.miningInterval = miningInterval;
    this.logger = Logger.getInstance();
    this.random = new Random();
    this.running = true;
  }

  @Override public void run()
  {
    logger.logAction(name, "STARTED", "Mining valuable items");
    while (running)
    {
      try
      {
        // Mine a valuable
        logger.logAction(name, "MINING", "Digging for valuables...");
        Valuable valuable = excavate(minValue, maxValue);
        // Put it in the deposit
        logger.logAction(name, "FOUND", valuable.toString());
        deposit.put(valuable);
        // Sleep for mining interval
        Thread.sleep(miningInterval);
      }
      catch (InterruptedException e)
      {
        logger.logAction(name, "INTERRUPTED", "Mining operation interrupted");
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.logAction(name, "STOPPED", "No longer mining");
  }

  private Valuable excavate(int minValue, int maxValue)
  {
    int range = maxValue - minValue + 1;
    int value = minValue + random.nextInt(range);
    int type = random.nextInt(3);
    // Create and return a specific type of valuable based on the random value
    switch (type)
    {
      case 0:
        return new GoldNugget(value);
      case 1:
        return new Ruby(value);
      case 2:
        return new Diamond(value);
      default:
        return new GoldNugget(value);
    }
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