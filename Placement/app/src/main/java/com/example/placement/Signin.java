package com.example.placement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Signin extends Activity {
    EditText userId, password;
    FirebaseAuth mFirebaseAuth;
    Button signupButton;
    TextView alreadyRegistered;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        userId = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        mFirebaseAuth = FirebaseAuth.getInstance();
        alreadyRegistered = findViewById(R.id.already_registered);
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userId.getText().toString();
                String pwd = password.getText().toString();
                if(username.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Signin.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    userId.setError("Please enter Username");
                    userId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter a Password");
                    password.requestFocus();
                }
                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(username,pwd).addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(! task.isSuccessful()){
                                Toast.makeText(Signin.this,"SignUp Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(Signin.this,home.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signin.this,Login.class));
                finish();
            }
        });
    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, MainActivity.class);
        startActivity(gotoMainPage);
        finish();
    }
}
