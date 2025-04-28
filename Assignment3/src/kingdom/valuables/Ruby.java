package kingdom.valuables;

public class Ruby implements Valuable
{
  private final int value;

  public Ruby(int value)
  {
    this.value = value;
  }

  @Override public int getValue()
  {
    return value;
  }

  @Override public String getType()
  {
    return "Ruby";
  }

  @Override public String toString()
  {
    return "Ruby (value=" + value + ")";
  }
}
