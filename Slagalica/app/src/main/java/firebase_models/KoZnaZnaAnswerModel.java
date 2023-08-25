package firebase_models;

public class KoZnaZnaAnswerModel {

    private boolean isCorrectAnswer = true;

    private String timeStamp;

    public KoZnaZnaAnswerModel(boolean isCorrectAnswer, String timeStamp) {
        this.isCorrectAnswer = isCorrectAnswer;
        this.timeStamp = timeStamp;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
