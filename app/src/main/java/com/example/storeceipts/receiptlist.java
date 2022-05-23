package com.example.storeceipts;



import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class receiptlist extends RecyclerView.Adapter<receiptlist.ViewHolder>{
    private final ArrayList<receipt> listdata;

    // RecyclerView recyclerView;
    public receiptlist(ArrayList<receipt> listdata) {
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.receiptlistitem, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final receipt myListData = listdata.get(position);
        String description = myListData.type + " from " + myListData.place;
        String date = description + "\n on" + myListData.datestored +" \n"+myListData.price;
        holder.textView.setText(date);
        Log.d("RECEIPT INFO", "onBindViewHolder: "+date);
        //holder.imageView.setImageResource(listdata.get(position).getImgId());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+date,Toast.LENGTH_LONG).show();
            }
        });


        // Initializing the ViewPager Object
        // Initializing the ViewPagerAdapter
        //if(!(myListData.receiptsPictures ==null)) {
            holder.mViewPagerAdapter = new ViewPagerAdapter(holder.itemView.getContext(), myListData.receiptsPictures);
            // Adding the Adapter to the ViewPager
            holder.mViewPager.setAdapter(holder.mViewPagerAdapter);
        //}



        holder.editR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(holder.itemView.getContext(), EditReceiptsActivity.class);
                update.putExtra("receipt-Id", myListData.receiptId);
                holder.itemView.getContext().startActivity(update);
            }
        });
        holder.delR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to delete activity and delete and come to the main activity and refresh adapter
                //FirebaseFirestore.getInstance().collection("receipts").document(myListData.receiptId).delete();
                //listdata.remove(myListData);
                //refresh adapter if possible
                //notify();

                Intent dlIntent = new Intent(holder.itemView.getContext(),DeleteReceiptActivity.class);
                dlIntent.putExtra("receiptId",myListData.receiptId);
                dlIntent.putExtra("month", myListData.month);
                dlIntent.putExtra("year", myListData.year);

                holder.itemView.getContext().startActivity(dlIntent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager imageView;
        public TextView textView;
        public LinearLayout linearLayout;
        // creating object of ViewPager
        public ViewPager mViewPager;
        // Creating Object of ViewPagerAdapter
        public ViewPagerAdapter mViewPagerAdapter;
        public Button editR;
        public Button delR;



        public ViewHolder(View itemView) {
            super(itemView);
          //  this.imageView = (ViewPager) itemView.findViewById(R.id.sliderimg);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linLayout);
            mViewPager = (ViewPager)itemView.findViewById(R.id.sliderimg);
            editR = itemView.findViewById(R.id.editBtn);
            delR = itemView.findViewById(R.id.delBtn);


        }
    }
}