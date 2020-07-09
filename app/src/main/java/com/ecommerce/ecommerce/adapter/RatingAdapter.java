package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Models.RatingInfo;
import com.ecommerce.ecommerce.R;


import java.util.List;

public class RatingAdapter  extends RecyclerView.Adapter<RatingAdapter .AccountView>{

    private List<RatingInfo> list;
    private Context context;
    private Integer totalRating;

    public RatingAdapter (List<RatingInfo > list, Context context,Integer totalRating) {
        this.list = list;
        this.context = context;
        this.totalRating=totalRating;
    }

    @NonNull
    @Override
    public AccountView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_list_item,parent,false);
        return new AccountView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountView holder, int position) {
        double rateValue=(100*list.get(position).getRatingValue())/totalRating;
        String rateName=list.get(position).getRatingName()+"\u2605";
        if(position==0) {
            holder.ratingName.setText(rateName);
            holder.ratingValue.setProgress((int)rateValue);
            holder.ratingNo.setText(Integer.toString(list.get(position).getRatingValue()));
        }else if(position==1) {
            holder.ratingName.setText(rateName);
            holder.ratingValue.setProgress((int)rateValue);
            holder.ratingNo.setText(Integer.toString(list.get(position).getRatingValue()));
        }else if(position==2) {
            holder.ratingName.setText(rateName);
            holder.ratingValue.setProgress((int)rateValue);
            holder.ratingNo.setText(Integer.toString(list.get(position).getRatingValue()));
        }else if(position==3) {
            holder.ratingName.setText(rateName);
            holder.ratingValue.setProgress((int)rateValue);
            holder.ratingNo.setText(Integer.toString(list.get(position).getRatingValue()));
        }else if(position==4) {
            holder.ratingName.setText(rateName);
            holder.ratingValue.setProgress((int)rateValue);
            holder.ratingNo.setText(Integer.toString(list.get(position).getRatingValue()));
        }
    }

    public void setData(List<RatingInfo> list){this.list = list;}

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AccountView extends RecyclerView.ViewHolder{

        TextView ratingName,ratingNo;
        ProgressBar ratingValue;

        public AccountView(@NonNull View itemView) {
            super(itemView);
            ratingName = itemView.findViewById(R.id.rating_name);
            ratingValue= itemView.findViewById(R.id.rating_value);
            ratingNo=itemView.findViewById(R.id.rating_no);



        }

    }

}
