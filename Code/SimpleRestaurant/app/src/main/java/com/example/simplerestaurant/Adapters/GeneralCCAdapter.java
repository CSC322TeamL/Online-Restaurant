package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.CCBean;

import java.util.ArrayList;

public class GeneralCCAdapter extends RecyclerView.Adapter<GeneralCCAdapter.CCViewHolder>{
    private boolean showDispute = false;
    private ArrayList<CCBean> viewData;

    public GeneralCCAdapter(boolean showDispute, ArrayList<CCBean> viewData) {
        this.showDispute = showDispute;
        setViewData(viewData);
    }

    public void setShowDispute(Boolean showDispute){
        this.showDispute = showDispute;
    }

    @NonNull
    @Override
    public CCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_general_cc_item, parent, false);
        return new CCViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CCViewHolder holder, int position) {
        holder.getTvTitle().setText(viewData.get(position).getSubject());
        holder.getTvContext().setText(viewData.get(position).getContext());
        holder.getTvDate().setText(viewData.get(position).getCreateDate());
        holder.getTvStatus().setText(viewData.get(position).getStatus());
        if(showDispute){
            holder.getvDispute().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(null == viewData){
            return 0;
        }
        return viewData.size();
    }

    public void setViewData(ArrayList<CCBean> newList){
        viewData = new ArrayList<>(newList.size());;
        viewData.addAll(newList);
        this.notifyDataSetChanged();
    }


    public static class CCViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvContext, tvDate, tvStatus, tvDispute;
        private View vDispute;
        public CCViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_cc_general_title);
            tvContext = (TextView) itemView.findViewById(R.id.tv_cc_general_context);
            tvDate = (TextView) itemView.findViewById(R.id.tv_cc_general_date);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_cc_general_status);
            tvDispute = (TextView) itemView.findViewById(R.id.tv_cc_general_dispute);
            vDispute = (View) itemView.findViewById(R.id.view_cc_general_dispute);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvContext() {
            return tvContext;
        }

        public TextView getTvDate() {
            return tvDate;
        }

        public TextView getTvStatus() {
            return tvStatus;
        }

        public TextView getTvDispute() {
            return tvDispute;
        }

        public View getvDispute() {
            return vDispute;
        }
    }
}
