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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends Activity {
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mFirebaseAuth;
    EditText userId, password;
    Button loginButton;
    AppCompatRadioButton stud, comp;
    TextView dontHaveAnAccount, mOption;
    SignInButton googleLogin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, MainActivity.class);
        startActivity(gotoMainPage);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        userId = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        stud = findViewById(R.id.student_selector_2);
        comp = findViewById(R.id.company_selector_2);
        dontHaveAnAccount = findViewById(R.id.dont_have_an_account);
        mOption = findViewById(R.id.textView4);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser == null){
                    Toast.makeText(Login.this, "Please fill Correct Details", Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userId.getText().toString().trim();
                String pwd = password.getText().toString();
                if(username.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    userId.setError("Please enter Username");
                    userId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter a Password");
                    password.requestFocus();
                }
                else if(pwd.length()<6){
                    password.setError("Password should be at least 6 characters long");
                    password.requestFocus();
                }
                else{
                    mFirebaseAuth.signInWithEmailAndPassword(username,pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(! task.isSuccessful()){
                                Toast.makeText(Login.this,"Login Error, Please try Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(stud.isChecked()){
                                    if(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).isEmailVerified()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Students");
                                    ref.child(Objects.requireNonNull(mFirebaseAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                Toast.makeText(Login.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Login.this, studentLandingPage.class));
                                                finish();
                                            } else{
                                                Toast.makeText(Login.this, "Username Not in Students !", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });}
                                    else{
                                        Toast.makeText(Login.this, "Verify your e-mail address", Toast.LENGTH_LONG).show();
                                    }
                                } else if(comp.isChecked()){
                                    if(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).isEmailVerified()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Companies");
                                    ref.child(Objects.requireNonNull(mFirebaseAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                Toast.makeText(Login.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Login.this, companyLandingPage.class));
                                                finish();
                                            } else{
                                                Toast.makeText(Login.this, "Username Not in Companies !", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });}
                                    else{
                                        Toast.makeText(Login.this, "Verify your e-mail address", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(Login.this, "Please Check in your Type !", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                }
                            }
                        }
                    });
                }
            }
        });
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
                finish();
            }
        });

        stud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stud.isChecked()){
                    comp.setChecked(false);
//                    mOption.setVisibility(View.VISIBLE);
//                    googleLogin.setVisibility(View.VISIBLE);
                }
            }
        });

        comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(comp.isChecked()){
                    stud.setChecked(false);
                    mOption.setVisibility(View.GONE);
                    googleLogin.setVisibility(View.GONE);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleLogin = findViewById(R.id.google_login_button);
    }

    public void googleLogIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            if (result.isSuccess()){
                Toast.makeText(Login.this, "Logged in!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, studentLandingPage.class));
                finish();
            }
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
        }
    }
}
