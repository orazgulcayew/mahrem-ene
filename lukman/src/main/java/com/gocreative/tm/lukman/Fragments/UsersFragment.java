package com.gocreative.tm.lukman.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.lukman.Adapters.UsersAdapter;
import com.gocreative.tm.lukman.Models.User;
import com.gocreative.tm.lukman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    UsersAdapter usersAdapter;
    RecyclerView recyclerView;
    ArrayList<User> userArrayList;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    CollectionReference collectionReference;

    TextView empty;

    int objectCount=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        empty = view.findViewById(R.id.empty);

        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("users");
        auth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userArrayList = new ArrayList<User>();
        usersAdapter = new UsersAdapter(getContext(), userArrayList);

        recyclerView.setAdapter(usersAdapter);

        eventChangeListener();

        return view;
    }

    private void eventChangeListener() {
        collectionReference
                .whereEqualTo("doctor_id", auth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d("firebase firestore", "onEvent: " + error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        objectCount++;
                        userArrayList.add(documentChange.getDocument().toObject(User.class));
                    }
                    usersAdapter.notifyDataSetChanged();
                }
                if (objectCount != 0) {
                    empty.setVisibility(View.GONE);
                }
            }
        });
    }
}