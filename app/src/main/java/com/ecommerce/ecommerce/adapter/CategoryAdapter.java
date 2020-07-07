package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.activity.SubCategoryList;
import com.ecommerce.ecommerce.object.Product;
import com.ecommerce.ecommerce.object.SubCategory;
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


    public CategoryAdapter(Context context,List<String> productsList) {
        this.catList = productsList;
        this.mContext=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.category_recycler_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        final String catTitle=catList.get(i);
        viewHolder.title.setText(catTitle);
        viewHolder.subCatList.clear();
        getSubData(viewHolder,catTitle);
        viewHolder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubCategoryList.class);
                intent.putExtra("Category",catTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return catList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button viewAll;
        RecyclerView homeSubRecycler;
        private FirebaseDatabase database;
        List<SubCategory> subCatList=new ArrayList<>();
        private SubCategoryAdapter subCategoryAdapter;
//        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_title);
            viewAll=itemView.findViewById(R.id.viewAll);
            homeSubRecycler=itemView.findViewById(R.id.home_sub_recyclerview);


//            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
//            homeSubRecycler.setLayoutManager(gridLayoutManager);
            homeSubRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            Log.d(TAG, "Value is:startttttttttt ");
//            image=itemView.findViewById(R.id.pimage);
        }
    }

    private void getSubData(final MyViewHolder viewHolder, String catTitle){

        viewHolder.database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = viewHolder.database.getReference().child("Admin").child("CategoryData").child(catTitle);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.subCatList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String st=snapshot.getKey().toString();
                    Log.d(TAG, "Value is: " + st);
                    viewHolder.subCatList.add(snapshot.getValue(SubCategory.class));
                    viewHolder.subCategoryAdapter =new SubCategoryAdapter(mContext,viewHolder.subCatList,"grid");
                    viewHolder.homeSubRecycler.setAdapter(viewHolder.subCategoryAdapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
