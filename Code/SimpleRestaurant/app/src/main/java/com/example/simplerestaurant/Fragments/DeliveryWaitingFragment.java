package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.DeliveryOrderListAdapter;
import com.example.simplerestaurant.DeliveryDataStore;
import com.example.simplerestaurant.Interfaces.DeliveryActionInterface;
import com.example.simplerestaurant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DeliveryWaitingFragment extends Fragment implements View.OnClickListener{
    private DeliveryActionInterface actionListener;
    private RecyclerView orderList;
    private DeliveryOrderListAdapter adapter;
    private FloatingActionButton btnRefresh;

    public DeliveryWaitingFragment( DeliveryActionInterface actionListener){
        super(R.layout.fragment_delivery_general);
        this.actionListener = actionListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRefresh = (FloatingActionButton) view.findViewById(R.id.floatingbtn_delivery_refresh);
        btnRefresh.setOnClickListener(this);
        orderList = (RecyclerView) view.findViewById(R.id.recycler_delivery_order_list);
        adapter = new DeliveryOrderListAdapter(DeliveryDataStore.TYPE_WAITING, DeliveryDataStore.getInstance().getOrderWaiting());
        adapter.setActionListener(actionListener);
        orderList.setAdapter(adapter);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(null != actionListener){
            actionListener.onWaitingFragmentRefresh();
        }
    }

    public void notifyDataChange(){
        if(null != adapter){
            adapter.setOrderList(DeliveryDataStore.getInstance().getOrderWaiting());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(null != actionListener){
            actionListener.onWaitingFragmentRefresh();
        }
    }
}
