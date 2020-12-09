package com.example.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class viewholderApplication extends RecyclerView.ViewHolder {
    TextView context;
    TextView email;
    Button btn_accept;
    Button btn_deny;



    public viewholderApplication(@NonNull View itemView) {
        super(itemView);

        this.context = (TextView)itemView.findViewById(R.id.tv_application_context);
        this.email = (TextView)itemView.findViewById(R.id.tv_application_email);
        this.btn_accept = (Button)itemView.findViewById(R.id.application_accept);
        this.btn_deny = (Button)itemView.findViewById(R.id.appliication_deny);
    }
}
