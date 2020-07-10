package com.ecommerce.ecommerce.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProductVariation extends AppCompatActivity {

    private EditText categoryName,subCategoryName,productName,productVarName,productQuantity,productSalePrice,productActualPrice;
    private Button add;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ImageView img;
    private static final int PICK_IMAGE_REQUEST = 2;
    private Uri mImageUri;
    private StorageReference storageReference;

    private String catName,subName,proName,proVarName,proQua,proSalePrice,proActualPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_variation);

        init();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductCategory();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

    }

    private void AddProductCategory() {
       catName = categoryName.getText().toString().trim();
       subName = subCategoryName.getText().toString().trim();
        proName= productName.getText().toString().trim();
        proVarName = productVarName.getText().toString().trim();
        proQua = productQuantity.getText().toString().trim();
        proSalePrice = productSalePrice.getText().toString().trim();
        proActualPrice = productActualPrice.getText().toString().trim();

        if(mImageUri==null || catName.isEmpty()||subName.isEmpty()||proName.isEmpty()||proVarName.isEmpty()||proQua.isEmpty()||proSalePrice.isEmpty()||proActualPrice.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
        }
        else
        {
            final StorageReference reference = storageReference.child(getResources().getString(R.string.ProductVariation)).child(proVarName+getfilterExt(mImageUri));
            reference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            ProductVariation model = new ProductVariation(catName,subName,proName,proVarName,Integer.parseInt(proQua),Integer.parseInt(proSalePrice),Integer.parseInt(proActualPrice),uri.toString());
                                            databaseReference.child(getResources().getString(R.string.ProductVariation)).child(proName.toLowerCase()).child(proVarName.toLowerCase()).setValue(model);

                                        }
                                    });
                        }
                    });

        }

    }

    private void init() {
        img = findViewById(R.id.activity_add_product_Image);
        categoryName=findViewById(R.id.activity_add_product_categoryName);
        subCategoryName=findViewById(R.id.activity_add_product_subCatName);
        productName=findViewById(R.id.activity_add_product_productName);
        productVarName=findViewById(R.id.activity_add_product_productVariationName);
        productQuantity=findViewById(R.id.activity_add_product_productQuantity);
        productSalePrice=findViewById(R.id.activity_add_product_productSalePrice);
        productActualPrice = findViewById(R.id.activity_add_product_productActualPrice);
        add=findViewById(R.id.activity_add_product_productAdd);

        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getfilterExt(Uri videoUri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("2323232","111111");

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(img);
        }

    }



}