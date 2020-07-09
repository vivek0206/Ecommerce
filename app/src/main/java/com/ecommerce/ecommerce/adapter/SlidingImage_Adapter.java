package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.object.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SlidingImage_Adapter extends PagerAdapter {

    Context mContext;
//    private List<Integer> sliderImageId;
    private List<SubCategory> sliderImageId;
    public SlidingImage_Adapter(List<SubCategory> sliderImageId,Context context) {
        this.sliderImageId=sliderImageId;
        this.mContext = context;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(sliderImageId.get(position));
        Picasso.get().load(sliderImageId.get(position).getImageUrl()).into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        Log.d(TAG, "Value is:startttttttttt ");
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
