package com.example.administrator.easyparking.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.activitys.ChargePayAcitivy;
import com.example.administrator.easyparking.activitys.MyInfo;
import com.example.administrator.easyparking.activitys.motifyPasswordActivity;
import com.example.administrator.easyparking.activitys.safeToCheckOut;
import com.example.administrator.easyparking.model.GetChange;
import com.example.administrator.easyparking.utils.EasyParkingApplication;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2015/9/5 0005.
 */
public class fragment_mine extends Fragment {

    private TextView username_txt, txt_change;
    private ImageView img_charge;
    private RelativeLayout item_userInfo, item_motifypassword, item_checkout, item_reminder, item_share;
    private RadioGroup radioGroup;
    private RadioButton radioButton_select, radioButton_last5, radioButton_last10, radioButton_last15, radioButton_none;
    View view;
    private ProgressDialog progDialog;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mActivity = this.getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        username_txt = (TextView) view.findViewById(R.id.uMobileId);
        username_txt.setText(EasyParkingApplication.getUsername());
        txt_change = (TextView) view.findViewById(R.id.txt_change);
        img_charge = (ImageView) view.findViewById(R.id.img_charge);
        item_userInfo = (RelativeLayout) view.findViewById(R.id.item_userInfo);
        item_motifypassword = (RelativeLayout) view.findViewById(R.id.item_motifypassword);
        item_checkout = (RelativeLayout) view.findViewById(R.id.item_checkout);
        item_reminder = (RelativeLayout) view.findViewById(R.id.item_reminder);
        item_share = (RelativeLayout) view.findViewById(R.id.item_share);
        setListener();
        //获取信息
        progDialog = new ProgressDialog(this.getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("请稍等...");
        progDialog.show();
        new Service(fragment_mine.this).getUserChange(EasyParkingApplication.getUsername());
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void setListener() {

        img_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChargePayAcitivy.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username_txt.getText().toString());
                bundle.putString("changemoney", txt_change.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
//                getActivity().finish();
                mActivity.finish();
            }
        });

        item_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(getActivity()).withText("hello")
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).open();
            }
        });

        item_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View view = View.inflate(getActivity(), R.layout.reminder_time_toselect, null);
                radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioButton_select = (RadioButton) view.findViewById(checkedId);
                    }
                });

                radioButton_last5 = (RadioButton) view.findViewById(R.id.button_last5);
                radioButton_last10 = (RadioButton) view.findViewById(R.id.button_last10);
                radioButton_last15 = (RadioButton) view.findViewById(R.id.button_last15);
                radioButton_none = (RadioButton) view.findViewById(R.id.button_noreminder);
                switch (EasyParkingApplication.getReminder_time()) {
                    case 0:
                        radioButton_none.setChecked(true);
                        break;
                    case 5:
                        radioButton_last5.setChecked(true);
                        break;
                    case 10:
                        radioButton_last10.setChecked(true);
                        break;
                    case 15:
                        radioButton_last15.setChecked(true);
                        break;
                    default:
                        break;
                }
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = radioButton_select.getText().toString();
                        SharedPreferences preferences = getActivity().getSharedPreferences("user_reminder_time", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.d("TAG", content);
                        if (content.equals("不提醒")) {
                            EasyParkingApplication.setReminder_time(0);
                            editor.putInt("reminder_time", 0);
                        } else if (content.equals("最后5分钟")) {
                            EasyParkingApplication.setReminder_time(5);
                            editor.putInt("reminder_time", 5);
                        } else if (content.equals("最后10分钟")) {
                            EasyParkingApplication.setReminder_time(10);
                            editor.putInt("reminder_time", 10);
                        } else if (content.equals("最后15分钟")) {
                            editor.putInt("reminder_time", 15);
                            EasyParkingApplication.setReminder_time(15);
                        }
                        editor.commit();
                        Log.d("TAG", "EasyParking--reminer_time:" + EasyParkingApplication.getReminder_time());
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });


        item_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), safeToCheckOut.class);
                startActivity(intent);
            }
        });

        item_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyInfo.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        item_motifypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), motifyPasswordActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    public void getUserChangeResult(GetChange getChange) {
        progDialog.dismiss();
        if (getChange == null) {
            //获取失败
            new AlertDialog.Builder(this.getActivity())
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
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("获取信息失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //成功
                int change = getChange.getChangemoney();
                txt_change.setText(change + "");
            }
        }
    }
}
