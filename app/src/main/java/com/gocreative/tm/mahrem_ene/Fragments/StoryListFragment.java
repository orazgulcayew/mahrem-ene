package com.gocreative.tm.mahrem_ene.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.mahrem_ene.Adapters.InfoAdapter;
import com.gocreative.tm.mahrem_ene.Models.Info;
import com.gocreative.tm.mahrem_ene.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StoryListFragment extends Fragment {
    String infoDb;

    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    InfoAdapter infoAdapter;
    RecyclerView recyclerView;
    ArrayList<Info> infoArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_list, container, false);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        infoDb = getArguments().getString("info_db");

        if (infoDb != null){
            retrieveInformations(infoDb);
        }
        return view;
    }

    private void retrieveInformations(String infoDb) {
        collectionReference = firestore
                .collection("admin")
                .document("weekly_information")
                .collection(infoDb);

        infoArrayList = new ArrayList<Info>();
        infoAdapter = new InfoAdapter(getContext(), infoArrayList);

        recyclerView.setAdapter(infoAdapter);
        eventChangeListener(collectionReference);
    }

    private void eventChangeListener(CollectionReference collectionReference) {
        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.d("firebase firestore", "onEvent: " + error.getMessage());
                            return;
                        }
                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                infoArrayList.add(documentChange.getDocument().toObject(Info.class));
                            }
                            infoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}