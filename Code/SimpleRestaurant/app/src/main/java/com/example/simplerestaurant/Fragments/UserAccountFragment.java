package com.example.simplerestaurant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.Interfaces.UserAccountFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.UserPersonalInfoActivity;
import com.example.simplerestaurant.beans.UserBasicInfoBean;

public class UserAccountFragment extends Fragment implements View.OnClickListener{

    private UserAccountFragmentInterface accListener;
    private String userID, userType;
    private UserBasicInfoBean userBasicInfo;

    private TextView tvDisplayName, tvUserBalance, tvUserSpent;
    private TextView tvFiledComplaint, tvReceivedComplaint, tvDisputedComplaint, tvFiledComplement;
    private ImageButton imgbtnDetail;

    private String res;

    public UserAccountFragment(UserAccountFragmentInterface accListener){
        super(R.layout.fragment_user_account);
        this.accListener = accListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        userType = bundle.getString("userType");

        tvDisplayName = (TextView) view.findViewById(R.id.textview_user_account_name);
        tvUserBalance = (TextView) view.findViewById(R.id.textview_user_balance);
        tvUserSpent = (TextView) view.findViewById(R.id.textview_user_spent);
        tvFiledComplaint = (TextView) view.findViewById(R.id.textview_user_complaint_filed);
        tvReceivedComplaint = (TextView) view.findViewById(R.id.textview_user_complaint_recieved);
        tvDisputedComplaint = (TextView) view.findViewById(R.id.textview_user_complaint_disputed);
        tvFiledComplement = (TextView) view.findViewById(R.id.textview_user_complement_filed);
        imgbtnDetail = (ImageButton) view.findViewById(R.id.imgbtn_user_account_me);

        tvFiledComplaint.setOnClickListener(this);
        tvReceivedComplaint.setOnClickListener(this);
        tvDisputedComplaint.setOnClickListener(this);
        tvFiledComplement.setOnClickListener(this);
        imgbtnDetail.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // get data from server
        if(null != accListener){
            accListener.getUserInfoFromServer(userID, userType);
        }
    }

    private void setUpViewText(UserBasicInfoBean userBasicInfo){
        if(userBasicInfo != null){
            tvDisplayName.setText(userBasicInfo.getDisplayName());
            tvUserBalance.setText("$" + userBasicInfo.getBalance());
            tvUserSpent.setText("$" + userBasicInfo.getSpent());
        }
    }

    public void userInfoResponse(String res){
        this.res = res;
        userBasicInfo = UnitTools.getGson().fromJson(res, UserBasicInfoBean.class);
        setUpViewText(userBasicInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_user_account_me:
                Intent intent = new Intent(getActivity(), UserPersonalInfoActivity.class);
                intent.putExtra("userInfo", res);
                startActivity(intent);
                break;
            case R.id.textview_user_complaint_filed:
                break;
            case R.id.textview_user_complaint_recieved:
                break;
            case R.id.textview_user_complaint_disputed:
                break;
            case R.id.textview_user_complement_filed:
                break;
        }
    }
}
