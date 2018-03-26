package main.java;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class Main {
    private DialogSystem dialogSystem;
    private TreeMap<Integer, NPC> characters;
    private Player player;
    private InventoryBuilder inventoryBuilder;
    private InputOutputHelper inputOutputHelper;

    private void init() {
        inputOutputHelper = new InputOutputHelper("./charset_info.txt");

        characters = new TreeMap<>();
        player = new Player(new CommandLine(characters));
        dialogSystem = new DialogSystem();
        inventoryBuilder = new InventoryBuilder();
        inventoryBuilder.load("./data/inventory/items.txt");

        player.load(inventoryBuilder);
        characters.put(0, player);

        BufferedReader input = inputOutputHelper.openFile("./data/npc_info.txt");
        String s;
        ArrayList<Integer> npc_ids = new ArrayList<>();
        try {
            while ((s = input.readLine()) != null) {
                npc_ids.add(Integer.parseInt(s));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);

        Integer[] npc_ids_array = new Integer[npc_ids.size()];
        npc_ids_array = npc_ids.toArray(npc_ids_array);
        dialogSystem.load(npc_ids_array, characters);

        for (int npc_id : npc_ids) {
            NPC npc = new NPC(dialogSystem.getDialog(npc_id), npc_id);
            npc.load(inventoryBuilder);
            characters.put(npc_id, npc);
        }
    }

    private void run() {
        while (true) {
            player.process();
            for (Character character : characters.values()) {
                character.process();
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        main.run();
    }
}
