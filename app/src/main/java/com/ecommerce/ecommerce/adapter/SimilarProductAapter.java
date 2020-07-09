package com.ecommerce.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

public class SimilarProductAapter extends RecyclerView.Adapter<SimilarProductAapter.CategoryItemView> {

    private List<Product> list;
    private Context context;
    private Activity activity;

    public SimilarProductAapter(List<Product> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_list_item,parent,false);
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
//        holder.Saving.setText((Integer.parseInt(model.getOriginalPrice())-Integer.parseInt(model.getSalePrice()))+"Off");
        holder.categoryName = model.getCategoryName();
        holder.productNam = model.getProductName();
        holder.subCategoryName=model.getSubCategoryName();
//        holder.fetchProductDetail(model.getCategoryName(),model.getProductName());


    }

    public void setData(List<Product> list){this.list = list;}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryItemView extends RecyclerView.ViewHolder{

        private ImageView productImg,delete,add,wishlist;
        private TextView productName,OfferPrice,OriginalPrice,Saving;
        private RatingBar ratingBar;
        private int quantity;
        private DatabaseReference databaseReference;
        private FirebaseUser user;
        private Product modelGlobal;
        private String categoryName,productNam,subCategoryName;

        public CategoryItemView(@NonNull View itemView) {
            super(itemView);

            productImg=itemView.findViewById(R.id.psimage);
            productName=itemView.findViewById(R.id.pstitle);
            OfferPrice=itemView.findViewById(R.id.pssalePrice);
            OriginalPrice=itemView.findViewById(R.id.psorigPrice);
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


        }

    }


}
