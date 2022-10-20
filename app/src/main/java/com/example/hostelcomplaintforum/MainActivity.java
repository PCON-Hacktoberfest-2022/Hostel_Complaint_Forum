package com.example.hostelcomplaintforum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView student, faculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //app will not be affected by dark mode
        
        initializations();
        btnFuncs();

    }

    private void btnFuncs() {
        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            student.setOnClickListener(view -> {
                startActivity(new Intent(getApplicationContext(), StudentLogin.class));
                finish();
            });

            faculty.setOnClickListener(view -> {
                startActivity(new Intent(getApplicationContext(), FacultyLogin.class));
                finish();
            });
        }
        else{
            startActivity(new Intent(getApplicationContext(),PublicFeed.class));
            finish();
        }
    }

    private void initializations() {
        student = findViewById(R.id.student_btn);
        faculty = findViewById(R.id.faculty_btn);
    }


}
