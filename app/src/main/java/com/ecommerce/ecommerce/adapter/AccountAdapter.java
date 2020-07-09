package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.BuildConfig;
import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.activity.CartActivity;
import com.ecommerce.ecommerce.activity.DeliveryAddress;
import com.ecommerce.ecommerce.Models.AccountModel;
import com.ecommerce.ecommerce.activity.MainActivity;
import com.ecommerce.ecommerce.activity.ManageOrderActivity;
import com.ecommerce.ecommerce.activity.PersonalInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.ui.OrderFragment;
import com.ecommerce.ecommerce.ui.WishlistFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountView>{

    private List<AccountModel> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AccountAdapter(List<AccountModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        private FirebaseUser user;
        private FirebaseAuth auth;

        public AccountView(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.icon);
            textView = itemView.findViewById(R.id.category_name);
            user = FirebaseAuth.getInstance().getCurrentUser();
            auth = FirebaseAuth.getInstance();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(pos==0)
                    {
                        Intent intent = new Intent(context, ManageOrderActivity.class);
                        context.startActivity(intent);
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
                        onItemClickListener.onItemClick();
                    }
                    else if(pos==5)
                    {
                        auth.signOut();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context,"sign Out",Toast.LENGTH_SHORT).show();
                    }
                    else if(pos==6)
                    {
                        Intent intent = new Intent(context, CartActivity.class);
                        context.startActivity(intent);
                    }
                    else if(pos==7)
                    {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Smurfo");
                            String shareMessage= "\nHey Friend CheckOut This new cool app its called smurfo.\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                    }
                }
            });

        }

        public void setFragment(Fragment fragment)
        {
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.drawer_layout,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

    }

}
