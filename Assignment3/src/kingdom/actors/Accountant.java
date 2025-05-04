
package kingdom.actors;

import kingdom.treasureroom.TreasureRoomReadProxy;
import kingdom.utils.Logger;
import kingdom.valuables.Valuable;

import java.util.List;

public class Accountant implements Runnable {
    private final String name;
    private final TreasureRoomReadProxy treasureRoomReadProxy;
    private final int countingDuration;
    private final int accountingInterval;
    private final Logger logger;
    private volatile boolean running;

    public Accountant(String name, TreasureRoomReadProxy treasureRoomReadProxy,
                      int countingDuration, int accountingInterval) {
        this.name = name;
        this.treasureRoomReadProxy = treasureRoomReadProxy;
        this.countingDuration = countingDuration;
        this.accountingInterval = accountingInterval;
        this.logger = Logger.getInstance();
        this.running = true;
    }

    @Override
    public void run() {
        logger.logAction(name, "STARTED", "Accounting for valuables");

        while (running) {
            try {
                // Step 1: Access the treasure room
                logger.logAction(name, "COUNTING", "Starting to count valuables...");

                // Get the list of valuables from the treasure room
                List<Valuable> valuables = treasureRoomReadProxy.lookAtValuables();

                // Step 2: Count valuables (simulate counting time)
                Thread.sleep(countingDuration);

                // Calculate the total value
                int totalValue = 0;
                for (Valuable valuable : valuables) {
                    totalValue += valuable.getValue();
                }

                // Step 3: Log the total sum
                logger.logAction(name, "RESULT", "Total value in treasure room: " + totalValue);

                // Step 4: Release access to the treasure room
                treasureRoomReadProxy.release();

                // Step 5: Sleep before next accounting session
                Thread.sleep(accountingInterval);
            } catch (InterruptedException e) {
                logger.logAction(name, "INTERRUPTED", "Accounting operation interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (IllegalStateException e) {
                logger.logAction(name, "ERROR", e.getMessage());
                break;
            }
        }

        logger.logAction(name, "STOPPED", "No longer accounting");
    }

    public void stop() {
        running = false;
    }

    public String getName() {
        return name;
    }
}

