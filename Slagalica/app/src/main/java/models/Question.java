package models;

import java.util.List;

public class Question {

    private int id;
    private String question;
    private List<Solutions> solution;

    public Question(int id, String question, List<Solutions> solution) {
        this.id = id;
        this.question = question;
        this.solution = solution;
    }

    public Question(String question, List<Solutions> solution) {
        this.question = question;
        this.solution = solution;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Solutions> getSolution() {
        return solution;
    }

    public void setSolution(List<Solutions> solution) {
        this.solution = solution;
    }
}
