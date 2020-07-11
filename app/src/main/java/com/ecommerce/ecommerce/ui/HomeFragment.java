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

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.CategoryAdapter;
import com.ecommerce.ecommerce.adapter.SubCategoryAdapter;
import com.ecommerce.ecommerce.adapter.SlidingImage_Adapter;
import com.ecommerce.ecommerce.object.Product;
import com.ecommerce.ecommerce.object.SubCategory;
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
    private List<SubCategory> imageUrlList=new ArrayList<>();
    private List<String> catList=new ArrayList<>();
    private LinearLayout dotLayout;
    private int dot_pos=0;
    FirebaseDatabase database;
    private FirebaseAuth auth;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        auth = FirebaseAuth.getInstance();

        mViewPager = view.findViewById(R.id.imageslider_pager);
        dotLayout=view.findViewById(R.id.dotContainer);
        loadingDialog.startLoadingDialog();
        getSlideData();
//        prepareSlide();

        getCatData(view);



        mRecyclerView1=view.findViewById(R.id.recycler_view1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(image_pos>=imageUrlList.size())
                    image_pos=0;
//                image_pos++;
//                image_pos=position;
                preDot(image_pos);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void getSlideData() {
        Log.d("TAG","vvvvvvvvvvv");
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Admin").child("SlideImage");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrlList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

//                    String url=snapshot.child("imageUrl").getValue(String.class);
                    SubCategory data=snapshot.getValue(SubCategory.class);
                    imageUrlList.add(data);
//                    Log.d("TAG",url+"qwerty");



                }
                loadingDialog.DismissDialog();
                SlidingImage_Adapter adapterView = new SlidingImage_Adapter(imageUrlList,getContext());
                mViewPager.setAdapter(adapterView);
                image_pos=0;
                preDot(image_pos);
                createSlider();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void preDot(int currImage_pos){
        if(dotLayout.getChildCount()>0){
            dotLayout.removeAllViews();
        }
        ImageView dots[]=new ImageView[imageUrlList.size()];
        for(int i=0;i<imageUrlList.size();i++){
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
//                    mRecyclerView1.invalidate();
                    mRecyclerView1.setNestedScrollingEnabled(false);
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