package com.ecommerce.ecommerce.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.ManageOrderAdapter;
import com.ecommerce.ecommerce.ui.CategoryFragment;
import com.ecommerce.ecommerce.ui.HomeFragment;
import com.ecommerce.ecommerce.ui.OrderFragment;
import com.ecommerce.ecommerce.ui.UserAccount;
import com.ecommerce.ecommerce.ui.WishlistFragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawer;
    TextView userName;
    ImageView userImage;
    private FirebaseAuth auth;
    public static UserInfo staticModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.drawer_layout,new HomeFragment()).commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        setFragment(new HomeFragment());
                        Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_category:
                        setFragment(new CategoryFragment());
                        Toast.makeText(getApplicationContext(),"catego",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_orders:

                        Intent intent = new Intent(MainActivity.this, ManageOrderActivity.class);
                        startActivity(intent);
                    //    setFragment(new OrderFragment());
                        Toast.makeText(getApplicationContext(),"order",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_wishlist:
                        setFragment(new WishlistFragment());
                        Toast.makeText(getApplicationContext(),"wishlist",Toast.LENGTH_SHORT).show();
                        break;

//                    case R.id.nav_account:
//                        setFragment(new UserAccount());
//                        Toast.makeText(getApplicationContext(),"Your Account",Toast.LENGTH_SHORT).show();

                }

                return false;
            }
        });

        View headerView= navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.userName);
        userImage = headerView.findViewById(R.id.userImage);
        if(auth.getCurrentUser()!=null)
        {
            fetchUserInfo();
            FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.UserInfo));
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserInfo model = dataSnapshot.getValue(UserInfo.class);
                    if(model!=null)
                    {
                        if(!model.getUserName().isEmpty())
                        {
                            userName.setText(model.getUserName());
                        }
                        if(!model.getUserImageUrl().isEmpty())
                        {
                            Picasso.get().load(Uri.parse(model.getUserImageUrl())).into(userImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editPersonalInfo();
                }
            });

            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editPersonalInfo();
                }
            });

        }


    }

    private void editPersonalInfo() {
        Intent intent = new Intent(MainActivity.this, PersonalInfo.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.drawer_layout,fragment);
        fragmentTransaction.commit();
        drawer.closeDrawers();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setFragment(new HomeFragment());
            Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_category) {
            setFragment(new CategoryFragment());
            Toast.makeText(getApplicationContext(),"category",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_orders) {
            setFragment(new OrderFragment());
            Toast.makeText(getApplicationContext(),"orders",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_wishlist) {
            setFragment(new WishlistFragment());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void fetchUserInfo()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("");
        databaseReference.child("UserInfo").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staticModel = dataSnapshot.getValue(UserInfo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
