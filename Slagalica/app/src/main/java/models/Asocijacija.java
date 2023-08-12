package models;

import java.sql.Time;
import java.util.List;

public class Asocijacija {

    private String a1;
    private String a2;
    private String a3;
    private String a4;

    private String b1;
    private String b2;
    private String b3;
    private String b4;

    private String c1;
    private String c2;
    private String c3;
    private String c4;

    private String d1;
    private String d2;
    private String d3;
    private String d4;


    private String solutionA;
    private String solutionB;
    private String solutionC;
    private String solutionD;

    private String finalAnswer;

    public Asocijacija(String a1, String a2, String a3, String a4, String b1, String b2, String b3, String b4, String c1, String c2, String c3, String c4, String d1, String d2, String d3, String d4, String solutionA, String solutionB, String solutionC, String solutionD, String finalAnswer) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
        this.b4 = b4;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.d4 = d4;
        this.solutionA = solutionA;
        this.solutionB = solutionB;
        this.solutionC = solutionC;
        this.solutionD = solutionD;
        this.finalAnswer = finalAnswer;
    }

    public Asocijacija(){

    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public String getA4() {
        return a4;
    }

    public void setA4(String a4) {
        this.a4 = a4;
    }

    public String getB1() {
        return b1;
    }

    public void setB1(String b1) {
        this.b1 = b1;
    }

    public String getB2() {
        return b2;
    }

    public void setB2(String b2) {
        this.b2 = b2;
    }

    public String getB3() {
        return b3;
    }

    public void setB3(String b3) {
        this.b3 = b3;
    }

    public String getB4() {
        return b4;
    }

    public void setB4(String b4) {
        this.b4 = b4;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getD1() {
        return d1;
    }

    public void setD1(String d1) {
        this.d1 = d1;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getD3() {
        return d3;
    }

    public void setD3(String d3) {
        this.d3 = d3;
    }

    public String getD4() {
        return d4;
    }

    public void setD4(String d4) {
        this.d4 = d4;
    }

    public String getSolutionA() {
        return solutionA;
    }

    public void setSolutionA(String solutionA) {
        this.solutionA = solutionA;
    }

    public String getSolutionB() {
        return solutionB;
    }

    public void setSolutionB(String solutionB) {
        this.solutionB = solutionB;
    }

    public String getSolutionC() {
        return solutionC;
    }

    public void setSolutionC(String solutionC) {
        this.solutionC = solutionC;
    }

    public String getSolutionD() {
        return solutionD;
    }

    public void setSolutionD(String solutionD) {
        this.solutionD = solutionD;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }
}
