public class NPC extends Character {
    protected boolean is_in_dialog = false;
    private NPCDialog dialog;
    protected int id;

    public NPC() {
        dialog = null;
        id = -1;
    }

    public NPC(Inventory inventory, NPCDialog dialog, int id) {
        this.inventory = inventory;
        this.dialog = dialog;
        this.id = id;
    }

    public void load(String name) {
        //TODO: implement
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
