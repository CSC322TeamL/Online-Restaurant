package com.example.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class complaint_viewholder extends RecyclerView.ViewHolder {
    TextView tfromid;
    TextView ttoid;
    TextView tsubject;
    TextView tcomtext;
    Button btn_accept;
    Button btn_deny;
    Button btn_warning;

    public complaint_viewholder(@NonNull View itemView) {
        super(itemView);

        this.tfromid = itemView.findViewById(R.id.tv_complaint_fromid);
        this.tcomtext = itemView.findViewById(R.id.tv_complaint_context);
        this.ttoid = itemView.findViewById(R.id.tv_complaint_toid);
        this.tsubject = itemView.findViewById(R.id.tv_complaint_Subjecy);
        this.btn_accept = itemView.findViewById(R.id.complaint_accept);
        this.btn_deny = itemView.findViewById(R.id.complaint_deny);
        this.btn_warning = itemView.findViewById(R.id.complaint_warning);
    }
}
