package kingdom.treasureroom;

public interface TreasureRoomDoor
{
  TreasureRoomReadOnly acquireReadAccess();
  TreasureRoom acquireWriteAccess();
  void releaseReadAccess();
  void releaseWriteAccess();
}
