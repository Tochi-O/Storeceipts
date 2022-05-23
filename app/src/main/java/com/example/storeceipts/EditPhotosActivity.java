package com.example.storeceipts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditPhotosActivity extends AppCompatActivity {
    Button select, previous, next;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView total;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    Button saveimages;
    String receiptId;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    ArrayList<String> storageurl;
    FirebaseUser firebaseUser ;
    String collec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photos);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        receiptId = intent.getStringExtra("receiptId");
        collec = intent.getStringExtra("collec");
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();


        imageView = findViewById(R.id.image);
        previous = findViewById(R.id.previous);
        mArrayUri = new ArrayList<Uri>();
        select = findViewById(R.id.select);
        next = findViewById(R.id.next);
        saveimages = findViewById(R.id.saveallimages);

        saveimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSaveimages();
            }
        });

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });
        next = findViewById(R.id.next);

        // click here to select next image
        next.setOnClickListener(v -> {
            if (position < mArrayUri.size() - 1) {
                // increase the position by 1
                position++;
                imageView.setImageURI(mArrayUri.get(position));
            } else {
                Toast.makeText(EditPhotosActivity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
            }
        });

        // click here to view previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                }
            }
        });

        imageView = findViewById(R.id.image);

        // click here to select image
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if(data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void setSaveimages(){
        storageurl = new ArrayList<>();
        StorageReference storageRef = storage.getReference();


        for(Uri image: mArrayUri){
            Log.d("error 2 ", "onSuccess: App is working ");

            if(image != null) {
                Log.d("error 2 ", "onSuccess: App is working "+receiptId);

                //pd.show();
                String rimgId= UUID.randomUUID().toString();
                StorageReference childRef = storageRef.child(receiptId+"/"+ rimgId+".jpg");

                //uploading the image
                UploadTask uploadTask = childRef.putFile(image);



              //  Task<Uri> uri =  uploadTask.getResult().getTask().getSnapshot().getStorage().getDownloadUrl();

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        if(task.isSuccessful()) {
                            // pd.dismiss();
                            Task<Uri> uri = task.getResult().getStorage().getDownloadUrl();
                            //arecpt.receiptsPictures.add(taskSnapshot.getStorage().getDownloadUrl().toString());
                            uri.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (uri.isSuccessful()) {
                                        Log.d("Check where error", "onSuccess: " + uri.getResult().toString());

                                        String generatedFilePath = uri.getResult().toString();
                                        System.out.println("## Stored path is " + generatedFilePath);
                                        Log.d("Check where error 2 ", "onSuccess: " + uri.getResult().toString());

                                        storageurl.add(generatedFilePath);
                                        for (String rimg : storageurl) {
                                            Map<String, Object> data1 = new HashMap<>();
                                            data1.put("image", rimg);
                                            firestore.collection("receipts").document(receiptId)
                                                    .collection("images").add(data1);
                                        }
                                        Map<String, Object> data3 = new HashMap<>();
                                        data3.put("imagelist", storageurl);
                                        data3.put("receiptId", receiptId);

                                        firestore.collection("users").document(firebaseUser.getUid()).collection(collec).document(receiptId).set(data3, SetOptions.merge());
                                        startActivity(new Intent(EditPhotosActivity.this, MainActivity.class));
                                    }
                                }
                            });

                            Toast.makeText(EditPhotosActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pd.dismiss();
                        Toast.makeText(EditPhotosActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else {
                Toast.makeText(EditPhotosActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
            }

        }

    }
}