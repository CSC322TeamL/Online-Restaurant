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

public class Adapter_handledispute extends RecyclerView.Adapter<viewholder_handlddispute>{
    Context c;
    ArrayList<model_handledispute>lmodel;

    public Adapter_handledispute(Context c, ArrayList<model_handledispute> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public viewholder_handlddispute onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_handlecomplaint,null);
        return new viewholder_handlddispute(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder_handlddispute holder, int i) {

        holder.dispute.setText(lmodel.get(i).getDispute());
        holder.userid.setText(lmodel.get(i).getUserid());
        holder.complaint.setText(lmodel.get(i).getComplaint());
        holder.dispute.setText(lmodel.get(i).getDispute());
        String backid = lmodel.get(i).getBackid();
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:5000" + "/handle_dispute_complaint";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                bodyBuilder.add("disputeID",backid);
                bodyBuilder.add("determination", "accept");
                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });
                Toast.makeText(v.getContext(),"The dispute was accept. Re-enter page to see change", Toast.LENGTH_SHORT).show();

            }
        });

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:5000" + "/handle_dispute_complaint";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                bodyBuilder.add("disputeID",backid);
                bodyBuilder.add("determination", "deny");
                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });
                Toast.makeText(v.getContext(),"The dispute was Deny. Re-enter page to see change", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return lmodel.size();
    }
}
