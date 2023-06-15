package com.gocreative.tm.lukman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button start = findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth user = FirebaseAuth.getInstance();

        if (user.getCurrentUser() != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}