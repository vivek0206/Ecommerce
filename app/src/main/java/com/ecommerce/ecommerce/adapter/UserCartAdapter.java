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
import com.ecommerce.ecommerce.Models.ProductVariation;
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

    private List<ProductVariation> list;
    private Context context;
    OnDataChangeListener onDataChangeListener;
    int Tquantity=0;

    public UserCartAdapter(List<ProductVariation> list, Context context) {
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
        ProductVariation model = list.get(position);
        Picasso.get().load(Uri.parse(model.getImageUrl())).into(holder.productImage);
        holder.modelGlobal = list.get(position);
        holder.productName.setText(model.getProductName());
        holder.originalPrice.setText("\u20B9"+model.getProductActualPrice());
        holder.productQuantity.setText(model.getQuantity()+"");
        holder.categoryName = model.getCategoryName().toLowerCase().trim();
        holder.productNam = model.getProductName().toLowerCase().trim();
        holder.subCategory = model.getSubCategoryName().toLowerCase().trim();
        holder.proVarName = model.getProductVariationName().toLowerCase().trim();
        holder.cartQuantity = model.getQuantity();
        holder.fetchProductDetail();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setData(List<ProductVariation> list){this.list = list;}


    public class UserCartView extends RecyclerView.ViewHolder{

        private ImageView productImage,productDelete,productAdd;
        private TextView productName,offerPrice,originalPrice,savingPrice,eligible,productQuantity,outOfS;
        private FirebaseUser user;
        private DatabaseReference databaseReference;
        private String categoryName,productNam,subCategory,proVarName;
        private ProductVariation modelGlobal,modelAdmin;
        private int cartQuantity,adminQuantity;
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
            productQuantity= itemView.findViewById(R.id.raw_cart_ProductQuantity);
            outOfS = itemView.findViewById(R.id.raw_cart_outOfStock);
            user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            productAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IncreaseQuantity();
                }
            });

            productDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //decrease the quqntity
                    DecreaseQuantity();
                }
            });

        }

        private void IncreaseQuantity() {
            //fetch Total Quantity
            modelGlobal.setQuantity(modelGlobal.getQuantity()+1);
            productQuantity.setText(modelGlobal.getQuantity()+"");
            databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).child(proVarName).setValue(modelGlobal);
            onDataChangeListener.onCheckOutOfStock(1,1*modelGlobal.getProductSalePrice());
            databaseReference.child(context.getResources().getString(R.string.ProductVariation)).child(productNam).child(proVarName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Tquantity=0;
                    ProductVariation model = dataSnapshot.getValue(ProductVariation.class);
                    if(model!=null)
                    {
                        Tquantity=model.getQuantity();
                    }
                    if(Tquantity==modelGlobal.getQuantity())
                    {
                        productAdd.setEnabled(false);
                        outOfS.setVisibility(View.VISIBLE);
                        outOfS.setText("Max Limit");

                    }
                    else if(Tquantity<modelGlobal.getQuantity()){
                        productAdd.setEnabled(true);
                        outOfS.setVisibility(View.GONE);
                    }
                    else {
                        productAdd.setEnabled(false);
                        outOfS.setText("Out Of Stock");
                        outOfS.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void DecreaseQuantity() {

                if(modelGlobal.getQuantity()>0)
                {
                    modelGlobal.setQuantity(modelGlobal.getQuantity()-1);
                    productQuantity.setText(modelGlobal.getQuantity()+"");
                    databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).child(proVarName).setValue(modelGlobal);
                    onDataChangeListener.onCheckOutOfStock(-1,modelGlobal.getProductSalePrice());
                    databaseReference.child(context.getResources().getString(R.string.ProductVariation)).child(productNam).child(proVarName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Tquantity=0;
                                    ProductVariation model = dataSnapshot.getValue(ProductVariation.class);
                                    Log.d("poop poop  ",dataSnapshot.getValue()+"");
                                    if(model!=null)
                                    {
                                        Tquantity=model.getQuantity();
                                    }
                                    if(Tquantity==modelGlobal.getQuantity())
                                    {
                                        productAdd.setEnabled(false);
                                        outOfS.setVisibility(View.VISIBLE);
                                        outOfS.setText("Max Limit");
                                    }
                                    else if(Tquantity>modelGlobal.getQuantity()){

                                        outOfS.setVisibility(View.GONE);
                                        productAdd.setEnabled(true);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                }
                else{
                    //again load the list

                }

        }

        public void fetchProductDetail()
        {
            databaseReference.child(context.getResources().getString(R.string.ProductVariation)).child(productNam).child(proVarName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ProductVariation model = dataSnapshot.getValue(ProductVariation.class);
                            if(model!=null)
                            {
                                if(cartQuantity>model.getQuantity())
                                {
                                    outOfS.setVisibility(View.VISIBLE);
                                    productAdd.setEnabled(false);

                                    onDataChangeListener.onCheckOutOfStock(1,cartQuantity*model.getProductSalePrice());

                                }
                                else if(cartQuantity==model.getQuantity())
                                {
                                    productAdd.setEnabled(false);
                                    outOfS.setText("Max Limit");
                                    outOfS.setVisibility(View.VISIBLE);
                                    onDataChangeListener.onCheckOutOfStock(1,cartQuantity*model.getProductSalePrice());
                                }
                                else{
                                    productAdd.setEnabled(true);
                                    outOfS.setVisibility(View.GONE);
                                    onDataChangeListener.onCheckOutOfStock(1,cartQuantity*model.getProductSalePrice());

                                }
                                modelGlobal.setProductSalePrice(model.getProductSalePrice());
                                offerPrice.setText("\u20B9"+model.getProductSalePrice());
                                savingPrice.setText("\u20B9"+(model.getProductActualPrice()-model.getProductSalePrice()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


    }

}
