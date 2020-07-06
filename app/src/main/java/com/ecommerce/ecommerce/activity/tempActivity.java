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

public class tempActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText categoryName,productName,originalPrice,offPrice,productQuantity,productDetail;
    private Button submit;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 2;
    private String Cname,Pname,originalP,offP,productQ,productD;
    private Uri mImageUri;
    private RadioButton cod,returnable;
    private int r1=0,r2=0;



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
                uploadProduct();
            }
        });


    }


    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            case R.id.temp_return:
                if(checked)
                {
                    r1=1;
                }
                else
                {
                    r1=0;
                }
                break;

            case R.id.temp_cod:
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

        if(Cname.isEmpty()|| Pname.isEmpty()|| originalP.isEmpty()|| offP.isEmpty()|| productQ.isEmpty() || productD.isEmpty() || mImageUri==null)
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
                                            Product model = new Product(uri.toString(),Cname,Pname,originalP,offP,productQ,"0",productD,r1+"",r2+"");
                                            databaseReference.child(getResources().getString(R.string.Admin)).child(getResources().getString(R.string.Category)).child(Cname).child(Pname).setValue(model);

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
        imageView = findViewById(R.id.temp_image);
        categoryName = findViewById(R.id.temp_category_name);
        productName = findViewById(R.id.temp_product_Name);
        originalPrice = findViewById(R.id.temp_product_original_price);
        offPrice = findViewById(R.id.temp_product_off_price);
        productQuantity = findViewById(R.id.temp_product_quantity);
        productDetail = findViewById(R.id.temp_product_product_detail);
        submit = findViewById(R.id.temp_submit);
        cod=findViewById(R.id.temp_cod);
        returnable = findViewById(R.id.temp_return);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        storageReference = FirebaseStorage.getInstance().getReference();

    }
}