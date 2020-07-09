package com.ecommerce.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.ecommerce.Interface.OnItemClickListener;
import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;

import java.util.List;

public class ProductVarQuantityAdapter extends RecyclerView.Adapter<ProductVarQuantityAdapter.ProductQuantityView> {

    private List<ProductVariation> list;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int flag=0;
    int selected=0;
    public ProductVarQuantityAdapter(List<ProductVariation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductQuantityView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_product_quantity_variation,parent,false);
        return new ProductQuantityView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductQuantityView holder, int position) {
        holder.quantity.setText(list.get(position).getProductVariationName());

        if (selected==position)
        {
            holder.quantity.setBackground(context.getResources().getDrawable(R.drawable.border_square_red));
        }
        else {
            holder.quantity.setBackground(context.getResources().getDrawable(R.drawable.border_square_grey));

        }
        holder.bind(list.get(position),onItemClickListener);

    }

    public void setData(List<ProductVariation> list){this.list = list;}


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductQuantityView extends RecyclerView.ViewHolder{

        private TextView quantity;

        public ProductQuantityView(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.raw_product_var_quantity);
        }

        public void bind(final ProductVariation model,final OnItemClickListener onItemClickListener)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null)
                    {
                        selected = getAdapterPosition();
                        onItemClickListener.onItemClick(list.get(getAdapterPosition()));
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }

}
