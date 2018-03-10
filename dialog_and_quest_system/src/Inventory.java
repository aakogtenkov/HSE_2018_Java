import java.io.BufferedReader;
import java.util.HashMap;
import java.util.TreeMap;

public class Inventory {
    TreeMap<Integer, Integer> items = new TreeMap<>();
    TreeMap<Integer, String> item_id_to_name;
    HashMap<String, Integer> item_name_to_id;

    public Inventory(TreeMap<Integer, String> item_id_to_name, HashMap<String, Integer> item_name_to_id) {
        this.item_id_to_name = item_id_to_name;
        this.item_name_to_id = item_name_to_id;
    }

    public void load(String filename) {
        String s;
        BufferedReader input = InputOutputHelper.openFile(filename);
        try {
            while ((s = input.readLine()) != null) {
                String[] substr = InputOutputHelper.splitString(s, ' ');
                if (substr.length >= 2) {
                    int id = Integer.parseInt(substr[0]);
                    int num = Integer.parseInt(substr[1]);
                    num = Math.max(0, num);
                    if (item_name_to_id.containsValue(id)) {
                        items.put(id, num);
                    }
                    else {
                        throw new RuntimeException();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);
    }

    public void addItem(int item_id, int number) {
        number = Math.max(0, number);
        if (!items.containsKey(item_id)) {
            items.put(item_id, 0);
        }
        items.replace(item_id, items.get(item_id) + number);
    }

    public void addItem(String item, int number) {
        number = Math.max(0, number);
        int item_id = item_name_to_id.get(item);
        if (!items.containsKey(item_id)) {
            items.put(item_id, 0);
        }
        items.replace(item_id, items.get(item_id) + number);
    }

    public void deleteItem(int item_id, int number) {
        number = Math.max(0, number);
        if (!items.containsKey(item_id)) {
            return;
        }
        items.replace(item_id, Math.max(0, items.get(item_id) - number));
    }

    public void deleteItem(String item, int number) {
        number = Math.max(0, number);
        int item_id = item_name_to_id.get(item);
        if (!items.containsKey(item_id)) {
            return;
        }
        items.replace(item_id, Math.max(0, items.get(item_id) - number));
    }

    public boolean checkItemNumber(int item_id, int number) {
        number = Math.max(0, number);
        if (!items.containsKey(item_id)) {
            return false;
        }
        return (items.get(item_id) - number >= 0);
    }

    public String showInventory() {
        StringBuilder result = new StringBuilder();
        for (Integer item : items.keySet()) {
            result.append("  ");
            result.append(item_id_to_name.get(item));
            result.append(": ");
            result.append(items.get(item));
            result.append("\n");
        }
        return result.toString();
    }
}
