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
import android.widget.RadioGroup;
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
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.Models.UserInfo;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView price,shippingFee,totalAmount;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String itemPrice,shippingPrice,totalPrice;
    private Button pay_now;
    private int normalDelivery=1;
    private int onlinePayment=-1,cod=1;
    private String orderId,paymentStatus="0",paymentTransactionId="NA";
    private LoadingDialog loadingDialog;
    private RadioGroup paymentGroup,deliveryGroup;
    private String address1,address2;

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
        price.setText(getResources().getString(R.string.Rupee)+itemPrice);
        shippingFee.setText(getResources().getString(R.string.Rupee)+shippingPrice);
        totalPrice= String.valueOf(Integer.parseInt(itemPrice)+Integer.parseInt(shippingPrice));
        totalAmount.setText(getResources().getString(R.string.Rupee)+totalPrice);


        mRequestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
        MainActivity.fetchUserInfo();
        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderId = System.currentTimeMillis()+"";
                if(onlinePayment==-1 )
                {
                    Toast.makeText(getApplicationContext(),"Select Payment Type",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(onlinePayment==0)
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

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id==R.id.payment_online)
                {
                    onlinePayment=1;
//                    Toast.makeText(getApplicationContext(),"onlinePayment1",Toast.LENGTH_SHORT).show();
                }
                else if(id==R.id.payment_cod)
                {
//                    Toast.makeText(getApplicationContext(),"onlinePayment0",Toast.LENGTH_SHORT).show();

                    onlinePayment=0;
                }
            }
        });

        deliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id==R.id.payment_fast)
                {

                    if(Integer.parseInt(itemPrice)<300)
                    {

                        shippingPrice = "20";
                        shippingFee.setText(shippingPrice);
                        totalPrice= String.valueOf(Integer.parseInt(itemPrice)+Integer.parseInt(shippingPrice));
                        totalAmount.setText(totalPrice);
                    }
                    else
                    {

                        shippingPrice = "0";
                        shippingFee.setText(shippingPrice);
                    }
                    normalDelivery=0;
                }
                else if(id==R.id.payment_normal)
                {
                    if(Integer.parseInt(itemPrice)>300)
                    {
                        normalDelivery=0;
                    }
                    shippingPrice = "0";
                    shippingFee.setText(shippingPrice);
                    totalPrice= String.valueOf(Integer.parseInt(itemPrice)+Integer.parseInt(shippingPrice));
                    totalAmount.setText(totalPrice);
                    normalDelivery=1;

                }
            }
        });



    }


    private void sendNotificationAdmin(String message) {

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+"Admin");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","OrderId:"+orderId);
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


    private void UpdateQuantity(String productName,String proVarName ,final int quantity)
    {
        productName = productName.toLowerCase().trim();
        proVarName = proVarName.toLowerCase().trim();

        final DatabaseReference db=databaseReference.child(getResources().getString(R.string.ProductVariation)).child(productName).child(proVarName);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductVariation model = dataSnapshot.getValue(ProductVariation.class);
                if(model!=null)
                {
                    model.setQuantity(model.getQuantity()-quantity);
                    db.setValue(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            mainObj.put("to","/topics/"+user.getUid());
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

                    for (DataSnapshot ds2:ds.getChildren())
                    {
                        ProductVariation model = ds2.getValue(ProductVariation.class);
                        if(model!=null && model.getQuantity()>0)
                        {
                            String productName = model.getProductVariationName();
                            String pp = model.getProductName().toLowerCase().trim();
                            model.setOrderStatus("1");
                            databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderId).child(pp+productName.toLowerCase().trim()).setValue(model);
                                UpdateQuantity(model.getProductName(),model.getProductVariationName(),model.getQuantity());
                            //TODO: For Admin also

                        }
                    }
                }
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                UserOrderInfo model  = new UserOrderInfo(orderId,date,"Delivery Date",totalPrice,"1",paymentStatus,paymentTransactionId,shippingPrice,address1,address2,normalDelivery+"");
                databaseReference.child(getResources().getString(R.string.OrderInfo)).child(user.getUid()).child(orderId).setValue(model);
                databaseReference.child(getResources().getString(R.string.UserCart)).child(user.getUid()).removeValue();
                sendNotification("Your Order has been Placed with orderId:"+orderId);
                sendNotificationAdmin("Order with:"+orderId+" is placed Check it soon");
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

        fetchUserAddress();


    }

    private void fetchUserAddress() {
        databaseReference.child(getResources().getString(R.string.UserInfo)).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address1="";
                address2="";

                UserInfo model = dataSnapshot.getValue(UserInfo.class);
                if(model!=null)
                {
                    address1+=model.getDeliveryName()+", "+model.getDeliveryPhone();
                    address2+=model.getDeliveryFlat()+", "+model.getDeliveryArea()+", "+model.getDeliveryLandmark()+","+model.getDeliveryCity()+","+model.getDeliveryState()+","+model.getDeliveryPinCode();

                }
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
                    shippingPrice = "20";
                    shippingFee.setText(shippingPrice);
                }
                else
                {

                    shippingPrice = "0";
                    shippingFee.setText(shippingPrice);
                }
                break;

            case R.id.payment_normal:
                if(checked)
                {

                    shippingPrice = "0";
                    shippingFee.setText(shippingPrice);
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
                    Toast.makeText(getApplicationContext(),"fast ddddddDelivery checked",Toast.LENGTH_SHORT);

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
                    Toast.makeText(getApplicationContext(),"fast Deliffffvery checked",Toast.LENGTH_SHORT);

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
        paymentGroup = findViewById(R.id.payment_group_payment);
        deliveryGroup = findViewById(R.id.payment_group_delivery);
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
            options.put("prefill.contact",MainActivity.userPhone);
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
        paymentTransactionId=s;
        orderDone();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}