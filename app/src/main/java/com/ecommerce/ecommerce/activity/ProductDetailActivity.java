package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.BuildConfig;
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

    private TextView offer,productName,rating,offerPrice,originalPrice,savingPrice,productDetails,addtocart,addtowishlist,shareText;
    private ImageView productImg,payOnDelivery,nonReturnable,addToCart,share,addToWishlist;
    private RecyclerView recyclerView2;
    private LinearLayoutManager quantiyLayoutManager;
    private ProductVarQuantityAdapter quantityAdapter;
    private List<ProductVariation> quantityList;
    private Button buy_now;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String categoryName,productNam,subCategoryName,proVarName,productOriginalName,keyName;
    private int returnable,cod;
    private Toolbar toolbar;

    private SimilarProductAapter similarProductAapter;
    private List<Product> pSimList=new ArrayList<>();
    private RecyclerView simRecyclerView;
    private LoadingDialog loadingDialog;
    private Product modelGlobal;

    //Rating
    private TextView avgRatingTv;
    private RecyclerView ratingRecyclerView;
    private List<RatingInfo> ratingList;
    private RatingAdapter ratingAdapter;
    private LinearLayoutManager ratingLayoutManager;
    private LinearLayout codLayout,nonreturnableLayout;



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
        productOriginalName=productNam;
        subCategoryName=subCategoryName.toLowerCase().trim();
        categoryName = categoryName.toLowerCase().trim();
        productNam = productNam.toLowerCase().trim();

        keyName=categoryName+"_"+subCategoryName+"_"+productNam;
        fetchWishlistState();
        quantityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ProductVariation model, int type) {

            }

            @Override
            public void onItemClick() { }

            @Override
            public void onItemClick(ProductVariation model) {
                proVarName = model.getProductVariationName();
                productName.setText(model.getProductName()+", "+model.getProductVariationName());
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
        setSimProduct();
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(1);
            }
        });

        similarProductAapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ProductVariation model, int type) { }
            @Override
            public void onItemClick() {
                finish();
            }

            @Override
            public void onItemClick(ProductVariation model) { }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(0);
                Toast.makeText(getApplicationContext(),"Item Added in your cart",Toast.LENGTH_SHORT).show();
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(0);
                Toast.makeText(getApplicationContext(),"Item Added in your cart",Toast.LENGTH_SHORT).show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();

            }
        });

        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        addToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishlist();
            }
        });



    }

    private void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody ="https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void increaseQuantity(final int flag) {
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

                                if(flag==0)
                                {
                                    Toast.makeText(getApplicationContext(),"Item is already present in your Basket",Toast.LENGTH_SHORT).show();
                                }
                          /*
                                databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam.toLowerCase().trim()).child(proVarName.toLowerCase().trim())
                                        .setValue(model);*/

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
                            if(flag==1)
                            {
                                loadingDialog.startLoadingDialog();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.DismissDialog();
                                        Intent intent = new Intent(ProductDetailActivity.this,CartActivity.class);
                                        startActivity(intent);
                                    }
                                },500);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void setSimProduct() { }

    private void wishlist(){


        databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(categoryName.toLowerCase().trim()).child(subCategoryName.toLowerCase().trim())
                .child(productNam.toLowerCase().trim())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Product model = dataSnapshot.getValue(Product.class);
                        if (model!=null)
                        {
                            databaseReference.child(getResources().getString(R.string.Wishlist)).child(user.getUid()).child(keyName)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Product wishlistModel = dataSnapshot.getValue(Product.class);
                                            if(wishlistModel!=null)
                                            {
                                                databaseReference.child("WishList").child(user.getUid()).child(keyName).removeValue();
                                                addToWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_fill,null));
                                            }
                                            else
                                            {
                                                databaseReference.child("WishList").child(user.getUid()).child(keyName).setValue(model);
                                                addToWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist,null));

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
                                productOriginalName=","+model.getProductVariationName();
                                productName.setText(productOriginalName);
                                quantityList.add(0,model);
                                offerPrice.setText("\u20B9"+model.getProductSalePrice());
                                originalPrice.setText("\u20B9"+model.getProductActualPrice());
                                originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                savingPrice.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice()));
                                offer.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice())+"\nOff");
                            }
                            else
                            {
                                offer.setText("Out Of Stock");
                                buy_now.setEnabled(false);
                                addToCart.setEnabled(false);
                                addtocart.setEnabled(false);
                            }
                        }
                        if(dataSnapshot.getChildrenCount()==0)
                        {
                            offer.setText("Out Of Stock");
                            buy_now.setEnabled(false);
                            buy_now.setText("Out Of Stock");
                            buy_now.setBackgroundColor(getResources().getColor(R.color.grey));
                            addToCart.setEnabled(false);
                            addtocart.setEnabled(false);
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

    private void fetchWishlistState(){

        databaseReference.child(getResources().getString(R.string.Wishlist)).child(user.getUid()).child(keyName)
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product wishlistModel = dataSnapshot.getValue(Product.class);
                        if(wishlistModel!=null)
                        {
                            addToWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist,null));

                        }
                        else
                        {
                            addToWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_fill,null));

                        }
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
                            rating.setText(model.getRating());
                            productDetails.setText(model.getProductDetail());
                            productName.setText(model.getProductName());
                            offerPrice.setText(getResources().getString(R.string.Rupee)+model.getSalePrice());
                            originalPrice.setText(getResources().getString(R.string.Rupee)+model.getOriginalPrice());
                            savingPrice.setText(getResources().getString(R.string.Rupee)+(Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice())));
                            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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

                            if(model.getPayOnDelivery().equals("0")){
                                codLayout.setVisibility(View.GONE);
                            }else{
                                codLayout.setVisibility(View.VISIBLE);
                            }
                            if(model.getReturnable().equals("1")){
                                nonreturnableLayout.setVisibility(View.GONE);
                            }else{
                                nonreturnableLayout.setVisibility(View.VISIBLE);
                            }

                            fetchProductVariation(productNam.toLowerCase().trim());


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

        addToCart = findViewById(R.id.activity_product_cart_image);
        addtocart = findViewById(R.id.activity_product_cart_text);
        share = findViewById(R.id.activity_product_share);
        shareText = findViewById(R.id.activity_product_share_text);
        share.setVisibility(View.GONE);
        shareText.setVisibility(View.GONE);
        addToWishlist = findViewById(R.id.activity_product_wishlist);
        addtowishlist = findViewById(R.id.activity_product_wishlist_text);

        codLayout=findViewById(R.id.cod_layout);
        nonreturnableLayout=findViewById(R.id.nonreturnable_layout);




    }



}