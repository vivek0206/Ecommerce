package com.ecommerce.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.object.ImageSlider;
import com.ecommerce.ecommerce.object.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SlidingImage_Adapter extends PagerAdapter {

    Context mContext;
//    private List<Integer> sliderImageId;
    private List<ImageSlider> sliderImageId;
    private LayoutInflater layoutInflater;
    public SlidingImage_Adapter(List<ImageSlider> sliderImageId,Context context) {
        this.sliderImageId=sliderImageId;
        this.mContext = context;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_slider_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.slider_imageView);
        Picasso.get().load(sliderImageId.get(position).getImageUrl()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailCategoryList.class);
                intent.putExtra("category",sliderImageId.get(position).getCategoryName().toLowerCase().trim());
                intent.putExtra("subCategory",sliderImageId.get(position).getSubCategoryName().toLowerCase().trim());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    @Override
    public int getCount() {
        return sliderImageId.size();
    }
}
