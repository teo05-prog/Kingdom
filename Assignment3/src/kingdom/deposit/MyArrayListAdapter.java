package kingdom.deposit;

import kingdom.valuables.Valuable;

import java.util.ArrayList;
import java.util.List;

public class MyArrayListAdapter
{
  private List<Valuable> list;
  private int capacity;

  public MyArrayListAdapter(int capacity)
  {
    this.list = new ArrayList<>(capacity);
    this.capacity = capacity;
  }

  public synchronized boolean add(Valuable valuable)
  {
    if (list.size() >= capacity)
    {
      return false;
    }
    return list.add(valuable);
  }

  public synchronized Valuable remove(int index)
  {
    return list.remove(index);
  }

  public synchronized Valuable get(int index)
  {
    return list.get(index);
  }

  public synchronized int size()
  {
    return list.size();
  }

  public synchronized boolean isEmpty()
  {
    return list.isEmpty();
  }

  public synchronized boolean isFull()
  {
    return list.size() >= capacity;
  }
}
