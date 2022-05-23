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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MonthsListActivity extends AppCompatActivity {

    ArrayList<String> listMonths=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_list);
//To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //get the list of months in the collection
        // on click go to collection of receipts.

        FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("users").document(cUser.getUid()).collection("Monthly Collections").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                   String month = (String) documentSnapshot.get("month-name");
                   if(!listMonths.contains(month)) {
                       listMonths.add(month);
                       Log.d("Month names", "onSuccess: "+month);
                   }

                }
              RecyclerView recyclerView = (RecyclerView) findViewById(R.id.monthListrecyclerView);
              MonthListAdapter adapter = new MonthListAdapter(listMonths);
              recyclerView.setHasFixedSize(true);
              recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
              recyclerView.setAdapter(adapter);
            }
        });



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