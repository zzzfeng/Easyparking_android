package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.adapter.freeplaceListAdapter_listview;
import com.example.administrator.easyparking.dialog.ChangeBirthDialog;
import com.example.administrator.easyparking.dialog.TimePickerDialog;
import com.example.administrator.easyparking.model.freeplaceList;
import com.example.administrator.easyparking.model.orderParkingUtil;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class freePlaceList extends Activity {
    List<Integer> data;
    String money;
    freeplaceListAdapter_listview mAdapter;
    private ListView listView;
    private String title, local, start_time = "", end_time = "", current_time = "";
    private ImageView img_header_back;
    private ProgressDialog progDialog;
    private int parking_id, park_id;
    private boolean IsOk = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.freeplacelist_layout);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        local = bundle.getString("local");
        initView();
        setListener();
        new SelectTimeTask().execute();
    }

    private void setListener() {
        img_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(freePlaceList.this, orderlistActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        img_header_back = (ImageView) findViewById(R.id.btnHeaderBack);

    }

    public void getfreeplaceResult(freeplaceList freeplacelist) {

        if (freeplacelist == null) {
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
            int r = freeplacelist.getResult();
            String message = freeplacelist.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("获取失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .show();

                Log.d("TAG", "result :" + r + "message :" + message);
            } else {
                data = freeplacelist.getList();
                money = String.valueOf(freeplacelist.getExpense());
                parking_id = freeplacelist.getParking_id();
                setupList();
            }
        }

    }

    public void setupList() {

        mAdapter = new freeplaceListAdapter_listview(this, title, data, money);
        listView.setAdapter(mAdapter);
        Log.d("TAG", "freeplaceslistSize： " + listView.getCount());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                park_id = position;
                park_id++;
                AlertDialog.Builder builder = new AlertDialog.Builder(freePlaceList.this);
                builder.setTitle("是否支付?");
                builder.setMessage("车场名称：" + title + "\n" + "车位号：" + data.get(position) + "\n" + "停车费用：" + money);
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        new payProcedure().execute();
                    }
                }).setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    class SelectTimeTask extends AsyncTask<Void, Integer, Boolean> {

        private EditText edit_start_date;
        private EditText edit_start_time;
        private EditText edit_end_date;
        private EditText edit_end_time;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(freePlaceList.this);
            View view = View.inflate(freePlaceList.this, R.layout.timepicker_layout, null);
//            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.start_time_picker);
//            final TimePicker timePicker2 = (android.widget.TimePicker) view.findViewById(R.id.end_time_picker);
            edit_start_date = (EditText) view.findViewById(R.id.edit_start_date);
            edit_start_date.setInputType(InputType.TYPE_NULL);
            edit_start_time = (EditText) view.findViewById(R.id.edit_start_time);
            edit_start_time.setInputType(InputType.TYPE_NULL);
            edit_end_date = (EditText) view.findViewById(R.id.edit_end_date);
            edit_end_date.setInputType(InputType.TYPE_NULL);
            edit_end_time = (EditText) view.findViewById(R.id.edit_end_time);
            edit_end_time.setInputType(InputType.TYPE_NULL);
            setEditTextListener();
            builder.setView(view);
//            timePicker.setIs24HourView(true);
//            timePicker2.setIs24HourView(true);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new java.util.Date());
            edit_start_date.setText(date);
            edit_end_date.setText(date);

            builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String current_start_date, current_start_time, current_end_time, current_end_data;
                    current_start_date = edit_start_date.getText().toString();
                    current_start_time = edit_start_time.getText().toString();
                    current_end_data = edit_end_date.getText().toString();
                    current_end_time = edit_end_time.getText().toString();
                    if (current_end_data.equals("") || current_end_time.equals("") || current_start_date.equals("") || current_start_time.equals("")) {
                        Toast.makeText(freePlaceList.this, "输入信息不能为空", Toast.LENGTH_SHORT).show();
                    }
                    start_time = current_start_date + " " + current_start_time + ":00";
                    end_time = current_end_data + " " + current_end_time + ":00";
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date start = null, end = null;
                    try {
                        start = sd.parse(start_time);
                        end = sd.parse(end_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (end.getTime() - start.getTime() < 3600 * 1000) {
                        Toast.makeText(freePlaceList.this, "预定时间间隔不得少于1个小时", Toast.LENGTH_SHORT).show();
                        edit_end_date.setText("");
                        Intent intent = new Intent(freePlaceList.this, orderlistActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        IsOk = true;
                        dialog.cancel();
                    }


                }
            }).setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(freePlaceList.this, orderlistActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }

        private void setEditTextListener() {


            edit_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeBirthDialog changeBirthDialog = new ChangeBirthDialog(freePlaceList.this);
                    changeBirthDialog.show();
                    changeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

                        @Override
                        public void onClick(String year, String month, String day) {
                            // TODO Auto-generated method stub
                            edit_start_date.setText(year + "-" + month + "-" + day);
                            Log.d("TAG", "select time is " + year + "-" + month + "-" + day);
                        }
                    });
                }
            });

            edit_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeBirthDialog changeBirthDialog = new ChangeBirthDialog(freePlaceList.this);
                    changeBirthDialog.show();
                    changeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

                        @Override
                        public void onClick(String year, String month, String day) {
                            // TODO Auto-generated method stub
                            edit_end_date.setText(year + "-" + month + "-" + day);
                            Log.d("TAG", "select time is " + year + "-" + month + "-" + day);
                        }
                    });
                }
            });

            edit_start_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(freePlaceList.this);
                    timePickerDialog.show();
                    timePickerDialog.setOnTimeListener(new TimePickerDialog.OnTimeListener() {
                        @Override
                        public void onClick(String hour, String minute) {
                            edit_start_time.setText(hour + ":" + minute);
//                            Log.d("TAG", "select time is " + hour + ":" + minute);
                        }
                    });
                }
            });

            edit_end_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(freePlaceList.this);
                    timePickerDialog.show();
                    timePickerDialog.setOnTimeListener(new TimePickerDialog.OnTimeListener() {
                        @Override
                        public void onClick(String hour, String minute) {
                            edit_end_time.setText(hour + ":" + minute);
//                            Log.d("TAG", "select time is " + hour + "-" + minute);
                        }
                    });
                }
            });


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            while (true) {


                if (!start_time.equals("") && !end_time.equals("")&&IsOk == true) {
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.d("TAG", "startTime:  " + start_time);
            Log.d("TAG", "endTime:  " + end_time);
            Log.d("TAG", "title:  " + title);
            Log.d("TAG", "LOCAL:  " + local);
            new Service(freePlaceList.this).getfreeplacesList(title, local, start_time, end_time);
        }
    }

    class payProcedure extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(freePlaceList.this);
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
//            Log.d("TAG", "i make it!");
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            current_time = date;
//            Log.d("TAG", "Params: " + EasyParkingApplication.getUsername() + "  " + start_time + "  " + end_time + "  " + date + "  " + parking_id + "  " + park_id + "  " + money);
            doPay();

        }
    }

    private void doPay() {
        Log.d("TAG", "Params: " + EasyParkingApplication.getUsername() + "  start_time" + start_time + "  end_time" + end_time + "  current_time" + current_time + "  parking_id" + parking_id + "  park_id" + park_id + "  expense:" + money + "  paid:" + money + "   unpaid:" + 0);
        new Service(freePlaceList.this).orderParking(EasyParkingApplication.getUsername(), start_time, end_time, current_time, parking_id, park_id, 0, Float.valueOf(money), Float.valueOf(money), 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(freePlaceList.this, orderlistActivity.class);
        startActivity(intent);
        finish();
    }


    public void getOrderParkingResult(orderParkingUtil orderparkingutil) {
        progDialog.dismiss();
        if (orderparkingutil == null) {
            //获取失败
            Log.d("TAG", "Ok here");
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            //这里可以继续进行

            return;
            //如果异常，不需要往下运行
        } else {
            int r = orderparkingutil.getResult();
            String message = orderparkingutil.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("预定失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //成功
                Intent intent = new Intent(freePlaceList.this, GoToPay.class);
                Bundle bundle = new Bundle();
                bundle.putString("start_time", start_time);
                bundle.putString("end_time", end_time);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }

    }

}
