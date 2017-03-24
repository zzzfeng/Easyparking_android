package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.adapter.DemoAdapter;
import com.example.administrator.easyparking.model.GetChange;
import com.example.administrator.easyparking.model.ItemModel;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

import java.util.ArrayList;

/**
 * Created by tan on 2017/3/4 0004.
 */
public class ChargePayAcitivy extends Activity {

    private RecyclerView recyclerView;
    private DemoAdapter adapter;
    private TextView tvPay, tvUsername, tvRest;
    private ImageButton imgbtn_header;
    private ProgressDialog progDialog;
    private int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chargechange_layout);
        Bundle bundle = this.getIntent().getExtras();
        String username = bundle.getString("username");
        String changemoney = bundle.getString("changemoney");
        tvUsername = (TextView) findViewById(R.id.tx_username);
        tvRest = (TextView) findViewById(R.id.tx_rest_money);
        tvUsername.setText(username);
        tvRest.setText(changemoney);
        imgbtn_header = (ImageButton) findViewById(R.id.btnHeaderBack);
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        tvPay = (TextView) findViewById(R.id.tvPay);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter = new DemoAdapter());
        adapter.replaceAll(getData());
        setListener();
    }

    private void setListener() {

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPositon = adapter.getPosition();
//                Log.d("TAG", "the positon is " + selectedPositon);
                if (selectedPositon == -1) {
                    Toast.makeText(ChargePayAcitivy.this, "请选择获取输入需要充值的金额", Toast.LENGTH_SHORT).show();
                } else {
                    price = adapter.getPrice();
//                    Toast.makeText(ChargePayAcitivy.this, "充值金额为:" + price, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChargePayAcitivy.this);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setTitle("请确认");
                    builder.setMessage("是否进行充值" + price + "元");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 执行点击确定按钮的业务逻辑
                            new payProcedure().execute();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        imgbtn_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargePayAcitivy.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    public ArrayList<ItemModel> getData() {
        ArrayList<ItemModel> list = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            String count = i + "元";
            list.add(new ItemModel(ItemModel.ONE, count));
        }
//        list.add(new ItemModel(ItemModel.TWO, null));
        return list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("NUM", 2);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getChargeResult(GetChange getChange) {
        progDialog.dismiss();
        if (getChange == null) {
            //获取失败
//            Log.d("TAG", "Ok here");
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            //这里可以继续进行
            return;
            //如果异常，不需要往下运行
        } else {
            int r = getChange.getResult();
            String message = getChange.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("预定失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //成功
                Intent intent = new Intent(ChargePayAcitivy.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    }

    class payProcedure extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(ChargePayAcitivy.this);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setIndeterminate(false);
            progDialog.setCancelable(false);
            progDialog.setMessage("正在支付请稍等...");
            progDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            String date = sDateFormat.format(new java.util.Date());
//            current_time = date;
//            Log.d("TAG", "Params: " + EasyParkingApplication.getUsername() + "  " + start_time + "  " + end_time + "  " + date + "  " + parking_id + "  " + park_id + "  " + money);
            //进行支付
            new Service(ChargePayAcitivy.this).charge(EasyParkingApplication.getUsername(), price);

        }
    }
}
