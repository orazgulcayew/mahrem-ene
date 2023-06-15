package com.gocreative.tm.mahrem_ene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FillInfoActivity extends AppCompatActivity {
    private EditText nameV, surnameV, fNameV, passportV;
    private String name, surname, fName, passport;
    private String day, month, year, birthday;
    private DatePicker datePicker;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);

        nameV = findViewById(R.id.name);
        surnameV = findViewById(R.id.surname);
        fNameV = findViewById(R.id.fathers_name);
        passportV = findViewById(R.id.passport_id);
        datePicker = findViewById(R.id.birthdate);

        Button accept = findViewById(R.id.accept);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameV.getText().toString().trim();
                surname = surnameV.getText().toString().trim();
                fName = fNameV.getText().toString().trim();
                passport = passportV.getText().toString().trim();

                day = Integer.toString(datePicker.getDayOfMonth());
                month = Integer.toString(datePicker.getMonth() + 1);
                year = Integer.toString(datePicker.getYear());

                birthday = day + "-" + month + "-" + year;

                if (isOk(name, surname, fName, passport, birthday)){
                    saveInfo(name, surname, fName, passport, birthday);
                }else{
                    Toast.makeText(FillInfoActivity.this, "Maglumatlaryňyzy doly giriziň!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveInfo(String name,
                          String surname,
                          String fName,
                          String passport,
                          String birthday) {
        ProgressDialog progressDialog = new ProgressDialog(FillInfoActivity.this);
        progressDialog.setMessage("Ýatda saklanýar...");
        progressDialog.show();

        DocumentReference reference = firestore.collection("users").document(userId);

        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("surname", surname);
        info.put("f_name", fName);
        info.put("passport", passport);
        info.put("birthday", birthday);
        info.put("date_created", Timestamp.now());
        info.put("profile_image", "");

        reference.set(info, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(FillInfoActivity.this, FillInfo2Activity.class);

                    startActivity(intent);

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(FillInfoActivity.this, "Näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isOk(String name,
                         String surname,
                         String fName,
                         String passport,
                         String birthday) {
        return !name.isEmpty() &&
                !surname.isEmpty() &&
                !fName.isEmpty() &&
                !passport.isEmpty() &&
                !birthday.isEmpty();
    }
}