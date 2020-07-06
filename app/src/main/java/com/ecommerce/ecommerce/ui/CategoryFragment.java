package com.ecommerce.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.activity.SignUp;
import com.ecommerce.ecommerce.activity.tempActivity;

public class CategoryFragment extends Fragment {

    private Button randome,temp,detailCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        init(view);

        randome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignUp.class);
                startActivity(intent);
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), tempActivity.class);
                startActivity(intent);
            }
        });

        detailCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailCategoryList.class);
                intent.putExtra("category","Edible Oil And Ghee");
                startActivity(intent);
            }
        });

        return view;
    }

    private void init(View view) {
        randome = view.findViewById(R.id.random);
        temp = view.findViewById(R.id.temp_btn);
        detailCategory = view.findViewById(R.id.temp_detail_category);
    }
}