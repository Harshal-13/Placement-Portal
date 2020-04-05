package com.example.placement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import models.company;
import models.student;

public class profile_update extends Activity {
    private EditText studentName, studentEmail,studentCpi,companyName,companyEmail;
    FirebaseUser user;
    String profile_pic, type;
    TextView studentBranch, branchSelector;
    androidx.constraintlayout.widget.ConstraintLayout studentLayout, companyLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_update);
        user = FirebaseAuth.getInstance().getCurrentUser();
        type = getIntent().getStringExtra("Type");
        studentLayout = findViewById(R.id.student_layout);
        companyLayout = findViewById(R.id.company_layout);

        Button profileUpdater = findViewById(R.id.profile_button);
        if(type.equals("1")){
            studentLayout.setVisibility(View.VISIBLE);
            companyLayout.setVisibility(View.GONE);
            studentName = findViewById(R.id.student_name_edit_text_2);
            studentEmail = findViewById(R.id.student_email_edit_text_2);
            studentBranch = findViewById(R.id.student_branch_edit_text);
            studentCpi = findViewById(R.id.student_cpi_edit_text_2);

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Students").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    student student = Objects.requireNonNull(dataSnapshot.getValue(student.class));
                    studentName.setText(student.getName());
                    studentEmail.setText(student.getEmail());
                    studentCpi.setText(student.getCpi());
                    studentBranch.setText(student.getBranch());
                    profile_pic = student.getProfile_pic();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            
            branchSelector = findViewById(R.id.studentBranchTextView);
            branchSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] list = new String[]{"BIOTECHNOLOGY","CHEMICAL ENGINEERING","CHEMICAL SCIENCE AND TECHNOLOGY","CIVIL ENGINEERING","COMPUTER SCIENCE AND ENGINEERING","ELECTRONICS AND ELECTRICAL ENGINEERING","ELECTRONICS AND COMMUNICATION ENGINEERING","MATHEMATICS AND COMPUTING","MECHANICAL ENGINEERING","ENGINEERING PHYSICS"};
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(profile_update.this);
                    mBuilder.setTitle("Select a Branch").setIcon(R.drawable.ic_list)
                            .setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    studentBranch.setText(list[which]);
                                    dialog.dismiss();
                                }
                            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
        }
        else{
            studentLayout.setVisibility(View.GONE);
            companyLayout.setVisibility(View.VISIBLE);
            companyName = findViewById(R.id.company_name_edit_text_2);
            companyEmail = findViewById(R.id.company_email_edit_text_2);

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Companies").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    company company = Objects.requireNonNull(dataSnapshot.getValue(company.class));
                    companyName.setText(company.getName());
                    companyEmail.setText(company.getEmail());
                    profile_pic = company.getProfile_pic();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

        profileUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("1")){
                    String uName,uEmail,uCpi,uBranch;
                    uName = studentName.getText().toString();
                    uEmail = studentEmail.getText().toString();
                    uCpi = studentCpi.getText().toString();
                    uBranch =studentBranch.getText().toString();
                    final student student = new student(uName,uEmail,uBranch,uCpi,profile_pic);

                    if(uName.isEmpty() || uEmail.isEmpty() || uCpi.isEmpty() || uBranch.isEmpty()){
                        Toast.makeText(profile_update.this,"Fields are Empty !",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        user.updateEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseDatabase.getInstance().getReference().child("Students")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(profile_update.this,"Successfully Updated Profile",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(profile_update.this, ProfileActivity.class));
                                                finish();
                                            }else {
                                                Toast.makeText(profile_update.this,"Error while Updating Database",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(profile_update.this,"Error while Updating Email",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    String uName,uEmail;
                    uName = companyName.getText().toString();
                    uEmail = companyEmail.getText().toString();
                    final company company = new company(uName,uEmail,profile_pic);

                    if(uName.isEmpty() || uEmail.isEmpty()){
                        Toast.makeText(profile_update.this,"Fields are Empty !",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        user.updateEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseDatabase.getInstance().getReference().child("Companies")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(profile_update.this,"Successfully Updated Profile",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(profile_update.this, Companyprofile.class));
                                                finish();
                                            }else {
                                                Toast.makeText(profile_update.this,"Error while Updating Database",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(profile_update.this,"Error while Updating Email",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void back_to_profile(View view) {
        if(type.equals("1")){
            startActivity(new Intent(profile_update.this, ProfileActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(profile_update.this, Companyprofile.class));
            finish();
        }
    }
}







