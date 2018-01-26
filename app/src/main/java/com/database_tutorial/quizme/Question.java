package com.database_tutorial.quizme;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ivan on 1/19/2018.
 */

public class Question implements Serializable {
    @Expose
    private String id;
    private String text;
    private String quiz_id;
    private Answer[] answers;


    public Answer[] getAnswers() {
        return answers;
    }

    public String getId() {
        return id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
