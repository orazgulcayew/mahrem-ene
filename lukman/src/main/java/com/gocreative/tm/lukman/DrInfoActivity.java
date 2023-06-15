package com.gocreative.tm.lukman;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gocreative.tm.lukman.Models.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrInfoActivity extends AppCompatActivity {
    TextView nameV, phoneNum, birthdateV, provinceV, cityV, addressV, hospitalV, jobV;
    Doctor doctor;
    CircleImageView circleImageView;
    ImageButton back;
    String imageLink;

    FirebaseFirestore firestore;
    DocumentReference reference;
    FirebaseAuth auth;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_info);
        nameV = findViewById(R.id.edit_profile_name_live_view);
        birthdateV = findViewById(R.id.birthdate);
        provinceV = findViewById(R.id.province);
        cityV = findViewById(R.id.city);
        addressV = findViewById(R.id.address);
        hospitalV = findViewById(R.id.hospital);
        phoneNum = findViewById(R.id.phone_num);
        circleImageView = findViewById(R.id.edit_profile_profile_image);
        back = findViewById(R.id.edit_profile_back_arrow);
        jobV = findViewById(R.id.job_name);

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

        doctor = (Doctor) getIntent().getSerializableExtra("doctor");

        if (doctor != null){
            String fullName = doctor.getName() + " "
                    + doctor.getSurname() + " "
                    + doctor.getF_name();

            nameV.setText(fullName);
            birthdateV.setText(doctor.getBirthday());
            provinceV.setText(doctor.getProvince());
            cityV.setText(doctor.getCity());
            addressV.setText(doctor.getAddress());
            hospitalV.setText(doctor.getHospital());
            phoneNum.setText(doctor.getPhone_number());
            jobV.setText(doctor.getJob());

            Glide.with(this)
                    .load(doctor.getProfile_image())
                    .placeholder(R.drawable.ic_user)
                    .into(circleImageView);
        }
    }
}