package com.mohitgupta.android.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    private static String ACTIVITY_NAME = "LoginActivity";
    private static final String EMAIL_KEY = "EMAIL_KEY";

    private SharedPreferences preferences;

    private Button loginButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        emailEditText = findViewById(R.id.email_edit_text);

        preferences  = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String defaultEmail = preferences.getString(EMAIL_KEY, "example@domain.com");
        emailEditText.setText(defaultEmail);

        loginButton = findViewById(R.id.mybutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putString(EMAIL_KEY, emailEditText.getText().toString()).apply();
                startActivity(new Intent(LoginActivity.this, StartActivity.class));
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
