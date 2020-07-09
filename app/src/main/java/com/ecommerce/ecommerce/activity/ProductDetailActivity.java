package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.AccountModel;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.Models.RatingInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.AccountAdapter;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.ProductVarQuantityAdapter;
import com.ecommerce.ecommerce.adapter.RatingAdapter;
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
    private RecyclerView recyclerView2;
    private LinearLayoutManager quantiyLayoutManager;
    private ProductVarQuantityAdapter quantityAdapter;
    private List<ProductVariation> quantityList;
    private Button buy_now;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String categoryName,productNam,subCategoryName,proVarName;
    private int returnable,cod;
    private Toolbar toolbar;

    private SimilarProductAapter similarProductAapter;
    private List<Product> pSimList=new ArrayList<>();
    private RecyclerView simRecyclerView;
    private LoadingDialog loadingDialog;
    private Product modelGlobal;

    //Rating
    private RecyclerView ratingRecyclerView;
    private List<RatingInfo> ratingList;
    private RatingAdapter ratingAdapter;
    private LinearLayoutManager ratingLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = (Toolbar)findViewById(R.id.bar);
        toolbar.setTitle("Product");
        setSupportActionBar(toolbar);
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


        quantityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Product model, int type) {

            }

            @Override
            public void onItemClick() {

            }

            @Override
            public void onItemClick(ProductVariation model) {
                proVarName = model.getProductVariationName();
                offerPrice.setText("\u20B9"+model.getProductSalePrice());
                originalPrice.setText("\u20B9"+model.getProductActualPrice());
                originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                savingPrice.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice()));
                offer.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice())+"\nOff");
            }
        });

        loadingDialog.startLoadingDialog();
        fetchProduct(categoryName,subCategoryName,productNam);
        fetchSimilarProduct(categoryName,subCategoryName);
        fetchProductVariation(productNam.toLowerCase().trim());
        setSimProduct();
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this,CartActivity.class);
                increaseQuantity();
                startActivity(intent);
            }
        });



    }

    //TODO:CHECK FOR THE OUT OF STOCK

    private void increaseQuantity() {
        if(modelGlobal!=null)
        {
            databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam.toLowerCase().trim()).child(proVarName.toLowerCase().trim())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("CartUser",dataSnapshot.getValue()+"");
                            ProductVariation model = dataSnapshot.getValue(ProductVariation.class);
                            if(model!=null)
                            {
                                model.setQuantity(model.getQuantity()+1);
                                databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam.toLowerCase().trim()).child(proVarName.toLowerCase().trim())
                                        .setValue(model);

                            }
                            else
                            {
                                databaseReference.child(getResources().getString(R.string.ProductVariation)).child(productNam.toLowerCase().trim()).child(proVarName.toLowerCase().trim())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                ProductVariation proModel = dataSnapshot.getValue(ProductVariation.class);
                                                if(proModel!=null)
                                                {
                                                    proModel.setQuantity(1);
                                                    databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam.toLowerCase().trim()).child(proVarName.toLowerCase().trim())
                                                            .setValue(proModel);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void setSimProduct() {

    }

    private void fetchProductVariation(String productNam)
    {
        databaseReference.child(getResources().getString(R.string.ProductVariation)).child(productNam)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            ProductVariation model = ds.getValue(ProductVariation.class);
                            if(model!=null)
                            {
                                proVarName = model.getProductVariationName();
                                quantityList.add(0,model);
                                offerPrice.setText("\u20B9"+model.getProductSalePrice());
                                originalPrice.setText("\u20B9"+model.getProductActualPrice());
                                originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                savingPrice.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice()));
                                offer.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice())+"\nOff");
                            }
                        }
                        quantityAdapter.setData(quantityList);
                        recyclerView2.setLayoutManager(quantiyLayoutManager);
                        recyclerView2.setAdapter(quantityAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                        if(model!=null)
                        {
                            modelGlobal = dataSnapshot.getValue(Product.class);
                            Picasso.get().load(Uri.parse(model.getImageUrl())).placeholder(R.drawable.placeholder).into(productImg);
                            productName.setText(model.getProductName()+" ,"+model.getQuantity());
                            rating.setText(model.getRating());
                            productDetails.setText(model.getProductDetail());
                            returnable = Integer.parseInt(model.getReturnable());
                            cod = Integer.parseInt(model.getPayOnDelivery());

                            Integer totalRating=model.getRate1()+model.getRate2()+model.getRate3()+model.getRate4()+model.getRate5();
                            ratingList.clear();
                            ratingList.add(new RatingInfo("5 ",model.getRate5()));
                            ratingList.add(new RatingInfo("4 ",model.getRate4()));
                            ratingList.add(new RatingInfo("3 ",model.getRate3()));
                            ratingList.add(new RatingInfo("2 ",model.getRate2()));
                            ratingList.add(new RatingInfo("1 ",model.getRate1()));

                            ratingAdapter = new RatingAdapter(ratingList,getApplicationContext(),totalRating);
                            ratingRecyclerView.setHasFixedSize(true);
                            ratingAdapter.setData(ratingList);
                            ratingRecyclerView.setLayoutManager(ratingLayoutManager);
                            ratingRecyclerView.setAdapter(ratingAdapter);
                        }
                        loadingDialog.DismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void init() {

        loadingDialog = new LoadingDialog(this);
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
        buy_now = findViewById(R.id.product_detail_buy_now);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        simRecyclerView=findViewById(R.id.product_detail_similiar_product);
        similarProductAapter =new SimilarProductAapter(pSimList,this,ProductDetailActivity.this);

        //Rating
        ratingRecyclerView = findViewById(R.id.rating_list);
        ratingList = new ArrayList<>();
        ratingLayoutManager = new LinearLayoutManager(this);

        quantityList = new ArrayList<>();
        recyclerView2 = findViewById(R.id.product_detail_different_quantity);
        quantiyLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        quantityAdapter = new ProductVarQuantityAdapter(quantityList,this);



    }
}