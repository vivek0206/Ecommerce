package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.SearchModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
import com.ecommerce.ecommerce.object.SubCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class tempActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText categoryName,subcategoryName;
    private Button submit;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 2;
    private String Cname,Pname,originalP,offP,productQ,productD,ScName;
    private Uri mImageUri;
    private RadioButton cod,returnable;
    private int r1=0,r2=0;
    private LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        init();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                uploadProduct();
            }
        });


    }



    private void uploadProduct()
    {
        Cname = categoryName.getText().toString();
        ScName=subcategoryName.getText().toString();
//        Pname = productName.getText().toString();
//        originalP = originalPrice.getText().toString();
//        offP = offPrice.getText().toString();
//        productQ = productQuantity.getText().toString();
//        productD = productDetail.getText().toString();

//        if(Cname.isEmpty()|| Pname.isEmpty()|| originalP.isEmpty()|| offP.isEmpty()|| productQ.isEmpty() || productD.isEmpty() || mImageUri==null)
//        {
//            Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
//        }
        if(Cname.isEmpty()||mImageUri==null||ScName.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
        }
        else
        {

            final StorageReference reference = storageReference.child(getResources().getString(R.string.CategoryImage)).child(ScName+getfilterExt(mImageUri));
            reference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            SubCategory model = new SubCategory(uri.toString(),Cname,ScName);
                                            databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.CategoryData)).child(Cname.toLowerCase().trim()).child(ScName.toLowerCase().trim()).setValue(model);
                                            SearchModel searchModel = new SearchModel(Cname,Cname.toLowerCase().trim(),"NA","NA",1);
                                            databaseReference.child(getResources().getString(R.string.Search)).child(Cname.toLowerCase().trim()).setValue(searchModel);
                                            searchModel = new SearchModel(ScName,Cname.toLowerCase().trim(),ScName.toLowerCase().trim(),"NA",2);
                                            databaseReference.child(getResources().getString(R.string.Search)).child(ScName.toLowerCase().trim()).setValue(searchModel);

                                            loadingDialog.DismissDialog();
                                            Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

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
            Picasso.get().load(mImageUri).into(imageView);
        }

    }


    private void init() {
        MainActivity.OfflineCapabilities(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        imageView = findViewById(R.id.temp_image);
        categoryName = findViewById(R.id.temp_category_name);
        subcategoryName=findViewById(R.id.temp_subcategory_name);
//        productName = findViewById(R.id.temp_product_Name);
//        originalPrice = findViewById(R.id.temp_product_original_price);
//        offPrice = findViewById(R.id.temp_product_off_price);
//        productQuantity = findViewById(R.id.temp_product_quantity);
//        productDetail = findViewById(R.id.temp_product_product_detail);
        submit = findViewById(R.id.temp_submit);
//        cod=findViewById(R.id.temp_cod);
//        returnable = findViewById(R.id.temp_return);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        storageReference = FirebaseStorage.getInstance().getReference();

    }
}