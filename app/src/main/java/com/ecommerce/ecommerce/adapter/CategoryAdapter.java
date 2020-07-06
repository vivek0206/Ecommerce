package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    List<String> catList;
    Context mContext;
    private FirebaseDatabase database;
    List<Product> subCatList;
    public CategoryAdapter(Context context,List<String> productsList) {
        this.catList = productsList;
        this.mContext=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.category_recycler_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        String data=catList.get(i);
        viewHolder.title.setText(data);
        getSubData(viewHolder);

    }
    @Override
    public int getItemCount() {
        return catList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView homeSubRecycler;
//        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_title);
            homeSubRecycler=itemView.findViewById(R.id.home_sub_recyclerview);
//            image=itemView.findViewById(R.id.pimage);
        }
    }

    private void getSubData(MyViewHolder viewHolder){

    }
}
