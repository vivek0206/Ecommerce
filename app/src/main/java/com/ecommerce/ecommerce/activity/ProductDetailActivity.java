package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.SimilarProductAapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView offer,productName,rating,offerPrice,originalPrice,savingPrice,productDetails;
    private ImageView productImg,payOnDelivery,nonReturnable;
    private RecyclerView recyclerView1,recyclerView2;
    private Button buy_now;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String categoryName,productNam,subCategoryName;
    private int returnable,cod;
    private Toolbar toolbar;

    private SimilarProductAapter similarProductAapter;
    private List<Product> pSimList=new ArrayList<>();
    private RecyclerView simRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
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

        Intent intent = getIntent();
        subCategoryName=intent.getStringExtra("subCategory");
        categoryName = intent.getStringExtra("category");
        productNam = intent.getStringExtra("product");

        fetchProduct(categoryName,subCategoryName,productNam);
        fetchSimilarProduct(categoryName,subCategoryName);
        setSimProduct();
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });



    }

    private void setSimProduct() {

    }

    private void fetchSimilarProduct(String categoryName, String subCategoryName) {
        databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(categoryName).child(subCategoryName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pSimList.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Product model = snapshot.getValue(Product.class);
                            pSimList.add(model);

                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                        simRecyclerView.setLayoutManager(gridLayoutManager);
                        similarProductAapter.setData(pSimList);
                        simRecyclerView.setAdapter(similarProductAapter);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void fetchProduct(String categoryName,String subCategoryName, final String productNam) {
        databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(categoryName).child(subCategoryName).child(productNam)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product model = dataSnapshot.getValue(Product.class);
                        Picasso.get().load(Uri.parse(model.getImageUrl())).into(productImg);
                        productName.setText(model.getProductName()+" ,"+model.getQuantity());
                        rating.setText(model.getRating());
                        offerPrice.setText("\u20B9"+model.getSalePrice());
                        originalPrice.setText("\u20B9"+model.getOriginalPrice());
                        originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        savingPrice.setText("\u20B9"+(Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice())));
                        offer.setText("\u20B9"+(Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"\nOff");

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

        simRecyclerView=findViewById(R.id.product_detail_similiar_product);
        similarProductAapter =new SimilarProductAapter(pSimList,this,ProductDetailActivity.this);

    }
}