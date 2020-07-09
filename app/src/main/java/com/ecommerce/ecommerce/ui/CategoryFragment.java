package com.ecommerce.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.AddSubCat;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.activity.SignUp;
import com.ecommerce.ecommerce.activity.tempActivity;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.CustomExpandableListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CategoryFragment extends Fragment {

    private Button randome,temp,detailCategory,addSubCat;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        getCatData();
        init(view);

        randome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignUp.class);
                startActivity(intent);
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), tempActivity.class);
                startActivity(intent);
            }
        });

        detailCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailCategoryList.class);
                intent.putExtra("subCategory","Edible Oil And Ghee");
                startActivity(intent);
            }
        });
        addSubCat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSubCat.class);
                startActivity(intent);
            }
        });

        return view;
    }



    private void init(View view) {
        randome = view.findViewById(R.id.random);
        temp = view.findViewById(R.id.temp_btn);
        detailCategory = view.findViewById(R.id.temp_detail_category);
        addSubCat=view.findViewById(R.id.add_subcategory);

        expandableListView = view.findViewById(R.id.expandableListView);
//        expandableListDetail = ExpandableListDataPump.getData();

        expandableListTitle = new ArrayList<String>();
        expandableListDetail=new HashMap<>();

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                String key=expandableListTitle.get(groupPosition);
                Intent intent = new Intent(getContext(), DetailCategoryList.class);
                intent.putExtra("category",key.toLowerCase().trim());
                intent.putExtra("subCategory",expandableListDetail.get(key).get(childPosition).toLowerCase().trim());
                startActivity(intent);
                return false;
            }
        });
    }

    private void getCatData() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Admin").child("CategoryData");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                expandableListTitle.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String st=snapshot.getKey().toString();
                    expandableListTitle.add(st);
                }
                getSubCatData();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    private void getSubCatData(){
        database = FirebaseDatabase.getInstance();
        for(int i=0;i<expandableListTitle.size();i++){
            final String subcat=expandableListTitle.get(i);
            DatabaseReference myRef = database.getReference().child("Admin").child("CategoryData").child(subcat);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
//                        expandableListDetail.get(subcat).clear();
                    List<String> subL = new ArrayList<String>();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String st=snapshot.getKey().toString();
                        subL.add(st);
                    }
                    expandableListDetail.put(subcat,subL);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

    }
}