package com.example.administrator.easyparking.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.activitys.freePlaceList;
import com.example.administrator.easyparking.model.ParkinglotsRatio;
import com.example.administrator.easyparking.utils.EasyParkingApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/5 0005.
 */
public class fragment_map extends Fragment implements
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener {
    private MapView mapView;
    private AMap aMap;
    private View ConvertView;
    private LocationManagerProxy mLocationManagerProxy;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationListener aMapLocationListener;
    private ProgressDialog progDialog;
    private int currentPage;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private double longitude;
    private double latitude;
    private String city = "";
    private Boolean SearchFlag = true;
    private AMapLocation mAMapLocation;
    private List<PoiItem> poiItems;
    private Marker currentMarker = null;
    private ArrayList<ArrayList<String>> list_data = new ArrayList<ArrayList<String>>();
    private ArrayList<String> list_title = new ArrayList<String>();
    private ArrayList<String> list_content = new ArrayList<String>();
    private Activity mActivity;

    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConvertView = inflater.inflate(R.layout.fragment_map, container, false);
        return ConvertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = this.getActivity();
        mapView = (MapView) ConvertView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        init();
        new SearchTask().execute();
    }

    public void getParkinglotsRatioResult(ParkinglotsRatio parkinglotsRatio) {
        progDialog.dismiss();
        if (parkinglotsRatio == null) {
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
            int r = parkinglotsRatio.getResult();
            String message = parkinglotsRatio.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("获取信息失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //成功
                ArrayList<Integer> ratio_list = (ArrayList<Integer>) parkinglotsRatio.getRatio_list();
                drawMarkerOnMap(ratio_list);
            }
        }
    }

    class SearchTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            setupMap();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            while (true) {
                if (!city.equals("") && city != null) {
                    Log.d("TAG", "City:  " + city);
//                    doSearchQuery();
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            doSearchQuery();
        }
    }

    private void init() {
        progDialog = new ProgressDialog(this.getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("请稍等...");
        progDialog.show();
        aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
//                        mAMapLocation = aMapLocation;
                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                    Log.d("TAG", "longitude:" + longitude + "  latitude:" + latitude);
                    if (aMapLocation.getCity() != null)
                        city = aMapLocation.getCity();
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                }
            }
        };
    }

    private void setupMap() {
//        doSearchQuery();
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if (mLocationManagerProxy == null) {
                    mLocationManagerProxy = LocationManagerProxy.getInstance(EasyParkingApplication.getContext());
                    mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 10 * 1000, 15, aMapLocationListener);
                    Log.d("TAG", "activate");
//                    Log.d("TAG", "city：" + city);
//                    doSearchQuery();
                }
            }

            @Override
            public void deactivate() {
                mListener = null;
                if (mLocationManagerProxy != null) {
                    mLocationManagerProxy.removeUpdates(aMapLocationListener);
                    mLocationManagerProxy.destroy();
                }
                mLocationManagerProxy = null;

            }
        });

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式

//        LatLng mTarget = aMap.getCameraPosition().target;
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(mTarget));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
    }


    private void doSearchQuery() {
        EasyParkingApplication.setCity(city);
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query("停车", "", EasyParkingApplication.getCity());
        // 设置每页最多返回多少条poiitem
        query.setPageSize(6);
        // 设置查第一页
        query.setPageNum(currentPage);
        poiSearch = new PoiSearch(this.getActivity().getApplicationContext(), query);
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
//                                aMap.clear();// 清理之前的图标
//                                for (int i = 0; i < poiItems.size(); i++) {
//                                    Log.d("TAG", "the title is " + poiItems.get(i).getTitle() + " , " + " the content is " + poiItems.get(i).getSnippet());
//                                    Log.d("TAG", "the getLatitude is " + poiItems.get(i).getLatLonPoint().getLatitude() + " , " + " the getLongitude is " + poiItems.get(i).getLatLonPoint().getLongitude());
//                                    drawMarkerOnMap(new LatLng(poiItems.get(i).getLatLonPoint().getLatitude(), poiItems.get(i).getLatLonPoint().getLongitude()), poiItems.get(i).getTitle(), poiItems.get(i).getSnippet());
//                                }
//                                PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                                poiOverlay.removeFromMap();
//                                poiOverlay.addToMap();
//                                poiOverlay.zoomToSpan();
                                for (int i = 0; i < poiItems.size(); i++) {
                                    list_title.add(poiItems.get(i).getTitle());
                                    list_content.add(poiItems.get(i).getSnippet());
//                                    Log.d("TAG", "title : " + poiItems.get(i).getTitle() + " , the content : " + poiItems.get(i).getSnippet());
                                }
                                String parkingnames = gson.toJson(list_title);
                                String locals = gson.toJson(list_content);
//                                System.out.println(parkingnames+"  "+locals);
                                new Service(fragment_map.this).getParkinglotsRatio(parkingnames, locals);

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

    private void drawMarkerOnMap(List<Integer> list_ratio) {
//        if (aMap != null && point != null) {
//            Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
//                    .position(point)
//                    .title(title)
//                    .snippet(snippet)
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.greenratio)));
        if (aMap != null) {
            for (int i = 0; i < list_ratio.size(); i++) {
                int resource_id = 0;
                int temp = list_ratio.get(i);
                switch (temp) {
                    case 1:
                        resource_id = R.mipmap.greenratio;
                        break;
                    case 2:
                        resource_id = R.mipmap.yellow_ratio;
                        break;
                    case 3:
                        resource_id = R.mipmap.red_ratio;
                        break;
                    case 4:
                        resource_id = R.mipmap.gray_ratio;
                        break;
                    case 5:
                        resource_id = R.mipmap.black_ratio;
                        break;
                }
                LatLng point = new LatLng(poiItems.get(i).getLatLonPoint().getLatitude(), poiItems.get(i).getLatLonPoint().getLongitude());
                String title = poiItems.get(i).getTitle();
                String snippet = poiItems.get(i).getSnippet();
                if (point != null) {
                    aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
                            .position(point)
                            .title(title)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromResource(resource_id)));
                }
            }
        }

    }

    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(EasyParkingApplication.getContext(), infomation, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mLocationManagerProxy.removeUpdates(aMapLocationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            currentMarker = marker;
            jumpPoint(marker, marker.getPosition());
        }
        return false;
    }

    private void jumpPoint(final Marker marker, final LatLng latLng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * latLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * latLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        marker.hideInfoWindow();
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        for (int i = 0; i < poiItems.size(); i++) {
            list_title.add(poiItems.get(i).getTitle());
            list_content.add(poiItems.get(i).getSnippet());
//            Log.d("TAG", "the title is " + poiItems.get(i).getTitle() + " , " + " the content is " + poiItems.get(i).getSnippet());
        }
        final View infoWindow = this.getActivity().getLayoutInflater().inflate(R.layout.infowindow, null);
        TextView content = (TextView) infoWindow.findViewById(R.id.content);
        TextView spint = (TextView) infoWindow.findViewById(R.id.spint);
        Button show_Info = (Button) infoWindow.findViewById(R.id.show_info_btn);
        String s = marker.getTitle();
        content.setText(s);
        String sp = marker.getSnippet();
        spint.setText(sp);

        show_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(EasyParkingApplication.getContext(), "我要跳转！！", Toast.LENGTH_SHORT).show();
//                Toast.makeText(EasyParkingApplication.getContext(), "我要等待红歌", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("title", marker.getTitle());
                bundle.putString("local", marker.getSnippet());
                Intent intent = new Intent(mActivity, freePlaceList.class);
                intent.putExtras(bundle);
                startActivity(intent);
                onDestroyView();
                mActivity.finish();
            }
        });
        return infoWindow;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
        }
    }

    public String getCity() {
        return city;
    }

}
