import java.util.TreeMap;

public class KeywordTransition extends DialogTransition {
    private CommonQuestionStorage commonQuestionStorage = new CommonQuestionStorage();
    private int[] keyword_ids = new int[]{};        //may be empty

    public KeywordTransition(int id, int parent_id, int child_id, boolean locked,
                             RunnableScript script, int answer_id, int[] keyword_ids,
                             TreeMap<Integer, String> answers,
                             CommonQuestionStorage commonQuestionStorage) {
        super(id, parent_id, child_id, locked, script, answer_id, answers);
        this.commonQuestionStorage = commonQuestionStorage;
        this.keyword_ids = keyword_ids;
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

    public String getQuestion() {
        if (commonQuestionStorage.isAvailableQuestion(keyword_ids)) {
            return commonQuestionStorage.toString(keyword_ids);
        }
        return "";
    }
}
