package com.gocreative.tm.lukman;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gocreative.tm.lukman.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {
    TextView nameV, passportV, birthdateV, provinceV, cityV, addressV, hospitalV, doctorV;
    User user;
    CircleImageView circleImageView;
    ImageButton back;
    ImageButton save;
    String imageLink;

    FirebaseFirestore firestore;
    DocumentReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        nameV = findViewById(R.id.edit_profile_name_live_view);
        passportV = findViewById(R.id.passport_id);
        birthdateV = findViewById(R.id.birthdate);
        provinceV = findViewById(R.id.province);
        cityV = findViewById(R.id.city);
        addressV = findViewById(R.id.address);
        hospitalV = findViewById(R.id.hospital);
        doctorV = findViewById(R.id.doctor);
        circleImageView = findViewById(R.id.edit_profile_profile_image);
        back = findViewById(R.id.edit_profile_back_arrow);
        save = findViewById(R.id.edit_profile_save);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = firestore.collection("users")
                .document(auth.getUid());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        user = (User) getIntent().getSerializableExtra("user");
        if (user != null){

            String fullName = user.getName() + " "
                    + user.getSurname() + " "
                    + user.getF_name();


            nameV.setText(fullName);
            passportV.setText(user.getPassport());
            birthdateV.setText(user.getBirthday());
            provinceV.setText(user.getProvince());
            cityV.setText(user.getCity());
            addressV.setText(user.getAddress());
            hospitalV.setText(user.getHospital());
            doctorV.setText(user.getDoctor());

            Glide.with(this)
                    .load(user.getProfile_image())
                    .into(circleImageView);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(imageLink);
            }
        });

    }

    private void updateUserProfile(String imageLink) {
        return;
    }
}