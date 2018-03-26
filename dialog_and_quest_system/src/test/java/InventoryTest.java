package test.java;

import main.java.Inventory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.TreeMap;

public class InventoryTest extends Assert {
    private Inventory inventory;

    @Before
    public void prepareInventory() {
        TreeMap<Integer, String> item_id_to_name = new TreeMap<>();
        HashMap<String, Integer> item_name_to_id = new HashMap<>();
        item_id_to_name.put(1, "item1");
        item_id_to_name.put(-10, "item-10");
        item_id_to_name.put(100, "item100");
        item_name_to_id.put("item1", 1);
        item_name_to_id.put("item-10", -10);
        item_name_to_id.put("item100", 100);
        inventory = new Inventory(item_id_to_name, item_name_to_id);
    }

    @Test
    public void testAddItem() {
        inventory.addItem(1, 1);
        assertEquals(1, inventory.getItemNumber(1));
        assertEquals(0, inventory.getItemNumber(100));

        inventory.addItem(-10, 179);
        assertEquals(179, inventory.getItemNumber(-10));
        assertEquals(1, inventory.getItemNumber(1));

        inventory.addItem("item1", 100);
        assertEquals(179, inventory.getItemNumber(-10));
        assertEquals(101, inventory.getItemNumber(1));

        inventory.addItem("item100", -10);
        assertEquals(0, inventory.getItemNumber(100));

        inventory.addItem("item100", 10);
        assertEquals(0, inventory.getItemNumber(10));
    }

    @Test
    public void testDeleteItem() {
        inventory.addItem(1, 1);
        inventory.addItem(100, 10);
        inventory.addItem(-10, 179);

        inventory.deleteItem(100, 2);
        assertEquals(8, inventory.getItemNumber(100));

        inventory.deleteItem(-10, 10);
        assertEquals(169, inventory.getItemNumber(-10));

        inventory.deleteItem(-10, 0);
        assertEquals(169, inventory.getItemNumber(-10));

        inventory.deleteItem(-10, -1);
        assertEquals(169, inventory.getItemNumber(-10));

        inventory.deleteItem(1, 20);
        assertEquals(0, inventory.getItemNumber(1));
    }
}
