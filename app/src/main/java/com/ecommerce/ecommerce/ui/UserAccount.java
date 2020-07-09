package com.ecommerce.ecommerce.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.ecommerce.activity.MainActivity;
import com.ecommerce.ecommerce.Models.AccountModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.AccountAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAccount extends Fragment {

    private ImageView img;
    private TextView userName,userPhone;
    private RecyclerView recyclerView;
    private List<AccountModel> list;
    private AccountAdapter adapter;
    private String url,name,phone;
    private LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_account, container, false);
        init(view);

        if(MainActivity.staticModel!=null)
        {
            url = MainActivity.staticModel.getUserImageUrl();
            name = MainActivity.staticModel.getUserName();
            phone = MainActivity.staticModel.getUserPhone();
            Picasso.get().load(Uri.parse(url)).into(img);
            userName.setText(name);
            userPhone.setText(phone);
        }

        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_order,null),"Your Orders",0));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_cart_red,null),"Your Cart",6));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_wishlist,null),"Your Wishlist",1));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_delivery,null),"Delivery Address",2));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_person,null),"Personal Information",3));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_invite,null),"Invite on Super Local Baazar",7));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_password,null),"Change Password",4));
        list.add(new AccountModel(getResources().getDrawable(R.drawable.ic_wishlist,null),"Sign Out",5));

        adapter.setData(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void init(View view) {
        img = view.findViewById(R.id.account_userImage);
        userName = view.findViewById(R.id.account_userName);
        userPhone = view.findViewById(R.id.account_userPhone);
        recyclerView = view.findViewById(R.id.account_list);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new AccountAdapter(list,getContext());
        recyclerView.setHasFixedSize(true);

    }
}