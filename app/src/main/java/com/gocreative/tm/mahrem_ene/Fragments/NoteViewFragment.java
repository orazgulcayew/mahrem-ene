package com.gocreative.tm.mahrem_ene.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gocreative.tm.mahrem_ene.MainActivity;
import com.gocreative.tm.mahrem_ene.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteViewFragment extends Fragment {
    private TextView dateV;
    private EditText editTextV;
    private String date;
    private String note;
    private ImageView saveNoteV, editNoteV;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DocumentReference reference;
    private String documentId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_view, container, false);

        dateV = view.findViewById(R.id.date_note);
        editTextV = view.findViewById(R.id.edit_note);
        saveNoteV = view.findViewById(R.id.save_note);
        editNoteV = view.findViewById(R.id.edit_note_btn);

        date = getArguments().getString("date");
        note = getArguments().getString("note");
        documentId = getArguments().getString("id");

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = firestore
                        .collection("users")
                        .document(auth.getUid())
                        .collection("notes")
                        .document(documentId);

        dateV.setText(date);
        editTextV.setText(note);

        saveNoteV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextV.getText().toString().isEmpty()){
                    updateNote(editTextV.getText().toString());
                    editTextV.setEnabled(false);
                    saveNoteV.setVisibility(View.GONE);
                    editNoteV.setVisibility(View.VISIBLE);
                }else{
                    deleteNote();
                }
            }
        });

        editNoteV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextV.setEnabled(true);
                saveNoteV.setVisibility(View.VISIBLE);
                editNoteV.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void deleteNote() {
        reference.delete();
    }


    public void updateNote(String note) {
        reference.update("note", note);
    }
}