package com.example.placement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.placement.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
    }

    public void goto_login(View view) {
        Intent gotoLoginPage = new Intent(this, Login.class);
        startActivity(gotoLoginPage);
        finish();
    }

    public void goto_signup(View view) {
        Intent gotoSignupPage = new Intent(this, Signin.class);
        startActivity(gotoSignupPage);
        finish();
    }

}
