import java.util.TreeMap;

public class InventoryScript implements RunnableScript{
    private TreeMap<Integer, NPC> npcs;
    private int npc_id;
    private int item_id;
    private int number;
    private int operation_type;
    private boolean operation_result = true;

    public InventoryScript(TreeMap<Integer, NPC> npcs, int npc_id, int item_id, int number, int operation_type) {
        this.npcs = npcs;
        this.npc_id = npc_id;
        this.item_id = item_id;
        this.number = number;
        this.operation_type = operation_type;
    }

    public void run() {
        if (operation_type == 0) {
            if (number >= 0) {
                npcs.get(npc_id).addItem(item_id, number);
            }
            else {
                npcs.get(npc_id).removeItem(item_id, -number);
            }
        }
        else {
            operation_result = npcs.get(npc_id).getInventory().checkItemNumber(item_id, number);
        }
    }

    public boolean getResult() {
        return operation_result;
    }
}
