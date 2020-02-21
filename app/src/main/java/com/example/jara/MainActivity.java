package com.example.jara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jara.model.Users;
import com.example.jara.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
{
private Button joinNowButton, loginButton;
    private ProgressDialog LoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button)findViewById(R.id.main_join_Now_Btn);
        loginButton = (Button)findViewById(R.id.main_login_Btn);

        Paper.init( this );

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        String userPhoneKey = Paper.book().read( prevalent.UserPhoneKey);
        String userPassWord = Paper.book().read( prevalent.UserPasswordKey );

        if (userPhoneKey !=null && userPassWord !=null)
        {   LoadingBar = new ProgressDialog(this);
            LoadingBar.setTitle( "Log in" );
            LoadingBar.setMessage( "Already logged in" );
            LoadingBar.setCanceledOnTouchOutside( false );
            LoadingBar.dismiss();

            if(!TextUtils.isEmpty( userPhoneKey )&& !TextUtils.isEmpty( userPassWord ))
            {
                AllowAccess(userPhoneKey,userPassWord);
            }
        }

       }

    private void AllowAccess(final String phone,final String password)
    {
        LoadingBar = new ProgressDialog( this );
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users" ).child( phone ).exists())
                {
                    Users UserData = (dataSnapshot.child( "Users").child( phone ).getValue(Users.class));

                    if (UserData.getPhone().equals( phone ))
                    {

                        if (UserData.getPassword().equals( password ))
                        {
                            Toast.makeText( MainActivity.this,"login successful",Toast.LENGTH_SHORT ).show();
                            LoadingBar.dismiss();
                            Intent intent =new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);

                        }
                    }
                }
                else
                {
                    Toast.makeText( MainActivity.this,"Account with this phone number"+phone+"does not exist",Toast.LENGTH_SHORT ).show();
                    LoadingBar.dismiss();
                    Toast.makeText( MainActivity.this,"Login with different Account ",Toast.LENGTH_SHORT ).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

}
