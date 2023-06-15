package com.gocreative.tm.mahrem_ene.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.gocreative.tm.mahrem_ene.Adapters.MusicAdapter;
import com.gocreative.tm.mahrem_ene.Models.Music;
import com.gocreative.tm.mahrem_ene.R;

import java.util.ArrayList;

public class LullabyFragment extends Fragment {
    MusicAdapter musicAdapter;
    RecyclerView recyclerView;
    ArrayList<Music> musicArrayList;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios;
    JcPlayerManagerListener playerManagerListener;
    JcStatus jcPlayerStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lullaby, container, false);
        jcPlayerView = view.findViewById(R.id.music_player);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playerManagerListener = new JcPlayerManagerListener() {
            @Override
            public void onPreparedAudio(@NonNull JcStatus jcStatus) {

            }

            @Override
            public void onCompletedAudio() {

            }

            @Override
            public void onPaused(@NonNull JcStatus jcStatus) {
                jcPlayerStatus = jcStatus;
            }

            @Override
            public void onContinueAudio(@NonNull JcStatus jcStatus) {
                jcPlayerStatus = jcStatus;

            }

            @Override
            public void onPlaying(@NonNull JcStatus jcStatus) {
                jcPlayerStatus = jcStatus;

            }

            @Override
            public void onTimeChanged(@NonNull JcStatus jcStatus) {
                jcPlayerStatus = jcStatus;

            }

            @Override
            public void onStopped(@NonNull JcStatus jcStatus) {
                jcPlayerStatus = jcStatus;

            }

            @Override
            public void onJcpError(@NonNull Throwable throwable) {

            }
        };

        jcAudios = new ArrayList<>();
        musicArrayList = new ArrayList<Music>();
        musicAdapter = new MusicAdapter(getContext(),musicArrayList, jcAudios);
        recyclerView.setAdapter(musicAdapter);

        eventChangeListener();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventChangeListener() {

        for (int i = 1; i <= 3; i++) {
            musicArrayList.add(new Music("Hüwdi " + i, "Läle Begnazarowa"));
            musicAdapter.notifyDataSetChanged();
            jcAudios.add(JcAudio.createFromRaw(getResources().getIdentifier("music_"+i, "raw", getActivity().getPackageName())));
        }

        jcPlayerView.initPlaylist(jcAudios, null);

    }
}