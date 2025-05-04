package kingdom;

import kingdom.actors.*;
import kingdom.deposit.Deposit;
import kingdom.treasureroom.TreasureRoom;
import kingdom.treasureroom.TreasureRoomDoor;
import kingdom.treasureroom.TreasureRoomGuardsman;
import kingdom.treasureroom.TreasureRoomReadProxy;
import kingdom.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Logger logger = Logger.getInstance();
    logger.log("Starting the Kingdom simulation");

    // Initialize components
    TreasureRoom treasureRoom = new TreasureRoom();
    TreasureRoomDoor guardsman = new TreasureRoomGuardsman(treasureRoom);
    Deposit deposit = new Deposit(20); // Create deposit with capacity of 20

    // Create actors
    List<Thread> threads = new ArrayList<>();
    List<Object> actors = new ArrayList<>();

    // Create miners
    ValuableMiner miner1 = new ValuableMiner("Miner Bob", deposit, 100, 300, 1000);
    ValuableMiner miner2 = new ValuableMiner("Miner Jack", deposit, 50, 200, 1500);
    actors.add(miner1);
    actors.add(miner2);
    threads.add(new Thread(miner1));
    threads.add(new Thread(miner2));

    // Create valuable transporters
    ValuableTransporter transporter1 = new ValuableTransporter(
        "Transporter Alpha", deposit, 400, 800, 2000, guardsman);
    ValuableTransporter transporter2 = new ValuableTransporter(
        "Transporter Beta", deposit, 300, 600, 2500, guardsman);
    actors.add(transporter1);
    actors.add(transporter2);
    threads.add(new Thread(transporter1));
    threads.add(new Thread(transporter2));

    // Create king
    King king = new King("King Arthur", 500, 1200, 300, 3000, 5000, guardsman);
    actors.add(king);
    threads.add(new Thread(king));

    // Create accountants
    TreasureRoomReadProxy readProxy1 = (TreasureRoomReadProxy) guardsman.acquireReadAccess();
    TreasureRoomReadProxy readProxy2 = (TreasureRoomReadProxy) guardsman.acquireReadAccess();

    Accountant accountant1 = new Accountant("Accountant Smith", readProxy1, 1000, 4000);
    Accountant accountant2 = new Accountant("Accountant Johnson", readProxy2, 1200, 5000);
    actors.add(accountant1);
    actors.add(accountant2);
    threads.add(new Thread(accountant1));
    threads.add(new Thread(accountant2));

    // Start all threads
    logger.log("Starting all actors");
    for (Thread thread : threads) {
      thread.start();
    }

    // Let the simulation run for some time
    try {
      Thread.sleep(60000); // Run for 60 seconds
    } catch (InterruptedException e) {
      logger.log("Main thread interrupted");
    }

    // Stop all actors
    logger.log("Stopping all actors");
    for (Object actor : actors) {
      if (actor instanceof ValuableMiner) {
        ((ValuableMiner) actor).stop();
      } else if (actor instanceof ValuableTransporter) {
        ((ValuableTransporter) actor).stop();
      } else if (actor instanceof King) {
        ((King) actor).stop();
      } else if (actor instanceof Accountant) {
        ((Accountant) actor).stop();
      }
    }

    // Wait for all threads to finish
    try {
      for (Thread thread : threads) {
        thread.join(2000);
      }
    } catch (InterruptedException e) {
      logger.log("Main thread interrupted while waiting for actors to stop");
    }

    logger.log("Kingdom simulation completed");
  }
}