package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.DiscussionHeaderListAdapter;
import com.example.simplerestaurant.Interfaces.UserDiscussionFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.beans.DiscussionBean;
import com.example.simplerestaurant.beans.DiscussionDetailBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDiscussionFragment extends Fragment implements View.OnClickListener {

    private ArrayList<DiscussionBean> allDiscussions;
    private RecyclerView discussionHeadRecycler;
    private DiscussionHeaderListAdapter adapter;
    private Button btnRefresh, btnNew;

    private UserDiscussionFragmentInterface listener;
    private String userID, userType;

    public UserDiscussionFragment(UserDiscussionFragmentInterface listener){
        super(R.layout.fragment_user_main_discussion);
        this.listener = listener;
        allDiscussions = new ArrayList<>();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();

        userID = bundle.getString("userID");
        userType = bundle.getString("userType");

        discussionHeadRecycler = (RecyclerView) view.findViewById(R.id.recycler_main_discussion);
        btnRefresh = (Button) view.findViewById(R.id.button_main_discussion_refresh);
        btnNew = (Button) view.findViewById(R.id.button_main_discussion_new);

        // hide the new discussion button when user is a surfer
        if(userID.equals("-1")){
            btnNew.setVisibility(View.GONE);
        }

        adapter = new DiscussionHeaderListAdapter(userID, userType, allDiscussions);
        discussionHeadRecycler.setAdapter(adapter);
        discussionHeadRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(null != listener){
            listener.getDiscussionFromServer(userID);
        }
    }

    public void discussionResponse(String res){
        try {
            JSONObject resJson = new JSONObject(res);
            DiscussionResponseBean responseBean = UnitTools.getGson()
                    .fromJson(resJson.getString("result").toString().trim()
                            , DiscussionResponseBean.class);
            this.allDiscussions.clear();
            this.allDiscussions.addAll(responseBean.getAllDiscussion());
            if(null != adapter){
                adapter.setDiscussionList(allDiscussions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_main_discussion_refresh:
                if(null != listener){
                    listener.getDiscussionFromServer(userID);
                }
                break;
            case R.id.button_main_discussion_new:
                break;
        }
    }

    class DiscussionResponseBean{
        private ArrayList<DiscussionBean> allDiscussion;
        private ArrayList<DiscussionBean> discussionCreated;
        private ArrayList<DiscussionBean> discussionReplied;

        public ArrayList<DiscussionBean> getAllDiscussion() {
            return allDiscussion;
        }

        public void setAllDiscussion(ArrayList<DiscussionBean> allDiscussion) {
            this.allDiscussion = allDiscussion;
        }

        public ArrayList<DiscussionBean> getDiscussionCreated() {
            return discussionCreated;
        }

        public void setDiscussionCreated(ArrayList<DiscussionBean> discussionCreated) {
            this.discussionCreated = discussionCreated;
        }

        public ArrayList<DiscussionBean> getDiscussionReplied() {
            return discussionReplied;
        }

        public void setDiscussionReplied(ArrayList<DiscussionBean> discussionReplied) {
            this.discussionReplied = discussionReplied;
        }

        @Override
        public String toString() {
            return "DiscussionResponseBean{" +
                    "allDiscussion=" + allDiscussion +
                    ", discussionCreated=" + discussionCreated +
                    ", discussionReplied=" + discussionReplied +
                    '}';
        }
    }
}
