package models;

import java.util.List;

public class KoZnaZna extends Game{

    public KoZnaZna(String score, int numberOfRounds, long timeOnTheRound) {
        super(score, numberOfRounds, timeOnTheRound);
    }

    private Question currentQuestion;
    private Solutions correctSolution;
    private int selectedButton;

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Solutions getCorrectSolution() {
        return correctSolution;
    }

    public void setCorrectSolution(Solutions correctSolution) {
        this.correctSolution = correctSolution;
    }

    public int getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }

    /*private void getCorrectAnswer(int selectedButton){

        Question thisQuestion = currentQuestion.get(currentQuestion);

        if(correctSolution.isCorrect() == true){
            int points = 10;
            setScore(getScore()+10);
        }else{
            int points = 5;
            setScore(getScore()+5);
        }
        currentQuestion = (currentQuestion + 1) % thisQuestion.size();
    }
     */
}
