package com.example.chef;

import android.app.AlertDialog;
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

public class AdapterMenu extends RecyclerView.Adapter<viewholderMenu>{


    Context c;
    ArrayList<modelMenu> lmodel;


    public AdapterMenu (Context c, ArrayList<modelMenu> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public viewholderMenu onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_cardview,null);

        return new viewholderMenu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderMenu holder, int position) {
        holder.dishname.setText(lmodel.get(position).getDishname());
        holder.price.setText(lmodel.get(position).getPrice());
        holder.describe.setText(lmodel.get(position).getDescribe());
        holder.imgpath.setImageResource(lmodel.get(position).getImgpath());
        holder.rating.setText(lmodel.get(position).getRaiting());
        String dishid = (lmodel.get(position).getDishid());
        String price = (lmodel.get(position).getPrice1());
        String describe = (lmodel.get(position).getDescribe1());
        String name = (lmodel.get(position).getName1());
        String key = (lmodel.get(position).getKey());


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), editItemMenus_chef.class);
                intent.putExtra("dishid", dishid);
                intent.putExtra("price", price);
                intent.putExtra("name", name);
                intent.putExtra("describe", describe);
                intent.putExtra("key", key);
                v.getContext().startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"The action done, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/delete_dish";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                bodyBuilder.add("dishID", dishid);

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
