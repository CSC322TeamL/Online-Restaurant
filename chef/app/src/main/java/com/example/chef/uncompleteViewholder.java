package com.example.chef;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class uncompleteViewholder extends RecyclerView.ViewHolder {
    TextView tcustid;
    TextView tdishs;
    TextView tordernote;
    TextView tordertotal;
    TextView torderid;
    Button btn_finishorder;


    public uncompleteViewholder(@NonNull View itemView) {
        super(itemView);

        this.tcustid = itemView.findViewById(R.id.tv_order_custid);
        this.tdishs = itemView.findViewById(R.id.tv_order_dishs);
        this.tordernote = itemView.findViewById(R.id.tv_order_note);
        this.tordertotal = itemView.findViewById(R.id.tv_order_Total);
        this.torderid = itemView.findViewById(R.id.tv_order_id);
        this.btn_finishorder = itemView.findViewById(R.id.btn_uncomplete_done);
    }
}