package com.example.pankaj.lapichat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mdatabase;
    private FirebaseUser mcurrent_user;

    TextView Settings_name;
    TextView Settings_status;



   private  CircleImageView display_img;

    Button mstatusbtn;
    Button mimagebtn;

    //for gallery image
    private static  final int gallery_picker =1;

    //progress bar
    private ProgressDialog mprogressDialog;

    //storage firebase
    private StorageReference mimagestorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // view refrencess
        display_img=findViewById(R.id.Settings_image);
        Settings_name=findViewById(R.id.display_name);
        Settings_status=findViewById(R.id.text_status);

        mimagebtn=findViewById(R.id.image_btn);
        mstatusbtn=findViewById(R.id.status_btn);
        //firebase use  id
        mcurrent_user= FirebaseAuth.getInstance().getCurrentUser();

        String  uid = mcurrent_user.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        //for storage
        mimagestorage= FirebaseStorage.getInstance().getReference();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            String name = dataSnapshot.child("name").getValue().toString();
            String image = dataSnapshot.child("image").getValue().toString();
            String status = dataSnapshot.child("status").getValue().toString();
            String thumb_img =dataSnapshot.child("thumb_img").getValue().toString();

            Settings_name.setText(name);
            Settings_status.setText(status);
            if(!image.equals("default")){
            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.if_male).into(display_img);
            }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mstatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_values = Settings_status.getText().toString();
                Intent status_intent = new Intent(SettingsActivity.this,StatusActivity.class);
                status_intent.putExtra("status_v",status_values);
                startActivity(status_intent);
            }
        });
        mimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent();
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery_intent,"SELECT IMAGE"),gallery_picker);
               /* CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                                                                            */
            }
        });
    }

    //given imge
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallery_picker && resultCode== RESULT_OK){

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){

                mprogressDialog = new ProgressDialog(SettingsActivity.this);
                mprogressDialog.setTitle("Uploading Image....");
                mprogressDialog.setMessage("please wait while we upload and process the image");
                mprogressDialog.show();
                mprogressDialog.setCanceledOnTouchOutside(false);

                Uri resulturi = result.getUri();

                final File thumb_filepath = new File(resulturi.getPath());

                 final String current_user_id  = mcurrent_user.getUid();


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(60)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap .compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[]  thumb_byte  = baos.toByteArray();


                final StorageReference filpath = mimagestorage.child("Profile_images").child(current_user_id+".jpg");
                final StorageReference thumb_file = mimagestorage.child("Profile_images").child("thumbs ").child(current_user_id+".jpg");

                 filpath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override

                  public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            filpath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    final String download_url = task.getResult().toString();
                                    UploadTask uploadTask = thumb_file.putBytes(thumb_byte);

                                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            thumb_file.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {

                                                    String thumb_url = task.getResult().toString();

                                                    Map updatemp = new HashMap();
                                                    updatemp.put("image",download_url);
                                                    updatemp.put("thumb_img",thumb_url);

                                                    mdatabase.updateChildren(updatemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                mprogressDialog.dismiss();
                                                                Toast.makeText(SettingsActivity.this,"Success  uploading",Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });

                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(SettingsActivity.this,"Error in uplaoding ",Toast.LENGTH_LONG).show();
                            mprogressDialog.dismiss();
                        }
                    }
                });
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }


    }




}
