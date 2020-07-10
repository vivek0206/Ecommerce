package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryAddress extends AppCompatActivity {


    private EditText name,phone,flat,area,landmark,state,city,pinCode;
    private Button button;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private String Sname,Sphone,Sflat,Sarea,Slandmark,Sstate,Scity,Spincode;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        init();
        loadingDialog = new LoadingDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                updateAddress();
            }
        });

        fetchAddress();

    }

    private void updateAddress() {

        Sname = name.getText().toString();
        Sphone = phone.getText().toString();
        Sflat = flat.getText().toString();
        Sarea = area.getText().toString();
        Slandmark = landmark.getText().toString();
        Sstate = state.getText().toString();
        Scity = city.getText().toString();
        Spincode = pinCode.getText().toString();

        if(Sname.isEmpty() || Sphone.isEmpty()|| Sarea.isEmpty()|| Slandmark.isEmpty()|| Sstate.isEmpty()|| Scity.isEmpty()|| Spincode.isEmpty() )
        {
            Toast.makeText(getApplicationContext(),"Fill All Fields ",Toast.LENGTH_SHORT).show();
            loadingDialog.DismissDialog();
        }
        else
        {
            UserInfo model = new UserInfo(Sname,Sphone,Sflat,Sarea,Slandmark,Sstate,Scity,Spincode);
            databaseReference.child(getResources().getString(R.string.Address)).child(user.getUid()).setValue(model);
            loadingDialog.DismissDialog();
            Toast.makeText(getApplicationContext(),"Updated ",Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAddress() {
        if(user!=null)
        {
            databaseReference.child(getResources().getString(R.string.Address)).child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserInfo model = dataSnapshot.getValue(UserInfo.class);
                            if(model!=null)
                            {
                                name.setText(model.getDeliveryName());
                                phone.setText(model.getDeliveryPhone());
                                flat.setText(model.getDeliveryFlat());
                                area.setText(model.getDeliveryArea());
                                landmark.setText(model.getDeliveryLandmark());
                                state.setText(model.getDeliveryState());
                                city.setText(model.getDeliveryCity());
                                pinCode.setText(model.getDeliveryPinCode());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }

    }

    private void init() {
        name = findViewById(R.id.delivery_userName);
        phone = findViewById(R.id.delivery_phone);
        flat = findViewById(R.id.delivery_flat);
        area =findViewById(R.id.deliver_area);
        landmark = findViewById(R.id.delivery_landmark);
        state = findViewById(R.id.delivery_state);
        city = findViewById(R.id.delivery_city);
        pinCode = findViewById(R.id.delivery_pincode);
        button = findViewById(R.id.deliveru_update_address);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("");
    }



}