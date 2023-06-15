package com.gocreative.tm.mahrem_ene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gocreative.tm.mahrem_ene.Models.Hospital;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    Spinner provinceV;
    EditText addressV;
    LinearLayout hospitalV, familyDrV, cityV;
    TextView selectedHospitalV, selectedFamilyDocV, selectedCityV;
    ProgressDialog progressDialog;

    FirebaseAuth user;
    FirebaseFirestore firestore;
    DocumentReference reference;


    String selectedCity, selectedHospital, address, selectedStaff, selectedStaffId;
    Hospital staff;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info2);

        Button accept = findViewById(R.id.accept);

        provinceV = findViewById(R.id.province);
        cityV = findViewById(R.id.select_city);
        hospitalV = findViewById(R.id.select_hospital);
        familyDrV = findViewById(R.id.select_family_doc);
        addressV = findViewById(R.id.address);
        selectedCityV = findViewById(R.id.selected_city);
        selectedHospitalV = findViewById(R.id.selected_hospital);
        selectedFamilyDocV = findViewById(R.id.selected_doc);

        user = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = firestore
                .collection("users")
                .document(user.getUid());

        progressDialog = new ProgressDialog(FillInfo2Activity.this);
        progressDialog.setMessage("Biraz garaşyň...");
        progressDialog.setCancelable(false);

        selectedCity = getIntent().getStringExtra("selectedCity");
        selectedHospital = getIntent().getStringExtra("selectedHospital");
        position = getIntent().getIntExtra("position", 0);
        address = getIntent().getStringExtra("address");
        staff = (Hospital) getIntent().getSerializableExtra("staff");
        selectedStaff = getIntent().getStringExtra("selectedStaff");
        selectedStaffId = getIntent().getStringExtra("id");

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
        if (selectedStaff != null){
            selectedFamilyDocV.setText(selectedStaff);
            selectedFamilyDocV.setTextColor(getResources().getColor(R.color.black));
        }

        ArrayAdapter<String> provincesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.provinces));
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        provinceV.setAdapter(provincesAdapter);
        provinceV.setSelection(position);

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
                familyDrV.setEnabled(false);
            }
        });

        hospitalV.setEnabled(!selectedCityV.getText().toString().equals("Saýlanmadyk"));
        familyDrV.setEnabled(!selectedHospitalV.getText().toString().equals("Saýlanmadyk"));

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

                startActivity(intent);
            }
        });

        familyDrV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillInfo2Activity.this, PickDocActivity.class);
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
                intent.putExtra("hospital", selectedHospital);
                intent.putExtra("staff", (Hospital) staff);

                startActivity(intent);
            }
        });
        // Setting info to db.
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedHospital = selectedHospitalV.getText().toString();
                String selectedFamilyDr = selectedFamilyDocV.getText().toString();
                String selectedProvince = provinceV.getSelectedItem().toString();
                String selectedCity = selectedCityV.getText().toString();
                String address = addressV.getText().toString().trim();

                if (isOk(selectedProvince,
                        selectedCity,
                        address,
                        selectedHospital,
                        selectedFamilyDr)){

                    saveUserData(selectedProvince,
                            selectedCity,
                            address,
                            selectedHospital,
                            selectedFamilyDr);

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
                              String selectedFamilyDr) {
        progressDialog.show();

        Map<String, Object> info = new HashMap<>();
        info.put("province", selectedProvince);
        info.put("address", address);
        info.put("hospital", selectedHospital);
        info.put("doctor", selectedFamilyDr);
        info.put("city", selectedCity);
        info.put("doctor_id", selectedStaffId);

        reference.set(info, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference = firestore.collection("rooms").document(user.getUid());
                    Map<String, String> map = new HashMap<>();
                    map.put("doctor_id", selectedStaffId);
                    map.put("user_id", reference.getId());

                    reference.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                reference = firestore.collection("doctors").document(selectedStaffId);
                                reference.update("users", FieldValue.arrayUnion(user.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            progressDialog.dismiss();
                                            Intent intent = new Intent(FillInfo2Activity.this, AddPregnantDateActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                        else{
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
            }
        });
    }

    private boolean isOk(String selectedProvince,
                         String selectedCity,
                         String address,
                         String selectedHospital,
                         String selectedFamilyDr) {
        return !selectedProvince.equals("Saýlanmadyk") &&
                !selectedCity.equals("Saýlanmadyk") &&
                !selectedHospital.equals("Saýlanmadyk") &&
                !selectedFamilyDr.equals("Saýlanmadyk") &&
                !address.isEmpty();
    }
}