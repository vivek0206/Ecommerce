package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.DeliveryAddress;
import com.ecommerce.ecommerce.Models.AccountModel;
import com.ecommerce.ecommerce.PersonalInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.ui.OrderFragment;
import com.ecommerce.ecommerce.ui.WishlistFragment;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountView>{

    private List<AccountModel> list;
    private Context context;

    public AccountAdapter(List<AccountModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_account_list,parent,false);
        return new AccountView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountView holder, int position) {
        holder.img.setImageDrawable(list.get(position).getImg());
        holder.textView.setText(list.get(position).getText());
        holder.pos = list.get(position).getPos();
    }

    public void setData(List<AccountModel> list){this.list = list;}

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AccountView extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView textView;
        private int pos;

        public AccountView(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.icon);
            textView = itemView.findViewById(R.id.category_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(pos==0)
                    {
                        setFragment(new OrderFragment());
                    }
                    else if(pos==1)
                    {
                        setFragment(new WishlistFragment());
                    }
                    else if(pos==2)
                    {
                        Intent intent = new Intent(context, DeliveryAddress.class);
                        context.startActivity(intent);
                    }
                    else if(pos==3)
                    {
                        Intent intent = new Intent(context, PersonalInfo.class);
                        context.startActivity(intent);
                    }
                    else if(pos==4)
                    {

                    }
                    else if(pos==5)
                    {

                    }
                }
            });

        }

        public void setFragment(Fragment fragment)
        {
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.drawer_layout,fragment);
            fragmentTransaction.commit();

        }


    }

}
