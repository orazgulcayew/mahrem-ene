package com.gocreative.tm.mahrem_ene.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gocreative.tm.mahrem_ene.Adapters.ProductsAdapter;
import com.gocreative.tm.mahrem_ene.Models.Product;
import com.gocreative.tm.mahrem_ene.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

import butterknife.BindView;

public class PharmaciesFragment extends Fragment {
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    ProductsAdapter productsAdapter;
    RecyclerView recyclerView;
    ArrayList<Product> productArrayList;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btn_cart)
    FrameLayout btnCart;
    @BindView(R.id.main_view)
    RelativeLayout mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pharmacies, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        firestore = FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("admin")
                .document("products")
                .collection("dermanhana");

        productArrayList = new ArrayList<Product>();
        productsAdapter = new ProductsAdapter(getContext(), productArrayList);

        recyclerView.setAdapter(productsAdapter);
        eventChangeListener();

        return view;
    }
    private void eventChangeListener() {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d("firebase firestore", "onEvent: " + error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(documentChange.getDocument().toObject(Product.class));
                    }
                    productsAdapter.notifyDataSetChanged();

                }
            }
        });
    }
}