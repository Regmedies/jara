package com.example.jara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jara.model.AdminCategoryActivity;
import com.example.jara.model.Users;
import com.example.jara.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private Button LogInBtn;
    private EditText phoneNumber, passWord;
    private ProgressDialog LoadingBar;
    private String DbParentName = "Users";
    private CheckBox ChkbRememberMe;
    private TextView AdminLink,NotAdminLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        LogInBtn = (Button) findViewById( R.id.login_Btn );
        phoneNumber = (EditText) findViewById( R.id.login_phone_number_input );
        passWord = (EditText) findViewById( R.id.login_passWord_input );
        LoadingBar = new ProgressDialog( this );
        AdminLink = (TextView) findViewById( R.id.admin_link );
        NotAdminLink = (TextView) findViewById( R.id.not_admin_link );
        ChkbRememberMe =(CheckBox)findViewById(R.id.Remember_Me_Chkb );
        Paper.init( this );

        LogInBtn.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser();
            }
        } );
      AdminLink.setOnClickListener( new OnClickListener() {
          @Override
          public void onClick(View v) {
              LogInBtn.setText( " login admin" );
              AdminLink.setVisibility( View.INVISIBLE );
              NotAdminLink.setVisibility( View.VISIBLE );
              DbParentName= "Admins";
          }
      } );
      NotAdminLink.setOnClickListener( new OnClickListener() {
          @Override
          public void onClick(View v) {
              LogInBtn.setText("login" );
              AdminLink.setVisibility( View.VISIBLE );
              NotAdminLink.setVisibility( View.INVISIBLE );
              DbParentName ="Users";

          }
      } );

    }

    private void LogInUser() {
        String phone = phoneNumber.getText().toString();
        String password = passWord.getText().toString();

        if (TextUtils.isEmpty( phone )) {

            Toast.makeText( this, "please enter your phone number", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( password )) {

            Toast.makeText( this, "please enter your password", Toast.LENGTH_SHORT ).show();
        }
        else
            {
               LoadingBar.setTitle( "Log in" );
               LoadingBar.setMessage( "Logging in" );
               LoadingBar.setCanceledOnTouchOutside( false );
               LoadingBar.dismiss();

               AllowAccessToAccount(phone,password);
            }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(ChkbRememberMe.isChecked())
        {
           Paper.book().write( prevalent.UserPhoneKey,phone);
           Paper.book().write( prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child( DbParentName ).child( phone ).exists())
                {
                     Users UserData = (dataSnapshot.child( DbParentName ).child( phone ).getValue(Users.class));

                    if (UserData.getPhone().equals( phone ))
                    {

                        if (UserData.getPassword().equals( password ))
                        {
                            if (DbParentName.equals("Admins"))
                            {
                                Toast.makeText( LoginActivity.this,"Welcome Admin",Toast.LENGTH_SHORT ).show();
                                LoadingBar.dismiss();
                                Intent intent =new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (DbParentName.equals( "Users" ))
                            {
                                Toast.makeText( LoginActivity.this,"login successful",Toast.LENGTH_SHORT ).show();
                                LoadingBar.dismiss();
                                Intent intent =new Intent(LoginActivity.this,HomeActivity.class);
                                prevalent.CurrentOnlineUser = UserData;
                                startActivity(intent);
                            }

                        }
                    }
                }
                else
                    {
                        Toast.makeText( LoginActivity.this,"Account with this phone number"+phone+"does not exist",Toast.LENGTH_SHORT ).show();
                        LoadingBar.dismiss();
                        Toast.makeText( LoginActivity.this,"Login with different Account ",Toast.LENGTH_SHORT ).show();
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
