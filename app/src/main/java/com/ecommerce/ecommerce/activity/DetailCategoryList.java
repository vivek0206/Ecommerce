package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryItemAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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
    private Toolbar toolbar;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;
    private Button sortBtn,LowToHighBtn,HighToLowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category_list);
        toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Product");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();

        Intent intent = getIntent();
        category=intent.getStringExtra("category");
        subCategory = intent.getStringExtra("subCategory");
        fetchAllCategory(category,subCategory);
        Log.d("pop pop",category);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        LowToHighBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighToLowBtn.setTextColor(Color.parseColor("#17202A"));
                LowToHighBtn.setTextColor(Color.parseColor("#FF0000"));
                fetchAllByLowToHigh(category,subCategory);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        HighToLowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighToLowBtn.setTextColor(Color.parseColor("#FF0000"));
                LowToHighBtn.setTextColor(Color.parseColor("#17202A"));
                fetchAllByHighToLow(category,subCategory);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });



    }

    private void fetchAllCategory(String category,String subCategory) {
        Log.d("Tag","hereeee");
        databaseReference.child(getResources().getString(R.string.Admin)).child("Category").child(category).child(subCategory)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
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
    private void fetchAllByHighToLow(String category,String subCategory) {
        Log.d("Tag","hereeee");
        databaseReference.child(getResources().getString(R.string.Admin)).child("Category").child(category).child(subCategory).orderByChild("salePrice")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            {
                                Product model = ds.getValue(Product.class);
                                Log.d("Tag",model.toString());
                                list.add(model);
                            }
                        }
                        Collections.reverse(list);
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
    private void fetchAllByLowToHigh(String category,String subCategory) {
        Log.d("Tag","hereeee");
        databaseReference.child(getResources().getString(R.string.Admin)).child("Category").child(category).child(subCategory).orderByChild("salePrice")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
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
        bottomSheet=findViewById(R.id.bottom_sheet);
        sortBtn=findViewById(R.id.sort_btn);
        LowToHighBtn=findViewById(R.id.low2high_btn);
        HighToLowBtn=findViewById(R.id.high2low_btn);

        list = new ArrayList<>();
        adapter = new CategoryItemAdapter(list,getBaseContext(),DetailCategoryList.this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}