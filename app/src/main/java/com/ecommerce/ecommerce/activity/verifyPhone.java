package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class verifyPhone extends AppCompatActivity {

    private EditText otp;
    private Button verify,resend;
    private TextView phoneNoTv;
    private String string_otp,mobile,password,name;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private DatabaseReference databaseReference;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        init();
        Intent intent = getIntent();
        mobile = intent.getStringExtra("phone");
        Toast.makeText(getApplicationContext(),mobile,Toast.LENGTH_SHORT).show();
//        password = intent.getStringExtra("password");
//        name = intent.getStringExtra("name");
        phoneNoTv.setText("+91"+mobile);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(mobile);
                Toast.makeText(getApplicationContext(),"Verification Code Sent",Toast.LENGTH_SHORT).show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendVerificationCode(mobile);
            }
        },30);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string_otp = otp.getText().toString().trim();
                if (string_otp.isEmpty() || string_otp.length() < 6) {
                    otp.setError("Enter valid code");
                    otp.requestFocus();
                    return;
                }
                verifyVerificationCode(string_otp);

            }
        });
    }

    private void sendVerificationCode(String mobile) {
        loadingDialog.startLoadingDialog();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        loadingDialog.DismissDialog();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(verifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user

        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loadingDialog.startLoadingDialog();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(verifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"checking",Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            Toast.makeText(getApplicationContext(),"success to me",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            databaseReference.child(getResources().getString(R.string.UserInfo)).child(user.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            loadingDialog.DismissDialog();
                                            UserInfo model = dataSnapshot.getValue(UserInfo.class);
                                            if(model==null)
                                            {
                                                Intent intent = new Intent(verifyPhone.this, SignUp.class);
                                                intent.putExtra("phone",mobile);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Intent intent = new Intent(verifyPhone.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        } else {
                            Toast.makeText(getApplicationContext(),"Resend Again",Toast.LENGTH_SHORT).show();
                            loadingDialog.DismissDialog();
                        }
                    }
                });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        resend = findViewById(R.id.otp_resend);
        phoneNoTv=findViewById(R.id.phone_No);
        otp = findViewById(R.id.otp_input);
        verify = findViewById(R.id.otp_verify);
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        loadingDialog = new LoadingDialog(this);

    }


}