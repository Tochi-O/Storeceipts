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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    String monthTitleStr;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<String> idList= new ArrayList<>();
    ArrayList<receipt> receiptArrayListMonth = new ArrayList<>();
    String cUserId;
    receipt newreceipt=new receipt();
    RecyclerView recyclerView;

    receiptlist adapter;

    static ArrayList<receipt> receiptArrayListt = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Intent monthTitle = getIntent();
        monthTitleStr=monthTitle.getStringExtra("month-name");
        cUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        firestore.collection("users").document(cUserId).collection(monthTitleStr).get().addOnCompleteListener(
//                new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
//                               // newreceipt=new receipt();
//
//                                newreceipt.receiptsPictures=new ArrayList<>();
//                                String docId = documentSnapshot.get("receiptId").toString();
//                                newreceipt.receiptId=docId;
//                                newreceipt.receiptsPictures = (ArrayList<String>) documentSnapshot.get("imagelist");
//                                Log.d("doc id", "onComplete: "+docId);
//                                idList.add(docId);
//                                firestore.collection("receipts").document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                      @Override
//                                      public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
//
//
//                                          if (task1.isSuccessful() && task1.getResult().exists()) {
//
//                                              DocumentSnapshot documentSnapshot1=task1.getResult();
//                                              newreceipt.place= (String) documentSnapshot1.get("place");
//                                              newreceipt.type= (String) documentSnapshot1.get("type");
//                                              newreceipt.day= (long) documentSnapshot1.get("day");
//                                              newreceipt.month= (String) documentSnapshot1.get("month");
//                                              newreceipt.year= (long) documentSnapshot1.get("year");
//                                              newreceipt.datestored= (String) documentSnapshot1.get("datestored");
//                                              newreceipt.searchField= (String) documentSnapshot1.get("searchField");
//                                              newreceipt.userId =(String) documentSnapshot1.get("userId");
//
//
////
////                                              firestore.collection("receipts").document(documentSnapshot1.getId()).collection("images")
////                                               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////
////
////                                                   @Override
////                                                   public void onComplete(@NonNull Task<QuerySnapshot> task3) {
////                                                   if (task3.isSuccessful()) {
////                                                   for (QueryDocumentSnapshot queryDocumentSnapshot : task3.getResult()) {
////                                                   newreceipt.receiptsPictures.add(queryDocumentSnapshot.get("image").toString());
////                                                    }
////                                                       Log.d("month images", "onComplete: receipt images a"+newreceipt.receiptsPictures);
////
////                                                       receiptArrayListMonth.add(newreceipt);
////
////                                                  }
////
////                                                       Log.d("month images", "onComplete: receipt id b"+newreceipt.receiptId);
////
////                                                      adapter.notifyDataSetChanged();
////
////                                                   }
////
////                                              });
////
////                                              receiptArrayListMonth.add(newreceipt);
////
////                                              Log.d("month images", "onComplete: receipt images b"+newreceipt.receiptsPictures);
////
////                                              Log.d("Number Monthly receipts", "onComplete: c" + receiptArrayListMonth.size());
//////                                               Log.d("Number Monthly receipts", "onComplete: " + newreceipt.price);
////                                          //    recyclerView = (RecyclerView) findViewById(R.id.permonthrecyclerView);
////                                               adapter = new receiptlist(receiptArrayListMonth);
////                                              recyclerView.setHasFixedSize(true);
////                                              recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
////                                              recyclerView.setAdapter(adapter);
////
////
//                                            }
//
//
//                                          receiptArrayListMonth.add(newreceipt);
//                                            newreceipt=new receipt();
//                                          //newreceipt = task1.getResult().toObject(receipt.class);
//                                          Log.d("Monthly receipts", "onComplete: " + newreceipt.place);
//
//                                          //assert newreceipt != null;
//                                          //  newreceipt.receiptId = task1.getResult().getId();
//                                          Log.d("Monthly receipts", "onComplete: " + newreceipt.receiptId);
//
//                                          //                                              receiptArrayListMonth.add(newreceipt);
//
//                                          Log.d("month images", "onComplete: receipt images b"+newreceipt.receiptsPictures);
//                                          adapter = new receiptlist(receiptArrayListMonth);
//                                          recyclerView.setHasFixedSize(true);
//                                          recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                          recyclerView.setAdapter(adapter);
//                                          //  Log.d("Number Monthly receipts", "onComplete: c" + receiptArrayListMonth.size());
////                                               Log.d("Number Monthly receipts", "onComplete: " + newreceipt.price);
//                                          //    recyclerView = (RecyclerView) findViewById(R.id.permonthrecyclerView);
//
//                                         }
//                                     }
//                                );
//
//
//                            }
//
//                        }
//                    }
//                }
//        );
//To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView name = findViewById(R.id.collecName);
        name.setText(monthTitleStr);

        //receiptArrayList= retrieveReceipts();
        //view adapter
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //show all receipts in the collection it came from
        receiptArrayListMonth = retrieveReceipts1();
         recyclerView = (RecyclerView) findViewById(R.id.permonthrecyclerView);
         adapter = new receiptlist(receiptArrayListMonth);

      //  adapter = new receiptlist(receiptArrayListMonth);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //


    }
    public  ArrayList<receipt> retrieveReceipts1(){

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        //receipt newreceipt = new receipt();

        firestore.collection("receipts").whereEqualTo("collecting",currUser.getUid()+monthTitleStr).get()
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

                                            Log.d("reee", "onComplete: "+receiptArrayListt.size());

                                           if (!receiptArrayListt.contains(newreceipt)){
                                               receiptArrayListt.add(newreceipt);
                                               adapter.notifyDataSetChanged();

                                           }
                                        }

                                    }
                                });

                               // if (!receiptArrayListt.contains(document.getId())) {
                                 //   receiptArrayListt.add(newreceipt);
                                  //  receiptArrayListt.add(document.getId());

                              //  }

                                Log.d("", document.getId() + " => " + document.getData());
                            }

                            adapter = new receiptlist(receiptArrayListt);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                            recyclerView.setAdapter(adapter);
                            Log.d("lENGTH", "onCreate: LISTRECEIPTS "+receiptArrayListt.size());


                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return receiptArrayListt;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                startActivity(new Intent(this,MonthsListActivity.class));
                Toast.makeText(this,"Back button pressed!",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}