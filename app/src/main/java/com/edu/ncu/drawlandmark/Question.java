package com.edu.ncu.drawlandmark;

import java.util.Random;

public class Question {

    private String question;
    private String answer;
    private String[] wrong_answers;
    private String[] answers = new String[4];
    private String solve;

    public String getQuestion() {
        return this.question;
    }

    public String[] getWrongAnswers() {
        return this.wrong_answers;
    }

    public String getAnswer() {
        return this.answer;
    }

    public String[] getAnswers() {
        return this.answers;
    }

    public String getSolve(){ return  this.solve; }

    private static Random random = new Random();
    private static void shuffleArray(String[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            int index = Question.random.nextInt(i + 1);

            String a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }

    public Question(String question, String answer, String[] wrong_answers, String solve) {
        this.question = question;
        this.answer = answer;
        this.wrong_answers = wrong_answers;

        this.answers[0] = wrong_answers[0];
        this.answers[1] = wrong_answers[1];
        this.answers[2] = wrong_answers[2];
        this.answers[3] = answer;

        this.solve = solve;

        Question.shuffleArray( this.answers );
    }

}
