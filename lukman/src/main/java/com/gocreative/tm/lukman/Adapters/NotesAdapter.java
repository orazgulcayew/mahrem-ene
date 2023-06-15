package com.gocreative.tm.lukman.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.lukman.Models.Note;
import com.gocreative.tm.lukman.R;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    ArrayList<Note> noteArrayList;
    String dateToSend;

    public NotesAdapter(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list, parent, false);
        return new NotesViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = noteArrayList.get(position);

        // Goyulan senesi
        SimpleDateFormat year = new SimpleDateFormat("yy", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat today = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        SimpleDateFormat hourMinute = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Instant now = Instant.now();
        Date yesterday = Date.from(now.minus(1, ChronoUnit.DAYS));
        String yesterdatDate = today.format(yesterday);
        Date datePub = note.getDate_created().toDate();
        Date todayDate = new Date();
        String _today = today.format(todayDate);
        String pubDate = today.format(datePub);
        String setDate = "• " + getMonthName(month.format(datePub))
                + " " + day.format(datePub) + ", "
                + hourMinute.format(datePub) + ", "
                + year.format(datePub) + "ý";

        if (_today.equals(pubDate)){
            setDate = "• şu gün, " + hourMinute.format(datePub);
            dateToSend = setDate;
        }else if (yesterdatDate.equals(pubDate)){
            setDate = "• düýn, " + hourMinute.format(datePub);
            dateToSend = setDate;
        }
        else {
            dateToSend = setDate;
        }

        String finalSetDate = setDate;

        holder.date.setText(finalSetDate);
        holder.note.setText(note.getNote());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("date", finalSetDate);
                bundle.putString("note", note.getNote());
                bundle.putString("id", note.getId());
//
//                NoteViewFragment noteViewFragment = new NoteViewFragment();
//                noteViewFragment.setArguments(bundle);
//
//                FragmentManager manager = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
//                manager.beginTransaction()
//                        .replace(R.id.fragment_container, noteViewFragment, noteViewFragment.getClass().getSimpleName())
//                        .addToBackStack(HomeFragment.class.getSimpleName())
//                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView date, note;
        CardView cardView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            note = itemView.findViewById(R.id.note);
            cardView = itemView.findViewById(R.id.card_note);
        }
    }

    public static String getMonthName(String month){
        String[] monthNames = {"Ýanwar", "Fewral", "Mart",
                "Aprel", "Maý", "Iýun", "Iýul", "Awgust",
                "Sentýabr", "Oktýabr", "Noýabr", "Dekabr"};
        return monthNames[Integer.parseInt(month)-1];
    }
}
