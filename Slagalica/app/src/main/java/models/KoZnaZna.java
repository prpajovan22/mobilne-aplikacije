package models;

import java.util.List;

public class KoZnaZna extends Game{

    public KoZnaZna(int score, int numberOfRounds, long timeOnTheRound) {
        super(score, numberOfRounds, timeOnTheRound);
    }

    private int currentQuestion;
    List<Question> questions;
    private int selectedButton;

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }

    private void getCorrectAnswer(int selectedButton){

        Question currentQuestionObject = this.questions.get(currentQuestion);

        Solutions selectedSolution = currentQuestionObject.getSolution().get(selectedButton);

        if(selectedSolution.isCorrect() == true){
            int points = 10;
            setScore(getScore() + 10);
        }else{
            int points = 5;
            setScore(getScore() - 5);
        }
        currentQuestion++;
    }

}
