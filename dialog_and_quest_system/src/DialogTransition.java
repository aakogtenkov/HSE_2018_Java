import java.util.TreeMap;

public class DialogTransition {
    protected int id = 0;
    protected int parent_id = 0;
    protected int child_id = 0;
    protected boolean locked = false;
    protected RunnableScript script = null;
    protected int question_id = 0;
    protected int answer_id = 0;
    protected TreeMap<Integer, String> questions = new TreeMap<>();
    protected TreeMap<Integer, String> answers = new TreeMap<>();

    public DialogTransition() {

    }

    public DialogTransition(int id, int parent_id, int child_id, boolean locked,
                            RunnableScript script, int question_id, int answer_id,
                            TreeMap<Integer, String> questions, TreeMap<Integer, String> answers) {
        this.id = id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.locked = locked;
        this.script = script;
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.questions = questions;
        this.answers = answers;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public int getChild() {
        return child_id;
    }

    public int getParent() {
        return parent_id;
    }

    public int getId() {
        return id;
    }

    public String makeTransition() {
        if (script != null) {
            script.run();
        }
        return answers.get(answer_id);
    }

    public boolean isLocked() {
        return locked;
    }

    public int getQuestionId() {
        return question_id;
    }

    public int getAnswerId() {
        return answer_id;
    }

    public String getQuestion() {
        return questions.get(question_id);
    }
}
