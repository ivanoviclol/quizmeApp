package com.database_tutorial.quizme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private Communication communication;
    private int id = 1;
    private String ip ="";
    private String port ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = share.getString(SettingsActivity.NODEJS_IP,"192.168.0.184");
        port = share.getString(SettingsActivity.NODEJS_PORT,"3000");
        String url = "http://" + ip + ":" + port +"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        communication = retrofit.create(Communication.class);

        final Button registerButton = findViewById(R.id.register_configm_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((TextView)(findViewById(R.id.accEmail_text))).getText().toString();
                Call<User> call = communication.checkUser(email);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.body() != null){
                            Log.e("responsebody", response.body().getId());
                            if (!response.body().getId().equals("0")){
                                Toast.makeText(getApplicationContext(), "User exists", Toast.LENGTH_SHORT).show();

                            }
                                else {
                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setId("1");
                                newUser.setName(((TextView)(findViewById(R.id.accName_text))).getText().toString());
                                newUser.setPasswd(((TextView)(findViewById(R.id.accPasswd_text))).getText().toString());
                                Call<User> calll = communication.createUser(newUser);

                                calll.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.e("REST","Could not make request: " + t);
                                    }
                                });

                                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(login);

                            }
                        }else {
                            Log.e("REST", "Response has no body");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("REST","Could not make request: " + t);
                    }
                });
            }
        });

    }
}
