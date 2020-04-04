package com.example.placement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import models.company;

public class companyLandingPage extends Activity {
    FirebaseAuth mFirebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    TextView companyName;
    String cName, cPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_landing_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        companyName = findViewById(R.id.company_name_text_view);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Companies").child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cName = Objects.requireNonNull(dataSnapshot.getValue(company.class)).getName();
                cPic = Objects.requireNonNull(dataSnapshot.getValue(company.class)).getProfile_pic();
                companyName.setText(cName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(companyLandingPage.this, "Signed Out Successfully !", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(companyLandingPage.this,MainActivity.class));
        finish();
    }

    public void goto_applications(View view){
        startActivity(new Intent(companyLandingPage.this, comp_applications.class));
        finish();
    }

    public void goto_compProfile(View view) {
        startActivity(new Intent(companyLandingPage.this, Companyprofile.class));
        finish();
    }

    public void goto_add_page(View view) {
        startActivity(new Intent(companyLandingPage.this, add_intern_or_job_activity.class).putExtra("Company_Name", cName).putExtra("Profile_Pic", cPic));
        finish();
    }
}

