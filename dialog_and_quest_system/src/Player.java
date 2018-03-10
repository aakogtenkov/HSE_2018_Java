public class Player extends NPC {
    private CommandLine commandLine;

    public Player(CommandLine commandLine) {
        this.commandLine = commandLine;
        this.id = 0;
    }

    public void process() {
        if (!is_in_dialog) {
            commandLine.process();
        }
    }

    public void startDialog() {
        is_in_dialog = true;
    }
}
