package com.gocreative.tm.lukman.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gocreative.tm.lukman.FillInfo2Activity;
import com.gocreative.tm.lukman.Fragments.ChatFragment;
import com.gocreative.tm.lukman.Models.Hospital;
import com.gocreative.tm.lukman.Models.User;
import com.gocreative.tm.lukman.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    Context context;
    ArrayList<User> userArrayList;

    public UsersAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = userArrayList.get(position);

        holder.nameSurname.setText(user.getName() + " " + user.getSurname());
        holder.state.setText("We don't believe what's on TV");

        Glide.with(context)
                .load(user.getProfile_image())
                .placeholder(R.drawable.ic_user)
                .into(holder.profileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(bundle);
                FragmentManager managerList = ((FragmentActivity)context).getSupportFragmentManager();
                managerList.beginTransaction()
                        .replace(R.id.fragment_container, chatFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView nameSurname, state;
        CircleImageView profileImage;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            nameSurname = itemView.findViewById(R.id.name_surname);
            state = itemView.findViewById(R.id.state);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}