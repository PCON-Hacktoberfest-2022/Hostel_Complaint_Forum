package com.example.hostelcomplaintforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class PublicFeed extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    CardView addBtn;
    RecyclerView recyclerView;
    ArrayList<ComplaintItem> mFiles = new ArrayList<ComplaintItem>();
    ComplaintAdapter complaintAdapter;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_feed);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.public_toolBar);
        addBtn = findViewById(R.id.add_complaint);
        recyclerView = findViewById(R.id.public_recycler_view);
        user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot snapshot=task.getResult();
                        if(snapshot.getBoolean("uAuthority").equals(true)) {
                            addBtn.setVisibility(View.GONE);
                        }
                        else {
                            addBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
        configureToolbar();
        configureNavigationDrawer();
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.a) {
                filterHostel("a");
            } else if (item.getItemId() == R.id.b) {
                filterHostel("b");
            } else if (item.getItemId() == R.id.c) {
                filterHostel("c");
            } else if (item.getItemId() == R.id.d) {
                filterHostel("d");
            } else if (item.getItemId() == R.id.e) {
                filterHostel("e");
            } else if (item.getItemId() == R.id.f) {
                filterHostel("f");
            } else if (item.getItemId() == R.id.g) {
                filterHostel("g");
            } else if (item.getItemId() == R.id.h) {
                filterHostel("h");
            } else if (item.getItemId() == R.id.i) {
                filterHostel("i");
            } else if (item.getItemId() == R.id.j) {
                filterHostel("j");
            } else if (item.getItemId() == R.id.k) {
                filterHostel("k");
            } else if (item.getItemId() == R.id.all) {
                getFiles();
            }
            return false;
        });
        if (mFiles.size() > 0 || complaintAdapter == null) getFiles();

        addBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, AddComplaint.class));
            finish();
        });
        //getFiles();

    }

    private void filter(String s) {
        ArrayList<ComplaintItem> temp = new ArrayList<>();
        if (mFiles.size() > 0) {
            for (ComplaintItem b : mFiles) {
                if (b.subject.toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))
                        || b.desc.toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT)))
                    temp.add(b);
            }
        }
        if (complaintAdapter != null) complaintAdapter.updateData(temp);
    }

    private void filterHostel(String s) {
        if(s==null) return;
        ArrayList<ComplaintItem> temp = new ArrayList<>();
        if (mFiles.size() > 0) {
            for (ComplaintItem b : mFiles) {
                if (b.hostel.toLowerCase(Locale.ROOT).equals(s.toLowerCase(Locale.ROOT)))
                    temp.add(b);
            }
        }
        if (complaintAdapter != null) complaintAdapter.updateData(temp);
    }

    private void filterSearch(String s) {
        if(s==null) return;

        ArrayList<ComplaintItem> temp = new ArrayList<>();
        if (mFiles.size() > 0) {
            for (ComplaintItem b : mFiles) {
                //System.out.println(b.email);
                if (b.email.equals(s))
                    temp.add(b);
            }
        }
        if (complaintAdapter != null) complaintAdapter.updateData(temp);
    }

    private void getFiles() {
        mFiles.clear();
        FirebaseFirestore.getInstance()
                .collection("complaints")
                .orderBy("cTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshots = task.getResult();
                        for (DocumentSnapshot snapshot : querySnapshots) {
                            String subject, desc, mode, name, email, room, hostel, reply, state;
                            long time;
                            subject = snapshot.getString("cSubject");
                            desc = snapshot.getString("cDesc");
                            mode = snapshot.getString("cMode");
                            name = snapshot.getString("cName");
                            email = snapshot.getString("cEmail");
                            room = snapshot.getString("cRoom");
                            hostel = snapshot.getString("cHostel");
                            state = snapshot.getString("cState");
                            reply = snapshot.getString("cReply");
                            time = Long.parseLong(snapshot.getString("cTime"));
                            mFiles.add(new ComplaintItem(subject, desc, mode, name, email, room, hostel, time, state, reply));
                            if (complaintAdapter != null)
                                complaintAdapter.updateData(mFiles);
                            else {
                                if (mFiles.size() >= 1) {
                                    complaintAdapter = new ComplaintAdapter(getApplicationContext(), mFiles);
                                    recyclerView.setAdapter(complaintAdapter);
                                    if (getApplicationContext() != null)
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                                                , RecyclerView.VERTICAL, false));
                                }
                            }
                        }
                        if (mFiles.size() >= 1) {
                            complaintAdapter = new ComplaintAdapter(getApplicationContext(), mFiles);
                            recyclerView.setAdapter(complaintAdapter);
                            if (getApplicationContext() != null)
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                                        , RecyclerView.VERTICAL, false));
                        }
                    }
                });

    }

    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.public_toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.public_feed_menu, menu);
        MenuItem menuItem= menu.findItem(R.id.public_search);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String inputs =s.toLowerCase();
                ArrayList<ComplaintItem> filteredFiles = new ArrayList<ComplaintItem>();
                for(ComplaintItem item : mFiles){
                    if (item.name.contains(inputs) || item.email.contains(inputs)){
                        filteredFiles.add(item);
                    }
                }
                complaintAdapter.updateData(filteredFiles);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.resetPassword) {
                String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                EditText editText= new EditText(this);
                AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(this);
                passwordResetDialog.setTitle("Password Reset");
                passwordResetDialog.setMessage("Enter your Email-ID to receive password reset link.");
                passwordResetDialog.setView(editText);
                editText.setText(email);

                passwordResetDialog.setPositiveButton("Proceed", (dialogInterface, i) -> {
                    String mail= editText.getText().toString().trim();
                    if(validateEmail(mail))
                        FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(PublicFeed.this, "Password reset link sent.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(PublicFeed.this, "Password reset link not sent. "+ e.getMessage(), Toast.LENGTH_SHORT).show());
                    else Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancel", (dialogInterface, i) -> {

                });
                passwordResetDialog.create().show();

                return false;
            }
            if (menuItem.getItemId() == R.id.myComplaints) {
                filterSearch(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
            if (menuItem.getItemId() == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                Toast.makeText(PublicFeed.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return false;
        }
        return true;
    }
    public boolean validateEmail(String s){
        if(s==null || s.isEmpty()){
            return false;
        }
        String emailRegex= "^[a-zA-Z0-9_+&-]+(?:\\."+"[a-zA-Z0-9_+&-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern= Pattern.compile(emailRegex);
        return pattern.matcher(s).matches();
    }
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}