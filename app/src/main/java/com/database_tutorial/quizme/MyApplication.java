package com.database_tutorial.quizme;

import android.app.Application;

/**
 * Created by Ivan on 1/19/2018.
 */

public class MyApplication extends Application {
    private Quiz[] quizList;

    public Quiz[] getQuizList() {
        return quizList;
    }

    public void setQuizList (Quiz[] someVariable) {
        this.quizList = someVariable;
    }
}
