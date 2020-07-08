package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.SearchModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.DetailCategoryList;
import com.ecommerce.ecommerce.activity.ProductDetailActivity;
import com.ecommerce.ecommerce.activity.SubCategoryList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SearchModel> list;
    private Context context;

    public SearchAdapter(List<SearchModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_search_item,parent,false);
        return new SearchViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.search_text.setText(list.get(position).getSearchName());
        holder.type = list.get(position).getType();
        holder.category = list.get(position).getCategory();
        holder.subCategory = list.get(position).getSubCategory();
        holder.productName = list.get(position).getProductName();

    }
    public void setData(List<SearchModel> list)
    {
        this.list =list;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView search_text;
        int type;
        FirebaseUser user;
        DatabaseReference mDatabaseReference;
        String category,subCategory,productName;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            search_text = itemView.findViewById(R.id.search_text);
            user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextActivity();
                }
            });




        }

        private void nextActivity() {
            if(type==1)
            {
                Intent intent =new Intent(context, SubCategoryList.class);
                intent.putExtra("Category",category);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else if(type==2)
            {

                Intent intent =new Intent(context, DetailCategoryList.class);
                intent.putExtra("category",category);
                intent.putExtra("subCategory",subCategory);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else if(type==3)
            {

                Intent intent =new Intent(context, ProductDetailActivity.class);
                intent.putExtra("category",category);
                intent.putExtra("subCategory",subCategory);
                intent.putExtra("product",productName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
