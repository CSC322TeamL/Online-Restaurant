package com.example.simplerestaurant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.GeneralCCActivity;
import com.example.simplerestaurant.Interfaces.UserAccountFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.UserPersonalInfoActivity;
import com.example.simplerestaurant.UserRefillBalanceActivity;
import com.example.simplerestaurant.beans.UserBasicInfoBean;

import org.w3c.dom.Text;

public class UserAccountFragment extends Fragment implements View.OnClickListener{

    private UserAccountFragmentInterface accListener;
    private String userID, userType;
    private UserBasicInfoBean userBasicInfo;

    private TextView tvDisplayName, tvUserBalance, tvUserSpent;
    private TextView tvFiledComplaint, tvReceivedComplaint, tvFiledComplement;
    private TextView tvWarnings, tvRefill;
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
        tvFiledComplement = (TextView) view.findViewById(R.id.textview_user_complement_filed);
        imgbtnDetail = (ImageButton) view.findViewById(R.id.imgbtn_user_account_me);
        tvWarnings = (TextView) view.findViewById(R.id.textview_user_warnings);
        tvRefill = (TextView) view.findViewById(R.id.textview_user_refill_balance);

        tvFiledComplaint.setOnClickListener(this);
        tvReceivedComplaint.setOnClickListener(this);
        tvFiledComplement.setOnClickListener(this);
        imgbtnDetail.setOnClickListener(this);
        tvRefill.setOnClickListener(this);
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

            int ws = userBasicInfo.getWarnings();
            tvWarnings.setText(String.valueOf(ws));
            if(ws > 0){
                tvWarnings.setTextColor(ContextCompat.getColor(getActivity(), R.color.primaryRedDark));
            } else {
                tvWarnings.setTextColor(ContextCompat.getColor(getActivity(), R.color.primaryGreen));
            }
        }
    }

    public void userInfoResponse(String res){
        this.res = res;
        userBasicInfo = UnitTools.getGson().fromJson(res, UserBasicInfoBean.class);
        setUpViewText(userBasicInfo);
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
        Intent intent;
        switch (v.getId()){
            case R.id.imgbtn_user_account_me:
                intent = new Intent(getActivity(), UserPersonalInfoActivity.class);
                intent.putExtra("userInfo", res);
                startActivity(intent);
                break;
            case R.id.textview_user_complaint_filed:
                startGeneralCCActivity(GeneralCCActivity.FILED_COMPLAINT);
                break;
            case R.id.textview_user_complaint_recieved:
                startGeneralCCActivity(GeneralCCActivity.RECEIVED_COMPLAINT);
                break;
            case R.id.textview_user_complement_filed:
                startGeneralCCActivity(GeneralCCActivity.FILED_COMPLIMENT);
                break;
            case R.id.textview_user_refill_balance:
                intent = new Intent(getActivity(), UserRefillBalanceActivity.class);
                intent.putExtra("userInfo", res);
                startActivity(intent);
                break;
        }
    }
}
