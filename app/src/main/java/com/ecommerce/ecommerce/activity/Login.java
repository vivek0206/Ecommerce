package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText phone,pswd;
    private Button login_btn;
    private DatabaseReference databaseReference;
    String userPhone,userPswd;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verigyUser();
            }
        });



    }

    private void verigyUser() {
        userPhone = phone.getText().toString().trim();
        userPswd = pswd.getText().toString().trim();
        databaseReference.child(getResources().getString(R.string.UserInfo)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    UserInfo model = ds.getValue(UserInfo.class);
                    if(model!=null)
                    {
                        if(model.getUserPhone().equals(userPhone))
                        {
                            flag=1;
                            if(userPswd.equals(model.getUserPswd()))
                            {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
                if(flag==0)
                {
                    Toast.makeText(getApplicationContext(),"Invalidate details",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        phone = findViewById(R.id.login_phone);
        pswd = findViewById(R.id.login_pswd);
        login_btn = findViewById(R.id.login_btn);
    }


}