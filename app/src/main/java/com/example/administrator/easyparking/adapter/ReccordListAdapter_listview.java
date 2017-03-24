package com.example.administrator.easyparking.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.activitys.RecordActivity;
import com.example.administrator.easyparking.activitys.showRecordActivity;
import com.example.administrator.easyparking.model.RecordBean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/5 0005.
 */
public class ReccordListAdapter_listview extends BaseAdapter {
    private List<RecordBean> mData;
    private LayoutInflater mInflater;
    private RecordActivity recordActivity;
    private Context context;


    public ReccordListAdapter_listview(Context context, List<RecordBean> list, RecordActivity recordActivity) {
        this.mData = list;
        this.context = context;
        this.recordActivity = recordActivity;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.viewholder_recordlist, null);
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.content = (TextView) view.findViewById(R.id.content);
            viewHolder.img_IsCancel = (ImageView) view.findViewById(R.id.img_IsCancel);
            viewHolder.img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
            viewHolder.img_IsComplete = (ImageView) view.findViewById(R.id.img_record_isComplete);
            viewHolder.img_record_info = (ImageView) view.findViewById(R.id.img_record_info);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageResource(R.mipmap.order);
        viewHolder.title.setText(mData.get(position).getParking_name());
        viewHolder.content.setText(String.valueOf(mData.get(position).getParking_num()));
        final boolean isComplete = mData.get(position).isComplete();
        boolean enable = mData.get(position).isEnable();
        Log.d("TAG", "isComplete :" + isComplete + "   enable :" + enable);
        if (enable == true) {
            viewHolder.img_IsCancel.setVisibility(View.GONE);
            viewHolder.img_IsComplete.setImageResource(R.mipmap.valid);
            if (isComplete == true) {
                viewHolder.img_cancel.setVisibility(View.GONE);
            } else if (isComplete == false) {
                viewHolder.img_IsComplete.setImageResource(R.mipmap.ing);
            }
        } else if (enable == false) {
            viewHolder.img_IsCancel.setVisibility(View.VISIBLE);
            viewHolder.img_IsComplete.setImageResource(R.mipmap.invalid);
            viewHolder.img_cancel.setVisibility(View.GONE);
        }

        viewHolder.img_record_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordBean target = mData.get(position);
                Log.d("TAG", "Target Parking Name:---" + target.getParking_name());
                Intent intent = new Intent(context, showRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("parking_name", target.getParking_name());
                bundle.putString("park_id", String.valueOf(target.getParking_num()));
                bundle.putString("order_id", target.getRecord_id());
                bundle.putString("start_time", target.getStart_time().substring(0, target.getStart_time().length() - 2));
                bundle.putString("end_time", target.getEnd_time().substring(0, target.getEnd_time().length() - 2));
                bundle.putString("order_time", target.getOrder_time().substring(0, target.getOrder_time().length() - 2));
                bundle.putString("expense", String.valueOf(target.getExpense()));
                bundle.putString("remark",target.getRemark());
//                if (target.isFlag()) {
//                    bundle.putString("flag", "已付款");
//                } else {
//                    bundle.putString("flag", "未付款");
//                }
                bundle.putString("flag", "已付款");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        viewHolder.img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(recordActivity)
                        .setTitle("请确认")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage("是否取消该订单")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //这里取消订单的决定
                                new Service(recordActivity).cancelOrder(mData.get(position).getRecord_id());
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    public final class ViewHolder {
        public TextView title, content;
        public ImageView icon, img_IsCancel, img_cancel, img_IsComplete, img_record_info;
    }

//    public interface ItemClickListener {
//        boolean startService();
//    }
//
//    public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }


}
