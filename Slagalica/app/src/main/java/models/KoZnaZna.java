package models;

import java.util.List;

public class KoZnaZna extends Game{


    private List<String> wrongAnswer;

    private String correctAnswer;

    public KoZnaZna(int id, int score1, int score2) {
        super(id, score1, score2);
    }

    public KoZnaZna(int id, int score1, int score2, List<String> wrongAnswer, String correctAnswer) {
        super(id, score1, score2);
        this.wrongAnswer = wrongAnswer;
        this.correctAnswer = correctAnswer;
    }

    public List<String> getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(List<String> wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
