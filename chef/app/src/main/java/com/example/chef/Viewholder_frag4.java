package com.example.chef;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Viewholder_frag4 extends RecyclerView.ViewHolder {
    TextView subject;
    TextView context;

    public Viewholder_frag4(@NonNull View itemView) {
        super(itemView);
        this.subject = itemView.findViewById(R.id.tv_compliment_Subjecy);
        this.context = itemView.findViewById(R.id.tv_compliment_context);
    }
}
