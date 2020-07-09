package com.ecommerce.ecommerce.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecommerce.ecommerce.Interface.OnDataChangeListener;
import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.MainActivity;
import com.ecommerce.ecommerce.adapter.UserCartAdapter;
import com.ecommerce.ecommerce.adapter.WishlistAdapter;
import com.ecommerce.ecommerce.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment implements onBackPressed {

    private RecyclerView recyclerView;
    private WishlistAdapter adapter;
    private List<Product> list;
    private LinearLayoutManager layoutManager;

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wishlist, container, false);

        init(view);
        loadingDialog.startLoadingDialog();
        fetchUserWishlist();

        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged(int size, int price, boolean flag) {
                loadingDialog.startLoadingDialog();
                fetchUserWishlist();
            }

            @Override
            public void onDataRemoveChange() {

            }
        });

        return view;
    }

    private void fetchUserWishlist() {

        databaseReference.child(getResources().getString(R.string.Wishlist)).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Product model = ds.getValue(Product.class);
                    if(model!=null)
                    {
                        list.add(model);
                    }
                }
                loadingDialog.DismissDialog();
                adapter.setData(list);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child(getString(R.string.Wishlist)).keepSynced(true);



    }



    private void init(View view) {
        MainActivity.OfflineCapabilities(getContext());
        loadingDialog = new LoadingDialog(getActivity());
        recyclerView = view.findViewById(R.id.wishlist_recyclerView);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new WishlistAdapter(list,getContext());
        recyclerView.setHasFixedSize(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onBackPressed() {

    }
}