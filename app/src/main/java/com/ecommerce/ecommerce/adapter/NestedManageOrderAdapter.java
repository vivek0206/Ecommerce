package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.OrderIndividualActivity;
import com.ecommerce.ecommerce.object.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NestedManageOrderAdapter extends RecyclerView.Adapter<NestedManageOrderAdapter.NestedView> {

    private List<ProductVariation> list;
    private Context context;
    private String orderId;

    public NestedManageOrderAdapter(List<ProductVariation> list, Context context, String orderId) {
        this.list = list;
        this.context = context;
        this.orderId = orderId;
    }

    @NonNull
    @Override
    public NestedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_nested_order_detail_item,parent,false);
        return new NestedView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedView holder, int position) {
        ProductVariation item = list.get(position);
        Picasso.get().load(Uri.parse(item.getImageUrl())).into(holder.img);
        holder.itemName.setText(item.getProductName()+", "+item.getProductVariationName());
        holder.itemPrice.setText("\u20B9"+item.getProductSalePrice()+"");
        holder.itemQuantity.setText("Quantity: "+item.getQuantity());
        holder.orderStatus = item.getOrderStatus();
        Log.d("TAG",item.getOrderStatus()+"vvvvv");
        holder.catName = item.getCategoryName().toLowerCase().trim();
        holder.subCatName = item.getSubCategoryName().toLowerCase().trim();
        holder.productName = item.getProductName().toLowerCase().trim();
        holder.proVar=item.getProductVariationName().toLowerCase().trim();
        holder.setOrderSatuts();
    }

    public void setData(List<ProductVariation> list){this.list = list;}


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NestedView extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView itemName,itemPrice,cancel,itemQuantity;
        private CardView card;
        private String orderStatus="2",productName,proVar,catName,subCatName;

        public NestedView(@NonNull View itemView) {
            super(itemView);

            init(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderIndividualActivity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("productName",catName+"_"+subCatName+"_"+productName+"_"+proVar);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });





        }
        private  void setOrderSatuts(){

            if(orderStatus.equals("1"))
            {
                cancel.setText("Order Confirmed");
            }
            else if(orderStatus.equals("2"))
            {
                cancel.setText("Order Packed");
            }
            else if(orderStatus.equals("3"))
            {
                cancel.setText("Out For Delivery");
            }
            else if(orderStatus.equals("4"))
            {
                cancel.setText("Delivered");
            }
            else if(orderStatus.equals("5"))
            {
                cancel.setText("Cancelled");
            }
        }
        private void init(View itemView) {
            img = itemView.findViewById(R.id.raw_nested_detail_image);
            itemName = itemView.findViewById(R.id.raw_nested_detail_itemName);
            itemPrice = itemView.findViewById(R.id.raw_nested_detail_itemPrice);
            cancel = itemView.findViewById(R.id.raw_nested_detail_cancelItem);
            card = itemView.findViewById(R.id.raw_nested_order_card);
            itemQuantity = itemView.findViewById(R.id.raw_nested_detail_itemQuantiy);
        }
    }

}
