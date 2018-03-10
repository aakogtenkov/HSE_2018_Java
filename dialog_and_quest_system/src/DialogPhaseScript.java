import java.util.TreeMap;

public class DialogPhaseScript implements RunnableScript {
    private TreeMap<Integer, NPC> npcs;
    private int npc_id;
    private int set_state;

    public DialogPhaseScript(TreeMap<Integer, NPC> npcs, int npc_id, int set_state) {
        this.set_state = set_state;
        this.npcs = npcs;
        this.npc_id = npc_id;
    }

    public void run() {
        if (set_state == 0) {
            npcs.get(npc_id).endDialog();
        }
        else if (set_state == 1) {
            npcs.get(npc_id).startDialog();
        }
    }
}
