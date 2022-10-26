package com.example.hostelcomplaintforum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

public class FacultyLogin extends AppCompatActivity {

    TextInputLayout mEmail, mPassword;
    ProgressBar progressBar;
    String email, password;
    Button continueBtn;
    FirebaseAuth fAuth;
    FirebaseUser user;
    private final int RC_SIGN_IN=2;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(this, PublicFeed.class));
            finish();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(this, PublicFeed.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(this, PublicFeed.class));
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        initializations();
        (findViewById(R.id.registerText)).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FacultyRegistration.class));
            finish();
        });
        continueBtn.setOnClickListener(view -> {
            email= Objects.requireNonNull(mEmail.getEditText()).getText().toString().trim();
            password= Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();

            if(!validateEmail(email) | !validatePassword(password)){

                if(!validateEmail(email)) {
                    mEmail.setError(null);
                    mEmail.setErrorEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    mEmail.setError("Please enter a valid email address");
                } else {
                    mEmail.setError(null);
                    mEmail.setErrorEnabled(false);
                }
                if(!validatePassword(password)) {
                    mPassword.setError(null);
                    mPassword.setErrorEnabled(true);
                    mPassword.setCounterEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    mPassword.setError("Password should be atleast 6 characters long");
                } else{
                    mPassword.setError(null);
                    mPassword.setErrorEnabled(false);
                    mPassword.setCounterEnabled(false);
                }
            }
            else{
                mEmail.setError(null);
                mEmail.setErrorEnabled(false);
                mPassword.setError(null);
                mPassword.setErrorEnabled(false);
                mPassword.setCounterEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(email)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        DocumentSnapshot snapshot= task1.getResult();
                                        if(snapshot.getBoolean("uAuthority").equals(true)){
                                            if (fAuth.getCurrentUser().isEmailVerified()) {
                                                Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), PublicFeed.class));
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                            }else{
                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                fAuth.signOut();
                                                Toast.makeText(getApplicationContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }else{
                                            fAuth.signOut();
                                            Toast.makeText(getApplicationContext(), "You are not authorised", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        (findViewById(R.id.forgotPasswordLogin)).setOnClickListener(view -> {
            email= Objects.requireNonNull(mEmail.getEditText()).getText().toString().trim();
            EditText editText= new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Password Reset");
            passwordResetDialog.setMessage("Enter your Email-ID to receive password reset link.");
            passwordResetDialog.setView(editText);
            editText.setText(email);

            passwordResetDialog.setPositiveButton("Proceed", (dialogInterface, i) -> {
                String mail= editText.getText().toString().trim();
                if(validateEmail(mail))
                    fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(FacultyLogin.this, "Password reset link sent.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(FacultyLogin.this, "Password reset link not sent. "+ e.getMessage(), Toast.LENGTH_SHORT).show());
                else Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            }).setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            passwordResetDialog.create().show();
        });
    }

    public boolean validateEmail(String s){
        if(s==null || s.isEmpty()){
            return false;
        }
        String emailRegex= "^[a-zA-Z0-9_+&-]+(?:\\."+"[a-zA-Z0-9_+&-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; // email verification
        Pattern pattern= Pattern.compile(emailRegex);
        return pattern.matcher(s).matches();
    }
    public boolean validatePassword(String s){
        return s.length() > 5;
    }

    private void initializations() {
        mEmail= findViewById(R.id.emailLogin);
        mPassword= findViewById(R.id.passwordLogin);
        continueBtn= findViewById(R.id.continueBtnLogin);
        progressBar= findViewById(R.id.progressBarLogin);
        fAuth= FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
    }
}
