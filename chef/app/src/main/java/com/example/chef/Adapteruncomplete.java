package com.example.chef;

import android.content.Context;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
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


public class Adapteruncomplete extends RecyclerView.Adapter<uncompleteViewholder> {


    Context c;
    ArrayList<model> lmodel;

    public Adapteruncomplete(Context c, ArrayList<model> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }



    @NonNull
    @Override
    public uncompleteViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ordercardview,null);

        return new uncompleteViewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull uncompleteViewholder holder, int i) {

        holder.tcustid.setText(lmodel.get(i).getCustid());
        holder.tdishs.setText(lmodel.get(i).getDishs());
        holder.torderid.setText(lmodel.get(i).getOrderid());
        holder.tordertotal.setText(lmodel.get(i).getOrdertotal());
        String orderid = (lmodel.get(i).getOrderid());
        String ordernumber = (lmodel.get(i).getOrdernumber());



        holder.btn_finishorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"The action done, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/pick_order";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String userid = getuserid.userid;
                String role = "chef";
                bodyBuilder.add("role", role);
                bodyBuilder.add("userID",userid);
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
