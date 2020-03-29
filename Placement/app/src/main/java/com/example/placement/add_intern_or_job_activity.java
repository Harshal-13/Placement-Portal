package com.example.placement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

import models.intern;
import models.job;

public class add_intern_or_job_activity extends Activity {
    EditText opPosition, opDescription, opCpi, opBranchesAllowed;
    TextView dateView;
//    LocalDate date;
    String date;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    FirebaseAuth mFirebaseAuth;
    Button registerButton;
    AppCompatRadioButton internButton, jobButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_intern_or_job_page);
        mFirebaseAuth = FirebaseAuth.getInstance();

        opPosition = findViewById(R.id.position_edit_text);
        opDescription = findViewById(R.id.description_edit_text);
        opCpi = findViewById(R.id.cpi_edit_text);
        dateView = findViewById(R.id.dateView);
        opBranchesAllowed = findViewById(R.id.branch_edit_text);
        registerButton = findViewById(R.id.register_button);
        internButton = findViewById(R.id.intern_selector);
        jobButton = findViewById(R.id.job_selector);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(add_intern_or_job_activity.this,R.style.Theme_AppCompat_DayNight_Dialog,onDateSetListener,year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date_str;
                if(dayOfMonth < 10){
                    if(month < 9){
                        date_str = "0" + dayOfMonth + "/0" + (month+1) + "/" + year;
                    } else{
                        date_str = "0" + dayOfMonth + "/" + (month+1) + "/" + year;
                    }
                } else{
                    if(month < 9){
                        date_str = dayOfMonth + "/0" + (month+1) + "/" + year;
                    } else {
                        date_str = dayOfMonth + "/" + (month+1) + "/" + year;
                    }
                }
                dateView.setText(date_str);
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
//                date = LocalDate.parse(date_str, formatter);
                date = date_str;
            }
        };

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internButton.isChecked()){
                    final String companyName = getIntent().getStringExtra("Company_Name");
                    final String position = opPosition.getText().toString();
                    final String description = opDescription.getText().toString();
                    final String branch = opBranchesAllowed.getText().toString();
                    final String cpi = opCpi.getText().toString();

                    if(position.isEmpty() || description.isEmpty() || date.isEmpty() || branch.isEmpty() || cpi.isEmpty()){
                        Toast.makeText(add_intern_or_job_activity.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                    }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(add_intern_or_job_activity.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                    else {
                        intern intern = new intern(companyName, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid(),position,description,cpi,date,branch);
                        FirebaseDatabase.getInstance().getReference("Internships")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .child(position)
                                .setValue(intern).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(add_intern_or_job_activity.this, "Intern Registration Successful !!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(add_intern_or_job_activity.this,"Registration Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else if(jobButton.isChecked()){
                    final String companyName = getIntent().getStringExtra("Company_Name");
                    final String position = opPosition.getText().toString();
                    final String description = opDescription.getText().toString();
                    final String branch = opBranchesAllowed.getText().toString();
                    final String cpi = opCpi.getText().toString();

                    if(position.isEmpty() || description.isEmpty() || date.isEmpty() || branch.isEmpty() || cpi.isEmpty()){
                        Toast.makeText(add_intern_or_job_activity.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                    }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(add_intern_or_job_activity.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                    else {
                        job job = new job(companyName, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid(),position,description,cpi,date,branch);
                        FirebaseDatabase.getInstance().getReference("Jobs")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .child(position)
                                .setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(add_intern_or_job_activity.this, "Intern Registration Successful !!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(add_intern_or_job_activity.this,"Registration Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        internButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(internButton.isChecked()){
                    jobButton.setChecked(false);
                }
            }
        });

        jobButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jobButton.isChecked()){
                    internButton.setChecked(false);
                }
            }
        });
    }

    public void back_to_company_page(View view) {
        startActivity(new Intent(add_intern_or_job_activity.this, companyLandingPage.class));
        finish();
    }
}
