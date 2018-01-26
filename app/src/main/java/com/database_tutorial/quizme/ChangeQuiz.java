package com.database_tutorial.quizme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeQuiz extends AppCompatActivity {
    private TableLayout tl;
    private Quiz quiz;
    private Communication communication;
    private String ip ="";
    private String port ="";
    private int passedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_quiz);
        Quiz[] quizList = ((MyApplication) this.getApplication()).getQuizList();
        passedValue =(getIntent().getIntExtra("Number",1));
        quiz = quizList[passedValue];

        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = share.getString(SettingsActivity.NODEJS_IP,"192.168.0.184");
        port = share.getString(SettingsActivity.NODEJS_PORT,"3000");
        String url = "http://" + ip + ":" + port +"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        communication = retrofit.create(Communication.class);


        tl =  (TableLayout) findViewById(R.id.changeQuiz_tableLayout);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));

        EditText quizNameText = new EditText(this);
        quizNameText.setText(quiz.getName());
        quizNameText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

        quizNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                quiz.setName(editable.toString());
            }
        });
        tr.addView(quizNameText);
        tl.addView(tr);

        for (int i=0; i< quiz.getQuestions().length; i++){
            final int number = i;
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            Button changeQuestion = new Button(getApplicationContext());
            changeQuestion.setText(quiz.getQuestions()[i].getText());
            changeQuestion.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            changeQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =  new Intent(getApplicationContext(), ChangeQuestion.class);
                    intent.putExtra("Question", number);
                    intent.putExtra("Quiz",passedValue);
                    startActivity(intent);
                }
            });
            tableRow.addView(changeQuestion);

            Button deleteQuestion = new Button(getApplicationContext());
            deleteQuestion.setText("Delete");
            deleteQuestion.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            deleteQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Question[] newQuestions = new Question[quiz.getQuestions().length-1];

                    boolean passedValue = false;
                    for (int j =0; j <quiz.getQuestions().length-1;j++){
                        if (passedValue){
                            newQuestions[j] = quiz.getQuestions()[j+1];
                        }
                        else{
                            if (j != number){
                                newQuestions[j] = quiz.getQuestions()[j];
                            }
                            else{
                                passedValue = true;
                                newQuestions[j] = quiz.getQuestions()[j+1];
                            }
                        }

                    }
                    quiz.setQuestions(newQuestions);
                    finish();
                    startActivity(getIntent());

                }
            });
            tableRow.addView(deleteQuestion);
            tl.addView(tableRow);
        }

        TableRow tableRow = new TableRow(getApplicationContext());
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        final Button newQuestionButton = new Button(getApplicationContext());
        newQuestionButton.setText("New");
        newQuestionButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        newQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question[] newQuestions = new Question[quiz.getQuestions().length+1];
                for (int g = 0; g < quiz.getQuestions().length; g++){
                    newQuestions[g] = quiz.getQuestions()[g];
                }
                Question newQuestion = new Question();

                newQuestion.setQuiz_id(quiz.getId());
                Answer[] answers = new Answer[0];
                newQuestion.setAnswers(answers);
                newQuestions[quiz.getQuestions().length] = newQuestion;
                newQuestions[quiz.getQuestions().length].setText("New Question");
                quiz.setQuestions(newQuestions);
                finish();
                startActivity(getIntent());
            }
        });
        tableRow.addView(newQuestionButton);

        TableRow tableRow2 = new TableRow(getApplicationContext());
        tableRow2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        Button updateQuiz = new Button(getApplicationContext());
        updateQuiz.setText("Update");
        updateQuiz.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        tableRow2.addView(updateQuiz);
        updateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Quiz> call = communication.updateQuiz(quiz);
                call.enqueue(new Callback<Quiz>() {
                    @Override
                    public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                        finish();
                                            }

                    @Override
                    public void onFailure(Call<Quiz> call, Throwable t) {

                    }
                });

            }
        });
        tl.addView(tableRow);
        tl.addView(tableRow2);

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}



