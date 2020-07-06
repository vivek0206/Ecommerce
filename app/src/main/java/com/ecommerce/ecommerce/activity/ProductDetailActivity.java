package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView offer,productName,rating,offerPrice,originalPrice,savingPrice,productDetails;
    private ImageView productImg,payOnDelivery,nonReturnable;
    private RecyclerView recyclerView1,recyclerView2;
    private Button buy_now;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String categoryName,productNam;
    private int returnable,cod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category");
        productNam = intent.getStringExtra("product");

        fetchProduct(categoryName,productNam);



    }

    private void fetchProduct(String categoryName, final String productNam) {
        databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(categoryName).child(productNam)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product model = dataSnapshot.getValue(Product.class);
                        Picasso.get().load(Uri.parse(model.getImageUrl())).into(productImg);
                        productName.setText(model.getProductName());
                        rating.setText(model.getRating());
                        offerPrice.setText(model.getSalePrice());
                        originalPrice.setText(model.getOriginalPrice());
                        savingPrice.setText((Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"");
                        productDetails.setText(model.getProductDetail());
                        returnable = Integer.parseInt(model.getReturnable());
                        cod = Integer.parseInt(model.getPayOnDelivery());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void init() {
        offer = findViewById(R.id.product_detail_off);
        productName = findViewById(R.id.product_detail_productName);
        rating = findViewById(R.id.product_detail_rating);
        offerPrice = findViewById(R.id.product_detail_productOfferPrice);
        originalPrice = findViewById(R.id.product_detail_productOriginalPrice);
        savingPrice = findViewById(R.id.product_detail_prductSaving);
        productDetails = findViewById(R.id.product_detail_product_details);
        productImg = findViewById(R.id.product_detail_product_image);
        payOnDelivery = findViewById(R.id.product_detail_payOnDelivery);
        nonReturnable = findViewById(R.id.product_detail_non_returnable);
        recyclerView1 = findViewById(R.id.product_detail_different_quantity);
        recyclerView2 = findViewById(R.id.product_detail_similiar_product);
        buy_now = findViewById(R.id.product_detail_buy_now);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
}