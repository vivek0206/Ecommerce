package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Interface.OnDataChangeListener;
import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.UserCartAdapter;
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

public class CartActivity extends AppCompatActivity {

    private TextView address1,address2,change,itemPrice,shippingPrice,totalAmount;
    private RecyclerView recyclerView;
    private UserCartAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ProductVariation> list;

    private Button buy_now;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String addressText1="",addressText2="",userName="Tanish";
    private int price=0,itemNo=0;
    private LoadingDialog loadingDialog;
    int flag=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Cart");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
        MainActivity.OfflineCapabilities(getApplicationContext());
        fetchUserCart();
        fetchUserInfo();

        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged(int size, int pric,boolean flag) {

            }

            @Override
            public void onDataRemoveChange() {
                fetchUserCart();
            }

            @Override
            public void onCheckOutOfStock(int flag,int pric) {
                if(flag==0)
                {

                }
                else if(flag==1)
                {
                    price+=pric;
                    itemPrice.setText("\u20B9"+price+"");
                    totalAmount.setText("\u20B9"+price+"");

                }
                else if(flag==-1)
                {
                    price-=pric;
                    itemPrice.setText("\u20B9"+price+"");
                    totalAmount.setText("\u20B9"+price+"");

                }
            }


        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this,DeliveryAddress.class);
                startActivity(intent);
            }
        });

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size()==0)
                {
                    buy_now.setText("Add Item To Cart");
                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(addressText1.isEmpty() || addressText2.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Enter Address",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        checkOutOfStock();

                    }

                }

            }
        });

    }

    private void checkOutOfStock() {

        loadingDialog.startLoadingDialog();

        databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        flag=1;
                        for(DataSnapshot ds1: dataSnapshot.getChildren())
                        {
                            for(DataSnapshot ds2: ds1.getChildren())
                            {
                                final ProductVariation model = ds2.getValue(ProductVariation.class);
                                if(model!=null)
                                {
                                    databaseReference.child(getResources().getString(R.string.ProductVariation)).child(model.getProductName().toLowerCase().trim()).child(model.getProductVariationName().toLowerCase().trim())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    ProductVariation modelI = dataSnapshot.getValue(ProductVariation.class);
                                                    if(model.getQuantity() > modelI.getQuantity())
                                                    {
                                                        Log.d("popop  ", "ghr laynge  ");
                                                        flag=0;
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                    if(flag==0)
                                    {
                                        break;
                                    }
                                }
                            }
                            if(flag==0)
                            {
                                break;
                            }
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(flag==0)
                                {
                                    Toast.makeText(getApplicationContext(),"Some Items are Out Of Stock",Toast.LENGTH_SHORT).show();
                                    loadingDialog.DismissDialog();
                                }
                                else
                                {
                                    loadingDialog.DismissDialog();
                                    Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                                    intent.putExtra("price",price+"");
                                    intent.putExtra("shippingPrice","0");
                                    startActivity(intent);
                                }

                            }
                        },1500);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void fetchUserInfo() {
        databaseReference.child(getResources().getString(R.string.UserInfo)).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInfo model = dataSnapshot.getValue(UserInfo.class);
                        Log.d("pop pop ",dataSnapshot.getValue()+"");
                        if(model!=null)
                        {
                            userName+=model.getUserName();
                            fetchUserAddress();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
        databaseReference.child(getString(R.string.UserInfo)).child(user.getUid()).keepSynced(true);

    }

    private void fetchUserAddress() {

        databaseReference.child(getResources().getString(R.string.Address)).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        addressText1="";
                        addressText2="";

                        UserInfo model = dataSnapshot.getValue(UserInfo.class);
                        if(model!=null)
                        {
                            addressText1+=model.getDeliveryName()+", "+model.getDeliveryPhone();
                            addressText2+=model.getDeliveryFlat()+", "+model.getDeliveryArea()+", "+model.getDeliveryLandmark()+","+model.getDeliveryCity()+","+model.getDeliveryState()+","+model.getDeliveryPinCode();
                            address1.setText(addressText1);
                            address2.setText(addressText2);
                        }
                        else
                        {
                            change.setText("Add Address");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
        databaseReference.child(getString(R.string.Address)).child(user.getUid()).keepSynced(true);



    }

    private void fetchUserCart() {

        loadingDialog.startLoadingDialog();
        databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        price=0;
                        list.clear();
                        for(DataSnapshot ds1: dataSnapshot.getChildren())
                        {
                            for(DataSnapshot ds2: ds1.getChildren())
                            {
                                ProductVariation model = ds2.getValue(ProductVariation.class);
                                list.add(0,model);
                            }
                        }
                        loadingDialog.DismissDialog();
                        adapter.setData(list);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);


                        if(list.size()==0)
                        {
                            loadingDialog.DismissDialog();
                            buy_now.setText("Add Items To Cart");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child(getString(R.string.UserCart)).child(user.getUid()).keepSynced(true);



    }

    private void init() {
        loadingDialog = new LoadingDialog(this);
        address1 = findViewById(R.id.cart_address1);
        address2 = findViewById(R.id.cart_address2);
        change = findViewById(R.id.cart_address_change);
        itemPrice = findViewById(R.id.cart_item_total_price);
        shippingPrice = findViewById(R.id.cart_item_shipping_charges);
        totalAmount = findViewById(R.id.cart_amount_total);
        recyclerView = findViewById(R.id.cart_recyclerView);
        buy_now = findViewById(R.id.cart_buy_btn);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getBaseContext());
        adapter = new UserCartAdapter(list,getBaseContext());
        recyclerView.setHasFixedSize(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
}