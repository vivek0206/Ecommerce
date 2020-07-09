package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.ecommerce.ecommerce.Models.SearchModel;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.object.Product;
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

public class AddSubCat extends AppCompatActivity {


    private ImageView imageView;
    private EditText categoryName,productName,originalPrice,offPrice,productQuantity,productDetail,subCategory;
    private Button submit;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 2;
    private String Cname,Pname,originalP,offP,productQ,productD,subcatN;
    private Uri mImageUri;
    private RadioButton cod,returnable;
    private int r1=0,r2=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_cat);

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
                uploadProduct();
            }
        });


    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            case R.id.add_return:
                if(checked)
                {
                    r1=1;
                }
                else
                {
                    r1=0;
                }
                break;

            case R.id.add_cod:
                if(checked)
                {
                    r2=1;
                }
                else
                {
                    r2=0;
                }
                break;
        }
    }

    private void uploadProduct()
    {
        Cname = categoryName.getText().toString();
        Pname = productName.getText().toString();
        originalP = originalPrice.getText().toString();
        offP = offPrice.getText().toString();
        productQ = productQuantity.getText().toString();
        productD = productDetail.getText().toString();
        subcatN=subCategory.getText().toString();

        if(subcatN.isEmpty()||Cname.isEmpty()|| Pname.isEmpty()|| originalP.isEmpty()|| offP.isEmpty()|| productQ.isEmpty() || productD.isEmpty() || mImageUri==null)
        {
            Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
        }
        else
        {

            final StorageReference reference = storageReference.child(getResources().getString(R.string.ProductImage)).child(Pname+getfilterExt(mImageUri));
            reference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Product model = new Product(uri.toString(),Cname,Pname,originalP,offP,productQ,"0",productD,r1+"",r2+"",subcatN,"-1");
                                            databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(Cname.toLowerCase().trim()).child(subcatN.toLowerCase().trim()).child(Pname.toLowerCase().trim()).setValue(model);


                                            SearchModel searchModel = new SearchModel(Cname,Cname,"NA","NA",1);
                                            databaseReference.child(getResources().getString(R.string.Search)).child(Cname.toLowerCase().trim()).setValue(searchModel);
                                            searchModel = new SearchModel(subcatN,Cname.toLowerCase().trim(),subcatN.toLowerCase().trim(),"NA",2);
                                            databaseReference.child(getResources().getString(R.string.Search)).child(subcatN.toLowerCase().trim()).setValue(searchModel);

                                            searchModel = new SearchModel(Pname,Cname.toLowerCase().trim(),subcatN.toLowerCase().trim(),Pname.toLowerCase().trim(),3);
                                            databaseReference.child(getResources().getString(R.string.Search)).child(Pname.toLowerCase().trim()).setValue(searchModel);

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
        imageView = findViewById(R.id.add_image);
        categoryName = findViewById(R.id.add_category_name);
        subCategory=findViewById(R.id.add_subcategory_name);

        productName = findViewById(R.id.add_product_Name);
        originalPrice = findViewById(R.id.add_product_original_price);
        offPrice = findViewById(R.id.add_product_off_price);
        productQuantity = findViewById(R.id.add_product_quantity);
        productDetail = findViewById(R.id.add_product_product_detail);
        submit = findViewById(R.id.add_submit);
        cod=findViewById(R.id.add_cod);
        returnable = findViewById(R.id.add_return);



        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        storageReference = FirebaseStorage.getInstance().getReference();

    }
}