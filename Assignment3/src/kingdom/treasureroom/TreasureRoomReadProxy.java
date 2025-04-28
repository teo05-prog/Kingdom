package kingdom.treasureroom;

import kingdom.utils.Logger;
import kingdom.valuables.Valuable;

import java.util.List;

public class TreasureRoomReadProxy implements TreasureRoomReadOnly
{

  private final TreasureRoomReadOnly realTreasureRoom;
  private final TreasureRoomGuardsman guardsman;
  private boolean accessReleased = false;

  public TreasureRoomReadProxy(TreasureRoomReadOnly realTreasureRoom, TreasureRoomGuardsman guardsman)
  {
    this.realTreasureRoom = realTreasureRoom;
    this.guardsman = guardsman;
  }

  @Override public List<Valuable> lookAtValuables()
  {
    if (accessReleased){
      throw new IllegalStateException("Access has been released!");
    }
    return realTreasureRoom.lookAtValuables();
  }

  public void release(){
    if (!accessReleased){
      Logger.getInstance().log("Read Proxy released.");
      accessReleased = true;
      guardsman.releaseReadAccess();
    }
  }
}
