package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.OrderInfoModel;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.Models.RedeemAward;
import com.ecommerce.ecommerce.Models.UserOrderInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OrderIndividualActivity extends AppCompatActivity {

    private TextView orderId,orderDate,expectedDeliveryDate,totalPrice,itemName,itemPrice,cancelled,address1,address2,shippingCharge,amountPrice,redeem;
    private ImageView itemImage;
    private CardView card;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String orderIdString,productName;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_individual);

        init();
        Intent intent = getIntent();
        orderIdString = intent.getStringExtra("orderId");
        productName = intent.getStringExtra("productName");

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EarnScartch();
            }
        });


        MainActivity.fetchUserPoints();

        fetchOrderInfo();

        fetchOrderProductInfo();

    }

    private void checkForScratch()
    {
        databaseReference.child(getResources().getString(R.string.OrderRewards)).child(user.getUid()).child(orderIdString).child(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingDialog.startLoadingDialog();
                RedeemAward model = dataSnapshot.getValue(RedeemAward.class);
                if(model!=null && model instanceof RedeemAward)
                {
                    if(model.getFlag().equals("1"))
                    {
                        redeem.setEnabled(false);
                        redeem.setText("Redeemed ");
                    }
                }
                loadingDialog.DismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void EarnScartch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View customView = getLayoutInflater().inflate(R.layout.raw_scratch_view,null);
        builder.setView(customView);

        ScratchView scratchView = customView.findViewById(R.id.raw_order_detail_scratchView);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                Toast.makeText(getApplicationContext(), "You got Rs 20", Toast.LENGTH_LONG).show();
                RedeemAward model = new RedeemAward(orderIdString,user.getUid(),"20","1");
                databaseReference.child(getResources().getString(R.string.OrderRewards)).child(user.getUid()).child(orderIdString).child(productName).setValue(model);
                int points = Integer.parseInt(model.getPrize());
                MainActivity.staticUserPointModel.setPoints(points+MainActivity.staticUserPointModel.getPoints());
                MainActivity.updateUserPoints();
                redeem.setEnabled(false);
                redeem.setText("Redeemed");
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if(percent>=0.3f)
                {
                    scratchView.reveal();
                }
            }
        });


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private void fetchOrderProductInfo() {
        databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderIdString).child(productName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProductVariation model = dataSnapshot.getValue(ProductVariation.class);
                        if(model!=null)
                        {
                            itemName.setText(model.getProductName());
                            itemPrice.setText(model.getProductSalePrice()+"");

                            String orderStatus = model.getOrderStatus();
                            if(orderStatus.equals("5"))
                            {
                                cancelled.setText("Cancelled");
                                redeem.setEnabled(false);
                                redeem.setVisibility(View.GONE);
                            }
                            else if(orderStatus.equals("4"))
                            {
                                cancelled.setText("Delivered");

                            }else
                            {
                                cancelled.setText("In Transit");
                                redeem.setEnabled(false);
                                redeem.setVisibility(View.GONE);

                            }
                            checkForScratch();
                            Picasso.get().load(Uri.parse(model.getImageUrl())).into(itemImage);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    private void fetchOrderInfo() {
        databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid()).child(orderIdString)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserOrderInfo model = dataSnapshot.getValue(UserOrderInfo.class);

                        if(model!=null)
                        {
                            orderId.setText(model.getOrderId());
                            orderDate.setText(model.getOrderDate());
                            expectedDeliveryDate.setText(model.getDeliveryDate());
                            totalPrice.setText(model.getTotalPrice());
                            address1.setText(model.getAddress1());
                            address2.setText(model.getAddress2());
                            shippingCharge.setText(model.getShippingCharges());

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void init() {
        orderId=findViewById(R.id.raw_order_individual_orderId);
        orderDate=findViewById(R.id.raw_order_individual_orderDate);
        expectedDeliveryDate=findViewById(R.id.raw_order_individual_DeliveryDate);
        totalPrice=findViewById(R.id.raw_order_individual_orderPrice);
        itemName=findViewById(R.id.raw_order_individual_itemName);
        itemPrice=findViewById(R.id.raw_order_individual_itemPrice);
        cancelled=findViewById(R.id.raw_order_individual_cancelItem);
        address1=findViewById(R.id.order_individual_address1);
        address2=findViewById(R.id.order_individual_address2);
        shippingCharge=findViewById(R.id.order_individual_item_shipping_charges);
        amountPrice=findViewById(R.id.order_individual_item_total_price);
        itemImage=findViewById(R.id.raw_order_individual_image);
        card=findViewById(R.id.raw_order_individual_card);
        redeem = findViewById(R.id.activity_order_individual_redeem);
        loadingDialog = new LoadingDialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
}