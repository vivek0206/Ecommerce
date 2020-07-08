package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Interface.OnDataChangeListener;
import com.ecommerce.ecommerce.LoadingDialog;
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
    private List<Product> list;

    private Button buy_now;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String addressText1,addressText2,userName="Tanish";
    private int price=0;
    private LoadingDialog loadingDialog;
    public static boolean outOfStock=false;


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
        loadingDialog.startLoadingDialog();
        MainActivity.OfflineCapabilities(getApplicationContext());
        fetchUserCart();
        fetchUserInfo();

        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged(int size, int pric,boolean flag) {
                Toast.makeText(getApplicationContext(),"pop pop "+pric,Toast.LENGTH_SHORT).show();
                Log.d("pop pop pop",pric+" tac");
                if(flag==false)
                {
                    price += pric;
                    itemPrice.setText(price+"");
                    totalAmount.setText(price+"");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Some Items are out of Stock",Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                    intent.putExtra("price",price+"");
                    intent.putExtra("shippingPrice","0");
                    startActivity(intent);
                }

            }
        });

    }

    private void checkStock(final Product productModel)
    {
        String category = productModel.getCategoryName().toLowerCase().trim();
        String subCategory = productModel.getSubCategoryName().toLowerCase().trim();
        String product = productModel.getProductName().toLowerCase().trim();
        final String quantity = productModel.getQuantity();

        databaseReference.child(getResources().getString(R.string.Admin)).child(category).child(subCategory).child(product)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product model = dataSnapshot.getValue(Product.class);
                        if(Integer.parseInt(model.getQuantity()) < Integer.parseInt(quantity))
                        {
                            Toast.makeText(getApplicationContext(),"Some Items are out of Stock",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            price+= Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getSalePrice());
                        }
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

        databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Product model = ds.getValue(Product.class);
                    if(model!=null)
                    {
                        list.add(model);
                        checkStock(model);
                    }
                }
                loadingDialog.DismissDialog();
                itemPrice.setText(price+"");
                totalAmount.setText(price+"");
                adapter.setData(list);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                if(list.size()==0)
                {
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