import java.util.TreeMap;

public class UnconditionalTransition extends DialogTransition {
    protected int question_id = 0;
    protected TreeMap<Integer, String> questions = new TreeMap<>();

    public UnconditionalTransition(int id, int parent_id, int child_id, boolean locked,
                                   RunnableScript script, int question_id, int answer_id,
                                   TreeMap<Integer, String> questions, TreeMap<Integer, String> answers) {
        super(id, parent_id, child_id, locked, script, answer_id, answers);
        this.question_id = question_id;
        this.questions = questions;
    }

    public String getQuestion() {
        return questions.get(question_id);
    }
}
