package com.gocreative.tm.lukman;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.lukman.Adapters.HospitalsAdapter;
import com.gocreative.tm.lukman.Models.Hospital;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PickHospitalActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    HospitalsAdapter hospitalsAdapter;
    RecyclerView recyclerView;
    ArrayList<Hospital> hospitalArrayList;
    ProgressDialog progressDialog;

    String province, city, address, _name;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_hospital);

        progressDialog = new ProgressDialog(PickHospitalActivity.this);
        progressDialog.setMessage("Biraz garaşyň...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        address = getIntent().getStringExtra("address");
        position = getIntent().getIntExtra("position", 0);
        _name = getIntent().getStringExtra("name");

        Log.d("ASDASD", "onCreate: " + _name);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PickHospitalActivity.this));

        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore
                .collection("admin")
                .document("provinces")
                .collection(province)
                .document(city)
                .collection("hospitals");

        hospitalArrayList = new ArrayList<Hospital>();
        hospitalsAdapter = new HospitalsAdapter(PickHospitalActivity.this, hospitalArrayList, position, address, city, _name);

        recyclerView.setAdapter(hospitalsAdapter);
        eventChangeListener();
    }

    private void eventChangeListener() {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("firebase firestore", "onEvent: " + error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        hospitalArrayList.add(documentChange.getDocument().toObject(Hospital.class));
                    }
                    hospitalsAdapter.notifyDataSetChanged();


                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}