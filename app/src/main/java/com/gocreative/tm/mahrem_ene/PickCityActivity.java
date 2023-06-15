package com.gocreative.tm.mahrem_ene;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gocreative.tm.mahrem_ene.Adapters.CitiesAdapter;
import com.gocreative.tm.mahrem_ene.Models.City;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PickCityActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    CitiesAdapter citiesAdapter;
    RecyclerView recyclerView;
    ArrayList<City> cityArrayList;
    ProgressDialog progressDialog;

    String province;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_city);

        progressDialog = new ProgressDialog(PickCityActivity.this);
        progressDialog.setMessage("Biraz garaşyň...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        province = getIntent().getStringExtra("province");
        position = getIntent().getIntExtra("position", 0);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PickCityActivity.this));

        firestore = FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("admin").document("provinces").collection(province);

        cityArrayList = new ArrayList<City>();
        citiesAdapter = new CitiesAdapter(PickCityActivity.this, cityArrayList, position);

        recyclerView.setAdapter(citiesAdapter);
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
                    Toast.makeText(PickCityActivity.this, "Näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                    Log.d("firebase firestore", "onEvent: " + error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        cityArrayList.add(documentChange.getDocument().toObject(City.class));
                    }
                    citiesAdapter.notifyDataSetChanged();

                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}