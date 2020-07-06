package com.ecommerce.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.R;


public class SignUp extends AppCompatActivity {

    private EditText name,phoneNo,pswd,cnfPswd;
    private Button signup;
    private TextView login;

    String userName,userPhone,userPswd,userCnf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });



    }


    private void RegisterUser() {
        userName = name.getText().toString();
        userPhone = phoneNo.getText().toString();
        userPswd = pswd.getText().toString();
        userCnf = cnfPswd.getText().toString();

        if(userName.isEmpty() || userPhone.isEmpty() || userPswd.isEmpty() || userCnf.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Invalidate Input",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!userPswd.equals(userCnf) || userPswd.length()<6)
            {
                Toast.makeText(getApplicationContext(),"Either password doesnt match or password lenght is less than 6",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(SignUp.this, verifyPhone.class);
                intent.putExtra("phone",userPhone);
                intent.putExtra("password",userPswd);
                intent.putExtra("name",userName);
                startActivity(intent);
            }
        }
    }


    private void init() {
        name = findViewById(R.id.signup_yourname);
        phoneNo = findViewById(R.id.signup_phone);
        pswd = findViewById(R.id.signup_pswd);
        signup = findViewById(R.id.signup_btn);
        cnfPswd = findViewById(R.id.signup_cnfpswd);
        login = findViewById(R.id.signup_login);
    }



}