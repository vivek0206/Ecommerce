package com.ecommerce.ecommerce.ui;

import android.media.Image;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ecommerce.ecommerce.adapter.HomeCategory_adapter;
import com.ecommerce.ecommerce.adapter.SlidingImage_Adapter;
import com.ecommerce.ecommerce.object.Product;
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
    private HomeCategory_adapter homeCategory_adpater;
    List<Product> pList1;
    ViewPager mViewPager;
    private Timer timer;
    private int image_pos=0;
    private List<Integer> imageList=new ArrayList<>();
    private LinearLayout dotLayout;
    private int dot_pos=0;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);


        mViewPager = view.findViewById(R.id.imageslider_pager);
        dotLayout=view.findViewById(R.id.dotContainer);
        prepareSlide();
        SlidingImage_Adapter adapterView = new SlidingImage_Adapter(imageList,view.getContext());
        mViewPager.setAdapter(adapterView);
        preDot(image_pos);
        createSlider();


         database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Admin").child("Category");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        pList1=new ArrayList<>();
        pList1.add(new Product("oil"));
        pList1.add(new Product("ghee"));
        Log.d("tag","hiiiiiiiii"+pList1.size());
        mRecyclerView1=view.findViewById(R.id.recycler_view1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        mRecyclerView1.setLayoutManager(gridLayoutManager);
        homeCategory_adpater =new HomeCategory_adapter(view.getContext(),pList1);
        mRecyclerView1.setAdapter(homeCategory_adpater);


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
}