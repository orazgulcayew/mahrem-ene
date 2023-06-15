package com.gocreative.tm.mahrem_ene.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.gocreative.tm.mahrem_ene.Adapters.NotesAdapter;
import com.gocreative.tm.mahrem_ene.Models.Note;
import com.gocreative.tm.mahrem_ene.Models.StateInfo;
import com.gocreative.tm.mahrem_ene.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

public class HomeFragment extends Fragment {
    ViewFlipper viewFlipper;
    Button toRight, toLeft;
    CircularProgressBar progressBar;
    int progress=0, itemSize;
    public int currentProgress, dayProgress;
    CalendarView calendarView;
    FloatingActionButton floatingActionButton;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DocumentReference reference, stateReference;
    String userUid;

    Date firstDate;
    Date today = new Date();

    TextView addNoteV, notesText, progressText;
    ImageView addNoteImage;
    CardView healthyEating, problems;

    CollectionReference collectionReference;
    NotesAdapter notesAdapter;
    RecyclerView recyclerView;
    ArrayList<Note> noteArrayList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String selectedDate;
    List<EventDay> events;
    List<Calendar> calendarList;
    SwitchMaterial weekDaySwitch;

    String startDate, currentDate;

    @SuppressLint("ObjectAnimatorBinding")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewFlipper = view.findViewById(R.id.view_flipper);
        toRight = view.findViewById(R.id.to_right);
        toLeft = view.findViewById(R.id.to_left);
        progressBar = view.findViewById(R.id.week_bar);
        floatingActionButton = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recycler_view);
        calendarView = view.findViewById(R.id.calendar_view);
        addNoteV = view.findViewById(R.id.add_note);
        addNoteImage = view.findViewById(R.id.image_add_note);
        notesText = view.findViewById(R.id.notes);
        healthyEating = view.findViewById(R.id.healthy_eating);
        problems = view.findViewById(R.id.problems_may_occur);
        weekDaySwitch = view.findViewById(R.id.week_day_switch);
        progressText = view.findViewById(R.id.progress_text);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userUid = auth.getUid();

        Date date = new Date();
        selectedDate = simpleDateFormat.format(date);

        events = new ArrayList<>();
        calendarList = new ArrayList<>();

        stateReference = firestore.collection("users")
                .document(userUid).collection("pregnancy_state")
                .document("current_state");


        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Date date = eventDay.getCalendar().getTime();
                itemSize = 0;
                if (noteArrayList.size() == 0){
                    notesText.setVisibility(View.GONE);
                    addNoteImage.setVisibility(View.VISIBLE);
                    addNoteV.setVisibility(View.VISIBLE);

                }else{
                    notesText.setVisibility(View.VISIBLE);
                    addNoteImage.setVisibility(View.GONE);
                    addNoteV.setVisibility(View.GONE);
                }

                selectedDate = simpleDateFormat.format(date);

                retrieveNotes();

            }
        });
        Calendar date1 = Calendar.getInstance();
        date1.set(2022, 6, 18);

        calendarList.add(date1);

        calendarView.setHighlightedDays(calendarList);

        // View flipper slide left animation
        toLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // It is used to set the in and out
                        // animation of View flipper.
                        viewFlipper.setInAnimation(
                                getActivity(),
                                R.anim.slide_right);
                        viewFlipper.setOutAnimation(
                                getActivity(),
                                R.anim.slide_left);
                        viewFlipper.stopFlipping();
                        viewFlipper.setFlipInterval(7000);
                        viewFlipper.startFlipping();

                        // It shows previous item.
                        viewFlipper.showPrevious();
                    }
                });

        // View flipper slide right animation
        toRight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // It is used to set the in and out
                        // animation of View flipper.
                        viewFlipper.setInAnimation(getActivity(),
                                android.R.anim.slide_in_left);
                        viewFlipper.setOutAnimation(getActivity(),
                                android.R.anim.slide_out_right);
                        viewFlipper.stopFlipping();
                        viewFlipper.setFlipInterval(7000);
                        viewFlipper.startFlipping();


                        // It shows next item.
                        viewFlipper.showNext();
                    }
                });

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

        // Card view clicking
        healthyEating.setOnClickListener(v->{
            goToList("healthy_eating");
        });

        problems.setOnClickListener(v->{
            goToList("problems");
        });

        weekDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStart();
            }
        });

        getNotesAndSetIconsToCalendar();

        retrieveNotes();

        return view;
    }
    private void goToList(String infoDb){
        Bundle bundle = new Bundle();
        bundle.putString("info_db", infoDb);
        StoryListFragment storyListFragment = new StoryListFragment();
        storyListFragment.setArguments(bundle);
        FragmentManager managerList = getActivity().getSupportFragmentManager();
        managerList.beginTransaction()
                .replace(R.id.fragment_container, storyListFragment)
                .addToBackStack(HomeFragment.class.getSimpleName())
                .commit();
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
                if (task.isSuccessful()) {
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

    private void eventChangeListener(CollectionReference collectionReference) {
        collectionReference
                .whereEqualTo("date_in_calendar", selectedDate)
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
                        itemSize++;
                        noteArrayList.add(documentChange.getDocument().toObject(Note.class));
                    }
                    notesAdapter.notifyDataSetChanged();
                }
                if (noteArrayList.size() == 0){
                    notesText.setVisibility(View.GONE);
                    addNoteImage.setVisibility(View.VISIBLE);
                    addNoteV.setVisibility(View.VISIBLE);
                }else{
                    notesText.setVisibility(View.VISIBLE);
                    addNoteImage.setVisibility(View.GONE);
                    addNoteV.setVisibility(View.GONE);
                }

                if (itemSize == 0){
                    notesText.setVisibility(View.GONE);
                    addNoteImage.setVisibility(View.VISIBLE);
                    addNoteV.setVisibility(View.VISIBLE);
                }else{
                    notesText.setVisibility(View.VISIBLE);
                    addNoteImage.setVisibility(View.GONE);
                    addNoteV.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getNotesAndSetIconsToCalendar(){
        collectionReference = firestore
                .collection("users")
                .document(userUid)
                .collection("notes");
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

                        List<String> dates = new ArrayList<>();

                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                Note note = documentChange.getDocument().toObject(Note.class);
                                dates.add(note.getDate_in_calendar());
                            }
                            notesAdapter.notifyDataSetChanged();
                        }

                        for(String date: dates){
                            Calendar calendar = Calendar.getInstance();

                            String[] datt = date.split("-");
                            calendar.set(Integer.parseInt(datt[2]), Integer.parseInt(datt[1])-1, Integer.parseInt(datt[0]));
                            events.add(new EventDay(calendar, R.drawable.ic_bx_comment_detail));
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Retrieving form information
        stateReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    StateInfo stateInfo = value.toObject(StateInfo.class);
                    if (!stateInfo.getChild_birthdate().isEmpty()){
                        startDate = stateInfo.getChild_birthdate();
                    }
                    if (!stateInfo.getPregnancy_date().isEmpty()){
                        startDate = stateInfo.getPregnancy_date();
                    }
                    try {
                        SimpleDateFormat dates = new SimpleDateFormat("dd-M-yyyy");
                        currentDate = dates.format(today);

                        //Setting dates
                        firstDate = dates.parse(startDate);
                        today = dates.parse(currentDate);


                        //Comparing dates
                        long difference = Math.abs(firstDate.getTime() - today.getTime());
                        long differenceDates = difference / (24 * 60 * 60 * 1000);

                        //Convert long to String
                        String dayDifference = Long.toString(differenceDates);

                        dayProgress = Integer.parseInt(dayDifference);
                        currentProgress = dayProgress / 7;


                        if (weekDaySwitch.isChecked()){
                            progressText.setText("Hepde\n"+ currentProgress+"/40");

                        }else{
                            progressText.setText(" Gün\n"+ dayProgress+"/280");
                        }

                        // Progress animation
                        progress = 0;
                        final Handler handler = new Handler();
                        int finalCurrentProgress = currentProgress;
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if(progress != finalCurrentProgress){
                                    progress++;
                                    progressBar.setProgress(progress);
                                    handler.postDelayed(this, 30L);
                                }
                            }
                        };

                        handler.post(runnable);

                    } catch (Exception exception) {
                        Log.d("DIDN'T WORK", "exception " + exception);
                    }

                    // Setting check box for passed days
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(firstDate);
                    List<Calendar> calendarList = new ArrayList<>();

                    while (cal.getTime().before(today)) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        cal.add(Calendar.DATE, 1);
                        events.add(new EventDay(calendar, R.drawable.ic_bx_check));
                        calendarList.add(calendar);
                    }

                    calendarView.setHighlightedDays(calendarList);
                    calendarView.setEvents(events);
                }
            }
        });
    }
}