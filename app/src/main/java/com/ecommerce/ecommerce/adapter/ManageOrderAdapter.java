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
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.Models.UserOrderInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.OrderDetailActivity;
import com.ecommerce.ecommerce.object.Product;
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

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
    public void onBindViewHolder(@NonNull final ManageOrderItemView holder, int position) {

        UserOrderInfo orderModel = list.get(position);
        holder.orderId.setText(orderModel.getOrderId());
        holder.orderDate.setText(orderModel.getOrderDate());
        holder.totalPrice.setText(orderModel.getTotalPrice());
        holder.orderIdString = orderModel.getOrderId();
        holder.orderStatus = orderModel.getStatus();
        final List<ProductVariation> nestedList = new ArrayList<>();

        final NestedManageOrderAdapter adapter = new NestedManageOrderAdapter(nestedList,context,holder.orderIdString);
        holder.recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        nestedList.clear();

        databaseReference.child(context.getResources().getString(R.string.UserOrder)).child(user.getUid()).child(holder.orderIdString)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nestedList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            ProductVariation model = ds.getValue(ProductVariation.class);
                            if(model!=null)
                            {
                                nestedList.add(0,model);
                            }
                        }
                        Log.d("NestedList :",nestedList.size()+"");
                        adapter.setData(nestedList);
                        holder.recyclerView.setLayoutManager(layoutManager);
                        holder.recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        private RecyclerView recyclerView;
        private String orderIdString,orderStatus="2";


        public ManageOrderItemView(@NonNull View itemView) {
            super(itemView);

            init(itemView);



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
            deliveryDate = itemView.findViewById(R.id.raw_manage_order_DeliveryDate);

            recyclerView = itemView.findViewById(R.id.raw_manage_order_recyclerView);



        }

    }

}
