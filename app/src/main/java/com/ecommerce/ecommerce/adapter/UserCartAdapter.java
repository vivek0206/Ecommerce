package com.ecommerce.ecommerce.adapter;

import android.content.Context;
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
        holder.fetchProductDetail(model.getProductName());
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
                        checkStock(categoryName,subCategory,productNam,String.valueOf(quantity+1));
                    }
                }
            });

            productDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quantity>0)
                    {

                        Log.d("pop pop pop","delete  "+" act");
                        quantity--;
                        productQuantity.setText(quantity+"");
                        modelGlobal.setQuantity(quantity+"");

                        if(onDataChangeListener != null){
                            onDataChangeListener.onDataChanged(list.size(),-1*Integer.parseInt(modelGlobal.getSalePrice()),outOfStock);
                            Toast.makeText(context,"Quantity   ",Toast.LENGTH_SHORT).show();
                        }
                        databaseReference.child(context.getResources().getString(R.string.Cart)).child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);
                        databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).setValue(modelGlobal);

                        if(quantity==0)
                        {
                            //delete it

                            if(getAdapterPosition()>0) {
                                list.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition() + 1);
                                notifyItemRangeChanged(getAdapterPosition() + 1, list.size());
                            }
                            databaseReference.child(context.getResources().getString(R.string.Cart)).child(user.getUid()).child(categoryName).child(productNam).removeValue();
                            databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).removeValue();



                        }

                    }
                }
            });

        }

        private void fetchProductDetail(String product) {
            databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(product).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product model = dataSnapshot.getValue(Product.class);
                    if(model!=null)
                    {
                        modelGlobal = dataSnapshot.getValue(Product.class);
                        quantity = Integer.parseInt(modelGlobal.getQuantity());
                        productDelete.setVisibility(View.VISIBLE);
                        productAdd.setVisibility(View.VISIBLE);
                        productQuantity.setVisibility(View.VISIBLE);
                        productQuantity.setText(quantity+"");
                        checkStock(model.getCategoryName(),model.getSubCategoryName(),model.getProductName(),model.getQuantity());

                    }
                    else
                    {
                        productDelete.setVisibility(View.GONE);
                        productAdd.setVisibility(View.GONE);
                        productQuantity.setVisibility(View.GONE);
                        productQuantity.setText(quantity+"");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        private void checkStock(String category, String subCategory, String product, final String quantit)
        {
            category = category.toLowerCase().trim();
            subCategory = subCategory.toLowerCase().trim();
            product = product.toLowerCase().trim();

            databaseReference.child(context.getResources().getString(R.string.Admin)).child(category).child(subCategory).child(product)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Product model = dataSnapshot.getValue(Product.class);

                            if(model!=null)
                            {
                                modelAdmin = dataSnapshot.getValue(Product.class);
                                offerPrice.setText(model.getSalePrice());
                                savingPrice.setText((Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"Off");
                                quantityGlobal = Integer.parseInt(model.getQuantity());
                                if(Integer.parseInt(model.getQuantity()) < quantity)
                                {
                                    outOfS.setVisibility(View.VISIBLE);
                                    productAdd.setEnabled(false);
                                    Toast.makeText(context,"Out Of Stockkkkkkkkkkkkkkkkkkk",Toast.LENGTH_SHORT).show();
                                    outOfStock = true;
                                        if(onDataChangeListener != null){
                                            onDataChangeListener.onDataChanged(list.size(),-1*Integer.parseInt(modelGlobal.getSalePrice()),outOfStock);
                                            Toast.makeText(context,"Quantity   ",Toast.LENGTH_SHORT).show();
                                        }
                                }
                                else
                                {
                                    if(add==true)
                                    {
                                        if(onDataChangeListener != null){
                                            onDataChangeListener.onDataChanged(list.size(),Integer.parseInt(modelGlobal.getSalePrice()),outOfStock);
                                            Toast.makeText(context,"Quantity   ",Toast.LENGTH_SHORT).show();
                                        }

                                        quantity = quantity+1;
                                        productQuantity.setText(quantity+"");
                                        modelGlobal.setQuantity(quantity+"");
                                        databaseReference.child(context.getResources().getString(R.string.Cart)).child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);
                                        databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).setValue(modelGlobal);

                                    }
                                    else
                                    {
                                        if(onDataChangeListener != null){
                                            onDataChangeListener.onDataChanged(list.size(),-1*Integer.parseInt(modelGlobal.getSalePrice()),outOfStock);
                                            Toast.makeText(context,"Quantity   ",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    outOfStock = false;
                                    productAdd.setEnabled(true);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }


    }

}
