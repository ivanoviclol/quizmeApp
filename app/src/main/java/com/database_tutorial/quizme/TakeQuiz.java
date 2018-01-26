package com.database_tutorial.quizme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TakeQuiz extends AppCompatActivity {
    private TableLayout tl;
    private Quiz quiz;
    private int quizNumber;
    private int questionNumber;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        Quiz[] quizList = ((MyApplication) this.getApplication()).getQuizList();
        quizNumber =(getIntent().getIntExtra("Number",1));
        questionNumber = (getIntent().getIntExtra("QuestionNumber",0));
        points = getIntent().getIntExtra("Points",0);
        quiz = quizList[quizNumber];

        if(questionNumber == quiz.getQuestions().length){
            TextView question = new TextView(this);
            tl =  (TableLayout) findViewById(R.id.takeQuiz_tableLayout);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            question.setText("Your points are: " + points + "/" + quiz.getQuestions().length);
            question.setTextSize(20);
            question.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            tr.addView(question);
            tl.addView(tr);

            Button finish = new Button(this);
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            finish.setText("Finish");
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            finish.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            tableRow.addView(finish);
            tl.addView(tableRow);
        }else {
            TextView question = new TextView(this);
            tl =  (TableLayout) findViewById(R.id.takeQuiz_tableLayout);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            question.setText(quiz.getQuestions()[questionNumber].getText());
            question.setTextSize(20);
            question.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            tr.addView(question);
            tl.addView(tr);

            for (int i = 0; i< quiz.getQuestions()[questionNumber].getAnswers().length; i++){
                final int number = i;
                Button answer = new Button(this);
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
                answer.setText(quiz.getQuestions()[questionNumber].getAnswers()[i].getText());
                answer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                answer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String correct = "";
                        if (quiz.getQuestions()[questionNumber].getAnswers()[number].isCorrect()){
                            correct = "Correct :)";
                            points ++;
                        }else {
                            correct = "Wrong :(";
                        }
                        Toast.makeText(getApplicationContext(), correct, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), TakeQuiz.class);
                        questionNumber++;
                        intent.putExtra("Number", quizNumber);
                        intent.putExtra("QuestionNumber",questionNumber);
                        intent.putExtra("Points",points);
                        finish();
                        startActivity(intent);
                    }
                });
                tableRow.addView(answer);
                tl.addView(tableRow);

            }
        }






    }
}
