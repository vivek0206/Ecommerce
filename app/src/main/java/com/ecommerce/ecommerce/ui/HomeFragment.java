package com.ecommerce.ecommerce.ui;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.SubCategoryAdapter;
import com.ecommerce.ecommerce.adapter.SlidingImage_Adapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView1;
    private SubCategoryAdapter subCategoryAdpater;
    private CategoryAdapter categoryAdapter;

    ViewPager mViewPager;
    private Timer timer;
    private int image_pos=0;
    private List<Integer> imageList=new ArrayList<>();
    private List<String> catList=new ArrayList<>();
    private LinearLayout dotLayout;
    private int dot_pos=0;
    FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();

        mViewPager = view.findViewById(R.id.imageslider_pager);
        dotLayout=view.findViewById(R.id.dotContainer);
        prepareSlide();
        SlidingImage_Adapter adapterView = new SlidingImage_Adapter(imageList,view.getContext());
        mViewPager.setAdapter(adapterView);
        preDot(image_pos);
        createSlider();
        getCatData(view);



        mRecyclerView1=view.findViewById(R.id.recycler_view1);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),1);
//        mRecyclerView1.setLayoutManager(gridLayoutManager);

//        subCategoryAdpater =new SubCategoryAdapter(view.getContext(),pList1);
//        mRecyclerView1.setAdapter(homeCategory_adpater);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
//        linearLayoutManager.setOrientation();
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Log.d(TAG, "Value is:starttt ");



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(image_pos>imageList.size())
                    image_pos=0;
                preDot(image_pos);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }
    private void prepareSlide(){
//        private int[] sliderImageId = new int[]{
//                R.drawable.s1,R.drawable.s2,R.drawable.s3,
//        };
        imageList.add(R.drawable.s1);
        imageList.add(R.drawable.s2);
        imageList.add(R.drawable.s3);

    }
    private void preDot(int currImage_pos){
        if(dotLayout.getChildCount()>0){
            dotLayout.removeAllViews();
        }
        ImageView dots[]=new ImageView[imageList.size()];
        for(int i=0;i<imageList.size();i++){
            if(getContext()!=null)
            {
                dots[i]=new ImageView(this.getContext());
                if(i==currImage_pos)
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this.getContext(),R.drawable.active_dot));
                else dots[i].setImageDrawable(ContextCompat.getDrawable(this.getContext(),R.drawable.inactive_dot));

                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(4,0,4,0);
                dotLayout.addView(dots[i],layoutParams);
            }

        }

    }
    private void createSlider(){
        final android.os.Handler handler=new Handler();
        final Runnable sliderRunnable=new Runnable() {
            @Override
            public void run() {
                if(image_pos==imageList.size()){
                    image_pos=0;
                }
                mViewPager.setCurrentItem(image_pos++,true);
            }
        };
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(sliderRunnable);
            }
        },250,2500);

    }

    private void getCatData(final View view){

        Log.d(TAG, "Value is:startttttttttt ");
        Log.d(TAG, "Value is:startttttttttt ");
        if(auth.getCurrentUser()!=null){
            database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Admin").child("Category");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
                    catList.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String st=snapshot.getKey().toString();
                        Log.d(TAG, "Value is: " + st);
                        catList.add(st);


                    }
                    categoryAdapter =new CategoryAdapter(view.getContext(),catList);
                    mRecyclerView1.setAdapter(categoryAdapter);
                    mRecyclerView1.invalidate();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

    }
}