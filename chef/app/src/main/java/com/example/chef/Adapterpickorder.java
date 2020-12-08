package com.example.chef;

import android.content.Context;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

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


public class Adapterpickorder extends RecyclerView.Adapter<viewholder_pickorder> {


    Context c;
    ArrayList<model> lmodel;

    public Adapterpickorder(Context c, ArrayList<model> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public viewholder_pickorder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pickorder_cardview,null);

        return new viewholder_pickorder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewholder_pickorder holder, int i) {

        holder.tcustid.setText(lmodel.get(i).getCustid());
        holder.tdishs.setText(lmodel.get(i).getDishs());
        holder.torderid.setText(lmodel.get(i).getOrderid());
        holder.tordertotal.setText(lmodel.get(i).getOrdertotal());
        String orderid = (lmodel.get(i).getOrderid());
        String ordernumber = (lmodel.get(i).getOrdernumber());

        holder.btn_finishorder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Order Marked finished, click any button to refresh page.", Toast.LENGTH_SHORT).show();
                String url = "http://10.0.2.2:5000" + "/order_prepared";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String userid = getuserid.userid;
                String role = "chef";
                bodyBuilder.add("role", role);
                bodyBuilder.add("userID", userid);
                bodyBuilder.add("orderID", ordernumber);

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
