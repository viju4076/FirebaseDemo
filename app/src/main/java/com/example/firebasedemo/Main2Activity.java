package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    EditText name;
    ListView list;
    ArrayList<String> languages=new ArrayList<>();
    Uri imageUri;
    public static final int IMAGE_REQUEST=2;
    public void add(View view)
    {
        String value=name.getText().toString();
        if(value.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Element cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else
        {
            FirebaseDatabase.getInstance().getReference().child("Languages").push().setValue(value);

        }
    }
public void logout(View view)
{
    FirebaseAuth mauth=FirebaseAuth.getInstance();
    mauth.signOut();
    Toast.makeText(getApplicationContext(),"Signout successful",Toast.LENGTH_SHORT).show();
    Intent intent=new Intent(Main2Activity.this,MainActivity.class);
    startActivity(intent);
    finish();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name=findViewById(R.id.value);
        FirebaseDatabase.getInstance().getReference().child("Languages").child("L3").setValue("Sanskrit");
        FirebaseDatabase.getInstance().getReference().child("Languages").child("L4").setValue("french");
        list=findViewById(R.id.list);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,languages);
        list.setAdapter(arrayAdapter);
        FirebaseDatabase.getInstance().getReference().child("Languages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                languages.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    languages.add(dataSnapshot1.getValue().toString()) ;
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void uploadImage(View view)
    {
        openImage();
    }
    public void upload()
    {

        final ProgressDialog pd=new ProgressDialog(Main2Activity.this);
        pd.setMessage("Uploading");
        pd.show();
       if(imageUri!=null)
       {
           final StorageReference fileRef= FirebaseStorage.getInstance().getReference().child("upload").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
         fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                 fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         String url=uri.toString();
                         Log.i("download url",url);
                     pd.dismiss();
            Toast.makeText(getApplicationContext(),"Image upload successful",Toast.LENGTH_SHORT).show();
                     }

                 });
             }
         });

       }
    }
    public void openImage()
    {
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==IMAGE_REQUEST&&resultCode==RESULT_OK)
    {
        imageUri=data.getData();
        Log.i("imageuri",imageUri.toString());

        upload();
    }
    }
}
