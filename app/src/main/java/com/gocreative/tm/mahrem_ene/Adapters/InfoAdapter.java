package com.gocreative.tm.mahrem_ene.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gocreative.tm.mahrem_ene.Models.Info;
import com.gocreative.tm.mahrem_ene.R;
import com.gocreative.tm.mahrem_ene.ReadStoryActivity;

import java.util.ArrayList;


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.StoryViewHolder> {
    Context context;
    ArrayList<Info> infoArrayList;

    public InfoAdapter(Context context, ArrayList<Info> infoArrayList) {
        this.context = context;
        this.infoArrayList = infoArrayList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_list, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Info info = infoArrayList.get(position);
        holder.textView.setText(info.getText());
        holder.titleView.setText(info.getTitle());

        if (info.getWeek() != -1){
            holder.weekView.setVisibility(View.VISIBLE);
            holder.weekView.setText("Hepde: " + info.getWeek());
            holder.textView.setMaxLines(2);
        }

        Glide.with(holder.itemView.getContext())
                .load(info.getImageUrl())
                .placeholder(R.drawable.no_image)
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ReadStoryActivity.class);
                intent.putExtra("values", info);
                holder.itemView.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoArrayList.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, textView, weekView;
        CardView cardView;
        ImageView imageView;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textView = itemView.findViewById(R.id.story_about);
            titleView = itemView.findViewById(R.id.story_name);
            weekView = itemView.findViewById(R.id.week);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}