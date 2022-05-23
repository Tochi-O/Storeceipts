package com.example.storeceipts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class AddReceipts extends AppCompatActivity {



    receipt arecpt = new receipt();
    EditText editType;
    EditText editPlace;
    EditText editPrice;
    Button pckdt;
    Button select, previous, next;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView total;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    TextView datetxt;

    Button save;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
     Task<Uri> downloadUri;
    String receiptId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipts);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth =FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        datetxt = findViewById(R.id.datetxt);
        save = findViewById(R.id.saveall);
        editType = findViewById(R.id.type);
        editPlace = findViewById(R.id.place);
        editPrice = findViewById(R.id.price);
        pckdt = findViewById(R.id.datePickerbtn);
        total = findViewById(R.id.text);
      //  imageView = findViewById(R.id.image);
     //   previous = findViewById(R.id.previous);
        mArrayUri = new ArrayList<Uri>();
//        select = findViewById(R.id.select);
//To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                 firestoreReceipt();
            }
        });

        pckdt.setOnClickListener(v -> {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(AddReceipts.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String dtx = day + "/" + (month + 1) + "/" + year;
                            datetxt.setText(dtx);
                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });


//        // showing all images in imageswitcher
//        imageView.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                ImageView imageView1 = new ImageView(getApplicationContext());
//                return imageView1;
//            }
//        });
//        next = findViewById(R.id.next);
//
//        // click here to select next image
//        next.setOnClickListener(v -> {
//            if (position < mArrayUri.size() - 1) {
//                // increase the position by 1
//                position++;
//                imageView.setImageURI(mArrayUri.get(position));
//            } else {
//                Toast.makeText(AddReceipts.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // click here to view previous image
//        previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position > 0) {
//                    // decrease the position by 1
//                    position--;
//                    imageView.setImageURI(mArrayUri.get(position));
//                }
//            }
//        });
//
//        imageView = findViewById(R.id.image);
//
//        // click here to select image
//        select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // initialising intent
//                Intent intent = new Intent();
//
//                // setting type to select to be image
//                intent.setType("image/*");
//
//                // allowing multiple image to be selected
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
//            }
//        });

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

     public void firestoreReceipt(){
        FirebaseFirestore myDB;

       // Init FireStore
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] monthNames = symbols.getMonths();
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(dayOfMonth,month,year);
        arecpt.datestored =  cal.getTime().toString();
        arecpt.place = editPlace.getText().toString();
        arecpt.type = editType.getText().toString();
        arecpt.price = Double.parseDouble(editPrice.getText().toString());
        arecpt.day = dayOfMonth;
        arecpt.month = monthNames[month-1];
        arecpt.year = year;
        arecpt.userId = currentUser.getUid();
        arecpt.receiptsPictures = new ArrayList<>();
        arecpt.searchField = arecpt.place+" "+arecpt.type+" "+arecpt.month+" "+arecpt.year+" "+arecpt.price;
        arecpt.collecting = arecpt.userId+arecpt.month+" "+arecpt.year;
         final Task<Uri> downloadUri;
        myDB = FirebaseFirestore.getInstance();
       // Map<String, Object> data = new HashMap<>();
         StorageReference storageRef = storage.getReference();
         Log.d("error 1 ", "onSuccess: App is working "+receiptId);
         myDB.collection("receipts")
                 .add(arecpt).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentReference> task) {
                         Log.d("error 1 ", "onSuccess: App is working "+receiptId);

                         // data.put("task_name", edtData.getText().toString());
                         Log.d("error 1 ", "onSuccess: App is working ");

                         //receiptId = documentReference.getId();

                         receiptId = task.getResult().getId();

                         //add receipt to monthly collection in firestore.
                         Map<String, Object> data = new HashMap<>();
                         data.put("receiptId",receiptId);
                         myDB.collection("users").document(arecpt.userId).collection(arecpt.month+" "+arecpt.year)
                                 .add(data);
                         //add monthly collection names
                         Map<String, Object> data1 = new HashMap<>();
                         data1.put("month-name",arecpt.month+" "+arecpt.year);



                         myDB.collection("users").document(arecpt.userId).collection("Monthly Collections").document(arecpt.month+" "+arecpt.year).get().addOnCompleteListener(
                                 new OnCompleteListener<DocumentSnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()) {
                                            if (!task.getResult().exists()) {
                                                myDB.collection("users").document(arecpt.userId).collection("Monthly Collections").document(arecpt.month + " " + arecpt.year).set(data1);


                                            }
                                        }
                                     }
                                 }
                         );
                         Intent cont = new Intent(AddReceipts.this,EditPhotosActivity.class);
                         cont.putExtra("receiptId",receiptId);
                         cont.putExtra("collec",arecpt.month+" "+arecpt.year);
                         startActivity(cont);
                     }
                 });

//        myDB.collection("receipts")
//                .add(arecpt)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        //toastResult("Data added successfully");
//                        //add pictures to firebase storage
//
//                        Log.d("error 1 ", "onSuccess: App is working ");
//
//                        receiptId = documentReference.getId();
//
//
//                        //add receipt to monthly collection in firestore.
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("receiptId",receiptId);
//                        myDB.collection("users").document(arecpt.userId).collection(arecpt.month+" "+arecpt.year)
//                                .add(data);
//                        //add monthly collection names
//                        Map<String, Object> data1 = new HashMap<>();
//                        data1.put("month-name",arecpt.month+" "+arecpt.year);
//
//                        if( !myDB.collection("users").document(arecpt.userId).collection("Monthly Collections").document(arecpt.month+" "+arecpt.year).get().getResult().exists()){
//                            myDB.collection("users").document(arecpt.userId).collection("Monthly Collections").document(arecpt.month+" "+arecpt.year).set(data1);
//
//
//                        }
//                        Intent cont = new Intent(AddReceipts.this,EditPhotosActivity.class);
//                        cont.putExtra("receiptId",receiptId);
//                        startActivity(cont);
//                        //add pictures links to firestore collection for the receipt id
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                       // toastResult("Error while adding the data : " + e.getMessage());
//                    }
//                });


     }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                startActivity(new Intent(this,MainActivity.class));
                Toast.makeText(this,"Back button pressed!",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}