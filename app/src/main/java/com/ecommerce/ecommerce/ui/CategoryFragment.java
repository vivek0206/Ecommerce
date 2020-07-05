package com.ecommerce.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.SignUp;

public class CategoryFragment extends Fragment {

    private Button randome;

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

        return view;
    }

    private void init(View view) {
        randome = view.findViewById(R.id.random);
    }
}