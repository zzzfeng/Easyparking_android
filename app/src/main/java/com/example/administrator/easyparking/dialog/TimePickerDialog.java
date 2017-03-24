package com.example.administrator.easyparking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.easyparking.R;

import java.util.ArrayList;
import java.util.Calendar;

import wheel.widget.adapters.AbstractWheelTextAdapter;
import wheel.widget.views.OnWheelChangedListener;
import wheel.widget.views.OnWheelScrollListener;
import wheel.widget.views.WheelView;

/**
 * 时间选择器
 * Created by tan on 2017/2/20 0020.
 */
public class TimePickerDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private WheelView wvHour;
    private WheelView wvMinute;

    private View vChangeTime;
    private View vChangeTimeChild;
    private TextView btnSure;
    private TextView btnCancel;

    //这两个属性用来记录上限
    private int hours;
    private int minutes;

    //这两个属性用来记录当前时间
    private int currentHour = 0;
    private int currentMinute = 0;

    private ArrayList<String> arry_hours = new ArrayList<String>();
    private ArrayList<String> arry_minutes = new ArrayList<String>();

    private int maxTextSize = 24;
    private int minTextSize = 14;

    private boolean issetdata = false;

    private String selectHour;
    private String selectMinute;

    private OnTimeListener onTimeListener;
    private int hour;
    private int minute;

    private TimerTextAdapter mHourAdapter;
    private TimerTextAdapter mMinuteAdapter;

    public TimePickerDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myinfo_changetime);
        wvHour = (WheelView) findViewById(R.id.wv_time_hourr);
        wvMinute = (WheelView) findViewById(R.id.wv_time_minute);

        vChangeTime = findViewById(R.id.ly_myinfo_changetime);
        vChangeTimeChild = findViewById(R.id.ly_myinfo_changetime_child);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

        vChangeTime.setOnClickListener(this);
        vChangeTimeChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (!issetdata) {
            initData();
        }

        initHours();
        initMintes();

        mHourAdapter = new TimerTextAdapter(context, arry_hours, currentHour, maxTextSize, minTextSize);
        wvHour.setVisibleItems(24);
        wvHour.setViewAdapter(mHourAdapter);
//        wvHour.setCurrentItem(setMonth(currentMonth));

        mMinuteAdapter = new TimerTextAdapter(context, arry_minutes, currentMinute, maxTextSize, minTextSize);
        wvMinute.setVisibleItems(60);
        wvMinute.setViewAdapter(mMinuteAdapter);

        wvHour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                selectHour = currentText;
                setTextviewSize(currentText, mHourAdapter);
                currentHour = Integer.parseInt(currentText);
                //    setYear(currentYear);
                initMintes();
                mMinuteAdapter = new TimerTextAdapter(context, arry_minutes, 0, maxTextSize, minTextSize);
                wvMinute.setVisibleItems(60);
                wvMinute.setViewAdapter(mMinuteAdapter);
                wvMinute.setCurrentItem(0);
            }
        });

        wvHour.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                selectHour = currentText;
                setTextviewSize(currentText, mHourAdapter);
            }
        });

        wvMinute.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                selectMinute = currentText;
                setTextviewSize(currentText, mMinuteAdapter);
                //  setMonth(Integer.parseInt(currentText));
                // initDays(day);
                // mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
                //  wvDay.setVisibleItems(5);
                // wvDay.setViewAdapter(mDaydapter);
                //wvDay.setCurrentItem(0);
            }
        });

        wvMinute.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                selectMinute = currentText;
                setTextviewSize(currentText, mMinuteAdapter);
            }
        });

    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, TimerTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }


    private void initMintes() {
        selectMinute = "00";
        arry_minutes.clear();
        if (Integer.parseInt(selectHour) == getHour()) {
            int curMinute = getMinute();
            for (int i = curMinute; i < 60; i++) {
                if (i < 10) {
                    arry_minutes.add("0" + i);
                } else {
                    arry_minutes.add("" + i);
                }
            }
        } else {
            for (int i = 0; i < 60; i++) {
                if (i < 10) {
                    arry_minutes.add("0" + i);
                } else {
                    arry_minutes.add("" + i);
                }
            }
        }
    }

    //设置时间选择器的时间范围
    private void initHours() {
        arry_hours.clear();
        int curHour = getHour();
        for (int i = curHour; i < 24; i++) {
            if (i < 10) {
                arry_hours.add("0" + i);
            } else {
                arry_hours.add("" + i);
            }
        }
    }


    private void initData() {
        setTime(getHour(), getMinute());
        this.currentHour = getHour();
        this.currentMinute = getMinute();
    }

    private void setTime(int hour, int minute) {
        selectHour = "" + hour;
        selectMinute = "" + minute;
        issetdata = true;
        this.currentMinute = minute;
        this.currentHour = hour;
    }

    public int getHour() {
        Calendar c = Calendar.getInstance();
        Log.d("TAG", "this hour is " + c.get(Calendar.HOUR_OF_DAY));
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        Calendar c = Calendar.getInstance();
        Log.d("TAG", "this minte is " + c.get(Calendar.MINUTE));
        return c.get(Calendar.MINUTE);
    }


    private class TimerTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected TimerTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    public interface OnTimeListener {
         void onClick(String hour, String minute);
    }

    public void setOnTimeListener(OnTimeListener onTimeListener) {
        this.onTimeListener = onTimeListener;
    }

    @Override
    public void onClick(View view) {
        if (view == btnSure) {
            if (onTimeListener != null) {
                onTimeListener.onClick(selectHour, selectMinute);
            }
        } else if (view == btnSure) {

        } else if (view == vChangeTimeChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();

    }
}
