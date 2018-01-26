package com.database_tutorial.quizme;

import com.google.gson.annotations.Expose;

/**
 * Created by Ivan on 1/18/2018.
 */

public class User {
    @Expose
    private  String id;
    private  String name;
    private  String email;
    private  String passwd;

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }
}
