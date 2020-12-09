package com.example.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class viewholder_handlddispute extends RecyclerView.ViewHolder {

    TextView userid;
    TextView complaint;
    TextView dispute;
    Button accept;
    Button deny;

    public viewholder_handlddispute(@NonNull View itemView) {
        super(itemView);

        this.userid = (TextView)itemView.findViewById(R.id.tv_disputeuserid);
        this.complaint = (TextView)itemView.findViewById(R.id.tv_usercomplaint);
        this.dispute = (TextView)itemView.findViewById(R.id.tv_dispute_context);
        this.accept = (Button)itemView.findViewById(R.id.btn_acceptdispute);
        this.deny = (Button)itemView.findViewById(R.id.btn_denydispute);


    }
}
