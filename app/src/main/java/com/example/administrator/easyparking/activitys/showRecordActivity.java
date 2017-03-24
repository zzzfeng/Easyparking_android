package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.easyparking.R;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
public class showRecordActivity extends Activity {

    private TextView parking_name, parking_num, comlicate_Flag, startTime, endTime, orderTime, order_id, expense,remark;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_record_layout);
        initView();
    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        parking_name = (TextView) findViewById(R.id.parking_name);
        parking_num = (TextView) findViewById(R.id.parking_num);
        order_id = (TextView) findViewById(R.id.order_id);
        comlicate_Flag = (TextView) findViewById(R.id.complicate_Flag);
        startTime = (TextView) findViewById(R.id.start_Time);
        endTime = (TextView) findViewById(R.id.end_Time);
        orderTime = (TextView) findViewById(R.id.record_Time);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout_back);
        expense = (TextView) findViewById(R.id.expense);
        remark = (TextView) findViewById(R.id.remark);

        parking_name.setText(bundle.getString("parking_name"));
        parking_num.setText(bundle.getString("park_id"));
        order_id.setText(bundle.getString("order_id"));
        comlicate_Flag.setText(bundle.getString("flag"));
        startTime.setText(bundle.getString("start_time"));
        endTime.setText(bundle.getString("end_time"));
        expense.setText(bundle.getString("expense"));
        orderTime.setText(bundle.getString("order_time"));
        remark.setText(bundle.getString("remark"));
        setListener();
    }

    private void setListener() {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}



