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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.UserWishlistView>{

    private List<Product> list;
    private Context context;
    OnDataChangeListener onDataChangeListener;

    public WishlistAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        this.onDataChangeListener = onDataChangeListener;
    }


    @NonNull
    @Override
    public UserWishlistView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_wishlist_item,parent,false);
        return new UserWishlistView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserWishlistView holder, int position) {
        Product model = list.get(position);
        Picasso.get().load(Uri.parse(model.getImageUrl())).into(holder.productImage);
        holder.productName.setText(model.getProductName());
        holder.originalPrice.setText(model.getOriginalPrice());
        holder.offerPrice.setText(model.getSalePrice());
        holder.savingPrice.setText((Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"Off");
        holder.categoryName = model.getCategoryName();
        holder.productNam = model.getProductName();
        holder.fetchProductDetail(model.getProductName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setData(List<Product> list){this.list = list;}


    public class UserWishlistView extends RecyclerView.ViewHolder{

        private ImageView productImage,productDelete,productAdd,delete;
        private TextView productName,offerPrice,originalPrice,savingPrice,eligible,productQuantity;
        private FirebaseUser user;
        private DatabaseReference databaseReference;
        private String categoryName,productNam;
        private Product modelGlobal;
        private int quantity;

        public UserWishlistView(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.raw_wishlist_item_image);
            productDelete = itemView.findViewById(R.id.raw_wishlist_delete_quantity);
            productAdd= itemView.findViewById(R.id.raw_wishlist_add_quantity);
            productName= itemView.findViewById(R.id.raw_wishlist_productName);
            offerPrice= itemView.findViewById(R.id.raw_wishlist_productOfferPrice);
            originalPrice= itemView.findViewById(R.id.raw_wishlist_productOriginalPrice);
            savingPrice= itemView.findViewById(R.id.raw_wishlist_prductSaving);
            eligible= itemView.findViewById(R.id.raw_wishlist_eligible);
            productQuantity= itemView.findViewById(R.id.raw_wishlist_ProductQuantity);
            delete = itemView.findViewById(R.id.raw_wishlist_delete);
            user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child(context.getResources().getString(R.string.Wishlist)).child(user.getUid()).child(productNam).removeValue();
                }
            });

            productAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(modelGlobal!=null)
                    {
                        if(modelGlobal!=null)
                        {
                            Log.d("pop pop pop","add  "+" act");
                            if(quantity>=5)
                            {
                            }
                            else
                            {

                                if(onDataChangeListener != null){
                                    Toast.makeText(context,"No more avaiable",Toast.LENGTH_SHORT).show();
                                    onDataChangeListener.onDataChanged(list.size(),Integer.parseInt(modelGlobal.getSalePrice()));
                                }

                                quantity++;
                                productQuantity.setText(quantity+"");
                                modelGlobal.setQuantity(quantity+"");
                                databaseReference.child(context.getResources().getString(R.string.Cart)).child(user.getUid()).child(categoryName).child(productNam).setValue(modelGlobal);
                                databaseReference.child(context.getResources().getString(R.string.UserCart)).child(user.getUid()).child(productNam).setValue(modelGlobal);

                            }
                        }
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
                            onDataChangeListener.onDataChanged(list.size(),-1*Integer.parseInt(modelGlobal.getSalePrice()));
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
            databaseReference.child(context.getResources().getString(R.string.Wishlist)).child(user.getUid()).child(product).addValueEventListener(new ValueEventListener() {
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


    }

}
