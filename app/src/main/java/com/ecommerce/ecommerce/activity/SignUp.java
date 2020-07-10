package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    private EditText name,pswd,cnfPswd;
    private TextView phoneNo;
    private Button signup;
    private TextView login;
    String mobileNo;

    String userName,userPhone,userPswd,userCnf;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        mobileNo = intent.getStringExtra("phone");
        init();
        phoneNo.setText(mobileNo);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SignUp.this, Login.class);
//                startActivity(intent);
//            }
//        });


    }


    private void RegisterUser() {
        userName = name.getText().toString();
        userPhone = mobileNo;
//        userPswd = pswd.getText().toString();
//        userCnf = cnfPswd.getText().toString();

        if(userName.isEmpty() || userPhone.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Invalidate Input",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth=FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            UserInfo modelU = new UserInfo(userName,userPhone,"xyz","");
            Toast.makeText(getApplicationContext(),user.getUid().toString(),Toast.LENGTH_SHORT).show();
            databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("UserInfo").child(user.getUid()).setValue(modelU)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                                String message = "Welcome Your Account is Registered Successfully";
//                                Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                                snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) { }
//                                });
//                                snackbar.show();
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            }else{
//                                String message = "Something went wrong";
//                                Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                                snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) { }
//                                });
//                                snackbar.show();

                            }
                        }
                    });
        }
    }


    private void init() {
        name = findViewById(R.id.signup_yourname);
        phoneNo = findViewById(R.id.signup_phone);
//        pswd = findViewById(R.id.signup_pswd);
        signup = findViewById(R.id.signup_btn);
//        cnfPswd = findViewById(R.id.signup_cnfpswd);
//        login = findViewById(R.id.signup_login);
    }



}