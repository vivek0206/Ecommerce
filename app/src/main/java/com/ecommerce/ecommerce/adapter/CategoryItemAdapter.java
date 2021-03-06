package com.ecommerce.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Interface.OnItemClickListener;
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
        Picasso.get().load(Uri.parse(model.getImageUrl())).placeholder(R.drawable.placeholder).into(holder.productImg);
        holder.productName.setText(model.getProductName());
        holder.OriginalPrice.setText("\u20B9"+model.getOriginalPrice());
        holder.OriginalPrice.setPaintFlags(holder.OriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.OfferPrice.setText("\u20B9"+model.getSalePrice());
        holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
        holder.Saving.setText("\u20B9"+(Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"Off");
        holder.categoryName = model.getCategoryName();
        holder.productNam = model.getProductName();
        holder.subCategoryName=model.getSubCategoryName();
        holder.keyName = holder.categoryName.toLowerCase().trim()+"_"+holder.subCategoryName.toLowerCase().trim()+"_"+holder.productNam.toLowerCase().trim();
        holder.fetchWishlistState();

    }

    public void setData(List<Product> list){this.list = list;}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryItemView extends RecyclerView.ViewHolder{

        private ImageView productImg,wishlist;
        private TextView productName,OfferPrice,OriginalPrice,Saving;
        private RatingBar ratingBar;
        private int quantity;
        private DatabaseReference databaseReference;
        private FirebaseUser user;
        private Product modelGlobal;
        private String categoryName,productNam,subCategoryName,keyName;

        public CategoryItemView(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.raw_category_productImg);
            wishlist = itemView.findViewById(R.id.raw_category_wishlist);
            productName = itemView.findViewById(R.id.raw_category_productName);
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
                    intent.putExtra("subCategory",subCategoryName);
                    intent.putExtra("category",categoryName);
                    intent.putExtra("product",productNam);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeWishlistState();
                }
            });

        }

        private void fetchWishlistState(){

            databaseReference.child(context.getResources().getString(R.string.Wishlist)).child(user.getUid()).child(keyName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Product wishlistModel = dataSnapshot.getValue(Product.class);
                            if(wishlistModel!=null)
                            {
                                wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wishlist,null));

                            }
                            else
                            {
                                wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wishlist_fill,null));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        private void changeWishlistState() {

            final Product model = new Product(list.get(getAdapterPosition()).getImageUrl(),categoryName,productNam,list.get(getAdapterPosition()).getOriginalPrice(),list.get(getAdapterPosition()).getSalePrice(),"1",list.get(getAdapterPosition()).getRating(),list.get(getAdapterPosition()).getProductDetail(),
                    list.get(getAdapterPosition()).getReturnable(),list.get(getAdapterPosition()).getPayOnDelivery(),list.get(getAdapterPosition()).getSubCategoryName());
            databaseReference.child(context.getResources().getString(R.string.Wishlist)).child(user.getUid()).child(keyName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Product wishlistModel = dataSnapshot.getValue(Product.class);
                            if(wishlistModel!=null)
                            {
                                databaseReference.child(context.getResources().getString(R.string.Wishlist)).child(user.getUid()).child(keyName).removeValue();
                                wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wishlist_fill,null));
                                Toast.makeText(context,"Product Removed From Wishlist",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                databaseReference.child(context.getResources().getString(R.string.Wishlist)).child(user.getUid()).child(keyName).setValue(model);
                                wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wishlist,null));
                                Toast.makeText(context,"Product Added to Wishlist",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        private void increaseQuantity() {
            if(modelGlobal!=null)
            {
                if(quantity>=Integer.parseInt(list.get(getAdapterPosition()).getQuantity()))
                {
                    Toast.makeText(context,"No more avaiable",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    quantity++;
                    modelGlobal.setQuantity(quantity+"");
                    databaseReference.child(context.getResources().getString(R.string.Cart)).child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);
                    databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).setValue(modelGlobal);

                }
            }
        }

    }


}
