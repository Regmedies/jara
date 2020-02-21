package com.example.jara.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.jara.AdminAddProductActivity;
import com.example.jara.R;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tShirts,Sports,femaledress,sweathers;
    private ImageView glasses,purseBags,hats,shoes;
    private ImageView headpones,laptops,watches,mobilephones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_category );

        tShirts = (ImageView) findViewById( R.id.t_shirts );
        Sports = (ImageView) findViewById( R.id.sports );
        femaledress = (ImageView) findViewById( R.id.female_dress );
        sweathers = (ImageView) findViewById( R.id.sweathers );

        glasses = (ImageView) findViewById( R.id.t_glasses );
        purseBags = (ImageView) findViewById( R.id.purse_bags );
        hats = (ImageView) findViewById( R.id.hats );
        shoes = (ImageView) findViewById( R.id.shoes );

        headpones = (ImageView) findViewById( R.id.headphone);
        laptops = (ImageView) findViewById( R.id.laptop);
        watches = (ImageView) findViewById( R.id.watch );
        mobilephones = (ImageView) findViewById( R.id.mobile );

        tShirts.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( AdminCategoryActivity.this, AdminAddProductActivity.class );
                intent.putExtra( "category" ,"tShirts");
                startActivity( intent );
            }

        } );
        Sports.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"Sports");
                startActivity( intent );
            }
        } );
        femaledress.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"femaledress");
                startActivity( intent );
            }
        } );
        sweathers.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"sweather");
                startActivity( intent );
            }
        } );

         glasses.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( AdminCategoryActivity.this, AdminAddProductActivity.class );
                intent.putExtra( "category" ,"glasses");
                startActivity( intent );
            }

        } );
        purseBags.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"purse bags");
                startActivity( intent );
            }
        } );
        hats.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"hats");
                startActivity( intent );
            }
        } );
        shoes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"shoes");
                startActivity( intent );
            }
        } );

        headpones.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( AdminCategoryActivity.this, AdminAddProductActivity.class );
                intent.putExtra( "category" ,"head phones");
                startActivity( intent );
            }

        } );
        laptops.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"laptops");
                startActivity( intent );
            }
        } );
        watches.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"watches");
                startActivity( intent );
            }
        } );
        mobilephones.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class );
                intent.putExtra( "category" ,"mobile phones");
                startActivity( intent );
            }
        } );
    }
}
