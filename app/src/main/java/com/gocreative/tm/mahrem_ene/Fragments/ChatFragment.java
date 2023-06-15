package com.gocreative.tm.mahrem_ene.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gocreative.tm.mahrem_ene.Adapters.MessagesAdapter;
import com.gocreative.tm.mahrem_ene.DrInfoActivity;
import com.gocreative.tm.mahrem_ene.Models.Doctor;
import com.gocreative.tm.mahrem_ene.Models.Message;
import com.gocreative.tm.mahrem_ene.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    CollectionReference messageReference;
    FirebaseAuth auth;
    MessagesAdapter messagesAdapter;
    RecyclerView recyclerView;
    ArrayList<Message> messageArrayList;

    private ImageView sendMessageBtn;
    private EditText message;

    CircleImageView profileImageV;
    ImageView infoV;
    TextView doctorNameV, jobTitleV;
    String doctorId;
    Doctor doctor;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sendMessageBtn = view.findViewById(R.id.send_message_btn);
        message = view.findViewById(R.id.message_field);
        profileImageV = view.findViewById(R.id.profile_image);
        doctorNameV = view.findViewById(R.id.doctor_name);
        jobTitleV = view.findViewById(R.id.job_name);
        infoV = view.findViewById(R.id.info);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Biraz garaşyň...");
        progressDialog.show();

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        collectionReference = firestore
                .collection("rooms")
                .document(auth.getUid())
                .collection("messages");
        messageReference = firestore.collection("rooms").document(auth.getUid()).collection("messages");

        messageArrayList = new ArrayList<Message>();
        messagesAdapter = new MessagesAdapter(getContext(), messageArrayList);

        recyclerView.setAdapter(messagesAdapter);
        eventChangeListener();
        getDoctorInformation();

        infoV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctor != null){
                    Intent intent = new Intent(getContext(), DrInfoActivity.class);
                    intent.putExtra("doctor", doctor);
                    startActivity(intent);
                }
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                message.getText().clear();
                recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });

        return view;
    }

    private void eventChangeListener() {
        collectionReference
                .orderBy("sent_at").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d("firebase firestore", "onEvent: " + error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        messageArrayList.add(documentChange.getDocument().toObject(Message.class));
                    }
                    messagesAdapter.notifyDataSetChanged();

                }
            }
        });
    }
    private void getDoctorInformation(){
        DocumentReference reference = firestore.collection("users").document(auth.getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                doctorId = (String)documentSnapshot.get("doctor_id");

                DocumentReference reference = firestore.collection("doctors").document(doctorId);

                reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            doctor = task.getResult().toObject(Doctor.class);
                            if (doctor != null){
                                progressDialog.dismiss();
                                doctorNameV.setText(doctor.getName() + " " + doctor.getSurname());
                                jobTitleV.setText(doctor.getJob());
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Nasazlyk yuze cykdy", Toast.LENGTH_SHORT).show();
                            Log.d("ASD", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });

            }
        });
    }
    private void sendMessage(){
        Map<String, Object> setMessage = new HashMap<>();
        String txtMessage = message.getText().toString();
        setMessage.put("message", txtMessage);
        setMessage.put("type", "text");
        setMessage.put("sent_at", Timestamp.now());
        setMessage.put("sent_by", auth.getUid());

        messageReference.add(setMessage).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Log.d("Messaging Activity", "onComplete: " + txtMessage);
                    recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("Messaging Activity", "onFailure: " + e.toString());
            }
        });
    }
}