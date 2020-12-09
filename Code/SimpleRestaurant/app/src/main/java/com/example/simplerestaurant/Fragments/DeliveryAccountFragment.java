package com.example.simplerestaurant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.DeliveryDataStore;
import com.example.simplerestaurant.GeneralCCActivity;
import com.example.simplerestaurant.R;

public class DeliveryAccountFragment extends Fragment implements View.OnClickListener{

    private TextView tvCompliantReceived, tvComplaintFiled, tvFinished, tvSending;

    private String userID, userType;

    public DeliveryAccountFragment(String userID, String userType) {
        super(R.layout.fragment_delivery_account);
        this.userID = userID;
        this.userType = userType;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvComplaintFiled = view.findViewById(R.id.tv_delivery_complaint_filed);
        tvCompliantReceived = view.findViewById(R.id.tv_delivery_complaint_recieved);

        tvFinished = view.findViewById(R.id.tv_delivery_finished);
        tvSending = view.findViewById(R.id.tv_delivery_sending);

        tvCompliantReceived.setOnClickListener(this);
        tvComplaintFiled.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvFinished.setText(String.valueOf(DeliveryDataStore.getInstance().getOrderFinished().size()));
        tvSending.setText(String.valueOf(DeliveryDataStore.getInstance().getOrderSending().size()));
    }

    private void startGeneralCCActivity(int selection){
        Intent intent = new Intent(getActivity(), GeneralCCActivity.class);
        intent.putExtra("selection", selection);
        intent.putExtra("userID", userID);
        intent.putExtra("userType", userType);
        getActivity().startActivity(intent);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_delivery_complaint_filed:
                startGeneralCCActivity(GeneralCCActivity.FILED_COMPLAINT);
                break;
            case R.id.tv_delivery_complaint_recieved:
                startGeneralCCActivity(GeneralCCActivity.RECEIVED_COMPLAINT);
                break;
        }
    }
}
