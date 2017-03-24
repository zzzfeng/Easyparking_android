package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.adapter.parkingListAdapter;
import com.example.administrator.easyparking.model.Parkinglots;
import com.example.administrator.easyparking.utils.EasyParkingApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/30 0030.
 */
public class orderlistActivity extends Activity {

    private TextView title, content;
    private ImageButton headerback_btn;
    private ListView listView;
    private parkingListAdapter mAdapter;
    private ArrayList<ArrayList<String>> list_data = new ArrayList<ArrayList<String>>();
    private int currentPage;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private ProgressDialog progDialog;
    private ArrayList<PoiItem> poiItems;
    private ArrayList<String> list_title = new ArrayList<String>(), list_content = new ArrayList<String>();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.orderlist);
        initView();
        setListener();
        doSearch();

    }

    private void doSearch() {

        progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("请稍等...");
        progDialog.show();

        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query("停车", "", EasyParkingApplication.getCity());
        // 设置每页最多返回多少条poiitem
        query.setPageSize(6);
        // 设置查第一页
        query.setPageNum(currentPage);

        poiSearch = new PoiSearch(this.getApplicationContext(), query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                Log.d("TAG", "onPoiSearched, rCode = " + rCode);

                if (rCode == 0) {
                    // 搜索poi的结果
                    // 是否是同一条
                    if (poiResult != null && poiResult.getQuery() != null) {
                        if (poiResult.getQuery().equals(query)) {
                            poiResult = poiResult;
                            // 取得搜索到的poiitems有多少页
                            // 取得第一页的poiitem数据，页数从数字0开始
                            poiItems = poiResult.getPois();
                            // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                            List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                            if (poiItems != null && poiItems.size() > 0) {
                                for (int i = 0; i < poiItems.size(); i++) {
                                    list_title.add(poiItems.get(i).getTitle());
                                    list_content.add(poiItems.get(i).getSnippet());
                                    Log.d("TAG", "title : " + poiItems.get(i).getTitle() + " , the content : " + poiItems.get(i).getSnippet());
                                }
                                list_data.add(list_title);
                                list_data.add(list_content);
//                                initView();
//                                setListener();
                                String parkingnames = gson.toJson(list_title);
                                String locals = gson.toJson(list_content);
                                Log.d("TAG", "parking names :" + parkingnames + "  locals " + locals);
                                new Service(orderlistActivity.this).getParkinglots(parkingnames, locals);

                            } else if (suggestionCities != null
                                    && suggestionCities.size() > 0) {
                                progDialog.dismiss();
                                showSuggestCity(suggestionCities);
                            } else {
                                progDialog.dismiss();
                                Toast.makeText(EasyParkingApplication.getContext(), "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        progDialog.dismiss();
                        Toast.makeText(EasyParkingApplication.getContext(), "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                    }
                } else if (rCode == 27) {
                    progDialog.dismiss();
                    Toast.makeText(EasyParkingApplication.getContext(), "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
                } else if (rCode == 32) {
                    progDialog.dismiss();
                    Toast.makeText(EasyParkingApplication.getContext(), "key验证无效！", Toast.LENGTH_SHORT).show();
                } else {
                    progDialog.dismiss();
                    Toast.makeText(EasyParkingApplication.getContext(), "未知错误，请稍后重试!错误码为" + rCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    private void showSuggestCity(List<SuggestionCity> suggestionCities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < suggestionCities.size(); i++) {
            infomation += "城市名称:" + suggestionCities.get(i).getCityName() + "城市区号:"
                    + suggestionCities.get(i).getCityCode() + "城市编码:"
                    + suggestionCities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(EasyParkingApplication.getContext(), infomation, Toast.LENGTH_SHORT).show();
    }


    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                title = (TextView) view.findViewById(R.id.title);
                content = (TextView) view.findViewById(R.id.content);
                Log.d("TAG", "OrderList:" + title.getText());
                Bundle bundle = new Bundle();
                bundle.putString("title", title.getText().toString());
                bundle.putString("local", content.getText().toString());
                Intent intent = new Intent(orderlistActivity.this, freePlaceList.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        });
        headerback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderlistActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initView() {
//        progDialog.dismiss();
        headerback_btn = (ImageButton) findViewById(R.id.btnHeaderBack);
        listView = (ListView) findViewById(R.id.listView);
//        mAdapter = new parkingListAdapter(this, list_data);  创建列表的语句
//        listView.setAdapter(mAdapter);
//        setListViewHeightBasedOnChildren(listView);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
//            System.out.println("the len is " + len);
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
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

    public void getParkinglotsResult(Parkinglots parkinglots) {
        progDialog.dismiss();

        if (parkinglots == null) {
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
            int r = parkinglots.getResult();
            String message = parkinglots.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("预定失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //成功
                ArrayList<Double> list_charge_day = (ArrayList<Double>) parkinglots.getCharge_days();
                ArrayList<Double> list_charge_night = (ArrayList<Double>) parkinglots.getCharge_nights();
                ArrayList<Integer> frees = (ArrayList<Integer>) parkinglots.getFrees();
                mAdapter = new parkingListAdapter(this, list_data, list_charge_day, list_charge_night, frees);  //创建列表的语句
                listView.setAdapter(mAdapter);
                setListViewHeightBasedOnChildren(listView);
            }
        }

    }

}
