package com.example.chef;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class complaint_viewholder extends RecyclerView.ViewHolder {
    TextView ttoid;
    TextView tsubject;
    TextView tcontext;
    Button btn_dispute;


    public complaint_viewholder(@NonNull View itemView) {
        super(itemView);
        this.tsubject = itemView.findViewById(R.id.tv_complaint_Subjecy);
        this.tcontext = itemView.findViewById(R.id.tv_complaint_context);
        this.btn_dispute = itemView.findViewById(R.id.complaint_dispute);
    }
}
