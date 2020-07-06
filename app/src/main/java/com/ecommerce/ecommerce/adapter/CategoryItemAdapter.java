package com.ecommerce.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Models.AccountModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.ProductDetailActivity;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemView> {

    private List<Product> list;
    private Context context;
    private Activity activity;

    public CategoryItemAdapter(List<Product> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_category_list_item,parent,false);
        return new CategoryItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemView holder, int position) {
        Product model = list.get(position);
        Picasso.get().load(Uri.parse(model.getImageUrl())).into(holder.productImg);
        holder.productName.setText(model.getProductName());
        holder.OriginalPrice.setText(model.getOriginalPrice());
        holder.OfferPrice.setText(model.getSalePrice());
        holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
        holder.Saving.setText((Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"Off");
        holder.categoryName = model.getCategoryName();
        holder.productNam = model.getProductName();
        holder.fetchProductDetail(model.getCategoryName(),model.getProductName());


    }

    public void setData(List<Product> list){this.list = list;}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryItemView extends RecyclerView.ViewHolder{

        private ImageView productImg,delete,add,wishlist;
        private TextView productName,OfferPrice,OriginalPrice,Saving,ProductQuantity,quantityName;
        private RatingBar ratingBar;
        private int quantity;
        private DatabaseReference databaseReference;
        private FirebaseUser user;
        private Product modelGlobal;
        private String categoryName,productNam;

        public CategoryItemView(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.raw_category_productImg);
            delete  = itemView.findViewById(R.id.raw_category_delete_quantity);
            add = itemView.findViewById(R.id.raw_category_add_quantity);
            wishlist = itemView.findViewById(R.id.raw_category_wishlist);
            productName = itemView.findViewById(R.id.raw_category_productName);
            quantityName = itemView.findViewById(R.id.raw_category_quantity);
            ProductQuantity = itemView.findViewById(R.id.raw_category_ProductQuantity);
            OfferPrice = itemView.findViewById(R.id.raw_category_productOfferPrice);
            OriginalPrice = itemView.findViewById(R.id.raw_category_productOriginalPrice);
            Saving = itemView.findViewById(R.id.raw_category_prductSaving);
            ratingBar = itemView.findViewById(R.id.raw_category_productRating);
            databaseReference = FirebaseDatabase.getInstance().getReference("");
            user = FirebaseAuth.getInstance().getCurrentUser();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("category",categoryName);
                    intent.putExtra("product",productNam);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            quantityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quantity==0)
                    {
                        modelGlobal = new Product(list.get(getAdapterPosition()).getImageUrl(),categoryName,productNam,list.get(getAdapterPosition()).getOriginalPrice(),list.get(getAdapterPosition()).getSalePrice(),"1",list.get(getAdapterPosition()).getRating());
                        databaseReference.child("Product").child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);
                        delete.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        ProductQuantity.setVisibility(View.VISIBLE);
                        quantityName.setText("Qty:");
                        quantity++;
                        ProductQuantity.setText(quantity+"");
                    }
                }
            });

            wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product model = new Product(list.get(getAdapterPosition()).getImageUrl(),categoryName,productNam,list.get(getAdapterPosition()).getOriginalPrice(),list.get(getAdapterPosition()).getSalePrice(),list.get(getAdapterPosition()).getQuantity(),list.get(getAdapterPosition()).getRating());
                    wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wishlist,null));
                    databaseReference.child("WishList").child(user.getUid()).child(productNam).setValue(model);

                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //first fetch then increase
                    if(modelGlobal!=null)
                    {
                        if(quantity>=Integer.parseInt(list.get(getAdapterPosition()).getQuantity()))
                        {
                            Toast.makeText(context,"No more avaiable",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            quantity++;
                            ProductQuantity.setText(quantity+"");
                            modelGlobal.setQuantity(quantity+"");
                            databaseReference.child("Product").child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quantity>0)
                    {
                        quantity--;
                        ProductQuantity.setText(quantity+"");
                        modelGlobal.setQuantity(quantity+"");
                        databaseReference.child("Product").child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);

                        if(quantity==0)
                        {
                            //delete it
                            databaseReference.child("Product").child(user.getUid()).child(categoryName).child(productNam).removeValue();
                            delete.setVisibility(View.GONE);
                            add.setVisibility(View.GONE);
                            ProductQuantity.setVisibility(View.GONE);
                            quantityName.setText("Add To Basket");
                            ProductQuantity.setText(quantity+"");

                        }

                    }

                }
            });
        }


        private void fetchProductDetail(String category,String product) {
            databaseReference.child("Product").child(user.getUid()).child(category).child(product).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product model = dataSnapshot.getValue(Product.class);
                    if(model!=null)
                    {
                        modelGlobal = dataSnapshot.getValue(Product.class);
                        quantity = Integer.parseInt(modelGlobal.getQuantity());
                        delete.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        ProductQuantity.setVisibility(View.VISIBLE);
                        quantityName.setText("Qty:");
                        ProductQuantity.setText(quantity+"");

                    }
                    else
                    {
                        delete.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);
                        ProductQuantity.setVisibility(View.GONE);
                        quantityName.setText("Add To Basket");
                        ProductQuantity.setText(quantity+"");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("WishList").child(user.getUid()).child(product).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product model = dataSnapshot.getValue(Product.class);
                    if(model!=null)
                    {
                        wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wishlist,null));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


}
