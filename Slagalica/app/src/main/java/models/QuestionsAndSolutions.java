package models;

public class QuestionsAndSolutions {

    private int id;
    private String question;
    private String solution;

    public QuestionsAndSolutions(int id, String question, String solution) {
        this.id = id;
        this.question = question;
        this.solution = solution;
    }

    public QuestionsAndSolutions(String question, String solution) {
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

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
