package com.example.chef;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdapterComplaint extends RecyclerView.Adapter<complaint_viewholder> {
    Context c;
    ArrayList<modelComplaint>lmodel;

    public AdapterComplaint(Context c, ArrayList<modelComplaint> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public complaint_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comlaint_cardview,null);

        return new complaint_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull complaint_viewholder holder, int position) {
        holder.tcontext.setText(lmodel.get(position).getContext());
        holder.tsubject.setText(lmodel.get(position).getSubject());
        String caseid = lmodel.get(position).getCaseid();
        holder.btn_dispute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),dispute_page.class);
                i.putExtra("complaintid", caseid);
                v.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lmodel.size();
    }
}
