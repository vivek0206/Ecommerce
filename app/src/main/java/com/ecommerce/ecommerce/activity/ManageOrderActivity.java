package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ecommerce.ecommerce.Models.OrderInfoModel;
import com.ecommerce.ecommerce.Models.UserOrderInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.ManageOrderAdapter;
import com.ecommerce.ecommerce.adapter.OrderConfirmAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ManageOrderActivity extends AppCompatActivity {


    private TextView filter;
    private RecyclerView recyclerView;
    private ManageOrderAdapter adapter;
    private LinearLayoutManager layoutManager;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private List<UserOrderInfo> list;
    private int price=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Manage Order");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();
        fetchOrders();
    }

    private void fetchOrders() {
        databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            UserOrderInfo orderModel = ds.getValue(UserOrderInfo.class);
                            list.add(0,orderModel);
                        }
                        adapter.setData(list);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void init() {
        filter = findViewById(R.id.manage_order_filter);
        recyclerView = findViewById(R.id.manage_order_recyclerView);
        list = new ArrayList<>();
        adapter = new ManageOrderAdapter(list,getBaseContext());
        layoutManager= new LinearLayoutManager(getBaseContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }



}