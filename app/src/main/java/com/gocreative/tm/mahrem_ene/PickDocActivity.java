package com.gocreative.tm.mahrem_ene;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.mahrem_ene.Adapters.DoctorsAdapter;
import com.gocreative.tm.mahrem_ene.Models.Hospital;
import com.gocreative.tm.mahrem_ene.Models.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PickDocActivity extends AppCompatActivity {
    DoctorsAdapter doctorsAdapter;
    RecyclerView recyclerView;
    ArrayList<Staff> staffArrayList;

    List<String> staff;
    Hospital hospitalStaff;

    String province, city, address, hospital;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_doc);

        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        address = getIntent().getStringExtra("address");
        position = getIntent().getIntExtra("position", 0);
        hospital = getIntent().getStringExtra("hospital");
        hospitalStaff = (Hospital) getIntent().getSerializableExtra("staff");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PickDocActivity.this));

        staffArrayList = new ArrayList<Staff>();
        doctorsAdapter = new DoctorsAdapter(PickDocActivity.this,
                staffArrayList,
                position,
                address,
                city,
                hospital,
                staff);

        recyclerView.setAdapter(doctorsAdapter);
        eventChangeListener();
    }

    private void eventChangeListener() {
        for (HashMap<String,String> hashMap: hospitalStaff.getStaff()){
            staffArrayList.add(new Staff(hashMap.get("name"), hashMap.get("id")));
            doctorsAdapter.notifyDataSetChanged();
        }
    }
}