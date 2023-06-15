package com.gocreative.tm.lukman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillInfo2Activity extends AppCompatActivity {
    Spinner provinceV, jobTitleV;
    EditText addressV;
    LinearLayout hospitalV, cityV;
    TextView selectedHospitalV, selectedCityV;
    ProgressDialog progressDialog;

    FirebaseAuth user;
    FirebaseFirestore firestore;
    DocumentReference reference;

    String selectedCity, selectedHospital, address, selectedJob, _name, province;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info2);

        Button accept = findViewById(R.id.accept);

        provinceV = findViewById(R.id.province);
        cityV = findViewById(R.id.select_city);
        hospitalV = findViewById(R.id.select_hospital);
        addressV = findViewById(R.id.address);
        selectedCityV = findViewById(R.id.selected_city);
        selectedHospitalV = findViewById(R.id.selected_hospital);
        jobTitleV = findViewById(R.id.job_title);

        _name = getIntent().getStringExtra("name");

        user = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = firestore
                .collection("doctors")
                .document(user.getUid());

        progressDialog = new ProgressDialog(FillInfo2Activity.this);
        progressDialog.setMessage("Biraz garaşyň...");
        progressDialog.setCancelable(false);

        selectedCity = getIntent().getStringExtra("selectedCity");
        selectedHospital = getIntent().getStringExtra("selectedHospital");
        position = getIntent().getIntExtra("position", 0);
        address = getIntent().getStringExtra("address");

        if (address != null){
            addressV.setText(address);
        }

        if (selectedCity != null){
            selectedCityV.setText(selectedCity);
            selectedCityV.setTextColor(getResources().getColor(R.color.black));
        }

        if(selectedHospital != null){
            selectedHospitalV.setText(selectedHospital);
            selectedHospitalV.setTextColor(getResources().getColor(R.color.black));
        }
        // Provinces
        ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.provinces));
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        provinceV.setAdapter(provincesAdapter);
        provinceV.setSelection(position);

        // Job titles
        ArrayAdapter<String> jobsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jobs));
        jobsAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        jobTitleV.setAdapter(jobsAdapter);

        // Province selection handler.
        provinceV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isChoosed = provinceV.getSelectedItem().toString().equals("Saýlanmadyk");
                cityV.setEnabled(!isChoosed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cityV.setEnabled(false);
                hospitalV.setEnabled(false);
            }
        });

        hospitalV.setEnabled(!selectedCityV.getText().toString().equals("Saýlanmadyk"));

        cityV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillInfo2Activity.this, PickCityActivity.class);
                String province = provinceV.getSelectedItem().toString();

                switch (province) {
                    case "Balkan":
                        province = "balkan";
                        break;
                    case "Ahal":
                        province = "ahal";
                        break;
                    case "Lebap":
                        province = "lebap";
                        break;
                    case "Daşoguz":
                        province = "dasoguz";
                        break;
                    case "Mary":
                        province = "mary";
                        break;
                    case "Aşgabat":
                        province = "asgabat";
                        break;
                }
                intent.putExtra("province", province);
                intent.putExtra("position", provincesAdapter.getPosition(provinceV.getSelectedItem().toString()));
                intent.putExtra("name", _name);
                startActivity(intent);
            }
        });

        hospitalV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillInfo2Activity.this, PickHospitalActivity.class);
                String province = provincesAdapter.getItem(position);

                switch (province) {
                    case "Balkan":
                        province = "balkan";
                        break;
                    case "Ahal":
                        province = "ahal";
                        break;
                    case "Lebap":
                        province = "lebap";
                        break;
                    case "Daşoguz":
                        province = "dasoguz";
                        break;
                    case "Mary":
                        province = "mary";
                        break;
                    case "Aşgabat":
                        province = "asgabat";
                        break;
                }
                intent.putExtra("province", province);
                intent.putExtra("city", selectedCity);
                intent.putExtra("position", position);
                intent.putExtra("address", addressV.getText().toString());
                intent.putExtra("name", _name);

                startActivity(intent);
            }
        });


        // Setting info to db.
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedHospital = selectedHospitalV.getText().toString();
                String selectedProvince = provinceV.getSelectedItem().toString();
                String selectedCity = selectedCityV.getText().toString();
                String address = addressV.getText().toString().trim();
                selectedJob = jobTitleV.getSelectedItem().toString();

                if (isOk(selectedProvince,
                        selectedCity,
                        address,
                        selectedHospital,
                        selectedJob)){

                    saveUserData(selectedProvince,
                            selectedCity,
                            address,
                            selectedHospital,
                            selectedJob);
                }else {
                    Toast.makeText(FillInfo2Activity.this, "Maglumatlary dolduryň!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveUserData(String selectedProvince,
                              String selectedCity,
                              String address,
                              String selectedHospital,
                              String selectedJob) {
        progressDialog.show();
        List<String> list = new ArrayList<>();

        Map<String, Object> info = new HashMap<>();
        info.put("province", selectedProvince);
        info.put("address", address);
        info.put("hospital", selectedHospital);
        info.put("job", selectedJob);
        info.put("city", selectedCity);
        info.put("users", list);
        province = "";
        switch (selectedProvince) {
            case "Balkan":
                province = "balkan";
                break;
            case "Ahal":
                province = "ahal";
                break;
            case "Lebap":
                province = "lebap";
                break;
            case "Daşoguz":
                province = "dasoguz";
                break;
            case "Mary":
                province = "mary";
                break;
            case "Aşgabat":
                province = "asgabat";
                break;
        }
        reference.set(info, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference = firestore
                            .collection("admin")
                            .document("provinces")
                            .collection(province)
                            .document(selectedCity)
                            .collection("hospitals")
                            .document(selectedHospital);

                    Map<String, Object> drInfo = new HashMap<>();
                    drInfo.put("name", _name);
                    drInfo.put("id", user.getUid());

                    reference.update("staff", FieldValue.arrayUnion(drInfo)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent intent = new Intent(FillInfo2Activity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(FillInfo2Activity.this, "Näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                                Log.d("Uploading info", "onComplete: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isOk(String selectedProvince,
                         String selectedCity,
                         String address,
                         String selectedHospital,
                         String selectedJob) {
        return !selectedProvince.equals("Saýlanmadyk") &&
                !selectedCity.equals("Saýlanmadyk") &&
                !selectedHospital.equals("Saýlanmadyk") &&
                !selectedJob.equals("Saýlanmadyk") &&
                !address.isEmpty();
    }
}