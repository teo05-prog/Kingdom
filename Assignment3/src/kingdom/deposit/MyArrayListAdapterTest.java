package kingdom.deposit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import kingdom.deposit.MyArrayListAdapter;
import kingdom.valuables.Diamond;
import kingdom.valuables.GoldNugget;
import kingdom.valuables.Ruby;
import kingdom.valuables.Valuable;
import org.junit.Before;
import org.junit.Test;

public class MyArrayListAdapterTest
{
  private MyArrayListAdapter adapter;
  private final int CAPACITY = 5;

  @Before
  public void setUp() {
    // Create a fresh adapter before each test
    adapter = new MyArrayListAdapter(CAPACITY);
  }

  @Test
  public void testAdd() {
    Valuable diamond = new Diamond(30);
    boolean result = adapter.add(diamond);

    assertTrue("Should successfully add an item", result);
    assertEquals("Size should be 1 after adding one item", 1, adapter.size());
    assertEquals("Should retrieve the same item", diamond, adapter.get(0));
  }

  @Test
  public void testAddWhenFull() {
    // Fill the adapter to capacity
    for (int i = 0; i < CAPACITY; i++) {
      adapter.add(new Ruby(10 + i));
    }

    // Try to add one more
    boolean result = adapter.add(new Diamond(50));

    assertFalse("Should fail to add when at capacity", result);
    assertEquals("Size should remain at capacity", CAPACITY, adapter.size());
  }

  @Test
  public void testRemove() {
    Valuable diamond = new Diamond(30);
    Valuable ruby = new Ruby(20);
    adapter.add(diamond);
    adapter.add(ruby);

    Valuable removed = adapter.remove(0);

    assertEquals("Should remove the correct item", diamond, removed);
    assertEquals("Size should decrease after removal", 1, adapter.size());
    assertEquals("Remaining item should shift", ruby, adapter.get(0));
  }

  @Test
  public void testGet() {
    Valuable diamond = new Diamond(30);
    adapter.add(diamond);

    Valuable retrieved = adapter.get(0);

    assertEquals("Should retrieve the same item", diamond, retrieved);
    assertEquals("Size should remain unchanged", 1, adapter.size());
  }

  @Test
  public void testIsEmpty() {
    assertTrue("New adapter should be empty", adapter.isEmpty());

    adapter.add(new Ruby(20));
    assertFalse("Adapter with items should not be empty", adapter.isEmpty());

    adapter.remove(0);
    assertTrue("Adapter should be empty after removing all items", adapter.isEmpty());
  }

  @Test
  public void testIsFull() {
    assertFalse("New adapter should not be full", adapter.isFull());

    // Fill to capacity
    for (int i = 0; i < CAPACITY; i++) {
      adapter.add(new GoldNugget(10));
    }

    assertTrue("Adapter at capacity should be full", adapter.isFull());

    adapter.remove(0);
    assertFalse("Adapter below capacity should not be full", adapter.isFull());
  }

  @Test
  public void testSize() {
    assertEquals("New adapter should have size 0", 0, adapter.size());

    adapter.add(new Diamond(30));
    assertEquals("Size should be 1 after adding one item", 1, adapter.size());

    adapter.add(new Ruby(20));
    assertEquals("Size should be 2 after adding two items", 2, adapter.size());

    adapter.remove(0);
    assertEquals("Size should be 1 after removing an item", 1, adapter.size());
  }

  @Test
  public void testAddMultipleValuables() {
    Valuable diamond = new Diamond(30);
    Valuable ruby = new Ruby(25);
    Valuable nugget = new GoldNugget(15);

    adapter.add(diamond);
    adapter.add(ruby);
    adapter.add(nugget);

    assertEquals("Should have correct size after adding multiple items", 3, adapter.size());
    assertEquals("Should retrieve items in correct order", diamond, adapter.get(0));
    assertEquals("Should retrieve items in correct order", ruby, adapter.get(1));
    assertEquals("Should retrieve items in correct order", nugget, adapter.get(2));
  }

}