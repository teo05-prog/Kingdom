package kingdom.valuables;

public class Diamond implements Valuable
{
  private final int value;

  public Diamond(int value)
  {
    this.value = value;
  }

  @Override public int getValue()
  {
    return value;
  }

  @Override public String getType()
  {
    return "Diamond";
  }

  @Override public String toString()
  {
    return "Diamond (value=" + value + ")";
  }
}