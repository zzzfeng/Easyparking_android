package com.example.administrator.easyparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.easyparking.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/5 0005.
 */
public class parkingListAdapter extends BaseAdapter {
    private ArrayList<ArrayList<String>> mData;
    private LayoutInflater mInflater;
    private ArrayList<String> title_list;
    private ArrayList<String> content_list;
    private ArrayList<Double> charge_days_list;
    private ArrayList<Double> charge_nights_list;
    private ArrayList<Integer> frees;

    public parkingListAdapter(Context context, ArrayList<ArrayList<String>> data) {
        this.mData = data;
        mInflater = LayoutInflater.from(context);
        title_list = this.mData.get(0);
        content_list = this.mData.get(1);

    }

    public parkingListAdapter(Context context, ArrayList<ArrayList<String>> list_data, ArrayList<Double> list_charge_day, ArrayList<Double> list_charge_night, ArrayList<Integer> frees) {
        this.mData = list_data;
        mInflater = LayoutInflater.from(context);
        title_list = this.mData.get(0);
        content_list = this.mData.get(1);
        this.charge_days_list = list_charge_day;
        this.charge_nights_list = list_charge_night;
        this.frees = frees;
    }

    @Override
    public int getCount() {
        return mData.get(0).size();
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
            convertView = mInflater.inflate(R.layout.viewholder_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.charge_day = (TextView) convertView.findViewById(R.id.text_charge_day);
            viewHolder.charge_night = (TextView) convertView.findViewById(R.id.text_charge_night);
            viewHolder.free = (TextView) convertView.findViewById(R.id.text_free);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(title_list.get(position));
        viewHolder.content.setText(content_list.get(position));
        viewHolder.charge_day.setText(String.valueOf(charge_days_list.get(position))+"元/时");
        viewHolder.charge_night.setText(String.valueOf(charge_nights_list.get(position))+"元/时");
        viewHolder.free.setText(String.valueOf(frees.get(position)));
        return convertView;
    }

    public final class ViewHolder {
        public TextView title, content, charge_day, charge_night, free;
    }

}
