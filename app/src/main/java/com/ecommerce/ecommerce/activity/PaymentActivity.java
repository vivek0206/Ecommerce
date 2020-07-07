package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Models.OrderInfoModel;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView price,shippingFee,totalAmount;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String itemPrice,shippingPrice,totalPrice;
    private Button pay_now;
    private int fastDelivery=0,normalDelivery=0;
    private int onlinePayment=1,cod=0;
    private String orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());

        init();
        Intent intent = getIntent();
        itemPrice = intent.getStringExtra("price");
        shippingPrice = intent.getStringExtra("shippingPrice");
        price.setText(itemPrice);
        shippingFee.setText(shippingPrice);
        totalPrice= String.valueOf(Integer.parseInt(itemPrice)+Integer.parseInt(shippingPrice));
        totalAmount.setText(totalPrice);

        MainActivity.fetchUserInfo();
        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderId = System.currentTimeMillis()+"";
                if(onlinePayment==0 && cod==0)
                {
                    Toast.makeText(getApplicationContext(),"Select Payment Type",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(cod==1)
                    {
                        orderDone();
                    }
                    else
                    {
                        orderDone();
                        //startPayment();
                        //startPayment("Tanish","Order ",orderId,"https://image.shutterstock.com/image-photo/butterfly-grass-on-meadow-night-260nw-1111729556.jpg",Integer.parseInt(totalPrice)*100+"");
                    }
                }
            }
        });

    }

    private void orderDone() {
        databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Product model = ds.getValue(Product.class);
                    if(model!=null)
                    {
                        String productName = model.getProductName();
                        databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderId).child(productName).setValue(model);


                        //TODO: For Admin also


                    }
                }
                UserOrderInfo model  = new UserOrderInfo(orderId,"Order Date","Delivery Date",totalPrice,"Order confirmed");
                databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid()).child(orderId).setValue(model);
                databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).removeValue();
                Toast.makeText(getApplicationContext(),"Order Done",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this,OrderConfirmActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void ChooseDeliveryType(View view)
    {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            case R.id.payment_fast:
                if(checked)
                {
                    Toast.makeText(getApplicationContext(),"fast DElivery checked",Toast.LENGTH_SHORT);
                    fastDelivery=1;
                }
                else
                {

                    Toast.makeText(getApplicationContext(),"fast DElivery unchecked",Toast.LENGTH_SHORT);
                    fastDelivery=0;
                }
                break;

            case R.id.payment_normal:
                if(checked)
                {
                    normalDelivery=1;
                }
                else
                {
                    normalDelivery=0;
                }
                break;
        }
    }

    public void ChoosePaymentType(View view)
    {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            case R.id.payment_online:
                if(checked)
                {
                    onlinePayment=1;
                }
                else
                {
                    onlinePayment=0;
                }
                break;

            case R.id.payment_cod:
                if(checked)
                {
                    cod=1;
                }
                else
                {
                    cod=0;
                }
                break;
        }
    }

    private void init() {
        price = findViewById(R.id.payment_item_total_price);
        shippingFee = findViewById(R.id.payment_item_shipping_charges);
        totalAmount = findViewById(R.id.payment_amount_total);
        pay_now = findViewById(R.id.payment_payNow);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_Tu9mmCQr4y3e3R");

        /**
         * Set your logo here
         */
        checkout.setImage(R.color.orange);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Merchant Name");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", "500");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("pop op ", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        orderDone();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}