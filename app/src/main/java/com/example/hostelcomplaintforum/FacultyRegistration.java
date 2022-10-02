package com.example.hostelcomplaintforum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class FacultyRegistration extends AppCompatActivity {

    TextInputLayout mEmail, mPassword;
    LinearLayout loginText;
    Button continueBtn;
    ProgressBar progressBar;
    String email, password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_registration);

        initializations();

        loginText.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FacultyLogin.class));
            finish();
        });

        continueBtn.setOnClickListener(view -> {

            email = Objects.requireNonNull(mEmail.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();


            if ( !validateEmail(email) | !validatePassword(password) ) {


                if (!validateEmail(email)) {
                    mEmail.setError(null);
                    mEmail.setErrorEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    mEmail.setError("Please enter a valid email address");
                } else {
                    mEmail.setError(null);
                    mEmail.setErrorEnabled(false);
                }
                if (!validatePassword(password)) {
                    mPassword.setError(null);
                    mPassword.setErrorEnabled(true);
                    mPassword.setCounterEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    mPassword.setError("Password should be at least 6 characters long");
                } else {
                    mPassword.setError(null);
                    mPassword.setErrorEnabled(false);
                    mPassword.setCounterEnabled(false);
                }


            } else {

                mEmail.setError(null);
                mEmail.setErrorEnabled(false);
                mPassword.setError(null);
                mPassword.setErrorEnabled(false);
                mPassword.setCounterEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                continueBtn.setVisibility(View.INVISIBLE);
//                if (firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isEmailVerified()) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(StudentRegister.this, "Please verify the email with the link sent to you.", Toast.LENGTH_SHORT).show();
//                }
                //  if (firebaseAuth.getCurrentUser() == null) {
                //continueBtn.setVisibility(View.INVISIBLE);
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(unused -> {
                                Toast.makeText(FacultyRegistration.this, "Email verification link sent to " + email, Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent(getApplicationContext(), StudentLogin.class);
                                Map<String, Object> user = new HashMap<>();
                                user.put("uEmail", email);
                                user.put("uTimestamp",System.currentTimeMillis());
                                user.put("uAuthority", true);
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(email)
                                        .set(user)
                                        .addOnSuccessListener(unused1 -> {
                                            Toast.makeText(this, "Data added to cloud", Toast.LENGTH_SHORT).show();
                                            firebaseAuth.signOut();})
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Data not added. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            firebaseAuth.signOut();
                                        });
                                //startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                                continueBtn.setVisibility(View.VISIBLE);
                                finish();
                            }).addOnFailureListener(e -> {
                                continueBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(FacultyRegistration.this, "Email verification link not sent. " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            continueBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
//                }
//                else Toast.makeText(this, "yhi dikkat hai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializations() {

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        continueBtn = findViewById(R.id.continueBtn);
        progressBar = findViewById(R.id.progressBar);
        loginText = findViewById(R.id.loginText);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public boolean validateUserName(String s) {
        return s.length() >= 3;
    }

    public boolean validateEmail(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\." + "[a-zA-Z0-9_+&-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(s).matches();
    }

    public boolean validatePassword(String s) {
        return s.length() >= 6;
    }

}
