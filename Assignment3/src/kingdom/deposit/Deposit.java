package kingdom.deposit;

import kingdom.utils.Logger;
import kingdom.valuables.Valuable;

import java.util.ArrayList;
import java.util.List;

public class Deposit
{
  private final MyArrayListAdapter storage;
  private final int capacity;
  private final Logger logger = Logger.getInstance();

  public Deposit(int capacity)
  {
    this.capacity = capacity;
    this.storage = new MyArrayListAdapter(capacity);
  }

  public synchronized void put(Valuable valuable) throws InterruptedException
  {
    while (storage.isFull())
    {
      logger.logAction("Deposit", "WAITING", "Deposit is full, waiting for space...");
      wait();
    }

    logger.logAction("Deposit", "STORE", valuable.toString());
    storage.add(valuable);
    notifyAll();
  }

  public synchronized List<Valuable> take(int targetValue) throws InterruptedException
  {
    List<Valuable> taken = new ArrayList<>();
    int collectedValue = 0;

    while (collectedValue < targetValue)
    {
      while (storage.isEmpty())
      {
        logger.logAction("Deposit", "WAITING", "Deposit is empty, waiting for valuables...");
        wait();
      }

      // Take the first available valuable
      Valuable valuable = storage.remove(0);
      taken.add(valuable);
      collectedValue += valuable.getValue();

      logger.logAction("Deposit", "TAKE", valuable.toString() + " (Total: " + collectedValue + "/" + targetValue + ")");
      // If there are no more valuables, and we haven't reached the target, break
      if (storage.isEmpty() && collectedValue < targetValue)
      {
        logger.logAction("Deposit", "INCOMPLETE",
            "Could not reach target value " + targetValue + ", collected " + collectedValue);
        break;
      }
    }
    notifyAll(); // Notify producers that space is available
    return taken;
  }

  public synchronized int size()
  {
    return storage.size();
  }

  public int getCapacity()
  {
    return capacity;
  }
}
