package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Models.OrderInfoModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.OrderConfirmAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmActivity extends AppCompatActivity {

    private TextView orderId,orderDate;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private OrderConfirmAdapter adapter;
    private List<OrderInfoModel> list;
    private String orderIdString;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        init();

        Intent intent = getIntent();
        orderIdString = intent.getStringExtra("orderId");
        fetchOrderDetails();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Apke Sath Prank hua h",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void fetchOrderDetails() {
        databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderIdString)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            OrderInfoModel model = ds.getValue(OrderInfoModel.class);
                            if(model!=null)
                            {
                                list.add(model);
                                orderId.setText(model.getOrderId());
                                orderDate.setText(model.getDate());
                            }
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

        orderId = findViewById(R.id.order_confirm_id);
        orderDate = findViewById(R.id.order_confirm_order_Date);
        recyclerView = findViewById(R.id.order_confirm_recyclerView);
        list = new ArrayList<>();
        adapter = new OrderConfirmAdapter(list,getBaseContext());
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        btn = findViewById(R.id.order_confirm_btn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }
}