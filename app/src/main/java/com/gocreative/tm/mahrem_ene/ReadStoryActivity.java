package com.gocreative.tm.mahrem_ene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gocreative.tm.mahrem_ene.Models.Info;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ReadStoryActivity extends AppCompatActivity {
    ImageView imageView, back;
    TextView titleView, textView, weekView;
    CollapsingToolbarLayout toolbarLayout;

    Info values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);
        // Status bar color changing to pink
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.pink_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(0);
        }

        imageView = findViewById(R.id.story_image);
        titleView = findViewById(R.id.title);
        textView = findViewById(R.id.text);
        weekView = findViewById(R.id.week);
        toolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        back = findViewById(R.id.back);

        back.setOnClickListener(v->{
            onBackPressed();
        });

        values = (Info) getIntent().getSerializableExtra("values");

        //Setting values
        Glide.with(this)
                .load(values.getImageUrl())
                .placeholder(R.drawable.no_image)
                .into(imageView);

        toolbarLayout.setTitle(values.getTitle());
        titleView.setText(values.getTitle());
        textView.setText(values.getText());

        if(values.getWeek() != -1){
            weekView.setVisibility(View.VISIBLE);
            weekView.setText("Hepde: " + values.getWeek());
        }
    }
}