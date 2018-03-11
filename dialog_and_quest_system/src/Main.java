import java.util.TreeMap;

public class Main {
    private DialogSystem dialogSystem;
    private TreeMap<Integer, NPC> characters;
    private Player player;
    private InventoryBuilder inventoryBuilder;

    private void init() {
        characters = new TreeMap<>();
        player = new Player(new CommandLine(characters));
        dialogSystem = new DialogSystem();
        inventoryBuilder = new InventoryBuilder();
        inventoryBuilder.load("./data/inventory/items.txt");

        dialogSystem.load(new int[] {1, 2, 3}, characters);

        player.load(inventoryBuilder);
        characters.put(0, player);
        NPC npc = new NPC(new Inventory(null, null), dialogSystem.getDialog(1), 1);
        npc.load(inventoryBuilder);
        characters.put(1, npc);
        npc = new NPC(new Inventory(null, null), dialogSystem.getDialog(2), 2);
        npc.load(inventoryBuilder);
        characters.put(2, npc);
        npc = new NPC(new Inventory(null, null), dialogSystem.getDialog(3), 3);
        npc.load(inventoryBuilder);
        characters.put(3, npc);
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
