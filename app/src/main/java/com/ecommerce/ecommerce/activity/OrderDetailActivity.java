package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.OrderDetailAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private List<ProductVariation> list;
    private LinearLayoutManager layoutManager;
    private OrderDetailAdapter adapter;
    private String orderId;
    private LoadingDialog loadingDialog;


    private RequestQueue mRequestQueue;
    private String URL="https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Order Details");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
        loadingDialog.startLoadingDialog();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        fetchOrder();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ProductVariation model, int type) {
                OnCancel(model);
            }

            @Override
            public void onItemClick() {

            }

            @Override
            public void onItemClick(ProductVariation model) {

            }
        });


    }

    private void OnCancel(final ProductVariation model) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View customView = getLayoutInflater().inflate(R.layout.raw_cancel_alert_dialog,null);
        builder.setView(customView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        Button not_cancel = customView.findViewById(R.id.raw_alert_cancel_dont_cancel);
        Button cancelled = customView.findViewById(R.id.raw_alert_cancel_cancelled);

        not_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        final String productName = model.getProductName().toLowerCase().trim();
        cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setOrderStatus("5");
                databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderId).child(productName).setValue(model);
                alertDialog.cancel();
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                sendNotification("Order has been Cancelled");
                sendNotificationAdmin("Order has been Cancelled");
            }
        });


        mRequestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());


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


    private void fetchOrder() {
        databaseReference.child(getResources().getString(R.string.UserOrder)).child(user.getUid()).child(orderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            Log.e("pop oop::",ds.getValue()+"\n");
                            ProductVariation model = ds.getValue(ProductVariation.class);
                            list.add(0,model);
                        }
                        loadingDialog.DismissDialog();
                        adapter.setData(list);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child(getString(R.string.UserOrder)).keepSynced(true);


    }

    private void init() {
        MainActivity.OfflineCapabilities(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        recyclerView = findViewById(R.id.order_detail_recyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        adapter = new OrderDetailAdapter(list,getBaseContext());
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setHasFixedSize(true);
    }
}