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
import com.gocreative.tm.mahrem_ene.Models.City;
import com.gocreative.tm.mahrem_ene.R;

import java.util.ArrayList;


public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder> {
    Context context;
    ArrayList<City> cityArrayList;
    int provincePosition;

    public CitiesAdapter(Context context, ArrayList<City> cityArrayList, int provincePosition) {
        this.context = context;
        this.cityArrayList = cityArrayList;
        this.provincePosition = provincePosition;
    }

    @NonNull
    @Override
    public CitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new CitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesViewHolder holder, int position) {
        City city = cityArrayList.get(position);
        holder.textView.setText(city.getName());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FillInfo2Activity.class);
                intent.putExtra("selectedCity", city.getName());
                intent.putExtra("position", provincePosition);
                holder.itemView.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityArrayList.size();
    }

    public class CitiesViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout relativeLayout;
        public CitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.main_item_holder);
            textView = itemView.findViewById(R.id.item_name);
        }
    }
}