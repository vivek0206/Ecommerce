package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.activity.ProductDetailActivity;
import com.ecommerce.ecommerce.object.Product;
import com.ecommerce.ecommerce.object.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    List<SubCategory> productList;
    Context mContext;
    String flag;
    public SubCategoryAdapter(Context context, List<SubCategory> productsList, String flag) {
        this.productList = productsList;
        this.mContext=context;
        this.flag=flag;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if(flag=="grid")
            itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, viewGroup, false);
        else itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_linear_view_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        final SubCategory data=productList.get(i);
        viewHolder.title.setText(data.getSubCategoryName());
        Picasso.get().load(data.getImageUrl()).into(viewHolder.image);
        Log.d(TAG, "Value is: "+data.getCategoryName());
        Log.d(TAG, "Value is: "+data.getSubCategoryName());
        viewHolder.rviewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailCategoryList.class);
                intent.putExtra("category",data.getCategoryName());
                intent.putExtra("subCategory",data.getSubCategoryName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        RelativeLayout rviewGroup;
        public MyViewHolder(View itemView) {
            super(itemView);
            if(flag=="grid")
            {
                title = itemView.findViewById(R.id.ptitle);
                image=itemView.findViewById(R.id.pimage);
                rviewGroup=itemView.findViewById(R.id.pviewGroup);
            }else{
                title = itemView.findViewById(R.id.pLtitle);
                image=itemView.findViewById(R.id.pLimage);
                rviewGroup=itemView.findViewById(R.id.pLviewGroup);
            }

        }
    }
}
