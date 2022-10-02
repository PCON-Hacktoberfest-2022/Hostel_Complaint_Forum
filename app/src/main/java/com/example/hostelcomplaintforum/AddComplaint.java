package com.example.hostelcomplaintforum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class AddComplaint extends AppCompatActivity {

    CheckBox anonymous;
    Toolbar toolbar;
    Button sendBtn;
    FirebaseUser user;
    FirebaseFirestore fireStore;
    String subject, desc, mode = "public", name, email, room, hostel;
    EditText mSubject, mDesc, mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);
        initializations();
        sendBtn.setOnClickListener(view -> sendFunc());
    }

    private void sendFunc() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Complaint...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getStrings();
        if(subject.length()==0){
            Toast.makeText(this, "Enter the subject.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (mode == "anonymous") {
                            name = "Anonymous";
                            room = " ";
                        } else {
                            name = snapshot.getString("uName");
                            room = snapshot.getString("uRoom");
                        }
                        email = snapshot.getString("uEmail");
                        hostel = snapshot.getString("uHostel");
                        String uid = UUID.randomUUID().toString();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("cName", name);
                        map.put("cEmail", email);
                        map.put("cRoom", room);
                        map.put("cHostel", hostel);
                        map.put("cSubject", subject);
                        map.put("cDesc", desc);
                        map.put("mMode", mode);
                        map.put("cId", uid);
                        map.put("cTime",String.valueOf(System.currentTimeMillis()));
                        map.put("cState", "unseen");
                        map.put("cReply", null);
                        FirebaseFirestore.getInstance()
                                .collection("complaints")
                                .document(uid)
                                .set(map)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Complaint Added.", Toast.LENGTH_SHORT).show();
                                    mSubject.setText("");
                                    mDesc.setText("");
                                    progressDialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                });
                        startActivity(new Intent(getApplicationContext(),PublicFeed.class));
                        finish();
                    }
                });
    }

    private void getStrings() {
        subject = mSubject.getText().toString().trim();
        desc = mDesc.getText().toString().trim();

    }

    private void initializations() {
        anonymous = findViewById(R.id.anonymous);
        toolbar = findViewById(R.id.postToolbar);
        sendBtn = findViewById(R.id.send);
        mSubject = findViewById(R.id.subject);
        mDesc = findViewById(R.id.descriptionEditText);
        user = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
    }

    public void onCheckBoxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.anonymous:
                if (checked)
                    mode = "anonymous";
                else
                    mode = "public";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, PublicFeed.class));
        finish();
    }
}