package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ecommerce.ecommerce.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SlidingImage_Adapter extends PagerAdapter {

    Context mContext;
    private List<Integer> sliderImageId;
    public SlidingImage_Adapter(List<Integer> sliderImageId,Context context) {
        this.sliderImageId=sliderImageId;
        this.mContext = context;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(sliderImageId.get(position));
        ((ViewPager) container).addView(imageView, 0);
        Log.d(TAG, "Value is:startttttttttt ");
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return sliderImageId.size();
    }
}
