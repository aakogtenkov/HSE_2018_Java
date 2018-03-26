package main.java;

public class DialogTransitionScript implements RunnableScript {
    private boolean set_locked;
    private int npc_id;
    private int transition_id;
    private DialogSystem dialogSystem;

    public DialogTransitionScript(DialogSystem dialogSystem, boolean set_locked, int npc_id, int transition_id) {
        this.dialogSystem = dialogSystem;
        this.set_locked = set_locked;
        this.npc_id = npc_id;
        this.transition_id = transition_id;
    }

    public void run() {
        if (set_locked) {
            dialogSystem.lock_transition(npc_id, transition_id);
        } else {
            dialogSystem.unlock_transition(npc_id, transition_id);
        }
    }

    public boolean isSuccess() {
        return true;
    }
}
