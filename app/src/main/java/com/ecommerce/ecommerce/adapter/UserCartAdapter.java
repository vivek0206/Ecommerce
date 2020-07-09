package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Interface.OnDataChangeListener;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.CartActivity;
import com.ecommerce.ecommerce.activity.MainActivity;
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

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.UserCartView>{

    private List<Product> list;
    private Context context;
    OnDataChangeListener onDataChangeListener;

    public UserCartAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        this.onDataChangeListener = onDataChangeListener;
    }


    @NonNull
    @Override
    public UserCartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_cart_item,parent,false);
        return new UserCartView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCartView holder, int position) {
        Product model = list.get(position);
        Picasso.get().load(Uri.parse(model.getImageUrl())).into(holder.productImage);
        holder.productName.setText(model.getProductName());
        holder.originalPrice.setText(model.getOriginalPrice());
        holder.categoryName = model.getCategoryName();
        holder.productNam = model.getProductName();
        holder.subCategory = model.getSubCategoryName();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setData(List<Product> list){this.list = list;}


    public class UserCartView extends RecyclerView.ViewHolder{

        private ImageView productImage,productDelete,productAdd;
        private TextView productName,offerPrice,originalPrice,savingPrice,eligible,productQuantity,outOfS;
        private FirebaseUser user;
        private DatabaseReference databaseReference;
        private String categoryName,productNam,subCategory;
        private Product modelGlobal,modelAdmin;
        private int quantity,quantityGlobal;
        private boolean outOfStock=false,add=false;

        public UserCartView(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.raw_cart_item_image);
            productDelete = itemView.findViewById(R.id.raw_cart_delete_quantity);
            productAdd= itemView.findViewById(R.id.raw_cart_add_quantity);
            productName= itemView.findViewById(R.id.raw_cart_productName);
            offerPrice= itemView.findViewById(R.id.raw_cart_productOfferPrice);
            originalPrice= itemView.findViewById(R.id.raw_cart_productOriginalPrice);
            savingPrice= itemView.findViewById(R.id.raw_cart_prductSaving);
            eligible= itemView.findViewById(R.id.raw_cart_eligible);
            productQuantity= itemView.findViewById(R.id.raw_cart_ProductQuantity);
            outOfS = itemView.findViewById(R.id.raw_cart_outOfStock);
            user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            productAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(modelGlobal!=null)
                    {
                        add=true;

                    }
                }
            });

            productDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quantity>0)
                    {
                        add=false;

                    }
                }
            });

        }


    }

}
