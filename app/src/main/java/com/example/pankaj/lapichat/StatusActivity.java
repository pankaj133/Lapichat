package com.example.pankaj.lapichat;


import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

   private android.support.v7.widget.Toolbar mtoolbar;

   EditText status_value;
   Button save_chang;

   DatabaseReference mdatabase;
   FirebaseUser firebaseUser;

   ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
     String uid= firebaseUser.getUid();
     mdatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

     progressDialog = new  ProgressDialog(this);

    mtoolbar=findViewById(R.id.status_aapbar);
    //set toolbar
    setSupportActionBar(mtoolbar);
    getSupportActionBar().setTitle("Account Status");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String mstatus = getIntent().getStringExtra("status_v");

        //id find
        status_value=findViewById(R.id.status_enter);
        save_chang=findViewById(R.id.statuschangebtn);


        status_value.setText(mstatus);


   save_chang.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           //progressDialog
           progressDialog = new  ProgressDialog(StatusActivity.this);
           progressDialog.setTitle("Saving Changes");
           progressDialog.setMessage("Please wait while we Changes");
           progressDialog.show();
           String status = status_value.getText().toString().trim();

           mdatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {

                   if(task.isSuccessful()) {
                       progressDialog.dismiss();
                       Toast.makeText(StatusActivity.this,"Sucessfully save Your Staus",Toast.LENGTH_LONG).show();
                   }
                   else {

                       Toast.makeText(StatusActivity.this,"Error in Saving the Status",Toast.LENGTH_LONG).show();
                   }

               }
           });
       }
   });

   }


}
