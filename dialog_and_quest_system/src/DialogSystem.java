import java.util.TreeMap;

public class DialogSystem {
    private TreeMap<Integer, NPCDialog> npc_dialogs = new TreeMap<>();
    private CommonQuestionStorage commonQuestionStorage = new CommonQuestionStorage();

    public void load(int[] npc_ids, TreeMap<Integer, NPC> npcs) {
        commonQuestionStorage.load("./data/keywords/keywords.txt");
        for (int i = 0; i < npc_ids.length; i++) {
            NPCDialog dialog = new NPCDialog(commonQuestionStorage);
            dialog.load("./data/dialogs/npc-" + String.valueOf(npc_ids[i]), this, npcs);
            npc_dialogs.put(npc_ids[i], dialog);
        }
    }

    public NPCDialog getDialog(int npc_id) {
        return npc_dialogs.get(npc_id);
    }

    public void lock_transition(int npc_id, int transition_id) {
        System.out.println(npc_id);
        System.out.println(transition_id);
        npc_dialogs.get(npc_id).lock_transition(transition_id);
    }

    public void unlock_transition(int npc_id, int transition_id) {
        npc_dialogs.get(npc_id).unlock_transition(transition_id);
    }

    public void lock_keyword(int keyword_id) {
        commonQuestionStorage.lockKeyword(keyword_id);
    }

    public void unlock_keyword(int keyword_id) {
        commonQuestionStorage.unlockKeyword(keyword_id);
    }
}
