package com.example.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Viewholder_performance extends RecyclerView.ViewHolder {
    TextView userid;
    TextView complaint;
    TextView compliment;
    TextView promote;
    TextView demote;
    Button btn_remove;


    public Viewholder_performance(@NonNull View itemView) {
        super(itemView);
        this.userid = itemView.findViewById(R.id.tv_performance_userid);
        this.complaint = itemView.findViewById(R.id.tv_performance_complaint);
        this.compliment = itemView.findViewById(R.id.tv_performance_compliment);
        this.demote = itemView.findViewById(R.id.tv_performance_demote);
        this.promote = itemView.findViewById(R.id.tv_performance_promote);
        this.btn_remove = itemView.findViewById(R.id.btn_performance_removeaccount);
    }
}
