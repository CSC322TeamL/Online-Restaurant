package com.example.chef;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewholder_pickorder extends RecyclerView.ViewHolder{

    TextView tcustid;
    TextView tdishs;
    TextView tordernote;
    TextView tordertotal;
    TextView torderid;
    Button btn_finishorder2;


    public viewholder_pickorder(@NonNull View itemView) {
        super(itemView);

        this.tcustid = itemView.findViewById(R.id.tv_order_custid2);
        this.tdishs = itemView.findViewById(R.id.tv_order_dishs2);
        this.tordernote = itemView.findViewById(R.id.tv_order_note2);
        this.tordertotal = itemView.findViewById(R.id.tv_order_Total2);
        this.torderid = itemView.findViewById(R.id.tv_order_id2);
        this.btn_finishorder2 = itemView.findViewById(R.id.btn_pickorder_Finished2);
    }
}