package com.database_tutorial.quizme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizList extends AppCompatActivity {
    private Communication communication;
    private int id = 1;
    private String ip ="";
    private String port ="";
    private String user_id = "";
    private TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        TextView userName = new TextView(this);
        tl =  (TableLayout) findViewById(R.id.tableLayout_id);
        userName.setTextSize(20);
        userName.setText("Welcome " + this.getIntent().getStringExtra("User_Name"));
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        userName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        tr.addView(userName);
        tl.addView(tr);

        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = share.getString(SettingsActivity.NODEJS_IP,"192.168.0.184");
        port = share.getString(SettingsActivity.NODEJS_PORT,"3000");
        user_id =  getIntent().getStringExtra("User_ID");
        String url = "http://" + ip + ":" + port +"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        communication = retrofit.create(Communication.class);

        Call<List<Quiz>> call = communication.getQuizList(Integer.parseInt(user_id));
        call.enqueue(new Callback<List<Quiz>>() {
            @Override
            public void onResponse(Call<List<Quiz>> call, final Response<List<Quiz>> response) {
                TableLayout.LayoutParams paramRow = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
                TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                Quiz[] array = response.body().toArray(new Quiz[response.body().size()]);
                ((MyApplication) getApplication()).setQuizList(array);
                for (int i = 0; i< response.body().size();i++){
                    final int number =i;
                    TextView quizView = new TextView(getApplicationContext());
                    quizView.setTextSize(20);
                    quizView.setText(response.body().get(i).getName() );
                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(paramRow);
                    quizView.setLayoutParams(param);
                    tr.addView(quizView);

                    Button takeQuiz = new Button(getApplicationContext());
                    takeQuiz.setText("Take Quiz");
                    takeQuiz.setLayoutParams(param);
                    takeQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), TakeQuiz.class);
                            intent.putExtra("Number",number);
                            startActivity(intent);
                        }
                    });
                    tr.addView(takeQuiz);

                    Button changeQuiz = new Button(getApplicationContext());
                    changeQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent =  new Intent(getApplicationContext(), ChangeQuiz.class);
                            intent.putExtra("Number",number);
                            intent.putExtra("QuestionNumber",0);
                            intent.putExtra("Points",0);
                            startActivity(intent);
                        }
                    });
                    changeQuiz.setText("Change Quiz");
                    changeQuiz.setLayoutParams(param);
                    tr.addView(changeQuiz);

                    Button deleteQuiz = new Button(getApplicationContext());
                    deleteQuiz.setText("Delete Quiz");
                    deleteQuiz.setLayoutParams(param);
                    deleteQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Quiz[] quizList = ((MyApplication) getApplication()).getQuizList();
                            Call<Quiz> call = communication.deleteQuiz(quizList[number]);
                            call.enqueue(new Callback<Quiz>() {
                                @Override
                                public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                                    finish();
                                    startActivity(getIntent());
                                }

                                @Override
                                public void onFailure(Call<Quiz> call, Throwable t) {

                                }
                            });
                        }
                    });
                    tr.addView(deleteQuiz);
                    tl.addView(tr);

                }
                Button newQuiz = new Button(getApplicationContext());
                newQuiz.setText("New Quiz");
                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(paramRow);
                newQuiz.setLayoutParams(param);
                newQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<String> call = communication.createQuiz(user_id);
                        call.enqueue(new Callback<String>() {

                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                });
                tr.addView(newQuiz);
                tl.addView(tr);

            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {

            }
        });


    }
    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
