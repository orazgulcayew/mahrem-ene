package com.gocreative.tm.lukman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gocreative.tm.lukman.Fragments.AboutFragment;
import com.gocreative.tm.lukman.Fragments.ChatFragment;
import com.gocreative.tm.lukman.Fragments.HomeFragment;
import com.gocreative.tm.lukman.Fragments.OneAgeFragment;
import com.gocreative.tm.lukman.Fragments.SettingsFragment;
import com.gocreative.tm.lukman.Fragments.UsersFragment;
import com.gocreative.tm.lukman.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    Fragment fragment;
    TextView toolbarText;
    String currentUid;
    User user;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        toolbarText = findViewById(R.id.toolbar_text);

        auth = FirebaseAuth.getInstance();
        currentUid = auth.getUid();
        firestore = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_all_user);
        navigationView.setItemIconTintList(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();

        View view = navigationView.getHeaderView(0);

        TextView nameV = view.findViewById(R.id.name_surname);
        CircleImageView profileImage = view.findViewById(R.id.profile_image);
        RelativeLayout relativeLayout = view.findViewById(R.id.relative_layout);

        documentReference = firestore.collection("doctors").document(currentUid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    user = task.getResult().toObject(User.class);

                    nameV.setText(user.getName() + " " + user.getSurname());

                    if (user.getProfile_image() != null){
                        Glide.with(MainActivity.this)
                                .load(user.getProfile_image())
                                .placeholder(R.drawable.ic_user)
                                .into(profileImage);
                    }
                }else{
                    Log.d("Profile", "onComplete: " + task.getException().getMessage());
                }
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileEditActivity.class);
                intent.putExtra("user", user);
                if (user == null)
                {
                    Toast.makeText(MainActivity.this, "asdasdasdasfasdasf", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_all_user:
                toolbarText.setText(R.string.app_name);
                fragment = new UsersFragment();
                break;
            case R.id.nav_home:
                toolbarText.setText(R.string.app_name);
                fragment = new HomeFragment();
                break;
            case R.id.nav_one_age:
                toolbarText.setText("1 ýaşa çenli döwri");
                fragment = new OneAgeFragment();
                break;

            case R.id.nav_chat:
                toolbarText.setText("Ulanyjylar");
                fragment = new UsersFragment();
                break;
            case R.id.nav_settings:
                toolbarText.setText("Sazlamalar");
                fragment = new SettingsFragment();
                break;

            case R.id.nav_about:
                toolbarText.setText("Programma barada");
                fragment = new AboutFragment();
                break;
        }
        if (fragment != null){
            openFragment(fragment);
        }else{
            Log.d("Fragment Changing", "onNavigationItemSelected: " + "Error in creating fragment!");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openFragment(final Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        boolean isAddedFragment = false;
        Fragment addedFragment = manager.findFragmentByTag(backStateName);

        // if fragment is already in backstack
        if (addedFragment != null){
            isAddedFragment = addedFragment.isAdded();
        }
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, backStateName);
            if (!isAddedFragment){
                ft.addToBackStack(backStateName);
            }
            ft.commit();
        }
    }
}