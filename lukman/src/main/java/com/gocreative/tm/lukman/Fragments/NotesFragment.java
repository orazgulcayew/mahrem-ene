package com.gocreative.tm.lukman.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.lukman.Adapters.NotesAdapter;
import com.gocreative.tm.lukman.Models.Note;
import com.gocreative.tm.lukman.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotesFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userUid;
    DocumentReference reference;

    CollectionReference collectionReference;
    NotesAdapter notesAdapter;
    RecyclerView recyclerView;
    ArrayList<Note> noteArrayList;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        floatingActionButton = view.findViewById(R.id.fab);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userUid = auth.getUid();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_note_dialog);
                dialog.setCancelable(false);

                Window window = dialog.getWindow();
                if (window == null){
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);

                EditText noteV = dialog.findViewById(R.id.note_text);
                Button cancel = dialog.findViewById(R.id.cancel_saving);
                Button save = dialog.findViewById(R.id.save_note);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String note = noteV.getText().toString().trim();
                        if (!note.isEmpty()){
                            saveNote(note);
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }


        });

        retrieveNotes();

        return view;
    }
    private void saveNote(String note) {
        reference = firestore.collection("users").document(userUid).collection("notes").document();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Map<String, Object> noteInfo =  new HashMap<>();
        noteInfo.put("date_created", Timestamp.now());
        noteInfo.put("note", note);
        noteInfo.put("date_in_calendar", simpleDateFormat.format(date));
        noteInfo.put("id", reference.getId());

        reference.set(noteInfo, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("Successful", "onComplete: " + "Successfully saved");
                }else{
                    Toast.makeText(getActivity(), "Näsazlyk ýüze çykdy!", Toast.LENGTH_SHORT).show();
                    Log.d("Note saving fail", "onComplete: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Note saving fail", "onFailure: " + e.getMessage());
            }
        });
    }

    private void retrieveNotes() {
        collectionReference = firestore
                .collection("users")
                .document(userUid)
                .collection("notes");

        noteArrayList = new ArrayList<Note>();
        notesAdapter = new NotesAdapter(getContext(), noteArrayList);

        recyclerView.setAdapter(notesAdapter);
        eventChangeListener(collectionReference);
    }

    private void eventChangeListener(CollectionReference collectionReference) {
        collectionReference
                .orderBy("date_created", Query.Direction.ASCENDING)
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
                                noteArrayList.add(documentChange.getDocument().toObject(Note.class));
                            }
                            notesAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}