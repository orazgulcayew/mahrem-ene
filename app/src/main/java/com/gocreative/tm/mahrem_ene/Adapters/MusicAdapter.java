package com.gocreative.tm.mahrem_ene.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.gocreative.tm.mahrem_ene.Models.Music;
import com.gocreative.tm.mahrem_ene.R;

import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    Context context;
    ArrayList<Music> musicArrayList;
    ArrayList<JcAudio> jcAudios;

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_list, parent, false);
        return new MusicViewHolder(view);
    }

    public MusicAdapter(Context context, ArrayList<Music> musicArrayList, ArrayList<JcAudio> jcAudios) {
        this.context = context;
        this.jcAudios = jcAudios;
        this.musicArrayList = musicArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicArrayList.get(position);
        holder.artistView.setText(music.getMusicArtist());
        holder.titleView.setText(music.getMusicTitle());

    }

    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }


    public class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, artistView;
        EqualizerView equalizerView;
        ImageView playButton;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            artistView = itemView.findViewById(R.id.music_artist);
            titleView = itemView.findViewById(R.id.music_title);
            equalizerView = itemView.findViewById(R.id.equalizer_view);
            playButton = itemView.findViewById(R.id.play_song);
        }
    }
}