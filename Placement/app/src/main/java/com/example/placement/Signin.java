package com.example.placement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import models.company;
import models.student;

public class Signin extends Activity {
    EditText studentName, studentEmail ,studentPassword, studentBranch, studentCPI, companyName, companyEmail, companyPassword, companyBranches, companyCutoffCPI;
    FirebaseAuth mFirebaseAuth;
    Button signupButton;
    AppCompatRadioButton stud, comp;
    TextView alreadyRegistered;
    ConstraintLayout studLayout, compLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        mFirebaseAuth = FirebaseAuth.getInstance();

        studLayout = findViewById(R.id.studentView);
        stud = findViewById(R.id.student_selector);
        studentName = findViewById(R.id.fullname_edit_text);
        studentEmail = findViewById(R.id.username_edit_text);
        studentPassword = findViewById(R.id.password_edit_text);
        studentBranch = findViewById(R.id.branch_edit_text);
        studentCPI = findViewById(R.id.cpi_edit_text);
        compLayout = findViewById(R.id.companyView);
        comp = findViewById(R.id.company_selector);
        companyName = findViewById(R.id.company_name_edit_text);
        companyEmail = findViewById(R.id.company_email_edit_text);
        companyPassword = findViewById(R.id.company_password_edit_text);
        companyBranches = findViewById(R.id.branch_allowed_edit_text);
        companyCutoffCPI = findViewById(R.id.cpi_cutoff_edit_text);

        signupButton = findViewById(R.id.signup_button);
        alreadyRegistered = findViewById(R.id.already_registered);
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signin.this,Login.class));
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stud.isChecked()){
                    final String name = studentName.getText().toString();
                    final String username = studentEmail.getText().toString();
                    final String pwd = studentPassword.getText().toString();
                    final String branch = studentBranch.getText().toString();
                    final String cpi = studentCPI.getText().toString();

                    if(name.isEmpty() || username.isEmpty() || pwd.isEmpty() || branch.isEmpty() || cpi.isEmpty()){
                        Toast.makeText(Signin.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                    }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(Signin.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                    else if(pwd.length()<6){
                        Toast.makeText(Signin.this, "Min Length of Password is 6 !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mFirebaseAuth.createUserWithEmailAndPassword(username,pwd).addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(! task.isSuccessful()){
                                    Toast.makeText(Signin.this,"SignUp Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    student student = new student(name, username, branch, cpi);
                                    FirebaseDatabase.getInstance().getReference("Students")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signin.this, "SignUp Successful !!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Signin.this,home.class));
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(Signin.this,"SignUp Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
                else if(comp.isChecked()){
                    final String name = companyName.getText().toString();
                    final String username = companyEmail.getText().toString();
                    String pwd = companyPassword.getText().toString();
                    final String branch = companyPassword.getText().toString();
                    final String cpi = companyCutoffCPI.getText().toString();

                    if(name.isEmpty() || username.isEmpty() || pwd.isEmpty() || branch.isEmpty() || cpi.isEmpty()){
                        Toast.makeText(Signin.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                    }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(Signin.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                    else if(pwd.length()<6){
                        Toast.makeText(Signin.this, "Min Length of Password is 6 !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mFirebaseAuth.createUserWithEmailAndPassword(username,pwd).addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(! task.isSuccessful()){
                                    Toast.makeText(Signin.this,"SignUp Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    company company = new company(name, username, branch, cpi);
                                    FirebaseDatabase.getInstance().getReference("Companies")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signin.this, "SignUp Successful !!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Signin.this,home.class));
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(Signin.this,"SignUp Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });

        stud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stud.isChecked()){
                    comp.setChecked(false);
                    studLayout.setVisibility(View.VISIBLE);
                    compLayout.setVisibility(View.GONE);
                }
            }
        });

        comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(comp.isChecked()){
                    stud.setChecked(false);
                    compLayout.setVisibility(View.VISIBLE);
                    studLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, MainActivity.class);
        startActivity(gotoMainPage);
        finish();
    }
}
