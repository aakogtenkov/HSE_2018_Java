import java.util.ArrayList;
import java.util.TreeMap;

public class Script implements RunnableScript{
    private ArrayList<RunnableScript> basic_scripts = new ArrayList<>();
    private boolean result = true;

    public Script(ArrayList<String> script_description, DialogSystem dialogSystem, TreeMap<Integer, NPC> npcs) {
        for (String s : script_description) {
            String[] substr = InputOutputHelper.splitString(s, ' ');
            if (substr.length == 3 && substr[0].equals("lock_transition")) {
                basic_scripts.add(new DialogTransitionScript(dialogSystem, true, Integer.parseInt(substr[1]), Integer.parseInt(substr[2])));
            }
            else if (substr.length == 3 && substr[0].equals("unlock_transition")) {
                basic_scripts.add(new DialogTransitionScript(dialogSystem, false, Integer.parseInt(substr[1]), Integer.parseInt(substr[2])));
            }
            else if (substr.length == 2 && substr[0].equals("lock_keyword")) {
                basic_scripts.add(new KeywordScript(dialogSystem, true, Integer.parseInt(substr[1])));
            }
            else if (substr.length == 2 && substr[0].equals("unlock_keyword")) {
                basic_scripts.add(new KeywordScript(dialogSystem, false, Integer.parseInt(substr[1])));
            }
            else if (substr.length == 2 && substr[0].equals("start_dialog")) {
                basic_scripts.add(new DialogPhaseScript(npcs, Integer.parseInt(substr[1]), 1));
            }
            else if (substr.length == 2 && substr[0].equals("end_dialog")) {
                basic_scripts.add(new DialogPhaseScript(npcs, Integer.parseInt(substr[1]), 0));
            }
            else if (substr.length == 4 && substr[0].equals("add_item")) {
                basic_scripts.add(new InventoryScript(npcs, Integer.parseInt(substr[1]), Integer.parseInt(substr[2]), Integer.parseInt(substr[3]), 0));
            }
            else if (substr.length == 4 && substr[0].equals("check_item")) {
                basic_scripts.add(new InventoryScript(npcs, Integer.parseInt(substr[1]), Integer.parseInt(substr[2]), Integer.parseInt(substr[3]), 1));
            }
            else {
                System.out.println("WARNING: invalid script detected: " + s);
            }
        }
    }

    public void run() {
        result = true;
        for (RunnableScript script : basic_scripts) {
            if (result) {
                script.run();
                result = script.isSuccess();
            }
        }
    }

    public boolean isSuccess() {
        return true;
    }
}
