package com.example.manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Adapter_performance extends RecyclerView.Adapter<Viewholder_performance>{

    Context c;
    ArrayList<model_performance>lmodel;

    public Adapter_performance(Context c, ArrayList<model_performance> lmodel){
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public Viewholder_performance onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_performance,null);
        return new Viewholder_performance(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder_performance holder, int i) {
        holder.userid.setText(lmodel.get(i).getUserid());
        holder.promote.setText(lmodel.get(i).getPromote());
        holder.demote.setText(lmodel.get(i).getDemote());
        holder.compliment.setText(lmodel.get(i).getCompliment());
        holder.complaint.setText(lmodel.get(i).getComplaint());
        String backid = lmodel.get(i).getBackid();
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"The account is remove, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/de-register";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                bodyBuilder.add("userID", backid);
                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return lmodel.size();
    }
}
