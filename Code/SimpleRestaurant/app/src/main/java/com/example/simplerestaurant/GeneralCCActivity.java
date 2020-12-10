package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.GeneralCCAdapter;
import com.example.simplerestaurant.Interfaces.ComplaintDisputeInterface;
import com.example.simplerestaurant.beans.CCBean;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeneralCCActivity extends BaseActivity implements View.OnClickListener, ComplaintDisputeInterface {
    final public static int FILED_COMPLAINT = 1;
    final public static int FILED_COMPLIMENT = 2;
    final public static int RECEIVED_COMPLAINT = 3;

    private int selection = 0;
    private TextView tvWhere;
    private RecyclerView itemList;

    private String userID, userType;
    private ArrayList<CCBean> viewData;

    private ImageButton imgbtnBack;

    private GeneralCCAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_cc);

        tvWhere = (TextView) findViewById(R.id.tv_act_cc_general_where);
        itemList = (RecyclerView) findViewById(R.id.recycler_act_cc_general_list);
        imgbtnBack = (ImageButton) findViewById(R.id.imagebtn_backward);
        imgbtnBack.setOnClickListener(this);

        Intent intent = getIntent();
        selection = intent.getIntExtra("selection", 1);
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");

        viewData = new ArrayList<>();

        switch (selection){
            case FILED_COMPLAINT:
                tvWhere.setText("Filed Complaint");
                adapter = new GeneralCCAdapter(false, viewData);
                break;
            case FILED_COMPLIMENT:
                tvWhere.setText("Filed Compliment");
                adapter = new GeneralCCAdapter(false, viewData);
                break;
            case RECEIVED_COMPLAINT:
                tvWhere.setText("Received Complaint");
                adapter = new GeneralCCAdapter(true, viewData);
                break;
            default:
                adapter = new GeneralCCAdapter(false, viewData);
                break;
        }
        adapter.setOnDisputeClickListener(this);
        itemList.setAdapter(adapter);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        getData(selection);
    }

    private void handleResponse(String res){
        try {
            JSONObject jsonObject = new JSONObject(res);
            Type listType = new TypeToken<ArrayList<CCBean>>(){}.getType();
            ArrayList<CCBean> tempList = UnitTools.getGson().fromJson(jsonObject.getString("result").toString(), listType);
            this.viewData = new ArrayList<>(tempList);
            if(null != adapter){
                adapter.setViewData(this.viewData);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFromServer(String url){
        OkHttpClient client = UnitTools.getOkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role",userType);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleResponse(res);
                    }
                });
            }
        });
    }

    private void getData(int selection){
        String url = getString(R.string.base_url);
        switch (selection){
            case FILED_COMPLAINT:
                url += "/get_filedComplaint";
                break;
            case FILED_COMPLIMENT:
                url += "/get_filedCompliment";
                break;
            case RECEIVED_COMPLAINT:
                url += "/complaint";
                break;
            default:
                url+= "/get_filedComplaint";
                break;
        }
        getFromServer(url);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imagebtn_backward){
            this.finish();
        }
    }

    @Override
    public void onDisputeClick(String complaintID, String complaintText) {
        Intent intent = new Intent(this, DisputeComplaintActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("userType", userType);
        intent.putExtra("complaintID", complaintID);
        intent.putExtra("complaintContext", complaintText);
        startActivity(intent);
    }
}
