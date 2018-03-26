package main.java;

import java.util.TreeMap;

public class CommandLine implements Processable{
    private TreeMap<Integer, NPC> npcs;

    public CommandLine(TreeMap<Integer, NPC> npcs) {
        this.npcs = npcs;
    }

    public void process() {
        String s = InputOutputHelper.getInstance().readline();
        String[] substr = InputOutputHelper.splitString(s, ' ');
        if (substr.length == 1 && substr[0].equals("/help")) {
            System.out.println("Available commands:");
            System.out.println("    /help");
            System.out.println("    /talk [npc id]");
            System.out.println("    /npc_list");
            System.out.println("    /show_inventory [npc id]");
            System.out.println("    /add_item [npc id] [item id] [number of items]");
            System.out.println("    /remove_item [npc id] [item id] [number of items]");
        }
        else if (substr.length == 2 && substr[0].equals("/talk")) {
            try {
                int npc_id = Integer.parseInt(substr[1]);
                if (npc_id != 0) {
                    npcs.get(0).startDialog();
                    npcs.get(npc_id).startDialog();
                    System.out.println("In dialog with " + npcs.get(npc_id).getName() + ".\n");
                }
            } catch (Exception e) {
                System.out.println("Unknown command (use '/talk [npc id]')");
            }
        }
        else if (substr.length == 1 && substr[0].equals("/npc_list")) {
            for (NPC npc : npcs.values()) {
                System.out.print(npc.getId() + " " + npc.getName() + "\n");
            }
        }
        else if (substr.length == 2 && substr[0].equals("/show_inventory")) {
            try {
                int npc_id = Integer.parseInt(substr[1]);
                npcs.get(npc_id).getInventory().showInventory();
            } catch (Exception e) {
                System.out.println("Unknown command (use '/show_inventory [npc id]')");
            }
        }
        else if (substr.length == 4 && substr[0].equals("/add_item")) {
            try {
                int npc_id = Integer.parseInt(substr[1]);
                int item_id = Integer.parseInt(substr[2]);
                int number = Math.max(0, Integer.parseInt(substr[3]));
                npcs.get(npc_id).addItem(item_id, number);
                System.out.println("Adding " + String.valueOf(number) + " item(s) with id " + String.valueOf(item_id));
            } catch (Exception e) {
                System.out.println("Unknown command (use '/add_item [npc id] [item id] [number of items]')");
            }
        }
        else if (substr.length == 4 && substr[0].equals("/remove_item")) {
            try {
                int npc_id = Integer.parseInt(substr[1]);
                int item_id = Integer.parseInt(substr[2]);
                int number = Math.max(0, Integer.parseInt(substr[3]));
                npcs.get(npc_id).removeItem(item_id, number);
                System.out.println("Removing " + String.valueOf(number) + " item(s) with id " + String.valueOf(item_id));
            } catch (Exception e) {
                System.out.println("Unknown command (use '/remove_item [npc id] [item id] [number of items]')");
            }
        }
        else {
            System.out.println("Unknown command.");
        }
    }
}
