package com.abhiandroid.foodorderingin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhiandroid.foodorderingin.Adapter.PincodeListAdapter;
import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.Activities.MainActivity;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Activities.SplashScreen;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PincodeList extends Fragment {

    View view;
    @BindView(R.id.pincodeRecyclerView)
    RecyclerView pincodeRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pincode, container, false);
        ButterKnife.bind(this, view);
        setPicodeListData();
        return view;
    }

    public void setPicodeListData() {
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        pincodeRecyclerView.setLayoutManager(flowLayoutManager);
        PincodeListAdapter pincodeListAdapter = new PincodeListAdapter(getActivity(), SplashScreen.restaurantDetailResponseData.getDeliverycity());
        pincodeRecyclerView.setAdapter(pincodeListAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.title.setText("Available Pincode");
        Config.getCartList(getActivity(), true);
    }
}
