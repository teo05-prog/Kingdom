package kingdom.treasureroom;

import kingdom.valuables.Valuable;

import java.util.ArrayList;
import java.util.List;

public class TreasureRoom implements TreasureRoomReadOnly
{
  private List<Valuable> valuables;

  public TreasureRoom(){
    valuables = new ArrayList<>();
  }

  @Override public List<Valuable> lookAtValuables()
  {
    return new ArrayList<>(valuables);
  }

  public void addValuable(Valuable valuable){
    valuables.add(valuable);
  }

  public Valuable retrieveValuable(){
    if (!valuables.isEmpty()){
      return valuables.remove(0);
    }
    return null;
  }
}
