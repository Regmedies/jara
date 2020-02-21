package com.example.jara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
 private Button RegisterButton;
 private EditText RegisterName,RegisterPhoneNumber,RegisterPassWord;
 private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton = (Button)findViewById(R.id.register_Btn);
        RegisterName = (EditText)findViewById(R.id.register_name_input);
        RegisterPhoneNumber = (EditText)findViewById(R.id.register_phone_number_input);
        RegisterPassWord = (EditText)findViewById(R.id.register_passWord_input);
        loadingBar = new ProgressDialog(this);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = RegisterName.getText().toString();
        String phone = RegisterPhoneNumber.getText().toString();
        String password = RegisterPassWord.getText().toString();

        if (TextUtils.isEmpty(name)) {

            Toast.makeText(this, "please enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait, while we check your credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();




            ValidatePhoneNumber(name,phone,password);
        }


    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))

                {
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                            else if (!(task.isSuccessful()))
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this,"Network error, please try later",Toast.LENGTH_SHORT).show();
                                }
                                else
                                    {
                                        loadingBar.dismiss();
                                    }

                        }
                    });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"This"+phone+"already exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"please try again with a different number",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }
}
