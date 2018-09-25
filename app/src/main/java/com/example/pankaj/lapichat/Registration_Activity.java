package com.example.pankaj.lapichat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration_Activity extends AppCompatActivity {

    EditText mpassword,mname,memail;
    Button Registrartin_btn;
    private android.support.v7.widget.Toolbar regToolbar;

    //firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mreference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);

        mpassword=findViewById(R.id.Regname);
        memail=findViewById(R.id.Regemail);
        mname=findViewById(R.id.Regname);
        Registrartin_btn=findViewById(R.id.Registration);

        //toolbar
        regToolbar=findViewById(R.id.register_toolbar);

        //progress dialog
        progressDialog= new ProgressDialog(this);

            setSupportActionBar(regToolbar);
            getSupportActionBar().setTitle("Create Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            firebaseAuth=FirebaseAuth.getInstance();


             Registrartin_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String display_name = mname.getText().toString().trim();
            String email=memail.getText().toString().trim();
            String password=mpassword.getText().toString().trim();
            if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                progressDialog.setTitle("Registering User");
                progressDialog.setMessage("Please wait while we creating your Account");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                register_user(display_name,email,password);
            }

            }
    });
    }
    private void register_user(final String display_name, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid =  current_user.getUid();
                    mreference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    HashMap<String,String> users = new HashMap<>();
                    users.put("name",display_name);
                    users.put("status","I'm using lapi aap");
                    users.put("image","default");
                    users.put("thumb_img","default");
                    mreference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Intent mainIntent = new Intent(Registration_Activity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        }
                        });

                    }
                else{
                    progressDialog.hide();
                    Toast.makeText(Registration_Activity.this,"You Got Some Error" ,Toast.LENGTH_LONG).show();
                }
            }
        });

}
}
