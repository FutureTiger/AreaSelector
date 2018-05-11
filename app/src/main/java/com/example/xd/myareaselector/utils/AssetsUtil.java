package com.example.xd.myareaselector.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.xd.myareaselector.model.City;
import com.example.xd.myareaselector.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AssetsUtil {

    public   String getJsonString(String path, Context context) {
        AtomicReference<StringBuilder> sb = new AtomicReference<>(new StringBuilder());
        AssetManager am = context.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(path)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.get().append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    public  List<Province> jsonStringToArray(String str){
        List<Province> provinceList = new ArrayList<>();
        try {
            JSONObject dataJson = new JSONObject(str);
            JSONArray provinces = dataJson.getJSONArray("provinces");
            for(int i = 0; i < provinces.length(); i++){
                JSONObject province = provinces.getJSONObject(i);
                String pro = province.getString("name");  //得到省

                JSONArray cities = province.getJSONArray("city");
                List<City> cityList = new ArrayList<>();
                for(int j = 0; j < cities.length(); j++){
                    JSONObject cityJson = cities.getJSONObject(j);
                    String city = cityJson.getString("name");//得到城市
                    List<String> zoneList = new ArrayList<>();
                    JSONArray zones = cityJson.getJSONArray("area");
                   for (int k = 0;k < zones.length(); k++){
                       zoneList.add(zones.getString(k));
                   }
                   City c = new City();
                   c.city = city;
                   c.zoneList = zoneList;
                    cityList.add(c);
                }
                Province p = new Province();
                p.province = pro;
                p.cityList = cityList;
                provinceList.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return provinceList;
    }

}
