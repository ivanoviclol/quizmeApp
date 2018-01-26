package com.database_tutorial.quizme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeQuestion extends AppCompatActivity {
    private TableLayout tl;
    private Question question;
    private Communication communication;
    private String ip ="";
    private String port ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_question);
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = share.getString(SettingsActivity.NODEJS_IP,"192.168.0.184");
        port = share.getString(SettingsActivity.NODEJS_PORT,"3000");
        String url = "http://" + ip + ":" + port +"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        communication = retrofit.create(Communication.class);
        Quiz[] quizList = ((MyApplication) this.getApplication()).getQuizList();
        question = quizList[(getIntent().getIntExtra("Quiz",1))].getQuestions()[(getIntent().getIntExtra("Question",1))];

        tl =  (TableLayout) findViewById(R.id.changeQuestion_tableLayout);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));


        final EditText questionNameText = new EditText(this);
        questionNameText.setText(question.getText());
        questionNameText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        questionNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                question.setText(editable.toString());

            }
        });
        tr.addView(questionNameText);
        tl.addView(tr);



            for (int i=0; i< question.getAnswers().length; i++){
                final int number = i;
                TableRow tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
                EditText changeAnswer = new EditText(getApplicationContext());
                changeAnswer.setText(question.getAnswers()[i].getText());
                changeAnswer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        question.getAnswers()[number].setText(editable.toString());
                    }
                });
                changeAnswer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                tableRow.addView(changeAnswer);

                Button deleteQuestion = new Button(getApplicationContext());
                deleteQuestion.setText("Delete");
                deleteQuestion.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                deleteQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Answer[] newAnswers = new Answer[question.getAnswers().length-1];

                        boolean passedValue = false;
                        for (int j =0; j <question.getAnswers().length-1;j++){
                            if (passedValue){
                                newAnswers[j] = question.getAnswers()[j+1];
                            }
                            else{
                                if (j != number){
                                    newAnswers[j] = question.getAnswers()[j];
                                }
                                else{
                                    passedValue = true;
                                    newAnswers[j] = question.getAnswers()[j+1];
                                }
                            }

                        }
                        question.setAnswers(newAnswers);
                        finish();
                        startActivity(getIntent());
                    }
                });
                tableRow.addView(deleteQuestion);


                RadioButton correct = new RadioButton(getApplicationContext());

                correct.setChecked(question.getAnswers()[i].isCorrect());
                correct.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        question.getAnswers()[number].setCorrect(!question.getAnswers()[number].isCorrect());
                        finish();
                        startActivity(getIntent());
                    }
                });

                correct.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

                tableRow.addView(correct);
                tl.addView(tableRow);
            }





        TableRow tableRow = new TableRow(getApplicationContext());
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        Button newAnswer = new Button(getApplicationContext());
        newAnswer.setText("New");
        newAnswer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        newAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Answer[] newAnswers = new Answer[question.getAnswers().length+1];
                for (int g = 0; g < question.getAnswers().length; g++){
                    newAnswers[g] = question.getAnswers()[g];
                }
                Answer newAnser = new Answer();

                newAnswers[question.getAnswers().length] = newAnser;
                newAnswers[question.getAnswers().length].setText("");
                question.setAnswers(newAnswers);
                finish();
                startActivity(getIntent());

            }
        });
        tableRow.addView(newAnswer);
        tl.addView(tableRow);




    }
    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
