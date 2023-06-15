package com.gocreative.tm.mahrem_ene.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.mahrem_ene.FillInfo2Activity;
import com.gocreative.tm.mahrem_ene.Models.Hospital;
import com.gocreative.tm.mahrem_ene.Models.Staff;
import com.gocreative.tm.mahrem_ene.R;

import java.util.ArrayList;
import java.util.List;


public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder> {
    Context context;
    ArrayList<Staff> staffArrayList;
    int provincePosition;
    String address, city, hospital;
    List<String> staffs;


    public DoctorsAdapter(Context context, ArrayList<Staff> staffArrayList,
                          int provincePosition,
                          String address,
                          String city,
                          String hospital,
                          List<String> staffs) {
        this.context = context;
        this.staffArrayList = staffArrayList;
        this.provincePosition = provincePosition;
        this.address = address;
        this.city = city;
        this.hospital = hospital;
        this.staffs = staffs;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new DoctorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position) {
        Staff staff = staffArrayList.get(position);
        holder.textView.setText(staff.getStaffName());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FillInfo2Activity.class);
                intent.putExtra("selectedHospital", hospital);
                intent.putExtra("position", provincePosition);
                intent.putExtra("selectedCity", city);
                intent.putExtra("address", address);
                intent.putExtra("hospital", hospital);
                intent.putStringArrayListExtra("staff", (ArrayList<String>) staffs);
                intent.putExtra("selectedStaff", staff.getStaffName());
                intent.putExtra("id", staff.getId());
                holder.itemView.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffArrayList.size();
    }

    public class DoctorsViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout relativeLayout;
        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.main_item_holder);
            textView = itemView.findViewById(R.id.item_name);
        }
    }
}