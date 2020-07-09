package com.ecommerce.ecommerce.ui;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.activity.MainActivity;
import com.ecommerce.ecommerce.Models.AccountModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.adapter.AccountAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAccount extends Fragment {

    private ImageView img;
    private TextView userName,userPhone;
    private RecyclerView recyclerView;
    private List<AccountModel> list;
    private AccountAdapter adapter;
    private String url,name,phone,password;
    private LinearLayoutManager layoutManager;
    private FirebaseUser user;
    private DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_account, container, false);
        init(view);

        if(MainActivity.staticModel!=null)
        {
            url = MainActivity.staticModel.getUserImageUrl();
            name = MainActivity.staticModel.getUserName();
            phone = MainActivity.staticModel.getUserPhone();
            password = MainActivity.staticModel.getUserPswd();
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

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Product model, int type) {

            }

            @Override
            public void onItemClick() {
                chanePassword();
            }

            @Override
            public void onItemClick(ProductVariation model) {

            }
        });

        return view;
    }

    private void chanePassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        View customView = getLayoutInflater().inflate(R.layout.raw_change_password_alert_dialog,null);
        builder.setView(customView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText password = customView.findViewById(R.id.raw_change_password_password);
        final EditText confirmPassword = customView.findViewById(R.id.raw_change_password_confirmPassword);
        Button btn = customView.findViewById(R.id.raw_change_password_save);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pswd = password.getText().toString();
                String cnfPswd = confirmPassword.getText().toString();
                if(pswd.isEmpty() || cnfPswd.isEmpty() || (!pswd.equals(cnfPswd)))
                {
                    Toast.makeText(getContext(),"Invalidate Input",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child(getResources().getString(R.string.UserInfo)).child(user.getUid()).child("userPswd").setValue(pswd);
                    Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }
        });



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
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
}