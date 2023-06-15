package com.gocreative.tm.mahrem_ene.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gocreative.tm.mahrem_ene.Models.Product;
import com.gocreative.tm.mahrem_ene.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    Context context;
    ArrayList<Product> productArrayList;

    public ProductsAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_list, parent, false);
        return new ProductsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.nameView.setText(product.getName());
        holder.priceView.setText(Float.toString(product.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .into(holder.imageView);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void addToCart(Product product) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DocumentReference reference = firestore.collection("admin").document("orders").collection("pharmacy_orders").document();

//        Map<String, Object> order = new HashMap<>();
//
//        order.put("name")
//
//
//        reference.set()



    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button addToCart;
        TextView nameView, priceView;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameView = itemView.findViewById(R.id.name);
            priceView = itemView.findViewById(R.id.price);
            addToCart = itemView.findViewById(R.id.add_to_card);
        }
    }
}