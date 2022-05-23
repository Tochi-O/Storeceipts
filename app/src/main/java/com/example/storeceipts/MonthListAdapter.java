package com.example.storeceipts;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MonthListAdapter extends RecyclerView.Adapter<MonthListAdapter.ViewHolder>{
    private ArrayList<String> listdata;


    // RecyclerView recyclerView;
    public MonthListAdapter(ArrayList<String> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.month_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String myListData = listdata.get(position);
        holder.textView.setText(listdata.get(position));
        //holder.imageView.setImageResource(listdata[position].getImgId());
        holder.textView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), CollectionActivity.class);
                        intent.putExtra("month-name",myListData);
                        holder.itemView.getContext().startActivity(intent);
                    }
                }
        );
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+myListData,Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.monthTxt);
          //  relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}