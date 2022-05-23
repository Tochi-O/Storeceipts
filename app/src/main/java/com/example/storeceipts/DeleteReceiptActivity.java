package com.example.storeceipts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteReceiptActivity extends AppCompatActivity {


    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_receipt);



        //dialog to confirm
        Intent intent = getIntent();
        String delId = intent.getStringExtra("delId");
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete your receipt?").setTitle("Confirm delete");

        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure you want to delete your receipt?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseFirestore.getInstance().collection("receipts").document(delId).delete();
                        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection(month+" "+year).document(delId).delete();

                        Intent intent1 = new Intent(DeleteReceiptActivity.this, MainActivity.class);
                        startActivity(intent1);
                        Toast.makeText(getApplicationContext(),"Deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Intent intent1 = new Intent(DeleteReceiptActivity.this, MainActivity.class);
                        startActivity(intent1);
                        Toast.makeText(getApplicationContext(),"Canceled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Confirm delete");
        alert.show();








    }
}