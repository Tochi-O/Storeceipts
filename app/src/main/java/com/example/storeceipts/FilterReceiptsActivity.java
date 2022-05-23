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
import android.widget.TextView;
import android.widget.Toast;

import com.example.storeceipts.MainActivity.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.storeceipts.MainActivity.retrieveReceipts;

public class FilterReceiptsActivity extends AppCompatActivity {
    String searchfields="";
    ArrayList<receipt> receiptArrayListSearch=new ArrayList<>();
    ArrayList<receipt> receiptArrayListAll=new ArrayList<>();
    ArrayList<receipt> receiptArrayListAllOf=new ArrayList<>();


    TextView title;
    RecyclerView searchRecycler;
    TextView lessresults;
    receiptlist adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_receipts);
        title = findViewById(R.id.searchTl);
        searchRecycler = findViewById(R.id.searchRecyclerView);
        lessresults = findViewById(R.id.results0);

        //dialog to ask for search parameters

        Intent searchFieldIntn = getIntent();

        searchfields = searchFieldIntn.getStringExtra("search-fields");

        title.setText("Search Results For "+ searchfields);

//To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //in firestore get user receipts
        receiptArrayListAllOf = retrieveReceipts3();
    // sort out of those receipts that have search fields that contain the values of search field
        for(receipt rec: receiptArrayListAllOf){
            for(String sr: searchfields.toLowerCase().split(" ")){
                if(rec.searchField.toLowerCase().contains(sr.toLowerCase().trim())){
                    if(!receiptArrayListSearch.contains(rec)){
                        receiptArrayListSearch.add(rec);
                    }

                }
            }

            Log.d("search", "onCreate: search results 1"+receiptArrayListSearch);

        }

        if(receiptArrayListSearch.size()==0){
            lessresults.setVisibility(View.VISIBLE);
            searchRecycler.setVisibility(View.GONE);

        }else {
            //using adapter view search results.
             adapter = new receiptlist(receiptArrayListSearch);
            searchRecycler.setHasFixedSize(true);
            searchRecycler.setLayoutManager(new LinearLayoutManager(this));
            searchRecycler.setAdapter(adapter);
            Log.d("search", "onCreate: search results 2"+receiptArrayListSearch);

        }







    }

    public  ArrayList<receipt> retrieveReceipts3(){

        receiptArrayListSearch=new ArrayList<receipt>();
        receiptArrayListAllOf = new ArrayList<receipt>();
        receiptArrayListAll = new ArrayList<receipt>();

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


                                        if(!receiptArrayListAll.contains(newreceipt)){
                                            receiptArrayListAll.add(newreceipt);
                                          //  adapter.notifyDataSetChanged();

                                        }
                                        for(receipt rec: receiptArrayListAllOf){
                                            for(String sr: searchfields.toLowerCase().split(" ")){
                                                if(rec.searchField.toLowerCase().contains(sr.toLowerCase().trim())){
                                                    if(!receiptArrayListSearch.contains(rec)){
                                                        receiptArrayListSearch.add(rec);
                                                    }

                                                }
                                            }

                                            Log.d("search", "onCreate: search results 1"+receiptArrayListSearch);

                                        }

                                        if(receiptArrayListSearch.size()==0){
                                            lessresults.setVisibility(View.VISIBLE);
                                            searchRecycler.setVisibility(View.GONE);

                                        }else {
                                            //using adapter view search results.
//                                            adapter = new receiptlist(receiptArrayListSearch);
//                                            searchRecycler.setHasFixedSize(true);
//                                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                            searchRecycler.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                            lessresults.setVisibility(View.GONE);
                                            searchRecycler.setVisibility(View.VISIBLE);
                                            Log.d("search", "onCreate: search results 2"+receiptArrayListSearch);

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
                                    adapter = new receiptlist(receiptArrayListSearch);
                                    searchRecycler.setHasFixedSize(true);
                                    searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    searchRecycler.setAdapter(adapter);
//                                    lessresults.setVisibility(View.GONE);
//                                    searchRecycler.setVisibility(View.VISIBLE);
                                    Log.d("search", "onCreate: search results 2"+receiptArrayListSearch);

                     //           }


                                Log.d("", document.getId() + " => " + document.getData());
                                Log.d("search", "onCreate: search results 0"+receiptArrayListSearch);

                            }

//                            adapter = new receiptlist(receiptArrayListAll);
//                            searchRecycler.setHasFixedSize(true);
//                            searchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                            searchRecycler.setAdapter(adapter);
                            Log.d("lENGTH", "onCreate: SEARCH RE "+receiptArrayListAll.size());


                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return receiptArrayListAll;

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