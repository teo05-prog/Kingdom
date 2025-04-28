package kingdom.actors;

import kingdom.deposit.Deposit;
import kingdom.utils.Logger;
import kingdom.valuables.Valuable;

import java.util.List;
import java.util.Random;

public class ValuableTransporter implements Runnable
{
  private final String name;
  private final Deposit deposit;
  private final int minTargetValue;
  private final int maxTargetValue;
  private final int transportInterval;
  private final Random random;
  private final Logger logger;
  private volatile boolean running;

  public ValuableTransporter(String name, Deposit deposit, int minTargetValue, int maxTargetValue,
      int transportInterval)
  {
    this.name = name;
    this.deposit = deposit;
    this.minTargetValue = minTargetValue;
    this.maxTargetValue = maxTargetValue;
    this.transportInterval = transportInterval;
    this.random = new Random();
    this.logger = Logger.getInstance();
    this.running = true;
  }

  @Override public void run()
  {
    logger.logAction(name, "STARTED", "Transporting valuable items");

    while (running)
    {
      try
      {
        // Generate a random target value
        int targetValue = random.nextInt(maxTargetValue - minTargetValue + 1) + minTargetValue;
        logger.logAction(name, "TARGET", "Setting target value to " + targetValue);
        // Collect valuables until we reach the target value
        logger.logAction(name, "COLLECTING", "Taking valuables from deposit");
        List<Valuable> valuables = deposit.take(targetValue);
        // Calculate total value collected
        int totalValue = 0;
        for (Valuable valuable : valuables)
        {
          totalValue += valuable.getValue();
        }
        logger.logAction(name, "COLLECTED", valuables.size() + " items with total value " + totalValue);
        // For now, just "throw away" the valuables as per requirements
        // In future implementations, these will be transported to the TreasureRoom
        logger.logAction(name, "DISPOSING", "Throwing away collected valuables"); // to be changed
        valuables.clear();
        // Sleep for transport interval
        Thread.sleep(transportInterval);
      }
      catch (InterruptedException e)
      {
        logger.logAction(name, "INTERRUPTED", "Transport operation interrupted");
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.logAction(name, "STOPPED", "No longer transporting");
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


