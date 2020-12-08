package com.example.chef;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.jvm.internal.PropertyReference0Impl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class frag1_uncomplete extends Fragment {

    RecyclerView mrecyclerview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<model> models;

    private Adapteruncomplete myadapter;



    public frag1_uncomplete() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag1_uncomplete.
     */
    // TODO: Rename and change types and number of parameters
    public static frag1_uncomplete newInstance(String param1, String param2) {
        frag1_uncomplete fragment = new frag1_uncomplete();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag1_uncomplete, container, false);
        mrecyclerview = (RecyclerView)view.findViewById(R.id.recycle_uncomplete);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        String userid = getuserid.userid;
        models = new ArrayList<>();


        // Inflate the layout for this fragment
        return view;

    }

    public void getuncompleteorder(String userid){
        String url = getString(R.string.base_url) + "/uncompleted_order" ;
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("role", "chef");
        bodyBuilder.add("userID", userid);

        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String respond = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(respond);
                            JSONObject json2 = json.optJSONObject("result");
                            JSONArray jarray = json2.getJSONArray("waiting");
                            for (int i = 0; i < jarray.length(); i++){
                                JSONObject obj1 = jarray.getJSONObject(i);
                                String custid ="Cust ID : "+ obj1.getString("customerID");
                                String ordertotal = "Order Total: " + obj1.getString("orderTotal");
                                String ordenumber = obj1.getString("_id");
                                String orderid = "The Order number: " + obj1.getString("_id");
                                JSONArray jarray2 = obj1.getJSONArray("dishDetail");

                                String dish2= "";
                                String note2= "";
                                for (int k = 0; k < jarray2.length(); k++){
                                    JSONObject obj2 =  jarray2.getJSONObject(k);
                                    String dishs = obj2.getString("dishID") + " *" + obj2.getString("quantity") + "\n";
                                    String note = obj2.getString("specialNote");
                                    dish2 = dish2+dishs;
                                    note2 = note2+note;
                                }
                                models.add(new model(custid,dish2,note2,ordertotal,orderid, ordenumber));

                            }
                            myadapter = new Adapteruncomplete(getActivity(), models);
                            mrecyclerview.setAdapter(myadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }




}