package com.example.jara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jara.model.AdminCategoryActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAddProductActivity extends AppCompatActivity {
    private String CategoryName,Description,Price,PName,saveCurrentDate,saveCurrentTime;
    private Button AddNewProductButton;
    private TextView inputProductName,inputProductDescription,inputProductPrice;
    private ImageView addImage;
    private static int GalleryPick=(1);
    private Uri ImageUri;
    private String productRandomKey,DownloadImageUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_add_product );
        AddNewProductButton= (Button) findViewById( R.id.addProduct_button );
        addImage = (ImageView) findViewById( R.id.select_product_id );
        inputProductName = (TextView) findViewById( R.id.product_name );
        inputProductDescription = (TextView) findViewById( R.id.product_Description );
        inputProductPrice = (TextView) findViewById( R.id.product_price );

        LoadingBar = new ProgressDialog( this );

        CategoryName= getIntent().getExtras().get("category").toString();
        //.makeText( this, CategoryName, Toast.LENGTH_SHORT ).show();

        ProductImageRef = FirebaseStorage.getInstance().getReference().child( "Product Images" );
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");


        addImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }

        } );
        AddNewProductButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        } );
    }

    private void openGallery()
    {
        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction( Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType( "image/*" );
        startActivityForResult( GalleryIntent,GalleryPick );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data != null);
        {
        ImageUri=data.getData();
        addImage.setImageURI( ImageUri );
        }
    }
    private void ValidateProductData() {
        Description = inputProductDescription.getText().toString();
        Price = inputProductPrice.getText().toString();
        PName = inputProductName.getText().toString();

        if (ImageUri == null) {
            Toast.makeText( this, "Please select an Image", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( Description )) {
            Toast.makeText( this, "Please add Product description", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( Price ))
        {
            Toast.makeText( this, "Please input Price", Toast.LENGTH_SHORT ).show();
        }
        else
            {
                StoreProductInformation();
            }
    }

    private void StoreProductInformation()
    {
        LoadingBar.setTitle( "Adding new product..." );
        LoadingBar.setMessage( "Please wait while we add new product..." );
        LoadingBar.setCanceledOnTouchOutside( false );
        LoadingBar.show();


        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format( calendar.getTime() );
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss, a");
        saveCurrentTime = currentTime.format( calendar.getTime() );

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference FilePath = ProductImageRef.child( ImageUri.getLastPathSegment()+productRandomKey );
        final UploadTask uploadTask = FilePath.putFile( ImageUri );

        uploadTask.addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                LoadingBar.dismiss();
                String message = e.toString();
                Toast.makeText( AdminAddProductActivity.this, "Error:"+message, Toast.LENGTH_SHORT ).show();


            }
        } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                Toast.makeText( AdminAddProductActivity.this, "Product image uploaded successfully", Toast.LENGTH_SHORT ).show();

                Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        DownloadImageUrl = FilePath.getDownloadUrl().toString();
                    return FilePath.getDownloadUrl();
                    }

                } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            DownloadImageUrl = task.getResult().toString();
                            Toast.makeText( AdminAddProductActivity.this, "got iamge url successful...", Toast.LENGTH_SHORT ).show();
                            SaveProductInfoToDatabase();
                        }

                    }
                } );
            }

        } );

    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String,Object> ProductMap =new HashMap<>(  );
        ProductMap.put( "pid", productRandomKey);
        ProductMap.put( "date", saveCurrentDate);
        ProductMap.put( "time", saveCurrentTime);
        ProductMap.put( "description", Description);
        ProductMap.put( "image", DownloadImageUrl );
        ProductMap.put( "category", CategoryName);
        ProductMap.put("price", Price);
        ProductMap.put( "pname", PName);

        ProductRef.child(productRandomKey).updateChildren( ProductMap )
        .addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    LoadingBar.dismiss();
                    Toast.makeText( AdminAddProductActivity.this, "Product added successfully...", Toast.LENGTH_SHORT ).show();
                    Intent intent =new Intent(AdminAddProductActivity.this, AdminCategoryActivity.class);
                    startActivity( intent );


                }
                else
                    {
                        LoadingBar.dismiss();
                       String  message = task.getException().toString();
                        Toast.makeText( AdminAddProductActivity.this, "Error:"+message, Toast.LENGTH_SHORT ).show();

                    }
            }

        } );
    }


}
