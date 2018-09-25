package com.example.pankaj.lapichat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    private Toolbar mToolbar;

    EditText Login_email;
    EditText Login_password;

    Button b1;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        mToolbar = findViewById(R.id.login_header);

        Login_email=findViewById(R.id.Lemail);
        Login_password=findViewById(R.id.Lpassword);
        b1=findViewById(R.id.b1);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Login_email.getText().toString().trim();
                String password = Login_password.getText().toString().trim();
                if(!TextUtils.isEmpty(email )||!TextUtils.isEmpty(password)){

                    progressDialog.setTitle("Logging In");
                    progressDialog.setMessage("Please wait while we check your credentials.");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(true);
                    Login_User(email,password);
                }}
        });

    }
    private void Login_User(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent mainintent = new Intent(getApplicationContext(),MainActivity.class);
                            mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainintent);
                            finish();
                        }
                        else{
                            progressDialog.hide();
                            Toast.makeText(Login_Activity.this,"Cannot sign in please check the form and try again",Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}
