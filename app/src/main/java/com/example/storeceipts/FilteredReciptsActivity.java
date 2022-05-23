package com.example.storeceipts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.storeceipts.MainActivity.retrieveReceipts;

public class FilteredReciptsActivity extends AppCompatActivity {

    EditText minamount;
    EditText maxamount;
    RecyclerView amountRecycler;
    ArrayList<receipt> receiptArrayListPrice=new ArrayList<>();
    ArrayList<receipt> receiptArrayListFull=new ArrayList<>();
    Button saveFilter;

    TextView title;
    TextView lessresults;

    receiptlist adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_recipts);

        minamount = findViewById(R.id.minamount);
        maxamount = findViewById(R.id.maxamount);
        amountRecycler = findViewById(R.id.amountRecyclerView);
        title = findViewById(R.id.amountTl);
        lessresults = findViewById(R.id.resultsprice0);
        saveFilter = findViewById(R.id.amountsave);


        //get all receipts
        receiptArrayListFull = retrieveReceipts();
        //sort receipts by range
       // FirebaseFirestore.getInstance();

//To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //two enter text fields
        //for start and end range
        //if start range is 0 then less than end range
        // if end range is 0 then greater than end range.


        receiptArrayListFull=retrieveReceipts4();
        //and a send button
        saveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                receiptArrayListPrice.clear();

                if(!minamount.getText().toString().trim().isEmpty() && maxamount.getText().toString().trim().isEmpty()){
                    for(receipt receipt: receiptArrayListFull){
                        double minamnt = Double.parseDouble(minamount.getText().toString());
                        if(receipt.price>=minamnt ){
                            receiptArrayListPrice.add(receipt);
                        }
                        Log.d("search", "onCreate: min only price amount 2 "+minamnt);

                    }
                    adapter.notifyDataSetChanged();
                    if(receiptArrayListPrice.size()==0){
                        lessresults.setVisibility(View.VISIBLE);
                        amountRecycler.setVisibility(View.GONE);

                    }else {
                        //using adapter view search results.
//                                            adapter = new receiptlist(receiptArrayListSearch);
//                                            searchRecycler.setHasFixedSize(true);
//                                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                            searchRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        lessresults.setVisibility(View.GONE);
                        amountRecycler.setVisibility(View.VISIBLE);
                        Log.d("search", "onCreate: min only price results 2 "+receiptArrayListPrice);

                    }
                }else if(!maxamount.getText().toString().trim().isEmpty() && minamount.getText().toString().trim().isEmpty()) {

                    for(receipt receipt: receiptArrayListFull){
                        double maxamnt = Double.parseDouble(maxamount.getText().toString());
                        if(receipt.price<=maxamnt ){
                            receiptArrayListPrice.add(receipt);
                        }
                        Log.d("search", "onCreate: max only price amount 2 "+maxamnt);

                    }
                    adapter.notifyDataSetChanged();
                    if(receiptArrayListPrice.size()==0){
                        lessresults.setVisibility(View.VISIBLE);
                        amountRecycler.setVisibility(View.GONE);

                    }else {
                        //using adapter view search results.
//                                            adapter = new receiptlist(receiptArrayListSearch);
//                                            searchRecycler.setHasFixedSize(true);
//                                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                            searchRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        lessresults.setVisibility(View.GONE);
                        amountRecycler.setVisibility(View.VISIBLE);
                        Log.d("search", "onCreate: max only price results 2"+receiptArrayListPrice);

                    }
                }else if(!minamount.getText().toString().trim().isEmpty() && !maxamount.getText().toString().trim().isEmpty()){
                    for(receipt receipt: receiptArrayListFull){
                        double minamnt = Double.parseDouble(minamount.getText().toString());
                        double maxamnt = Double.parseDouble(maxamount.getText().toString());

                        if(receipt.price>=minamnt && receipt.price<=maxamnt ){
                            receiptArrayListPrice.add(receipt);
                        }
                        Log.d("search", "onCreate: min and max only price amount 2 "+minamnt+" "+maxamnt);

                    }
                    adapter.notifyDataSetChanged();
                    if(receiptArrayListPrice.size()==0){
                        lessresults.setVisibility(View.VISIBLE);
                        amountRecycler.setVisibility(View.GONE);

                    }else {
                        //using adapter view search results.
//                                            adapter = new receiptlist(receiptArrayListSearch);
//                                            searchRecycler.setHasFixedSize(true);
//                                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                            searchRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        lessresults.setVisibility(View.GONE);
                        amountRecycler.setVisibility(View.VISIBLE);
                        Log.d("search", "onCreate: price max and min results 2 filter "+receiptArrayListPrice);

                    }
                }

            }
        });

        if(receiptArrayListPrice.size()==0){
            lessresults.setVisibility(View.VISIBLE);
            amountRecycler.setVisibility(View.GONE);

        }else {
            //using adapter view search results.
             adapter = new receiptlist(receiptArrayListPrice);
            amountRecycler.setHasFixedSize(true);
            amountRecycler.setLayoutManager(new LinearLayoutManager(this));
            amountRecycler.setAdapter(adapter);
        }



    }

    public  ArrayList<receipt> retrieveReceipts4(){

        receiptArrayListFull=new ArrayList<receipt>();
        receiptArrayListPrice = new ArrayList<receipt>();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        //receipt newreceipt = new receipt();

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
                                            Log.d("search pictures", "onComplete: "+newreceipt.receiptsPictures);
                                        }


                                        if(!receiptArrayListFull.contains(newreceipt)){
                                            receiptArrayListFull.add(newreceipt);
                                            //  adapter.notifyDataSetChanged();

                                        }

                                        Log.d("search", "onCreate: price results 1"+receiptArrayListPrice);


                                        if(receiptArrayListPrice.size()==0){
                                            lessresults.setVisibility(View.VISIBLE);
                                            amountRecycler.setVisibility(View.GONE);

                                        }else {
                                            //using adapter view search results.
//                                            adapter = new receiptlist(receiptArrayListSearch);
//                                            searchRecycler.setHasFixedSize(true);
//                                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                            searchRecycler.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                            lessresults.setVisibility(View.GONE);
                                            amountRecycler.setVisibility(View.VISIBLE);
                                            Log.d("search", "onCreate: search results 2"+receiptArrayListFull);

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
//
//                                for(receipt rec: receiptArrayListAllOf){
//                                    for(String sr: searchfields.toLowerCase().split(" ")){
//                                        if(rec.searchField.toLowerCase().contains(sr.toLowerCase().trim())){
//                                            if(!receiptArrayListSearch.contains(rec)){
//                                                receiptArrayListSearch.add(rec);
//                                            }
//
//                                        }
//                                    }
//
//                                    Log.d("search", "onCreate: search results 1"+receiptArrayListSearch);
//
//                                }
//
//                                if(receiptArrayListSearch.size()==0){
//                                    lessresults.setVisibility(View.VISIBLE);
//                                    searchRecycler.setVisibility(View.GONE);
//
//                                }else {
//                                    //using adapter view search results.
                                adapter = new receiptlist(receiptArrayListPrice);
                                amountRecycler.setHasFixedSize(true);
                                amountRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                amountRecycler.setAdapter(adapter);
//                                    lessresults.setVisibility(View.GONE);
//                                    searchRecycler.setVisibility(View.VISIBLE);
                                Log.d("search", "onCreate: search results 2"+receiptArrayListFull);

                                //           }


                                Log.d("", document.getId() + " => " + document.getData());
                                Log.d("search", "onCreate: search results 0"+receiptArrayListFull);

                            }

//                            adapter = new receiptlist(receiptArrayListAll);
//                            searchRecycler.setHasFixedSize(true);
//                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                            searchRecycler.setAdapter(adapter);
                            Log.d("lENGTH", "onCreate: SEARCH RE "+receiptArrayListFull.size());


                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return receiptArrayListFull;

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