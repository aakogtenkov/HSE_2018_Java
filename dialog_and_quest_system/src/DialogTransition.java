import java.util.TreeMap;

public abstract class DialogTransition {
    private boolean locked;
    private int child_id;
    private int parent_id;
    private int id;
    private RunnableScript script;
    private int answer_id;
    private TreeMap<Integer, String> answers = new TreeMap<>();

    public DialogTransition(int id, int parent_id, int child_id, boolean locked, RunnableScript script, int answer_id, TreeMap<Integer, String> answers) {
        this.locked = locked;
        this.child_id = child_id;
        this.parent_id = parent_id;
        this.id = id;
        this.script = script;
        this.answer_id = answer_id;
        this.answers = answers;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getChild() {
        return child_id;
    }

    public int getParent() {
        return parent_id;
    }

    protected TreeMap<Integer, String> getAnswers() {
        return answers;
    }

    public int getId() {
        return id;
    }

    public int getAnswerId() {
        return answer_id;
    }

    public String getAnswer() {
        return answers.get(answer_id);
    }

    abstract public String getQuestion();

    public String makeTransition() {
        if (script != null) {
            script.run();
        }
        return answers.get(answer_id);
    }
}
