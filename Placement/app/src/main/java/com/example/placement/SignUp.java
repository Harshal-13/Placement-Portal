package com.example.placement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import models.company;
import models.student;

public class SignUp extends Activity {
    EditText studentName, studentEmail ,studentPassword, studentCPI, companyName, companyEmail, companyPassword;
    FirebaseAuth mFirebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Button signUpButton;
    AppCompatRadioButton stud, comp;
    TextView alreadyRegistered, branchSelector,branchText;
    ConstraintLayout studLayout, compLayout;
    Uri FilePathUri;
    ProgressDialog progressDialog;
    String url, branch;

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
        branchSelector = findViewById(R.id.branchTextView);
        branchText = findViewById(R.id.branch_edit_text);
        studentCPI = findViewById(R.id.cpi_edit_text);
        compLayout = findViewById(R.id.companyView);
        comp = findViewById(R.id.company_selector);
        companyName = findViewById(R.id.company_name_edit_text);
        companyEmail = findViewById(R.id.company_email_edit_text);
        companyPassword = findViewById(R.id.company_password_edit_text);

        progressDialog = new ProgressDialog(SignUp.this);
        signUpButton = findViewById(R.id.signup_button);
        alreadyRegistered = findViewById(R.id.already_registered);
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
            }
        });

        branchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] list = new String[]{"BIOTECHNOLOGY", "CHEMICAL ENGINEERING", "CHEMICAL SCIENCE AND TECHNOLOGY", "CIVIL ENGINEERING", "COMPUTER SCIENCE AND ENGINEERING", "ELECTRONICS AND ELECTRICAL ENGINEERING", "ELECTRONICS AND COMMUNICATION ENGINEERING", "MATHEMATICS AND COMPUTING", "MECHANICAL ENGINEERING", "ENGINEERING PHYSICS"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignUp.this);
                mBuilder.setTitle("Select a Branch").setIcon(R.drawable.ic_list)
                        .setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                branch = list[which];
                                branchText.setText(branch);
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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stud.isChecked()) {
                    final String name = studentName.getText().toString();
                    final String username = studentEmail.getText().toString();
                    final String pwd = studentPassword.getText().toString();
                    final String cpi = studentCPI.getText().toString();

                    if (name.isEmpty() || username.isEmpty() || pwd.isEmpty() || (branch == null) || cpi.isEmpty()) {
                        Toast.makeText(SignUp.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(cpi) > 10) {
                        Toast.makeText(SignUp.this, "CPI can't be more than 10", Toast.LENGTH_SHORT).show();
                    } else if (pwd.length() < 6) {
                        Toast.makeText(SignUp.this, "Min Length of Password is 6 !", Toast.LENGTH_SHORT).show();
                    } else if (FilePathUri == null) {
                        Toast.makeText(SignUp.this, "Select a File", Toast.LENGTH_SHORT).show();
                    } else {
                        mFirebaseAuth.createUserWithEmailAndPassword(username, pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "SignUp Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                } else {
                                    Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUp.this, "Registered successfully.Please check your email for verification", Toast.LENGTH_LONG).show();
                                                        progressDialog.setTitle("Profile Pic is Uploading...");
                                                        progressDialog.show();
                                                        storageReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(SignUp.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                                                storageReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        url = uri.toString();
                                                                        student student = new student(name, username, branch, cpi, url);

                                                                        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(SignUp.this, "SignUp Successful !! verify email and login with your credentials", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(SignUp.this, Login.class));
                                                                                    finish();
                                                                                } else {
                                                                                    Toast.makeText(SignUp.this, "SignUp Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SignUp.this, "Upload Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                }
                            }
                        });
                    }
                } else if (comp.isChecked()) {
                    final String name = companyName.getText().toString();
                    final String username = companyEmail.getText().toString();
                    String pwd = companyPassword.getText().toString();

                    if (name.isEmpty() || username.isEmpty() || pwd.isEmpty()) {
                        Toast.makeText(SignUp.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                    } else if (pwd.length() < 6) {
                        Toast.makeText(SignUp.this, "Min Length of Password is 6 !", Toast.LENGTH_SHORT).show();
                    } else if (FilePathUri == null) {
                        Toast.makeText(SignUp.this, "Select a File", Toast.LENGTH_SHORT).show();
                    } else {
                        mFirebaseAuth.createUserWithEmailAndPassword(username, pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "SignUp Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                } else {
                                    Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUp.this, "Registered successfully.Please check your email for verification", Toast.LENGTH_LONG).show();
                                                        progressDialog.setTitle("Profile is Uploading...");
                                                        progressDialog.show();
                                                        storageReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(SignUp.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                                                storageReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        url = uri.toString();
                                                                        company company = new company(name, username, url);
                                                                        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(SignUp.this, "SignUp Successful !!", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(SignUp.this, Login.class));
                                                                                    finish();
                                                                                } else {
                                                                                    Toast.makeText(SignUp.this, "SignUp Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SignUp.this, "Upload Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
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
                if (stud.isChecked()) {
                    comp.setChecked(false);
                    studLayout.setVisibility(View.VISIBLE);
                    compLayout.setVisibility(View.GONE);
                    storageReference = FirebaseStorage.getInstance().getReference("Students");
                    databaseReference = FirebaseDatabase.getInstance().getReference("Students");
                }
            }
        });

        comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (comp.isChecked()) {
                    stud.setChecked(false);
                    compLayout.setVisibility(View.VISIBLE);
                    studLayout.setVisibility(View.GONE);
                    storageReference = FirebaseStorage.getInstance().getReference("Companies");
                    databaseReference = FirebaseDatabase.getInstance().getReference("Companies");
                }
            }
        });
    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, MainActivity.class);
        startActivity(gotoMainPage);
        finish();
    }

    public void browseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 13);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            Toast.makeText(this,"Image Selected",Toast.LENGTH_SHORT).show();
        }
    }
}
