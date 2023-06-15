package com.gocreative.tm.mahrem_ene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddPregnantDateActivity extends AppCompatActivity {
    private LinearLayout hasChildView, childAgeView, pregnantDateView, babyBirthdateView;
    private RadioGroup isPregnantGroup, hasChildGroup, childAgeGroup;
    private DatePicker pregnantDateV, babyBirthdateV;
    private Button finish;

    private String pregnantDate = "", babyBirthdate = "";
    private boolean isPregnant, hasChild, isChildAgeGreaterThanOne;

    private FirebaseFirestore firestore;
    private DocumentReference reference;
    private FirebaseAuth auth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pregnant_date);
        hasChildView = findViewById(R.id.has_child_view);
        childAgeView = findViewById(R.id.child_age_view);
        pregnantDateView = findViewById(R.id.pregnant_date_view);
        isPregnantGroup = findViewById(R.id.is_pregnant);
        hasChildGroup = findViewById(R.id.has_child);
        childAgeGroup = findViewById(R.id.child_age);
        finish = findViewById(R.id.finish);
        pregnantDateV = findViewById(R.id.pregnant_date);
        babyBirthdateV = findViewById(R.id.baby_birthdate);
        babyBirthdateView = findViewById(R.id.baby_birthdate_view);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("users")
                .document(auth.getUid())
                .collection("pregnancy_state")
                .document("current_state");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Biraz garaşyň...");

        isPregnantGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                isPregnant = radioButton.getText().toString().equals("Hawa");
                viewSetting();
            }
        });

        hasChildGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                hasChild = radioButton.getText().toString().equals("Hawa");
                viewSetting();

            }
        });

        childAgeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                isChildAgeGreaterThanOne = radioButton.getText().toString().equals("Ýok");
                viewSetting();

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                startApp();
            }
        });
    }

    private void viewSetting(){
        if (isPregnant){
            pregnantDateView.setVisibility(View.VISIBLE);
            hasChildView.setVisibility(View.GONE);
            childAgeView.setVisibility(View.GONE);
            finish.setVisibility(View.VISIBLE);
            pregnantDateV.setVisibility(View.VISIBLE);
            babyBirthdateV.setVisibility(View.GONE);
            babyBirthdateView.setVisibility(View.GONE);
        }else{
            pregnantDateView.setVisibility(View.GONE);
            finish.setVisibility(View.GONE);

            pregnantDateV.setVisibility(View.GONE);
            babyBirthdateV.setVisibility(View.GONE);

            childAgeView.setVisibility(View.GONE);
            babyBirthdateView.setVisibility(View.GONE);
            hasChildView.setVisibility(View.VISIBLE);
            if (hasChild){
                childAgeView.setVisibility(View.VISIBLE);
                finish.setVisibility(View.GONE);

                pregnantDateV.setVisibility(View.GONE);
                babyBirthdateV.setVisibility(View.GONE);

                pregnantDateView.setVisibility(View.GONE);
                hasChildView.setVisibility(View.VISIBLE);
                babyBirthdateView.setVisibility(View.GONE);
                if (isChildAgeGreaterThanOne){
                    childAgeView.setVisibility(View.VISIBLE);
                    finish.setVisibility(View.VISIBLE);

                    pregnantDateV.setVisibility(View.GONE);
                    babyBirthdateV.setVisibility(View.VISIBLE);

                    babyBirthdateView.setVisibility(View.VISIBLE);
                    pregnantDateView.setVisibility(View.GONE);
                    hasChildView.setVisibility(View.VISIBLE);
                }else{
                    finish.setVisibility(View.VISIBLE);
                }
            }else{
                childAgeView.setVisibility(View.GONE);
                finish.setVisibility(View.VISIBLE);

                pregnantDateV.setVisibility(View.GONE);
                babyBirthdateV.setVisibility(View.GONE);

                babyBirthdateView.setVisibility(View.GONE);
                pregnantDateView.setVisibility(View.GONE);
                hasChildView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startApp() {
        if (pregnantDateV.getVisibility() == View.VISIBLE){
            int month = pregnantDateV.getMonth()+1;
            pregnantDate = pregnantDateV.getDayOfMonth() + "-" + month + "-" + pregnantDateV.getYear();
        }else{
            pregnantDate = "";
        }

        if (babyBirthdateV.getVisibility() == View.VISIBLE){
            int month = babyBirthdateV.getMonth()+1;
            babyBirthdate = babyBirthdateV.getDayOfMonth() + "-" + month + "-" + babyBirthdateV.getYear();
        }else{
            babyBirthdate = "";
        }
        saveData();
    }

    private void saveData() {
        Map<String, Object> stateInfo = new HashMap<>();

        if (isPregnant){
            hasChild = false;
            isChildAgeGreaterThanOne = false;
        }
        stateInfo.put("is_pregnant", isPregnant);
        stateInfo.put("pregnancy_date", pregnantDate);
        stateInfo.put("has_child", hasChild);
        stateInfo.put("is_child_age_greater_than_one", !isChildAgeGreaterThanOne);
        stateInfo.put("child_birthdate", babyBirthdate);

        reference.set(stateInfo, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    SharedPreferences preferences = getSharedPreferences("checkForm", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isFilled", true);
                    editor.apply();

                    Intent intent = new Intent(AddPregnantDateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    Toast.makeText(AddPregnantDateActivity.this, "Maglumatlaryňyz üstünlikli ýüklendi!", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AddPregnantDateActivity.this, "Näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                    Log.d("Save data", "onComplete: " + task.getException().getMessage());
                }
            }
        });
    }
}