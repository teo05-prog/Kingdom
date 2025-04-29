package test.kingdom.deposit;

import kingdom.actors.ValuableMiner;
import kingdom.actors.ValuableTransporter;
import kingdom.deposit.Deposit;
import kingdom.treasureroom.TreasureRoom;
import kingdom.treasureroom.TreasureRoomGuardsman;
import kingdom.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DepositAdapterTest
{
  public static void main(String[] args)
  {
    Logger logger = Logger.getInstance();
    logger.log("Starting Producer-Consumer pattern test");

    // Create a deposit with capacity of 15 items
    Deposit deposit = new Deposit(15);

    // Create miners (producers)
    List<ValuableMiner> miners = new ArrayList<>();
    miners.add(new ValuableMiner("Miner-1", deposit, 10, 50, 1000)); // Values 10-50, mines every ~1 second
    miners.add(new ValuableMiner("Miner-2", deposit, 20, 40, 1500)); // Values 20-40, mines every ~1.5 seconds

    //Create TreasureRoom so valuables have a destination
    TreasureRoom treasureRoom = new TreasureRoom();

    //Create Guardsman controls access to TreasureRoom
    TreasureRoomGuardsman guardsman = new TreasureRoomGuardsman(treasureRoom);

    // Create transporters (consumers)
    List<ValuableTransporter> transporters = new ArrayList<>();
    transporters.add(
        new ValuableTransporter("Transporter-1", deposit, 50, 100, 3000,guardsman)); // Goal 50-100, transports every ~3 seconds
    transporters.add(
        new ValuableTransporter("Transporter-2", deposit, 80, 150, 5000,guardsman)); // Goal 80-150, transports every ~5 seconds

    // Create and start miner threads
    List<Thread> minerThreads = new ArrayList<>();
    for (ValuableMiner miner : miners)
    {
      Thread thread = new Thread(miner);
      minerThreads.add(thread);
      thread.start();
    }

    // Create and start transporter threads
    List<Thread> transporterThreads = new ArrayList<>();
    for (ValuableTransporter transporter : transporters)
    {
      Thread thread = new Thread(transporter);
      transporterThreads.add(thread);
      thread.start();
    }
    // Wait for user input to stop the simulation
    logger.log("Mining system started! Press Enter to stop the simulation...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    scanner.close();
    // Stop all miners and transporters
    logger.log("Stopping miners...");
    for (ValuableMiner miner : miners)
    {
      miner.stop();
    }

    logger.log("Stopping transporters...");
    for (ValuableTransporter transporter : transporters)
    {
      transporter.stop();
    }
    // Wait for all threads to finish
    try
    {
      for (Thread thread : minerThreads)
      {
        thread.join(2000); // Wait up to 2 seconds for each thread
      }

      for (Thread thread : transporterThreads)
      {
        thread.join(2000); // Wait up to 2 seconds for each thread
      }
    }
    catch (InterruptedException e)
    {
      logger.log("Main thread interrupted while waiting for workers to stop");
    }

    logger.log("Mining system shutdown complete!");
  }
}