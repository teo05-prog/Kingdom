package kingdom.treasureroom;

import kingdom.utils.Logger;

public class TreasureRoomGuardsman implements TreasureRoomDoor
{
  private final TreasureRoom treasureRoom;
  private int readers = 0;
  private boolean writerActive = false;

  public TreasureRoomGuardsman(TreasureRoom treasureRoom){
    this.treasureRoom = treasureRoom;
  }

  @Override public synchronized TreasureRoomReadOnly acquireReadAccess()
  {
    while (writerActive){
      try{
        wait();
      }catch (InterruptedException e){
        Thread.currentThread().interrupt();
      }
    }
    readers++;
    Logger.getInstance().log("Reader acquired access. Active readers: "+ readers);
    return new TreasureRoomReadProxy(treasureRoom, this);
  }

  @Override public synchronized TreasureRoom acquireWriteAccess()
  {
    while (writerActive || readers>0){
      try{
        wait();
      }catch (InterruptedException e){
        Thread.currentThread().interrupt();
      }
    }
    writerActive = true;
    Logger.getInstance().log("Writer acquired access.");
    return treasureRoom;
  }

  @Override public synchronized void releaseReadAccess()
  {
    readers--;
    Logger.getInstance().log("Reader released access. Remaining readers: "+readers);
    if (readers == 0){
      notifyAll();
    }
  }

  @Override public synchronized void releaseWriteAccess()
  {
    writerActive = false;
    Logger.getInstance().log("Writer released access.");
    notifyAll();
  }
}
