package com.database_tutorial.quizme;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ivan on 1/19/2018.
 */

public class Quiz implements Serializable {

    @Expose
    private  String id;
    private  String name;
    private  String user_id;
    private Question[] questions;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
