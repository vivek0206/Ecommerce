package com.ecommerce.ecommerce.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryItemAdapter {




    public class CategoryItemView extends RecyclerView.ViewHolder{

        private ImageView productImg,delete,add,wishlist;
        private TextView productName,OfferPrice,OriginalPrice,Saving,ProductQuantity,quantityName;
        private RatingBar ratingBar;
        private int quantity;
        private DatabaseReference databaseReference;
        private FirebaseUser user;

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

            if (quantity==0)
            {
                delete.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                ProductQuantity.setVisibility(View.GONE);
                quantityName.setText("Add To Basket");
            }
            else
            {
                delete.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                ProductQuantity.setVisibility(View.VISIBLE);
                quantityName.setText("Qty:");
                ProductQuantity.setText(quantity+"");
            }


            quantityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quantity==0)
                    {
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
                    //add to wishlist
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //increase quantity in cart
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete quantiy in cart
                }
            });


        }
    }

}
