package models;

public class Game {

    private int id;
    private int score;
    private int numberOfRounds;
    private long timeOnTheRound;

    public Game(int score, int numberOfRounds, long timeOnTheRound) {
        this.score = score;
        this.numberOfRounds = numberOfRounds;
        this.timeOnTheRound = timeOnTheRound;
    }

    public Game(int id, int score, int numberOfRounds, long timeOnTheRound) {
        this.id = id;
        this.score = score;
        this.numberOfRounds = numberOfRounds;
        this.timeOnTheRound = timeOnTheRound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public long getTimeOnTheRound() {
        return timeOnTheRound;
    }

    public void setTimeOnTheRound(long timeOnTheRound) {
        this.timeOnTheRound = timeOnTheRound;
    }

}
