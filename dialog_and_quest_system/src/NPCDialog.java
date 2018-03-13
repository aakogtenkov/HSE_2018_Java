import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class NPCDialog {
    private String npcName = "";
    private ArrayList<DialogTransition> transitions = new ArrayList<>();
    private ArrayList<KeywordTransition> keyword_transitions = new ArrayList<>();
    private int cur_node_id = 0;
    private int start_node_id = 0;
    private ArrayList<DialogTransition> cur_questions = new ArrayList<>();
    private ArrayList<KeywordTransition> cur_keyword_questions = new ArrayList<>();
    private ArrayList<String> unknown_question_answers = new ArrayList<>();
    private int[] cur_keyword_ids = new int[]{};
    private final CommonQuestionStorage commonQuestionStorage;

    public NPCDialog(CommonQuestionStorage commonQuestionStorage) {
        this.commonQuestionStorage = commonQuestionStorage;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public void load(String start_filename, DialogSystem dialogSystem, TreeMap<Integer, NPC> npcs) {
        String[] separators;
        BufferedReader input = InputOutputHelper.openFile("data/separators.txt");
        String s;
        try {
            s = input.readLine();
            separators = s.trim().split("");
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);

        input = InputOutputHelper.openFile(start_filename + "-unknown_answers.txt");
        try {
            while ((s = input.readLine()) != null) {
                unknown_question_answers.add(s);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);

        input = InputOutputHelper.openFile(start_filename + "-questions.txt");
        TreeMap<Integer, String> questions = new TreeMap<>();
        try {
            while ((s = input.readLine()) != null) {
                String[] substr = InputOutputHelper.splitString(s, separators[0].charAt(0));
                if (substr.length == 2) {
                    int id = Integer.parseInt(substr[0]);
                    questions.put(id, substr[1]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);

        input = InputOutputHelper.openFile(start_filename + "-answers.txt");
        TreeMap<Integer, String> answers = new TreeMap<>();
        try {
            while ((s = input.readLine()) != null) {
                String[] substr = InputOutputHelper.splitString(s, separators[0].charAt(0));
                if (substr.length >= 2) {
                    int id = Integer.parseInt(substr[0]);
                    answers.put(id, substr[1]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);

        input = InputOutputHelper.openFile(start_filename + "-dialog_graph.txt");
        try {
            while ((s = input.readLine()) != null) {
                String[] substr = InputOutputHelper.splitString(s, separators[0].charAt(0));
                if (substr.length >= 7) {
                    int id = Integer.parseInt(substr[0]);
                    int parent_id = Integer.parseInt(substr[1]);
                    int child_id = Integer.parseInt(substr[2]);
                    boolean locked = (Integer.parseInt(substr[3]) != 0);
                    int answer_id = Integer.parseInt(substr[4]);
                    int type_question = Integer.parseInt(substr[5]);
                    ArrayList<String> script_info = new ArrayList<>();
                    s = input.readLine();
                    while (!s.equals("</script>")) {
                        if (!s.equals("<script>")) {
                            script_info.add(s);
                        }
                        s = input.readLine();
                    }
                    Script script = new Script(script_info, dialogSystem, npcs);
                    if (type_question == 0) {
                        int question_id = Integer.parseInt(substr[6]);
                        transitions.add(new DialogTransition(id, parent_id, child_id, locked, script, question_id, answer_id, questions, answers));
                    } else if (type_question >= 1) {
                        String[] __keyword_ids = InputOutputHelper.splitString(substr[6], separators[1].charAt(0));
                        int[] keyword_ids = new int[__keyword_ids.length];
                        for (int i = 0; i < __keyword_ids.length; i++) {
                            keyword_ids[i] = Integer.parseInt(__keyword_ids[i]);
                        }
                        keyword_transitions.add(new KeywordTransition(id, parent_id, child_id, locked, script, answer_id, keyword_ids, questions, answers, commonQuestionStorage));
                    }
                }
                else if (substr.length == 2 && substr[0].equals("name")) {
                    this.npcName = substr[1];
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);
    }

    public String getPossibleQuestions(String[] keywords) {
        int[] keyword_ids = new int[keywords.length];
        for (int i = 0; i < keyword_ids.length; i++) {
            if (commonQuestionStorage.isKeyword(keywords[i])) {
                keyword_ids[i] = commonQuestionStorage.keywordToQuestion(keywords[i]);
            }
            else {
                return this.getPossibleQuestions(new int[]{});
            }
        }
        return this.getPossibleQuestions(keyword_ids);
    }

    public String getPossibleQuestions(int[] keyword_ids) {
        cur_questions.clear();
        cur_keyword_questions.clear();
        this.cur_keyword_ids = keyword_ids;
        StringBuilder result = new StringBuilder();
        int counter = 1;
        for (DialogTransition transition : transitions) {
            if (transition.getParent() == cur_node_id && !transition.isLocked()) {
                result.append("  ");
                result.append(counter);
                result.append(". ");
                result.append(transition.getQuestion());
                result.append("\n");
                counter++;
                cur_questions.add(transition);
            }
        }
        if (start_node_id == cur_node_id && commonQuestionStorage.isAvailableQuestion(keyword_ids)) {
            result.append("  ");
            result.append(counter);
            result.append(". ");
            result.append(commonQuestionStorage.toString(keyword_ids));
            result.append("\n");
            counter++;
            for (KeywordTransition keyword_transition : keyword_transitions) {
                if (keyword_transition.getParent() == cur_node_id &&
                        !keyword_transition.isLocked() &&
                        keyword_transition.checkKeywords(keyword_ids)) {
                    cur_keyword_questions.add(keyword_transition);
                }
            }
        }
        if (result.length() == 0) {
        }
        return result.toString();
    }

    public String makeTransition(int num_question) {
        String result;
        num_question--;
        if (num_question >= 0 && num_question < cur_questions.size()) {
            result = ">> " + cur_questions.get(num_question).getQuestion() + "\n";
            result += npcName + ": " + cur_questions.get(num_question).makeTransition() + "\n";
            this.cur_node_id = cur_questions.get(num_question).getChild();
        }
        else if (num_question >= cur_questions.size() && num_question - cur_questions.size() < cur_keyword_questions.size()) {
            num_question -= cur_questions.size();
            result = ">> " + cur_keyword_questions.get(num_question).getQuestion() + "\n";
            result += npcName + ": " + cur_keyword_questions.get(num_question).makeTransition() + "\n";
            this.cur_node_id = cur_keyword_questions.get(num_question).getChild();
        }
        else {
            result = ">>\n";
            result += npcName + ": " + unknown_question_answers.get(0) + "\n";
        }
        this.cur_questions = new ArrayList<>();
        this.cur_keyword_questions = new ArrayList<>();
        this.cur_keyword_ids = new int[]{};
        return result;
    }

    public void resetDialog() {
        this.cur_questions.clear();
        this.cur_node_id = this.start_node_id;
    }

    public void lock_transition(int transition_id) {
        for (DialogTransition transition : transitions) {
            if (transition.getId() == transition_id) {
                transition.lock();
            }
        }
        for (KeywordTransition transition : keyword_transitions) {
            if (transition.getId() == transition_id) {
                transition.lock();
            }
        }
    }

    public void unlock_transition(int transition_id) {
        for (DialogTransition transition : transitions) {
            if (transition.getId() == transition_id) {
                transition.unlock();
            }
        }
        for (KeywordTransition transition : keyword_transitions) {
            if (transition.getId() == transition_id) {
                transition.unlock();
            }
        }
    }

    public void process() {
        System.out.println(this.getPossibleQuestions(cur_keyword_ids));
        System.out.flush();
        String input = InputOutputHelper.readline().toLowerCase();
        String[] substr = InputOutputHelper.splitString(input, ' ');
        if (substr.length == 1) {
            try {
                System.out.println(this.makeTransition(Integer.parseInt(substr[0])));
            } catch (NumberFormatException e) {

            }
        }
        else if (substr.length >= 2) {
            this.getPossibleQuestions(substr);
        }
    }
}
