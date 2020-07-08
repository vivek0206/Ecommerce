package com.ecommerce.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.SubCategoryAdapter;
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

public class SubCategoryList extends AppCompatActivity {

    private String category;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private List<SubCategory> subCatList=new ArrayList<>();
    private SubCategoryAdapter subCategoryAdapter;
    private Toolbar toolbar;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list);
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
        MainActivity.OfflineCapabilities(getApplicationContext());
        Intent intent = getIntent();
        category = intent.getStringExtra("Category");
        Log.d("pop pop",category);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        getSubData(category);

        recyclerView=findViewById(R.id.subCat_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }
    private void getSubData(String catTitle)
    {

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(getResources().getString(R.string.Admin)).child("CategoryData").child(catTitle);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                subCatList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String st=snapshot.getKey().toString();
                    Log.d(TAG, "Value is: " + st);
                    subCatList.add(snapshot.getValue(SubCategory.class));
                    subCategoryAdapter =new SubCategoryAdapter(getApplicationContext(),subCatList,"linear");
                    recyclerView.setAdapter(subCategoryAdapter);

                }
                loadingDialog.DismissDialog();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child(getString(R.string.Admin)).keepSynced(true);



    }
}
