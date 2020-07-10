package com.ecommerce.ecommerce.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ecommerce.ecommerce.Models.SearchModel;
import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.SearchAdapter;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

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
    private DatabaseReference databaseReference;

    private List<SearchModel> searchList;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager2;
    private RecyclerView searchRecyclerView;
    int flag=2;
    private MenuItem search;
    private SearchView searchView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.drawer_layout,new HomeFragment()).commit();
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        searchList = new ArrayList<>();
        searchRecyclerView = findViewById(R.id.mainActivity_searchList_recyclerView);
        searchAdapter = new SearchAdapter(searchList,getBaseContext());
        searchRecyclerView.setLayoutManager(layoutManager2);
        searchRecyclerView.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        OfflineCapabilities(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        toolbar.setTitle("Ecommerce");
                        setFragment(new HomeFragment(),"not home");
                        Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_category:
                        toolbar.setTitle("Shop by Category");
                        setFragment(new CategoryFragment(),"not home");
//                        Toast.makeText(getApplicationContext(),"catego",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_orders:
                        Intent intent = new Intent(MainActivity.this, ManageOrderActivity.class);
                        startActivity(intent);
                    //    setFragment(new OrderFragment());
//                        Toast.makeText(getApplicationContext(),"order",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_wishlist:
                        toolbar.setTitle("Wish List");
                        setFragment(new WishlistFragment(),"not home");
//                        Toast.makeText(getApplicationContext(),"wishlist",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_account:
                        toolbar.setTitle("User Account");
                        setFragment(new UserAccount(),"not home");
//                        Toast.makeText(getApplicationContext(),"User Account",Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
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

        search = menu.findItem(R.id.action_search);
        MenuItem cart = menu.findItem(R.id.action_cart);

        searchView = (SearchView) search.getActionView();
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecyclerView.setVisibility(View.VISIBLE);

                flag=1;
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                flag=1;
                searchRecyclerView.setVisibility(View.VISIBLE);

                searchList.clear();
                Query firebaseQuery = databaseReference.child(getResources().getString(R.string.Search)).orderByKey().startAt(newText.toLowerCase().trim()).endAt(newText.toLowerCase().trim()+"\uf8ff");
                firebaseQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {

                            SearchModel model = ds.getValue(SearchModel.class);
                            searchList.add(model);
                        }
                        searchAdapter.setData(searchList);
                        searchRecyclerView.setLayoutManager(layoutManager2);
                        searchRecyclerView.setAdapter(searchAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();


                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search.collapseActionView();
                searchView.setQuery("",false);
                searchView.onActionViewCollapsed();
                searchList.clear();
                flag=2;
                searchRecyclerView.setVisibility(View.GONE);
                return true;
            }
        });


        return true;

    }

    @Override
    public void onBackPressed() {

        toolbar.setTitle("Ecommerce");
        if(flag==1)
        {
            search.collapseActionView();
            searchView.setQuery("",false);
            searchView.onActionViewCollapsed();
            searchList.clear();
            flag=2;
            searchRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            int p = 0;
            p = tellFragments();
            if(p==0)
                super.onBackPressed();

        }

    }

    private int tellFragments(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments){
            if(f != null && (f instanceof WishlistFragment)) {
                ((WishlistFragment) f).onBackPressed();
            }
        }
        return 0;
    }

    public void setFragment(Fragment fragment,String name)
    {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.drawer_layout,fragment);

        final int count = fragmentManager.getBackStackEntryCount();
        if( name.equals( "not home") ) {
            fragmentTransaction.addToBackStack(name);
        }


        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if( fragmentManager.getBackStackEntryCount() <= count){
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack("not home", POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        });

        fragmentTransaction.commit();
        drawer.closeDrawers();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setFragment(new HomeFragment(),"home");
            Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_category) {
            setFragment(new CategoryFragment(),"not home");
            Toast.makeText(getApplicationContext(),"category",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_orders) {
            setFragment(new OrderFragment(),"not home");
            Toast.makeText(getApplicationContext(),"orders",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_wishlist) {
            setFragment(new WishlistFragment(),"not home");
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

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserInfo();
        if(userImageUrl!=null)
        {
            Picasso.get().load(Uri.parse(userImageUrl)).placeholder(getResources().getDrawable(R.drawable.placeholder,null)).into(userImage);
        }
    }
}
