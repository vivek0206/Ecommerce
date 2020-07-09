package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.OrderDetailAdapter;
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

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private List<Product> list;
    private LinearLayoutManager layoutManager;
    private OrderDetailAdapter adapter;
    private String orderId;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Order Details");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
        loadingDialog.startLoadingDialog();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        fetchOrder();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Product model, int type) {
                OnCancel(model);
            }
        });


    }

    private void OnCancel(final Product model) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View customView = getLayoutInflater().inflate(R.layout.raw_cancel_alert_dialog,null);
        builder.setView(customView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        Button not_cancel = customView.findViewById(R.id.raw_alert_cancel_dont_cancel);
        Button cancelled = customView.findViewById(R.id.raw_alert_cancel_cancelled);

        not_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        final String productName = model.getProductName().toLowerCase().trim();
        cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setOrderStatus("5");
                databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderId).child(productName).setValue(model);
                alertDialog.cancel();
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
            }
        });




    }


    private void fetchOrder() {
        databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            Log.e("pop oop::",ds.getValue()+"\n");
                            Product model = ds.getValue(Product.class);
                            list.add(0,model);
                        }
                        loadingDialog.DismissDialog();
                        adapter.setData(list);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child(getString(R.string.UserOrder)).keepSynced(true);


    }

    private void init() {
        MainActivity.OfflineCapabilities(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        recyclerView = findViewById(R.id.order_detail_recyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        adapter = new OrderDetailAdapter(list,getBaseContext());
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setHasFixedSize(true);
    }
}