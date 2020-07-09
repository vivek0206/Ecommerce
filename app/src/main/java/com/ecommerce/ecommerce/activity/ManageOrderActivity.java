package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ecommerce.ecommerce.LoadingDialog;
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
    private LoadingDialog loadingDialog;


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
        loadingDialog.startLoadingDialog();

        fetchOrders();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterMenu(view);
            }
        });



    }

    private void FilterMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.filter_menu,popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                switch (menuItem.getItemId())
                {
                    case R.id.filter_menu_inTransit:
                        fetchTransitOrders();
                        break;
                    case R.id.filter_menu_delivered:
                        fetchFilterOrders("4");
                        break;
                    case R.id.filter_menu_cancelled:
                        fetchFilterOrders("5");
                        break;
                }



                return true;
            }
        });


    }

    private void fetchFilterOrders(final String s) {
        loadingDialog.startLoadingDialog();
        databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            UserOrderInfo orderModel = ds.getValue(UserOrderInfo.class);
                            if(orderModel.getStatus().equals(s))
                            {
                                list.add(0,orderModel);
                            }
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
    }

    private void fetchTransitOrders() {
        loadingDialog.startLoadingDialog();
        databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            UserOrderInfo orderModel = ds.getValue(UserOrderInfo.class);
                            if(orderModel.getStatus().equals("1")||orderModel.getStatus().equals("2")||orderModel.getStatus().equals("3"))
                            {
                                list.add(0,orderModel);
                            }
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

    }


    private void fetchOrders() {
        databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            UserOrderInfo orderModel = ds.getValue(UserOrderInfo.class);
                            list.add(0,orderModel);
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
        databaseReference.child(getString(R.string.OrderInfo)).keepSynced(true);

    }

    private void init() {
        loadingDialog = new LoadingDialog(this);
        filter = findViewById(R.id.manage_order_filter);
        recyclerView = findViewById(R.id.manage_order_recyclerView);
        list = new ArrayList<>();
        adapter = new ManageOrderAdapter(list,getBaseContext());
        layoutManager= new LinearLayoutManager(getBaseContext());
        MainActivity.OfflineCapabilities(getApplicationContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }



}