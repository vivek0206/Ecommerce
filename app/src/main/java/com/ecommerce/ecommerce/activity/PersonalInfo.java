package com.ecommerce.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.ecommerce.LoadingDialog;
import com.ecommerce.ecommerce.Models.UserInfo;
import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.MainActivity;
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

public class PersonalInfo extends AppCompatActivity {

    private ImageView imageView;
    private TextView userName,userPhone;
    private Button save;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 2;
    private Uri mImageUri;
    private String name,phone,password;
    private LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Personal Info");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                uploadImage();
            }
        });

        MainActivity.fetchUserInfo();
        if(MainActivity.staticModel!=null)
        {
            name = MainActivity.staticModel.getUserName();
            phone = MainActivity.staticModel.getUserPhone();
            password = MainActivity.staticModel.getUserPswd();
            if(!MainActivity.staticModel.getUserImageUrl().isEmpty())
            {
                mImageUri = Uri.parse(MainActivity.staticModel.getUserImageUrl());
                Picasso.get().load(MainActivity.staticModel.getUserImageUrl()).into(imageView);
            }
            userName.setText(name);
            userPhone.setText(phone);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);
        }

    }

    private void uploadImage() {
        name = userName.getText().toString();
        phone = userPhone.getText().toString();
        final StorageReference reference = storageReference.child(getResources().getString(R.string.UserImage)).child(user.getUid()+getfilterExt(mImageUri));
        reference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        MainActivity.staticModel = new UserInfo(name,phone,password,uri.toString());
                                        databaseReference.child(getResources().getString(R.string.UserInfo)).child(user.getUid()).setValue(MainActivity.staticModel);
                                        loadingDialog.DismissDialog();
                                        Toast.makeText(PersonalInfo.this,"SucessFully Updated",Toast.LENGTH_SHORT).show();
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

    private void init() {

        loadingDialog = new LoadingDialog(this);
        imageView = findViewById(R.id.edit_userImage);
        userName = findViewById(R.id.edit_userName);
        userPhone = findViewById(R.id.edit_userPhone);
        save = findViewById(R.id.save_personal_info);
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
    }


}