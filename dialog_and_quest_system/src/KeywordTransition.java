import java.util.TreeMap;

public class KeywordTransition extends DialogTransition {
    private CommonQuestionStorage commonQuestionStorage = new CommonQuestionStorage();
    private int[] keyword_ids = new int[]{};        //may be empty

    public KeywordTransition() {

    }

    public KeywordTransition(int id, int parent_id, int child_id, boolean locked,
                             RunnableScript script, int answer_id, int[] keyword_ids,
                             TreeMap<Integer, String> questions, TreeMap<Integer, String> answers,
                             CommonQuestionStorage commonQuestionStorage) {
        this.id = id;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.locked = locked;
        this.script = script;
        this.answer_id = answer_id;
        this.keyword_ids = keyword_ids;
        this.questions = questions;
        this.answers = answers;
        this.commonQuestionStorage = commonQuestionStorage;
        this.question_id = 0;
    }

    public boolean checkKeywords(int[] keyword_ids) {
        if (keyword_ids.length != this.keyword_ids.length) {
            return false;
        }
        for (int income_keyword_id : keyword_ids) {
            boolean keyword_found = false;
            for (int keyword_id : this.keyword_ids) {
                if (keyword_id == income_keyword_id) {
                    keyword_found = true;
                }
            }
            if (!keyword_found) {
                return false;
            }
        }
        return true;
    }

    public int getQuestionId() {
        return 0;
    }

    public String getQuestion() {
        if (commonQuestionStorage.isAvailableQuestion(keyword_ids)) {
            return commonQuestionStorage.toString(keyword_ids);
        }
        return questions.get(question_id);
    }
}
