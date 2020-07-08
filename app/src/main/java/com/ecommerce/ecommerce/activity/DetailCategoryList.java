package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.SearchModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryItemAdapter;
import com.ecommerce.ecommerce.adapter.SearchAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private Button LowToHighBtn,HighToLowBtn;
    private Button sortBtn;
    private LoadingDialog loadingDialog;

    private List<SearchModel> searchList;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager2;
    private RecyclerView searchRecyclerView;
    int flag=2;

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
        MainActivity.OfflineCapabilities(getApplicationContext());
        loadingDialog.startLoadingDialog();

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
                loadingDialog.startLoadingDialog();
                HighToLowBtn.setTextColor(Color.parseColor("#17202A"));
                LowToHighBtn.setTextColor(Color.parseColor("#FF0000"));
                fetchAllByLowToHigh(category,subCategory);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        HighToLowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                HighToLowBtn.setTextColor(Color.parseColor("#FF0000"));
                LowToHighBtn.setTextColor(Color.parseColor("#17202A"));
                fetchAllByHighToLow(category,subCategory);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });



    }

    private void fetchAllCategory(String category,String subCategory) {
        Log.d("Tag","hereeee");
        databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(category).child(subCategory)
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
                        loadingDialog.DismissDialog();
                        adapter.setData(list);
                        resultCount.setText(Integer.toString(list.size())+"  Products");
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child(getString(R.string.Admin)).keepSynced(true);



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
                        loadingDialog.DismissDialog();
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
                        loadingDialog.DismissDialog();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.action_cart)
        {
            Intent intent = new Intent(DetailCategoryList.this,CartActivity.class);
            startActivity(intent);
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem cart = menu.findItem(R.id.action_cart);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecyclerView.setVisibility(View.VISIBLE);

                flag=1;
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                flag=1;
                searchRecyclerView.setVisibility(View.VISIBLE);

                searchList.clear();
                Query firebaseQuery = databaseReference.child(getResources().getString(R.string.Search)).orderByKey().startAt(newText.toLowerCase().trim()).endAt(newText.toLowerCase().trim()+"\uf8ff");
                firebaseQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {

                            SearchModel model = ds.getValue(SearchModel.class);
                            searchList.add(model);
                        }
                        searchAdapter.setData(searchList);
                        searchRecyclerView.setLayoutManager(layoutManager2);
                        searchRecyclerView.setAdapter(searchAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();


                return false;
            }
        });

        return true;

    }

    @Override
    public void onBackPressed() {
        if(flag==1)
        {
            searchList.clear();
            flag=2;
            searchRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            super.onBackPressed();

        }
    }

    private void init() {


        /*

         */

        loadingDialog = new LoadingDialog(this);
        resultCount = findViewById(R.id.detail_category_productCount);

        recyclerView = findViewById(R.id.detail_category_list_recyclerView);
        bottomSheet=findViewById(R.id.bottom_sheet);
        sortBtn= findViewById(R.id.sort_btn);
        LowToHighBtn=findViewById(R.id.low2high_btn);
        HighToLowBtn=findViewById(R.id.high2low_btn);

        list = new ArrayList<>();
        adapter = new CategoryItemAdapter(list,getBaseContext(),DetailCategoryList.this);
        layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);

        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        searchList = new ArrayList<>();
        searchRecyclerView = findViewById(R.id.detail_category_searchList_recyclerView);
        searchAdapter = new SearchAdapter(searchList,getBaseContext());
        searchRecyclerView.setLayoutManager(layoutManager2);
        searchRecyclerView.setHasFixedSize(true);

//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}