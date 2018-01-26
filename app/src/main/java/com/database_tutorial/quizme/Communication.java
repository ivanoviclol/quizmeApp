package com.database_tutorial.quizme;

/**
 * Created by Ivan on 1/18/2018.
 */
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Communication {
    @GET("checkuser")
    Call<User>  checkUser(
            @Header("email") String email
    );

    @POST("createuser")
    Call<User> createUser(
            @Body User user

    );

    @POST ("updatequiz")
    Call<Quiz> updateQuiz(
            @Body Quiz quiz
    );

    @GET ("createquiz")
    Call<String> createQuiz(
            @Header ("user_id") String user_id
    );

    @POST ("deletequiz")
    Call<Quiz> deleteQuiz(
            @Body Quiz quiz
    );

    @GET("login")
    Call<User> login(
            @Header("email") String email,
            @Header("passwd") String passwd
    );

    @GET("quiz")
    Call<List<Quiz>> getQuizList(
            @Header("user_id") int user_id
    );
}
