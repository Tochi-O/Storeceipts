package com.example.storeceipts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {




    static ArrayList<receipt> receiptArrayList =new  ArrayList<receipt>();
    static ArrayList<String> receiptArrayListt =new  ArrayList<>();

    receipt oneReceipt = new receipt();
    static FirebaseUser currUser=FirebaseAuth.getInstance().getCurrentUser();
    static  FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    Button gomonth, pricefil,addbtn,logoutbtn;
    ImageView srch;
    String searchinput;
    static RecyclerView recyclerView;
    static receiptlist adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        srch = findViewById(R.id.searchicon);
        gomonth = findViewById(R.id.gotomonth);
        pricefil = findViewById(R.id.pricebtn);
        addbtn = findViewById(R.id.newRecbtn);
        logoutbtn = findViewById(R.id.logoutbtn);
        receiptArrayList = new ArrayList<receipt>();
//To have the back button!!
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);



        FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
        if (currentU==null){
            Intent intent
                    = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);

        }

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent
                        = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

       pricefil.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this, FilteredReciptsActivity.class);
               startActivity(intent);

           }
       });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReceipts.class);
                startActivity(intent);
            }
        });

        gomonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthsListActivity.class);
                startActivity(intent);
            }
        });





        receiptArrayList= retrieveReceipts();
        //view adapter
         recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new receiptlist(receiptArrayList);
//        Log.d("lENGTH", "onCreate: LISTRECEIPTS "+retrieveReceipts().size());
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
//        recyclerView.setAdapter(adapter);



        //add reciept button to addreceiptactivity

        //search icon to dialog then search results view.
        srch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("SEARCH");
                        alertDialog.setMessage("Enter Search Inputs");

                        final EditText input = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);
                        //alertDialog.setIcon(R.drawable.key);

                        alertDialog.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        searchinput = input.getText().toString();

                                        if(!searchinput.isEmpty()){
                                            Intent srchIntent = new Intent(getApplicationContext(), FilterReceiptsActivity.class);
                                            srchIntent.putExtra("search-fields",searchinput);
                                            startActivity(srchIntent);

                                        }



                                        //take to search page with results



//                                        password = input.getText().toString();
//                                        if (password.compareTo("") == 0) {
//                                            if (pass.equals(password)) {
//                                                Toast.makeText(getApplicationContext(),
//                                                        "Password Matched", Toast.LENGTH_SHORT).show();
//
//                                            } else {
//                                                Toast.makeText(getApplicationContext(),
//                                                        "Wrong Password!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
                                    }
                                });

                        alertDialog.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();
                    }

                });

    }



    public static ArrayList<receipt> retrieveReceipts(){

         //receipt newreceipt = new receipt();
        receiptArrayList = new ArrayList<receipt>();
        firestore.collection("receipts").whereEqualTo("userId",currUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final receipt newreceipt = document.toObject(receipt.class);
                                newreceipt.receiptId = document.getId();

                                firestore.collection("receipts").document(document.getId()).collection("images")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            newreceipt.receiptsPictures = new ArrayList<>();
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                newreceipt.receiptsPictures.add(queryDocumentSnapshot.get("image").toString());
                                            }
                                            Log.d("pictures", "onComplete: "+newreceipt.receiptsPictures);
                                        }


                                        if(!receiptArrayListt.contains(newreceipt)){
                                            receiptArrayList.add(newreceipt);
                                            adapter.notifyDataSetChanged();

                                        }
                                    }
                                });

//                                if (! receiptArrayListt.contains(document.getId())) {
//                                   // receiptArrayList.add(newreceipt);
//                                    receiptArrayListt.add(document.getId());
//                                    Log.d("pictures", "onComplete: "+newreceipt.receiptsPictures);
//
//
//                                }

                                Log.d("", document.getId() + " => " + document.getData());
                            }

                            adapter = new receiptlist(receiptArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                            recyclerView.setAdapter(adapter);
                            Log.d("lENGTH", "onCreate: LISTRECEIPTS "+receiptArrayList.size());


                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return receiptArrayList;

    }
}



