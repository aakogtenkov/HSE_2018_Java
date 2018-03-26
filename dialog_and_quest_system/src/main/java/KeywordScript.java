package main.java;

public class KeywordScript implements RunnableScript {
    private DialogSystem dialogSystem;
    private boolean set_locked;
    private int keyword_id;

    public KeywordScript(DialogSystem dialogSystem, boolean set_locked, int keyword_id) {
        this.dialogSystem = dialogSystem;
        this.set_locked = set_locked;
        this.keyword_id = keyword_id;
    }

    public void run() {
        if (set_locked) {
            dialogSystem.lock_keyword(keyword_id);
        }
        else {
            dialogSystem.unlock_keyword(keyword_id);
        }
    }

    public boolean isSuccess() {
        return true;
    }
}
