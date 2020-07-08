package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ecommerce.ecommerce.LoadingDialog;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView price,shippingFee,totalAmount;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String itemPrice,shippingPrice,totalPrice;
    private Button pay_now;
    private int fastDelivery=0,normalDelivery=0;
    private int onlinePayment=1,cod=0;
    private String orderId,paymentStatus="0";
    private LoadingDialog loadingDialog;


    private RequestQueue mRequestQueue;
    private String URL="https://fcm.googleapis.com/fcm/send";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Payment Details");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Checkout.preload(getBaseContext());
        init();
        Intent intent = getIntent();
        itemPrice = intent.getStringExtra("price");
        shippingPrice = intent.getStringExtra("shippingPrice");
        price.setText(itemPrice);
        shippingFee.setText(shippingPrice);
        totalPrice= String.valueOf(Integer.parseInt(itemPrice)+Integer.parseInt(shippingPrice));
        totalAmount.setText(totalPrice);


        mRequestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("order");
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
                        if(MainActivity.staticModel!=null)
                        {
                            startPayment(MainActivity.userNam,orderId,orderId,"https://s3.amazonaws.com/rzp-mobile/images/rzp.png",(100*Integer.parseInt(totalPrice))+"");
                        }
                        else
                        {
                            startPayment("Tanish", "Tanish", "0723237826", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png", "5000");
                        }
                    }
                }
            }
        });

    }

    private void sendNotification(String message) {
  /*      //json object
        {
            "to": "topics/topic name"
                notification:   {
                    title: "some titlle"
                     body:  "some body"
                }
        }*/

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+"order");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Name");
            notificationObj.put("body",message);

            JSONObject extraData = new JSONObject();
            extraData.put("category","Chat");
            extraData.put("peopleId","PeopleId");
            extraData.put("routineId","routineId");

            mainObj.put("notification",notificationObj);
            mainObj.put("data",extraData);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(),"Process",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Process",Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAAqZKVDg:APA91bEeSXlfcNTvt8364bTbWD9VMXcv5vb1dWB4Tvbpeh26CtVczUnDA3jGvQeVTG_BY_oW-3ea53oqcALaBoq7ETRlO1khMctmcLLQrcnQPgU4DRC87OeEf-sGUWWGXUdJqvPmxswQ");
                    return header;
                }
            };

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                UserOrderInfo model  = new UserOrderInfo(orderId,"Order Date","Delivery Date",totalPrice,"1",paymentStatus);
                databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid()).child(orderId).setValue(model);
                databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).removeValue();
                sendNotification("done");
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
        loadingDialog = new LoadingDialog(this);
        price = findViewById(R.id.payment_item_total_price);
        shippingFee = findViewById(R.id.payment_item_shipping_charges);
        totalAmount = findViewById(R.id.payment_amount_total);
        pay_now = findViewById(R.id.payment_payNow);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void startPayment(String merchant,String desc,String order,String imageUrl,String amount) {
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", merchant);
            options.put("description", desc);
            options.put("image", imageUrl);
         //   options.put("order_id", orderId);
            options.put("currency", "INR");
            options.put("amount", amount);
            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            Log.e("mmmm", "Error in starting Razorpay Checkout"+e.getMessage(), e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        paymentStatus="1";
        orderDone();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}