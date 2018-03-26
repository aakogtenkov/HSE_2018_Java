package main.java;

import java.io.*;
import java.util.HashMap;
import java.util.TreeMap;

public class CommonQuestionStorage {
    private TreeMap<Integer, Boolean> keyword_locked = new TreeMap<>();
    private TreeMap<Integer, String> question_parts = new TreeMap<>();
    private TreeMap<Integer, Integer> part_type = new TreeMap<>();
    private HashMap<String, Integer> keyword_to_question = new HashMap<>();

    public void load(String filename) {
        BufferedReader input = InputOutputHelper.getInstance().openFile(filename);
        String s;
        String[] substr;
        try {
            while ((s = input.readLine()) != null) {
                substr = InputOutputHelper.splitString(s, '$');
                if (substr.length == 5) {
                    int id = Integer.parseInt(substr[0]);
                    boolean locked = (Integer.parseInt(substr[3]) != 0);
                    int part_question = Integer.parseInt(substr[4]);
                    keyword_locked.put(id, locked);
                    part_type.put(id, part_question);
                    question_parts.put(id, substr[2]);
                    keyword_to_question.put(substr[1], id);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        InputOutputHelper.closeFile(input);
    }

    public String toString(int[] keyword_ids) {
        if (!isAvailableQuestion(keyword_ids)) {
            return "";
        }
        if (part_type.get(keyword_ids[0]) > part_type.get(keyword_ids[1])) {
            return question_parts.get(keyword_ids[1]) + " " + question_parts.get(keyword_ids[0]);
        }
        return question_parts.get(keyword_ids[0]) + " " + question_parts.get(keyword_ids[1]);
    }

    public boolean isAvailableQuestion(int[] keyword_ids) {
        if (keyword_ids.length != 2 ||
                !question_parts.containsKey(keyword_ids[0]) ||
                !question_parts.containsKey(keyword_ids[1]) ||
                part_type.get(keyword_ids[0]).equals(part_type.get(keyword_ids[1])) ||
                keyword_locked.get(keyword_ids[0]) ||
                keyword_locked.get(keyword_ids[1])) {
            return false;
        }
        return true;
    }

    public int keywordToQuestion(String keyword) {
        return keyword_to_question.get(keyword);
    }

    public boolean isKeyword(String keyword) {
        return keyword_to_question.containsKey(keyword);
    }

    public void lockKeyword(int keyword_id) {
        keyword_locked.replace(keyword_id, true);
    }

    public void unlockKeyword(int keyword_id) {
        keyword_locked.replace(keyword_id, false);
    }
}
