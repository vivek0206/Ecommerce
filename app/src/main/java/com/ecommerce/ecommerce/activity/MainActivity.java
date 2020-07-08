package com.ecommerce.ecommerce.activity;

import android.content.Context;
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

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private int hot_number = 0;
    private TextView ui_hot = null;
    public static String userNam,userPhone,userPswd,userImageUrl;

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
        OfflineCapabilities(getApplicationContext());
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
            databaseReference.child(getString(R.string.UserInfo)).child(user.getUid()).keepSynced(true);
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

    public static void OfflineCapabilities(final Context context)
    {


        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
        presenceRef.onDisconnect().setValue("I disconnected!");
        presenceRef.onDisconnect().removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, @NonNull DatabaseReference reference) {
                if (error != null) {
                    Log.d("TAG", "could not establish onDisconnect event:" + error.getMessage());
                }
            }
        });
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toast.makeText(context,"Connected",Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "connected");
                } else {
                    Toast.makeText(context,"Not Connected",Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "not connected");
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Listener was cancelled");
            }
        });

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

        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.action_bar_cart_notification);
        RelativeLayout notifCount = (RelativeLayout)item.getActionView();
        TextView tv = (TextView) notifCount.findViewById(R.id.cart_no);
        setupBadge(tv);
//        tv.setText("12");


        return true;
    }
    private void setupBadge(final TextView tv) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("");
        databaseReference.child("UserCart").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv.setText(dataSnapshot.getChildrenCount()+"");
                Log.d("TAG",dataSnapshot.getChildrenCount()+"vvvvvvvvv");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.action_cart)
        {
            Intent intent = new Intent(MainActivity.this,CartActivity.class);
            startActivity(intent);
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem cart = menu.findItem(R.id.action_cart);

        SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();

                return false;
            }
        });


        return true;

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
                if(staticModel!=null)
                {
                    userNam = staticModel.getUserName();
                    userPhone = staticModel.getUserPhone();
                    userPswd = staticModel.getUserPswd();
                    userImageUrl = staticModel.getUserImageUrl();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
