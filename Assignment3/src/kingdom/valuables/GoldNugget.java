package kingdom.valuables;

public class GoldNugget implements Valuable
{
  private final int value;

  public GoldNugget(int value)
  {
    this.value = value;
  }

  @Override public int getValue()
  {
    return value;
  }

  @Override public String getType()
  {
    return "Gold Nugget";
  }


  @Override public String toString()
  {
    return "Gold Nugget (value=" + value + ")";
  }
}
