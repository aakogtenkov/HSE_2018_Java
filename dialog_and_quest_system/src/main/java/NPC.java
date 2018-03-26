package main.java;

import java.io.BufferedReader;

public class NPC extends Character {
    protected boolean is_in_dialog = false;
    private NPCDialog dialog;
    protected int id;

    public NPC() {
        dialog = null;
        id = -1;
    }

    public NPC(NPCDialog dialog, int id) {
        this.dialog = dialog;
        this.id = id;
    }

    public NPC(Inventory inventory, NPCDialog dialog, int id) {
        this.inventory = inventory;
        this.dialog = dialog;
        this.id = id;
    }

    public void load(final InventoryBuilder inventoryBuilder) {
        BufferedReader input = InputOutputHelper.getInstance().openFile("./data/characters/npc-" + String.valueOf(id) + "-info.txt");
        String s;
        try {
            s = input.readLine().trim();
            String[] substr = InputOutputHelper.splitString(s, ' ');
            if (substr.length == 2 && substr[0].equals("name")) {
                this.name = substr[1];
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);
        this.inventory = inventoryBuilder.buildInventory("./data/inventory/npc-" + String.valueOf(id) + "-inventory.txt");
    }

    public int getId() {
        return id;
    }

    public void startDialog() {
        is_in_dialog = true;
        dialog.resetDialog();
    }

    public void endDialog() {
        is_in_dialog = false;
    }

    public boolean isInDialog() {
        return is_in_dialog;
    }

    @Override
    public void process() {
        if (is_in_dialog) {
            dialog.process();
        }
    }
}
