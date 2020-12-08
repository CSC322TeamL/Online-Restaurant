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

public class AdapterCompliment extends RecyclerView.Adapter<Viewholder_frag4> {

    Context c;
    ArrayList<modelComplaint>lmodel;

    public AdapterCompliment(Context c, ArrayList<modelComplaint> lmodel) {
        this.c = c;
        this.lmodel = lmodel;
    }

    @NonNull
    @Override
    public Viewholder_frag4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.compliment_cardview,null);

        return new Viewholder_frag4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder_frag4 holder, int position) {
        holder.context.setText(lmodel.get(position).getContext());
        holder.subject.setText(lmodel.get(position).getSubject());

    }

    @Override
    public int getItemCount() {
        return lmodel.size();
    }
}
