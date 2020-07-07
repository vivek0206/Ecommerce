package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryItemAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailCategoryList extends AppCompatActivity {

    private TextView resultCount;
    private RecyclerView recyclerView;
    private List<Product> list;
    private CategoryItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String category,subCategory;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category_list);

        init();

        Intent intent = getIntent();
        category=intent.getStringExtra("category");
        subCategory = intent.getStringExtra("subCategory");
        fetchAllCategory(category,subCategory);
        Log.d("pop pop",category);



    }

    private void fetchAllCategory(String category,String subCategory) {
        Log.d("Tag","hereeee");
        databaseReference.child(getResources().getString(R.string.Admin)).child("Category").child(category).child(subCategory)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            {
                                Product model = ds.getValue(Product.class);
                                Log.d("Tag",model.toString());
                                list.add(model);
                            }
                        }
                        adapter.setData(list);
                        resultCount.setText(Integer.toString(list.size())+"  Products");
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void init() {
        resultCount = findViewById(R.id.detail_category_productCount);
        recyclerView = findViewById(R.id.detail_category_list_recyclerView);
        list = new ArrayList<>();
        adapter = new CategoryItemAdapter(list,getBaseContext(),DetailCategoryList.this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}