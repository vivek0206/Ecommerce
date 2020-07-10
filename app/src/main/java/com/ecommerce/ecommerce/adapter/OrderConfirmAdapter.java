package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Models.OrderInfoModel;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.Models.UserOrderInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderConfirmAdapter extends RecyclerView.Adapter<OrderConfirmAdapter.OrderItemView>{

    private List<ProductVariation> list;
    private Context context;

    public OrderConfirmAdapter(List<ProductVariation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_order_item,parent,false);
        return new OrderItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemView holder, int position) {

        ProductVariation item = list.get(position);
        Picasso.get().load(Uri.parse(item.getImageUrl())).into(holder.img);
        holder.itemName.setText(item.getProductName()+", "+item.getProductVariationName());
        holder.itemPrice.setText("\u20B9"+item.getProductSalePrice()*item.getQuantity());


    }

    public void setData(List<ProductVariation> list){this.list = list;}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderItemView extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView itemName,itemPrice;


        public OrderItemView(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.raw_order_item_image);
            itemName = itemView.findViewById(R.id.raw_order_item_name);
            itemPrice = itemView.findViewById(R.id.raw_order_item_price);

        }
    }

}
