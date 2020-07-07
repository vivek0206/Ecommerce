package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailView> {

    private List<Product> list;
    private Context context;

    public OrderDetailAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_order_detail_item,parent,false);
        return new OrderDetailView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailView holder, int position) {
        Product item = list.get(position);
        Picasso.get().load(Uri.parse(item.getImageUrl())).into(holder.img);
        holder.itemName.setText(item.getProductName());
        holder.itemPrice.setText(item.getSalePrice());
    }

    @Override
    public int getItemCount() { return list.size(); }

    public void setData(List<Product> list){this.list = list;}

    public class OrderDetailView extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView itemName,itemPrice,cancel;

        public OrderDetailView(@NonNull View itemView) {
            super(itemView);

            init(itemView);

        }

        private void init(View itemView) {
            img = itemView.findViewById(R.id.raw_order_detail_image);
            itemName = itemView.findViewById(R.id.raw_order_detail_itemName);
            itemPrice = itemView.findViewById(R.id.raw_order_detail_itemPrice);
            cancel = itemView.findViewById(R.id.raw_order_detail_cancelItem);
        }
    }
}
