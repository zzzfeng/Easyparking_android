package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.adapter.ReccordListAdapter_listview;
import com.example.administrator.easyparking.model.CancelOrder;
import com.example.administrator.easyparking.model.RecordBean;
import com.example.administrator.easyparking.model.RecordList;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

import java.util.List;

/**
 * Created by Administrator on 2015/11/19 0019.
 */
public class RecordActivity extends Activity {
    private Activity mActivity;
    private ProgressDialog progDialog;
    private List<RecordBean> recordBeanList;
    private ListView recorListView;
    private ImageButton img_header_back;
    private Boolean isShow = false;
    private ReccordListAdapter_listview mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.record_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.isEmpty()) {
                isShow = bundle.getBoolean("isShow");
            }
        }

        mActivity = this;
        img_header_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        img_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


        progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("请稍等...");
        progDialog.show();
        new Service(mActivity).getRecords(EasyParkingApplication.getUsername());

    }


    public void getRecordResult(RecordList recordList) {
        progDialog.dismiss();
        if (recordList == null) {
            //获取失败
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            //这里可以继续进行

            return;
            //如果异常，不需要往下运行
        } else {
            //获取成功
            int r = recordList.getResult();
            if (r <= 0) {
                //失败
                new AlertDialog.Builder(this)
                        .setTitle("操作失败")
                        .setMessage("获取订单失败")
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                Toast.makeText(this, "点击可查看详情", Toast.LENGTH_SHORT).show();
                List<RecordBean> list = recordList.getList();
                recordBeanList = list;
                initList();
                Log.d("TAG", "Frist OrderID:" + list.get(0).getRecord_id() + "   First expense" + list.get(0).getExpense());
            }
        }
    }

    private void initList() {
        recorListView = (ListView) findViewById(R.id.listView);
        reverseList(recordBeanList);
        mAdapter = new ReccordListAdapter_listview(this, recordBeanList, RecordActivity.this);
        recorListView.setAdapter(mAdapter);
        setListener();
    }

    private void reverseList(List<RecordBean> recordBeanList) {
        RecordBean temp;
        for (int i = 0; i <= recordBeanList.size() / 2; i++) {
            temp = recordBeanList.get(i);
            recordBeanList.set(i, recordBeanList.get(recordBeanList.size() - i - 1));
            recordBeanList.set(recordBeanList.size() - i - 1, temp);
        }
    }


    private void setListener() {

        if (isShow) {
            RecordBean target = recordBeanList.get(recordBeanList.size() - 1);
            Intent intent = new Intent(RecordActivity.this, showRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("order_id", target.getRecord_id());
            bundle.putString("parking_name", target.getParking_name());
            bundle.putString("park_id", String.valueOf(target.getParking_num()));
            bundle.putString("order_id", target.getRecord_id());
            bundle.putString("start_time", target.getStart_time().substring(0, target.getStart_time().length() - 2));
            bundle.putString("end_time", target.getEnd_time().substring(0, target.getEnd_time().length() - 2));
            bundle.putString("order_time", target.getOrder_time().substring(0, target.getOrder_time().length() - 2));
            bundle.putString("expense", String.valueOf(target.getExpense()));
//            if (target.isFlag()) {
//                bundle.putString("flag", "已付款");
//            } else {
//                bundle.putString("flag", "未付款");
//            }
            bundle.putString("flag", "已付款");
            intent.putExtras(bundle);
            startActivity(intent);
        }

//        mAdapter.setItemClickListener(new ReccordListAdapter_listview.ItemClickListener() {
//            @Override
//            public boolean startService() {
//                final boolean[] result = {false};
//                new AlertDialog.Builder(RecordActivity.this)
//                        .setTitle("请确认")
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                        .setMessage("是否取消该订单")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //这里取消订单的决定
//                                result[0] = true;
//                            }
//                        })
//                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                result[0] = false;
//                            }
//                        })
//                        .show();
//                return result[0];
//            }
//        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("NUM", 0);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void cencelOrderResult(CancelOrder cancelOrder) {
        if (cancelOrder == null) {
            //获取失败
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            //这里可以继续进行

            return;
            //如果异常，不需要往下运行
        } else {
            //获取成功
            int r = cancelOrder.getResult();
            if (r <= 0) {
                //失败
                new AlertDialog.Builder(this)
                        .setTitle("操作失败")
                        .setMessage("获取订单失败")
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                refreshActitivy();
            }
        }
    }

    private void refreshActitivy() {
        finish();
        Intent intent = new Intent(RecordActivity.this, RecordActivity.class);
        startActivity(intent);
    }
}
