package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    List<Product> productList;
    Context mContext;
    public SubCategoryAdapter(Context context, List<Product> productsList) {
        this.productList = productsList;
        this.mContext=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_view_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        Product data=productList.get(i);
        viewHolder.title.setText(data.getProductName());

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        String category;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ptitle);
            image=itemView.findViewById(R.id.pimage);
        }
    }
}
