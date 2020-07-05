package com.ecommerce.ecommerce;

import android.os.Bundle;

import com.ecommerce.ecommerce.ui.CategoryFragment;
import com.ecommerce.ecommerce.ui.HomeFragment;
import com.ecommerce.ecommerce.ui.OrderFragment;
import com.ecommerce.ecommerce.ui.WishlistFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.Toast;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.drawer_layout,new HomeFragment()).commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
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
                        setFragment(new OrderFragment());
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
