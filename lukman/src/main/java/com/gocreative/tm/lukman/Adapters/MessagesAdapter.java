package com.gocreative.tm.lukman.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.lukman.Models.Message;
import com.gocreative.tm.lukman.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    Context context;
    ArrayList<Message> messageArrayList;
    private String currentUid;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessagesAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_sent, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_received, parent, false);
        }
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        currentUid = FirebaseAuth.getInstance().getUid();
        holder.message.setText(messageArrayList.get(position).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getSent_by().equals(currentUid)){
            return VIEW_TYPE_MESSAGE_SENT;
        }else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_txt_view);
        }
    }
}