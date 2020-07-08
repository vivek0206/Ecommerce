package com.ecommerce.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ecommerce.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    FirebaseUser usesr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        usesr = FirebaseAuth.getInstance().getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(usesr==null){
                    Intent mainIntent = new Intent(SplashScreenActivity.this, SignUp.class);
                    startActivity(mainIntent);
                    SplashScreenActivity.this.finish();

                }
                else
                {
                    Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}