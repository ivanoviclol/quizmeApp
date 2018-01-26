package com.database_tutorial.quizme;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Ivan on 1/19/2018.
 */

public class Answer implements Serializable{
    @Expose
    private String id;
    private String text;
    private String question_id;
    private boolean correct;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
