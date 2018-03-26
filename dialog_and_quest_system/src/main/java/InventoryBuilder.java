package main.java;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.TreeMap;

public class InventoryBuilder {
    private TreeMap<Integer, String> item_id_to_name = new TreeMap<>();
    private HashMap<String, Integer> item_name_to_id = new HashMap<>();

    public void load(String filename) {
        String[] separators;
        BufferedReader input = InputOutputHelper.getInstance().openFile("./data/separators.txt");
        String s;
        try {
            s = input.readLine();
            separators = s.trim().split("");
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);

        item_id_to_name = new TreeMap<>();
        item_name_to_id = new HashMap<>();
        input = InputOutputHelper.getInstance().openFile(filename);
        try {
            while ((s = input.readLine()) != null) {
                String[] substr = InputOutputHelper.splitString(s, separators[0].charAt(0));
                if (substr.length >= 2) {
                    int id = Integer.parseInt(substr[0]);
                    item_id_to_name.put(id, substr[1]);
                    item_name_to_id.put(substr[1], id);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);
    }

    public Inventory buildInventory(String filename) {
        Inventory inventory = new Inventory(item_id_to_name, item_name_to_id);
        inventory.load(filename);
        return inventory;
    }
}
