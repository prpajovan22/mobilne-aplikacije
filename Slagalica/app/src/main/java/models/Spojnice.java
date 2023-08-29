package models;

import java.util.List;
import java.util.Map;

public class Spojnice {


    private List<String> leftColumn;
    private List<String> rightColumn;
    private Map<String,String> answers;

    public Spojnice(List<String> leftColumn, List<String> rightColumn, Map<String, String> answers) {
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
        this.answers = answers;
    }

    public List<String> getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(List<String> leftColumn) {
        this.leftColumn = leftColumn;
    }

    public List<String> getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(List<String> rightColumn) {
        this.rightColumn = rightColumn;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public Spojnice(){}
}
