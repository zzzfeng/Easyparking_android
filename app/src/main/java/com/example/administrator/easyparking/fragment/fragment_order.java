package com.example.administrator.easyparking.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.activitys.RecordActivity;
import com.example.administrator.easyparking.activitys.SelectTimeActivity;
import com.example.administrator.easyparking.activitys.orderlistActivity;

/**
 * Created by Administrator on 2015/9/5 0005.
 */
public class fragment_order extends Fragment {

    private Activity mActivity;
    private View view;
    private RelativeLayout pArrow1, pArrow2, pArrow3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = this.getActivity();
        view = getView();
        InitView();
        setListener();

    }

    private void setListener() {
        pArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("TAG","")
                Intent intent = new Intent(mActivity, orderlistActivity.class);
                startActivity(intent);
                mActivity.finish();
            }
        });

        pArrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow",false);
                intent.putExtras(bundle);
                startActivity(intent);
                mActivity.finish();
            }
        });

        pArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SelectTimeActivity.class);
                startActivity(intent);
                mActivity.finish();
            }
        });


    }

    private void InitView() {
        pArrow1 = (RelativeLayout) view.findViewById(R.id.pArrowId1);
        pArrow2 = (RelativeLayout) view.findViewById(R.id.pArrowId2);
        pArrow3 = (RelativeLayout) view.findViewById(R.id.pArrowId3);
//        pArrow4 = (ImageView) view.findViewById(R.id.pArrowId4);
    }
}
