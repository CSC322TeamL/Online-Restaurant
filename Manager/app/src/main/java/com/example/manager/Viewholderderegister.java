package com.example.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Viewholderderegister extends RecyclerView.ViewHolder {
    TextView balance;
    TextView spent;
    TextView warning;
    TextView userid;
    Button remove;

    public Viewholderderegister(@NonNull View itemView) {
        super(itemView);

        this.userid = itemView.findViewById(R.id.tv_disputeuserid);
        this.balance = itemView.findViewById(R.id.tv_usercomplaint);
        this.spent = itemView.findViewById(R.id.tv_dispute_context);
        this.warning = itemView.findViewById(R.id.tv_der_warnings);
        this.remove = itemView.findViewById(R.id.btn_denydispute);

    }
}
