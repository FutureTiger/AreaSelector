package com.example.xd.myareaselector.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.xd.myareaselector.R;
import com.example.xd.myareaselector.model.Province;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.START;

public class AddressSelectWindow extends PopupWindow {
    private PopupWindow mPopupWindow;
    private Activity mContext;

    private WheelView mProvince,mCity,mArea;
    private int mProvinceIndex,mCityIndex,mZoneIndex;

    private List<Province> mProvinceList;
    private List<String> mProvinceNameList = new ArrayList<>();
    private List mCityNameList = new ArrayList<>();
    private List<String> mZoneNameList = new ArrayList<>();
    private OnSelectedDataChangeListener mListener;

    public AddressSelectWindow(Activity context,List<Province> list,OnSelectedDataChangeListener listener) {
        super(context);
        mContext = context;
        mProvinceList = list;
        mListener = listener;
        initPopWindow();
    }

    public void disMissPopWindow(){
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public  void showPopWindow(){
        int[] location = new int[2];
        View view = mContext.getWindow().getDecorView();
        view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, START | BOTTOM, 0, -location[1]);
    }

    public void setWheelPosition(String province,String city,String zone){
        Log.i("VIND","province:"+province+"   city:"+city+"   zone:"+zone);
        if(province!=null&&!TextUtils.isEmpty(province)){
            int pIndex = mProvinceNameList.indexOf(province);
            List cities = mProvinceList.get(pIndex).getCityNameList();
            int cIndex = cities.indexOf(city);
            List<String> zones = mProvinceList.get(pIndex).cityList.get(cIndex).zoneList;
            int zIndex = zones.indexOf(zone);
            Log.i("VIND","pIndex:"+pIndex+"   cIndex:"+cIndex+"   zIndex:"+zIndex);
            mProvinceIndex = pIndex;
            mCityIndex = cIndex;
            mZoneIndex = zIndex;
            initData();
        }
    }

    private void initPopWindow(){
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        @SuppressLint("InflateParams")
        LinearLayout layout = (LinearLayout) mContext.getLayoutInflater().inflate(R.layout.pop_select_area, null);
        mPopupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        //点击空白处时，隐藏掉pop窗口
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.pop_back_color)));
        //添加弹出、弹入的动画
        mPopupWindow.setAnimationStyle(R.style.Popupwindow);


        mProvince = layout.findViewById(R.id.province);
        mProvince.setOnItemSelectedListener((index,item) -> doDataChange(0,index));
        mCity = layout.findViewById(R.id.city);
        mCity.setOnItemSelectedListener((index,item) -> doDataChange(1,index));
        mArea = layout.findViewById(R.id.area);
        mArea.setOnItemSelectedListener((index,item) -> doDataChange(2,index));
        initData();

        ImageView cancel = layout.findViewById(R.id.pop_cancel);
        cancel.setOnClickListener(v -> disMissPopWindow());
        TextView ok = layout.findViewById(R.id.pop_ok);
        ok.setOnClickListener(v -> doSelectOK());
    }

    private void initData(){
        mProvinceNameList = new ArrayList<>();
        mCityNameList = mProvinceList.get(mProvinceIndex).getCityNameList();
        mZoneNameList = mProvinceList.get(mProvinceIndex).cityList.get(mCityIndex).zoneList;
        for (Province p : mProvinceList){
            mProvinceNameList.add(p.province);
        }

        mProvince.setItems(mProvinceNameList,mProvinceIndex);
        mCity.setItems(mCityNameList,mCityIndex);
        mArea.setItems(mZoneNameList,mZoneIndex);
    }

    private void doSelectOK(){
        mListener.onItemChanged(mProvince.getSelectedItem(),mCity.getSelectedItem(),mArea.getSelectedItem());
        disMissPopWindow();
    }

    private void doDataChange(int type,int index){
        switch (type){
            case 0:
                mProvinceIndex=index;
                mCityNameList = mProvinceList.get(mProvinceIndex).getCityNameList();
                mCity.setItems(mCityNameList,0);
                mZoneNameList = mProvinceList.get(mProvinceIndex).cityList.get(0).zoneList;
                mArea.setItems(mZoneNameList,0);
                break;
            case 1:
                mCityIndex=index;
                mZoneNameList = mProvinceList.get(mProvinceIndex).cityList.get(mCityIndex).zoneList;
                mArea.setItems(mZoneNameList,0);
                break;
            case 2:
                mZoneIndex=index;
                break;
            default:
                break;
        }
    }

    public  interface OnSelectedDataChangeListener {
        void onItemChanged(String province,String city,String area);
    }
}
