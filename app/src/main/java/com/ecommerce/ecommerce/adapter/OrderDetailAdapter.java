package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.FOUR;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.ONE;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.THREE;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.TWO;

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
        private StateProgressBar stateProgressBar;
        private String[] descriptionData = {"Confirmed", "Packed", "Out For Delivery", "Delivered"};
        private String orderStatus="2";


        public OrderDetailView(@NonNull View itemView) {
            super(itemView);

            init(itemView);

            stateProgressBar.setStateDescriptionData(descriptionData);
            Log.d("pop pop",orderStatus+" pop pop ");
            if(!orderStatus.isEmpty() &&orderStatus.equals("1"))
            {

                stateProgressBar.setCurrentStateNumber(ONE);
                stateProgressBar.enableAnimationToCurrentState(true);
                stateProgressBar.checkStateCompleted(true);


            }
            else if(!orderStatus.isEmpty() &&orderStatus.equals("2"))
            {
                stateProgressBar.setCurrentStateNumber(TWO);
                stateProgressBar.enableAnimationToCurrentState(true);
                stateProgressBar.checkStateCompleted(true);


            }
            else if(!orderStatus.isEmpty() &&orderStatus.equals("3"))
            {
                stateProgressBar.setCurrentStateNumber(THREE);
                stateProgressBar.enableAnimationToCurrentState(true);
                stateProgressBar.checkStateCompleted(true);


            }
            else if(!orderStatus.isEmpty() &&orderStatus.equals("4"))
            {
                stateProgressBar.setCurrentStateNumber(FOUR);
                stateProgressBar.enableAnimationToCurrentState(true);
                stateProgressBar.setAllStatesCompleted(true);
                stateProgressBar.checkStateCompleted(true);

            }

        }

        private void init(View itemView) {
            img = itemView.findViewById(R.id.raw_order_detail_image);
            itemName = itemView.findViewById(R.id.raw_order_detail_itemName);
            itemPrice = itemView.findViewById(R.id.raw_order_detail_itemPrice);
            cancel = itemView.findViewById(R.id.raw_order_detail_cancelItem);
            stateProgressBar = itemView.findViewById(R.id.raw_manage_order_progress_bar_id);

        }
    }
}
