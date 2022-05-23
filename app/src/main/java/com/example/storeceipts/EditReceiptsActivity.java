package com.example.storeceipts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class EditReceiptsActivity extends AppCompatActivity {


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
    String rId;

    TextView dttxt;
    Button update;
    String dtx;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipts);

        Intent rInt= getIntent();
        rId = rInt.getStringExtra("receipt-Id");

//To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        dtx = "";
        editType = findViewById(R.id.typeed);
        editPlace = findViewById(R.id.placeed);
        editPrice  = findViewById(R.id.priceed);
        pckdt = findViewById(R.id.datePickerbtned);
        update = findViewById(R.id.updateall);
        dttxt = findViewById(R.id.datetxted);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editReceipt();
            }
        });
        pckdt.setOnClickListener(v -> {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(EditReceiptsActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                             dtx = day + "/" + (month + 1) + "/" + year;
                           // arecpt.datestored = dtx;
                            dttxt.setText(dtx);
                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });



        //get information for a receipt from and fill in these fields
        firestore.collection("receipts").document(rId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                arecpt = documentSnapshot.toObject(receipt.class);
                Date date = new Date();
                if(arecpt!=null) {
                    try {
                         date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(arecpt.month);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar calend = Calendar.getInstance();
                    calend.setTime(date);
                   // println(cal.get(Calendar.MONTH));

                    String pr = String.valueOf(arecpt.price);
                    editType.setText(arecpt.type);
                    editPlace.setText(arecpt.place);
                    editPrice.setText(pr);
                    year = (int) arecpt.year;
                    dayOfMonth = (int) arecpt.day;
                    month = calend.get(Calendar.MONTH);
                    dttxt.setText(arecpt.datestored);




                }
            }
        });




        //text fields with information
        //update button


    }


    void editReceipt(){
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] monthNames = symbols.getMonths();
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(dayOfMonth,month,year);
        arecpt.type = editType.getText().toString();
        arecpt.place = editPlace.getText().toString();
        arecpt.price = Double.parseDouble(editPrice.getText().toString());
        arecpt.datestored = dtx;
        arecpt.year = year;
        arecpt.month = monthNames[month-1];
        arecpt.day = dayOfMonth;
        arecpt.searchField = arecpt.place+" "+arecpt.type+" "+arecpt.month+" "+arecpt.year+" "+arecpt.price;
       // arecpt.userId = FirebaseAuth.getInstance().getUid();
        firestore.collection("receipts").document(rId).set(arecpt, SetOptions.merge());

        Intent intent = new Intent(EditReceiptsActivity.this,MainActivity.class);
        startActivity(intent);

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