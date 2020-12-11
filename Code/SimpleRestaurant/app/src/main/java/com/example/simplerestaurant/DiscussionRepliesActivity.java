package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.DiscussionRepliesAdapter;
import com.example.simplerestaurant.beans.DiscussionBean;
import com.example.simplerestaurant.beans.DiscussionDetailBean;
import com.example.simplerestaurant.beans.ReplyBean;
import com.example.simplerestaurant.beans.SimpleResponseBean;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscussionRepliesActivity extends BaseActivity implements View.OnClickListener{

    private String userID, userType;
    private DiscussionBean currentDiscussion;

    private TextView tvDiscussionOn, tvHeadTitle, tvHeadContext, tvHeadUser, tvHeadTime;
    private EditText etReply;
    private View vReplyGroup;
    private ImageButton imgbtnSubmit;
    private RecyclerView repliesRecycler;
    private DiscussionRepliesAdapter adapter;
    private ArrayList<ReplyBean> replies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_replies);

        Intent intent = getIntent();
        String discJson = intent.getStringExtra("disc");
        currentDiscussion = UnitTools.getGson().fromJson(discJson, DiscussionBean.class);
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");

        tvDiscussionOn = (TextView)findViewById(R.id.tv_disc_reply_on);
        tvHeadTitle = (TextView)findViewById(R.id.tv_disc_reply_head_title);
        tvHeadTime = (TextView)findViewById(R.id.tv_disc_reply_head_time);
        tvHeadContext = (TextView)findViewById(R.id.tv_disc_reply_head_context);
        tvHeadUser = (TextView)findViewById(R.id.tv_disc_reply_head_username);
        vReplyGroup = (View) findViewById(R.id.view_disc_reply_group);

        tvDiscussionOn.setText(currentDiscussion.getDiscussionOn());
        tvHeadTitle.setText(currentDiscussion.getTitle());
        tvHeadContext.setText(currentDiscussion.getDetail().getContext());
        tvHeadUser.setText(currentDiscussion.getDisplayName());
        tvHeadTime.setText(currentDiscussion.getDetail().getCreateDate());

        if(userID.equals("-1")){
            vReplyGroup.setVisibility(View.GONE);
        }

        etReply = (EditText) findViewById(R.id.et_disc_reply_reply_context);
        imgbtnSubmit = (ImageButton) findViewById(R.id.imgbtn_disc_reply_submit);
        imgbtnSubmit.setOnClickListener(this);

        repliesRecycler = (RecyclerView) findViewById(R.id.recycler_disc_reply_replies);

        replies = new ArrayList<>();
        adapter = new DiscussionRepliesAdapter(replies);
        repliesRecycler.setAdapter(adapter);
        repliesRecycler.setLayoutManager(new LinearLayoutManager(this));

        getRepliesFromServer();
    }

    private void handleRepliesResponse(String res){
        try {
            JSONObject jsonObject = new JSONObject(res);
            Type type = new TypeToken<ArrayList<ReplyBean>>(){}.getType();
            ArrayList<ReplyBean> beans = UnitTools.getGson()
                    .fromJson(jsonObject.getString("result").toString(), type);
            if(null == this.replies){
                this.replies = new ArrayList<>(beans.size());
            }
            replies.clear();
            replies.addAll(beans);
            if(null != adapter){
                adapter.setViewData(replies);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getRepliesFromServer(){
        String url = getString(R.string.base_url) + "/get_replies";
        // get OkHttp3 client from the base activity
        OkHttpClient client = UnitTools.getOkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("_id", currentDiscussion.get_id());
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleRepliesResponse(res);
                    }
                });
            }
        });
    }

    private void onSubmitReplyResponse(String res){
        etReply.setText("");
        SimpleResponseBean response;
        try {
            response = UnitTools.getGson().fromJson(res, SimpleResponseBean.class);
        } catch (Exception e){
            return;
        }
        if(response.getCode() == 0){
            toastMessage("Reply submitted");
        }  else if(response.getCode() == 1){
            toastMessage("Your reply has some taboo words.\nA waring received.");
        } else if(response.getCode() == -1){
            toastMessage("Your reply has more than 3 taboo words.\nThe discussion had been blocked.");
        }else {
            toastMessage("Unknow error");
        }
        getRepliesFromServer();
    }

    private void uploadReply2Server(String res){
        String url = getString(R.string.base_url) + "/reply_discussion";
        RequestBody body = RequestBody.create(
                res, MediaType.parse(UnitTools.TYPE_JSON)
        );
        final Request request = new Request.Builder().url(url).post(body).build();
        Call call = UnitTools.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSubmitReplyResponse(res);
                    }
                });
            }
        });
    }

    private String getReplyJson(){
        String userReply = etReply.getText().toString().trim();
        if(userReply.isEmpty() || userReply.equals("")){
            toastMessage("Reply empty");
            return null;
        }
        ReplyBean newReply = new ReplyBean();
        DiscussionDetailBean detail = new DiscussionDetailBean();
        detail.setContext(userReply);
        newReply.setDetail(detail);
        newReply.setUserID(userID);
        newReply.setTargetDiscussion(currentDiscussion.get_id());
        try {
            String temStr = UnitTools.getGson().toJson(newReply);
            JSONObject jsonTemp = new JSONObject(temStr);
            jsonTemp.remove("_id");
            jsonTemp.remove("displayName");
            return jsonTemp.toString().trim();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        toastMessage("Error when formatting data");
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_disc_reply_submit:
                String res = getReplyJson();
                if(null == res){
                    return;
                }
                uploadReply2Server(res);
                break;
        }
    }
}
