package com.example.placement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class change_password extends Activity {
    FirebaseUser user;
    AuthCredential credential;
    EditText currentP, newP, confirmP;
    ImageView backB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        user = FirebaseAuth.getInstance().getCurrentUser();
        currentP = findViewById(R.id.currentPassword);
        newP = findViewById(R.id.newPassword);
        confirmP = findViewById(R.id.confirmPassword);
        backB = findViewById(R.id.back_button);
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void submitPassword(View view) {
        final String currP,nP,conP;
        currP = currentP.getText().toString();
        nP = newP.getText().toString();
        conP = confirmP.getText().toString();
        if(currP.isEmpty() || nP.isEmpty() || conP.isEmpty()){
            Toast.makeText(change_password.this,"Fields are Empty",Toast.LENGTH_SHORT).show();
        } else{
            if(nP.equals(conP)){
                credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()),currP);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(nP.length()>5){
                                user.updatePassword(nP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(change_password.this, "Password Successfully Updated", Toast.LENGTH_SHORT).show();
                                            back();
                                        } else{
                                            Toast.makeText(change_password.this, "Error: Password could'nt be changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(change_password.this, "Min Length of Password is 6 !", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(change_password.this,"Please Provide correct current Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else{
                Toast.makeText(change_password.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void back() {
        if(Objects.equals(getIntent().getStringExtra("Type"), "1")){
            startActivity(new Intent(change_password.this, ProfileActivity.class));
        } else {
            startActivity(new Intent(change_password.this, Companyprofile.class));
        }
        finish();
    }
}
