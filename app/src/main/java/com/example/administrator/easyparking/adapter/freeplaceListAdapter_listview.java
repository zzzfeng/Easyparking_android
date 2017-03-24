package com.example.administrator.easyparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.easyparking.R;

import java.util.List;

/**
 * Created by Administrator on 2015/9/5 0005.
 */
public class freeplaceListAdapter_listview extends BaseAdapter {
    private List<Integer> mData;
    private LayoutInflater mInflater;
    private String title, money;


    public freeplaceListAdapter_listview(Context context, String title, List<Integer> mData, String money) {
        this.mData = mData;
        mInflater = LayoutInflater.from(context);
        this.title = title;
        this.money = money;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.viewholder_infreeplacelist_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.order_money = (TextView) convertView.findViewById(R.id.order_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(title);
        viewHolder.content.setText("车位号：" + String.valueOf(mData.get(position)));
        viewHolder.money.setText(money + "元");
        viewHolder.order_money.setText("1元");//暂时默认1元一次预定
        return convertView;
    }

    public final class ViewHolder {
        public TextView title, content, money, order_money;
    }

}
