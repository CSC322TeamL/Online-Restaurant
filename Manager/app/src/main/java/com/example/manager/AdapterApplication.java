package com.example.manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdapterApplication extends  RecyclerView.Adapter<viewholderApplication>{
    Context c;
    ArrayList<modelApplication>lmodel;

    public AdapterApplication(Context c, ArrayList<modelApplication> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }


    @NonNull
    @Override
    public viewholderApplication onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_application,null);
        return new viewholderApplication(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderApplication holder, int i) {
        holder.context.setText(lmodel.get(i).getContext());
        holder.email.setText(lmodel.get(i).getEmail());
        String email = lmodel.get(i).getEmail();
        holder.btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:5000" + "/handle_NewCustomer";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                bodyBuilder.add("requesterEmail", email);
                bodyBuilder.add("userID", "-1");
                bodyBuilder.add("determination", "determination");
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

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:5000" + "/handle_NewCustomer";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                bodyBuilder.add("requesterEmail", email);
                bodyBuilder.add("userID", "-1");
                bodyBuilder.add("determination", "determination");
                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });

                String url2 = "http://10.0.2.2:5000" + "/new_user";
                OkHttpClient client2 = new OkHttpClient();
                FormBody.Builder bodyBuilder2 = new FormBody.Builder();
                bodyBuilder2.add("userID", email);
                bodyBuilder2.add("userPassword", "000000");
                bodyBuilder2.add("role", "Customer");
                Request request1 = new Request.Builder().url(url2).post(bodyBuilder2.build()).build();
                client2.newCall(request1).enqueue(new Callback() {
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
