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

public class AdapterComplaint extends RecyclerView.Adapter<complaint_viewholder> {


    Context c;
    ArrayList<model> lmodel;

    public AdapterComplaint(Context c, ArrayList<model> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public complaint_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.complaint_cardview,null);

        return new complaint_viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull complaint_viewholder holder, int i) {

        holder.tfromid.setText(lmodel.get(i).getFromid());
        holder.ttoid.setText(lmodel.get(i).getToid());
        holder.tsubject.setText(lmodel.get(i).getSubject());
        holder.tcomtext.setText(lmodel.get(i).getContext());
        String complaintid = (lmodel.get(i).getCaseid());
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"The action done, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/handle_ComplaintAndCompliment";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String userid = "manager1";
                bodyBuilder.add("userID", userid);
                bodyBuilder.add("complaintID", complaintid);
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

            }
        });
        holder.btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"The action done, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/handle_ComplaintAndCompliment";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String userid = "manager1";
                bodyBuilder.add("userID", userid);
                bodyBuilder.add("complaintID", complaintid);
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

            }
        });
        holder.btn_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),"The action done, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/handle_ComplaintAndCompliment";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String userid = "manager1";
                bodyBuilder.add("userID", userid);
                bodyBuilder.add("complaintID", complaintid);
                bodyBuilder.add("determination", "warning");
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
