package models;

public class Solutions {

    private int id;
    private int questionId;
    private String solution;
    private boolean isCorrect;

    public Solutions(int id, int questionId, String solution, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.solution = solution;
        this.isCorrect = isCorrect;
    }

    public Solutions(int questionId, String solution, boolean isCorrect) {
        this.questionId = questionId;
        this.solution = solution;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
