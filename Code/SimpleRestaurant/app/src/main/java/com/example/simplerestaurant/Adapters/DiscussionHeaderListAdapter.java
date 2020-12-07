package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.DiscussionBean;

import java.util.ArrayList;

public class DiscussionHeaderListAdapter extends RecyclerView.Adapter<DiscussionHeaderListAdapter.DiscussionHeaderViewHolder> {
    private String userID, userType;
    private ArrayList<DiscussionBean> viewData;

    public DiscussionHeaderListAdapter(String userID, String userType, ArrayList<DiscussionBean> viewData) {
        this.userID = userID;
        this.userType = userType;
        setDiscussionList(viewData);
    }

    public void setDiscussionList(ArrayList<DiscussionBean> newList){
        if(null == this.viewData){
            viewData = new ArrayList<>(newList.size());
        }
        viewData.clear();
        viewData.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiscussionHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_discussion_main_item, parent, false);
        return new DiscussionHeaderViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionHeaderViewHolder holder, int position) {
        holder.getTvTitle().setText(viewData.get(position).getTitle());
        holder.getTvContext().setText(viewData.get(position).getDetail().getContext());
    }

    @Override
    public int getItemCount() {
        if(null == viewData){
            return 0;
        }
        return viewData.size();
    }

    public static class DiscussionHeaderViewHolder extends RecyclerView.ViewHolder{
        private View card;
        private TextView tvTitle, tvContext;
        public DiscussionHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_main_discussion_card_title);
            tvContext = (TextView) itemView.findViewById(R.id.tv_main_discussion_card_context);
            card = (View) itemView.findViewById(R.id.view_main_discussion_card);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvContext() {
            return tvContext;
        }
    }
}
