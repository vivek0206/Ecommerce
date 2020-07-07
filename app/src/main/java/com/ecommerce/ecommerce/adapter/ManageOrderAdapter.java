package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Models.OrderInfoModel;
import com.ecommerce.ecommerce.Models.UserOrderInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.OrderDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar.StateNumber;

import java.util.ArrayList;
import java.util.List;

import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.FOUR;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.ONE;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.THREE;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.TWO;


public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.ManageOrderItemView> {

    private List<UserOrderInfo> list;
    private Context context;

    public ManageOrderAdapter(List<UserOrderInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ManageOrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_manage_order_item,parent,false);
        return new ManageOrderItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageOrderItemView holder, int position) {

        UserOrderInfo orderModel = list.get(position);
        holder.orderId.setText(orderModel.getOrderId());
        holder.orderDate.setText(orderModel.getOrderDate());
        holder.totalPrice.setText(orderModel.getTotalPrice());
        holder.orderIdString = orderModel.getOrderId();
        holder.orderStatus = orderModel.getStatus();
        Log.d("pop pop pop ",orderModel.getStatus()+" oo ");

    }

    public void setData(List<UserOrderInfo> list)
    {
        this.list=list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ManageOrderItemView extends RecyclerView.ViewHolder{

        private TextView orderId,orderDate,totalPrice,deliveryDate;
        private FirebaseUser user;
        private DatabaseReference databaseReference;
        private String orderIdString,orderStatus="2";
        private StateProgressBar stateProgressBar;
        private String[] descriptionData = {"Confirmed", "Packed", "Out For Delivery", "Delivered"};




        public ManageOrderItemView(@NonNull View itemView) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("orderId",orderIdString);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });


        }



        private void init(View itemView) {

            orderId=itemView.findViewById(R.id.raw_manage_order_orderId);
            orderDate=itemView.findViewById(R.id.raw_manage_order_orderDate);
            totalPrice=itemView.findViewById(R.id.raw_manage_order_orderPrice);
            stateProgressBar = itemView.findViewById(R.id.raw_manage_order_progress_bar_id);
            deliveryDate = itemView.findViewById(R.id.raw_manage_order_DeliveryDate);
            user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

        }

    }

}
