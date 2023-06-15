package com.gocreative.tm.lukman.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.lukman.FillInfo2Activity;
import com.gocreative.tm.lukman.Models.Hospital;
import com.gocreative.tm.lukman.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.HospitalsViewHolder> {
    Context context;
    ArrayList<Hospital> hospitalArrayList;
    int provincePosition;
    String address, city, _name;


    public HospitalsAdapter(Context context, ArrayList<Hospital> hospitalArrayList, int provincePosition, String address, String city, String _name) {
        this.context = context;
        this.hospitalArrayList = hospitalArrayList;
        this.provincePosition = provincePosition;
        this.address = address;
        this.city = city;
        this._name = _name;
    }

    @NonNull
    @Override
    public HospitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new HospitalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalsViewHolder holder, int position) {
        Hospital hospital = hospitalArrayList.get(position);
        holder.textView.setText(hospital.getName());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FillInfo2Activity.class);
                intent.putExtra("selectedHospital", hospital.getName());
                intent.putExtra("position", provincePosition);
                intent.putExtra("selectedCity", city);
                intent.putExtra("address", address);
                intent.putExtra("name", _name);

                holder.itemView.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitalArrayList.size();
    }

    public class HospitalsViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout relativeLayout;
        public HospitalsViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.main_item_holder);
            textView = itemView.findViewById(R.id.item_name);
        }
    }
}