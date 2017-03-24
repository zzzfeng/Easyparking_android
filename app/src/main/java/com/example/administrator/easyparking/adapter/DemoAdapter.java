package com.example.administrator.easyparking.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.model.ItemModel;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.BaseViewHolder> {
    private ArrayList<ItemModel> dataList = new ArrayList<>();
    private int lastPressIndex = -1;
    private int selectedPositon = -1, price = -1;

    public void replaceAll(ArrayList<ItemModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public DemoAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.ONE:
                return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one, parent, false));
            case ItemModel.TWO:
                return new TWoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.two, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DemoAdapter.BaseViewHolder holder, int position) {

        holder.setData(dataList.get(position).data);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(Object data) {
        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private TextView tv;

        public OneViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "OneViewHolder: ");
                    int position = getAdapterPosition();
                    if (lastPressIndex == position) {
                        lastPressIndex = -1;
                        selectedPositon = -1;
                        price = -1;
                    } else {
                        lastPressIndex = position;
                    }
                    notifyDataSetChanged();
                }

            });
        }

        @Override
        void setData(Object data) {
            if (data != null) {
                String text = (String) data;
                tv.setText(text);
                if (getLayoutPosition() == lastPressIndex) {
                    tv.setSelected(true);
                    tv.setTextColor(itemView.getResources().getColor(R.color.white));
                    char[] temps = tv.getText().toString().toCharArray();
                    price = Integer.parseInt(temps[0] + "");
                    selectedPositon = getLayoutPosition();
                } else {
                    tv.setSelected(false);
                    tv.setTextColor(itemView.getResources().getColor(R.color.blue_500));
                }

            }


        }
    }

    private class TWoViewHolder extends BaseViewHolder {
        private EditText et;

        public TWoViewHolder(View view) {
            super(view);
            et = (EditText) view.findViewById(R.id.et);
        }

        @Override
        void setData(Object data) {
            super.setData(data);
        }
    }

    public int getPosition() {
        return selectedPositon;
    }

    public int getPrice() {
        return price;
    }
}
