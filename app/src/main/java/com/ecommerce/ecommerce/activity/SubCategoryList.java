package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.SearchModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.SearchAdapter;
import com.ecommerce.ecommerce.adapter.SubCategoryAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.ecommerce.ecommerce.object.SubCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private List<SearchModel> searchList;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager2;
    private RecyclerView searchRecyclerView;
    int flag=2;

    private MenuItem search,cart;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list);
        toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
//        setTitle("Product");
        Intent intent = getIntent();
        category = intent.getStringExtra("Category");
        Log.d("pop pop",category);

        setTitle(category);
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        MainActivity.OfflineCapabilities(getApplicationContext());

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        getSubData(category);


        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        searchList = new ArrayList<>();
        searchRecyclerView = findViewById(R.id.subCat_searchList_recyclerView);
        searchAdapter = new SearchAdapter(searchList,getBaseContext());
        searchRecyclerView.setLayoutManager(layoutManager2);
        searchRecyclerView.setHasFixedSize(true);

        recyclerView=findViewById(R.id.subCat_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
            Intent intent = new Intent(SubCategoryList.this,CartActivity.class);
            startActivity(intent);
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        search = menu.findItem(R.id.action_search);
        cart = menu.findItem(R.id.action_cart);

        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecyclerView.setVisibility(View.VISIBLE);

                flag=1;
                cart.setVisible(false);
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                flag=1;
                cart.setVisible(false);

                searchRecyclerView.setVisibility(View.VISIBLE);

                searchList.clear();
                Query firebaseQuery = database.getReference().child(getResources().getString(R.string.Search)).orderByKey().startAt(newText.toLowerCase().trim()).endAt(newText.toLowerCase().trim()+"\uf8ff");
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

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search.collapseActionView();
                searchView.setQuery("",false);
                searchView.onActionViewCollapsed();
                searchList.clear();
                flag=2;
                cart.setVisible(true);
                searchRecyclerView.setVisibility(View.GONE);
                return true;
            }
        });


        return true;

    }

    @Override
    public void onBackPressed() {
        if(flag==1)
        {
            search.collapseActionView();
            searchView.setQuery("",false);
            searchView.onActionViewCollapsed();
            searchList.clear();
            flag=2;
            cart.setVisible(true);
            searchRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            super.onBackPressed();

        }
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
