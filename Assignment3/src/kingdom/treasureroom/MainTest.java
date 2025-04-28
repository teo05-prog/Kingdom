package kingdom.treasureroom;

public class MainTest
{
  public static void main(String[] args)
  {
    TreasureRoom treasureRoom = new TreasureRoom();
    TreasureRoomGuardsman guardsman = new TreasureRoomGuardsman(treasureRoom);

    new Thread(()->{
      TreasureRoomReadOnly readAccess = guardsman.acquireReadAccess();
      System.out.println("Reader: " +readAccess.lookAtValuables());
      ((TreasureRoomReadProxy) readAccess).release();
    }).start();
    new Thread(()->{
      TreasureRoom writeAccess = guardsman.acquireWriteAccess();
      writeAccess.addValuable(null);
      guardsman.acquireWriteAccess();
    }).start();
  }
}
