package com.example.chef;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag4_viewcompliment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag4_viewcompliment extends Fragment {

    private ArrayList<modelComplaint> models;
    RecyclerView mrecyclerview;
    private AdapterCompliment myadapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frag4_viewcompliment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag4_viewcompliment.
     */
    // TODO: Rename and change types and number of parameters
    public static frag4_viewcompliment newInstance(String param1, String param2) {
        frag4_viewcompliment fragment = new frag4_viewcompliment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frag4_viewcompliment, container, false);
        mrecyclerview = (RecyclerView)view.findViewById(R.id.recycle_compliment);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        String userid = getuserid.userid;
        models = new ArrayList<>();
        getcompliment(userid);

        return view;
    }


    public void getcompliment(String userid) {
        String url = getString(R.string.base_url) + "/compliment";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userid);
        bodyBuilder.add("role", "chef");

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
                            JSONArray jarray = json.getJSONArray("result");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject object     = jarray.getJSONObject(i);
                                String mcaseid = object.getString("_id");
                                String mtoid = "to ID: " + object.getString("toID");
                                String msubject = "Subject: " + object.getString("subject");
                                String mcontext = "Context: " + object.getString("context");

                                models.add(new modelComplaint(mtoid,msubject,mcontext,mcaseid));

                            }
                            myadapter = new AdapterCompliment(getActivity(), models);
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