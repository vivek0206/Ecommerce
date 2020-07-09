package com.ecommerce.ecommerce.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.FOUR;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.ONE;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.THREE;
import static com.kofigyan.stateprogressbar.StateProgressBar.StateNumber.TWO;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailView> {

    private List<ProductVariation> list;
    private Context context;
    private View customView;
    LayoutInflater inflater;
    OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public OrderDetailAdapter(List<ProductVariation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_order_detail_item,parent,false);
         inflater = (LayoutInflater) parent.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        return new OrderDetailView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailView holder, int position) {
        ProductVariation item = list.get(position);
        Picasso.get().load(Uri.parse(item.getImageUrl())).into(holder.img);
        holder.itemName.setText(item.getProductName());
        holder.itemPrice.setText(item.getProductActualPrice()+"");
        holder.productName = item.getProductName().toLowerCase().trim();
    }

    @Override
    public int getItemCount() { return list.size(); }

    public void setData(List<ProductVariation> list){this.list = list;}

    public class OrderDetailView extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView itemName,itemPrice,cancel;
        private StateProgressBar stateProgressBar;
        private String[] descriptionData = {"Confirmed", "Packed", "Out For Delivery", "Delivered"};
        private String orderStatus="2";
        private FirebaseUser user;
        private DatabaseReference databaseReference;
        private String productName;


        public OrderDetailView(@NonNull View itemView) {
            super(itemView);

            init(itemView);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null)
                    {
                        onItemClickListener.onItemClick(list.get(getAdapterPosition()),1);
                        orderStatus="5";
                    }
                }
            });

            checkStatus();


        }

        private void checkStatus() {
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
            else if(orderStatus.equals("5"))
            {
                cancel.setText("Cancelled");
                cancel.setEnabled(false);
            }
        }


        private void init(View itemView) {
            img = itemView.findViewById(R.id.raw_order_detail_image);
            itemName = itemView.findViewById(R.id.raw_order_detail_itemName);
            itemPrice = itemView.findViewById(R.id.raw_order_detail_itemPrice);
            cancel = itemView.findViewById(R.id.raw_order_detail_cancelItem);
            stateProgressBar = itemView.findViewById(R.id.raw_manage_order_progress_bar_id);
            stateProgressBar.setStateDescriptionData(descriptionData);

            user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

        }
    }
}
