package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.ReplyBean;

import java.util.ArrayList;

public class DiscussionRepliesAdapter extends RecyclerView.Adapter<DiscussionRepliesAdapter.DiscussionReplyViewHolder> {

    private ArrayList<ReplyBean> viewData;

    public DiscussionRepliesAdapter(ArrayList<ReplyBean> viewData) {
        setViewData(viewData);
    }

    @NonNull
    @Override
    public DiscussionReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reply_item, parent, false);
        return new DiscussionReplyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionReplyViewHolder holder, int position) {
        holder.getTvReplyContext().setText(viewData.get(position).getDetail().getContext());
        holder.getTvReplyDisplayName().setText(viewData.get(position).getDisplayName());
        holder.getTvReplyTime().setText(viewData.get(position).getDetail().getCreateDate());
    }

    @Override
    public int getItemCount() {
        if(null == this.viewData){
            return 0;
        }
        return this.viewData.size();
    }

    public void setViewData(ArrayList<ReplyBean> newList){
        if (null == this.viewData){
            this.viewData = new ArrayList<>(newList.size());
        }
        this.viewData.clear();
        this.viewData.addAll(newList);
    }

    public static class DiscussionReplyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvReplyContext, tvReplyDisplayName, tvReplyTime;

        public DiscussionReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReplyContext = (TextView) itemView.findViewById(R.id.tv_reply_card_context);
            tvReplyDisplayName = (TextView) itemView.findViewById(R.id.tv_reply_card_username);
            tvReplyTime = (TextView) itemView.findViewById(R.id.tv_reply_card_time);

        }

        public TextView getTvReplyContext() {
            return tvReplyContext;
        }

        public TextView getTvReplyDisplayName() {
            return tvReplyDisplayName;
        }

        public TextView getTvReplyTime() {
            return tvReplyTime;
        }
    }
}
