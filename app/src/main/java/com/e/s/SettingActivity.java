package com.e.s;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    private Button Savebtn;
    private EditText usernameEt, userbioEt;
    private ImageView profileImageview;
    private static int galleryPick = 1;
    private Uri imageuri;
    private StorageReference userprofileimagerefrence;
    private String downloadurl;
    private DatabaseReference userref;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_setting);
        userprofileimagerefrence = FirebaseStorage.getInstance ().getReference ().child ("Profile Images");
        userref = FirebaseDatabase.getInstance ().getReference ().child ("Users");
        Savebtn = findViewById (R.id.bio_setting);
        progressDialog = new ProgressDialog (this);
        usernameEt = findViewById (R.id.username);
        userbioEt = findViewById (R.id.username_setting);
        profileImageview = findViewById (R.id.setting_profile_image);

        profileImageview.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent ();
                i.setAction (Intent.ACTION_GET_CONTENT);
                i.setType ("Image/+");
                startActivityForResult (i, galleryPick);

            }
        });

        Savebtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                reteriveUserInfo();
            }

        });
    }
    private void saveuserData()
    {
        final String getUserName=usernameEt.getText ().toString ();
        final String getUserStatus=userbioEt.getText ().toString ();

            if (imageuri==null)
            {
                userref.addValueEventListener (new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ()).hasChild ("image"))
                        {
                         saveinfoOnlywithoutimage();
                        }
                        else
                        {
                            Toast.makeText (SettingActivity.this, "please select an image", Toast.LENGTH_SHORT).show ();
                        }

                    }

                    private void saveinfoOnlywithoutimage()
                    {
                        final String getUserName=usernameEt.getText ().toString ();
                        final String getUserStatus=userbioEt.getText ().toString ();


                        if (getUserName.equals (""))
                    {
                        Toast.makeText (SettingActivity.this, "username fill it is mendatory", Toast.LENGTH_SHORT).show ();
                    }
                    else if (getUserStatus.equals (""))
                    {
                        Toast.makeText (SettingActivity.this, "fill it is mendatory", Toast.LENGTH_SHORT).show ();
                    }
                    else
                    {
                        progressDialog.setTitle ("Accounts setting");
                        progressDialog.setMessage ("Wait Account is updating");
                        progressDialog.show ();
                        HashMap<String,Object> profilemap=new HashMap<> ();
                        profilemap.put ("uid",FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
                        profilemap.put ("name",getUserName);
                        profilemap.put ("Status",getUserStatus);

                        userref.child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ())
                                .updateChildren (profilemap).addOnCompleteListener (new OnCompleteListener<Void> () {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful ())
                                {
                                    Intent i=new Intent (SettingActivity.this,MainActivity.class);
                                    startActivity (i);
                                    Toast.makeText (SettingActivity.this, "Profile has been updated", Toast.LENGTH_SHORT).show ();
                                }


                            }
                        });
                    }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if (getUserName.equals (""))
            {

            }
            else if (getUserStatus.equals (""))
            {

            }
            else
            {
                progressDialog.setTitle ("Accounts setting");
                progressDialog.setMessage ("Wait Account is updating");
                progressDialog.show ();

                final  StorageReference filepath=userprofileimagerefrence.child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
                final UploadTask uploadTask=filepath.putFile (imageuri);
                uploadTask.continueWithTask (new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                   if (task.isSuccessful ())
                   {
                       throw task.getException ();
                   }
                  downloadurl=filepath.getDownloadUrl ().toString ();
                   return filepath.getDownloadUrl ();
                    }
                }).addOnCompleteListener (new OnCompleteListener<Uri> () {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful ())
                        {
                            downloadurl=task.getResult ().toString ();

                            HashMap<String,Object> profilemap=new HashMap<> ();
                            profilemap.put ("uid",FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
                            profilemap.put ("name",getUserName);
                            profilemap.put ("Bio",getUserStatus);
                            profilemap.put ("Image",downloadurl);
                            userref.child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ())
                                    .updateChildren (profilemap).addOnCompleteListener (new OnCompleteListener<Void> () {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                if (task.isSuccessful ())
                                {
                                    Intent i=new Intent (SettingActivity.this,MainActivity.class);
                                    startActivity (i);
                                    finish ();
                                    progressDialog.dismiss ();
                                    Toast.makeText (SettingActivity.this, "Profile has been updated", Toast.LENGTH_SHORT).show ();
                                }


                                }
                            });
                        }
                    }
                });
            }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == galleryPick && requestCode ==   RESULT_OK )
        {
            //set  profile image here
            imageuri = data.getData ();
            profileImageview.setImageURI (imageuri);

        }

    }
    private  void reteriveUserInfo()
    {
        userref.child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ())
                .addValueEventListener (new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists ())
                        {
                            String imagedb=dataSnapshot.child ("image").getValue ().toString ();
                            String namedb=dataSnapshot.child ("name").getValue ().toString ();
                            String biodb=dataSnapshot.child ("Bio").getValue ().toString ();
                            usernameEt.setText (namedb);
                            userbioEt.setText (biodb);
                            Picasso.get ().load (imagedb).placeholder (R.drawable.profile_image).into (profileImageview);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}



