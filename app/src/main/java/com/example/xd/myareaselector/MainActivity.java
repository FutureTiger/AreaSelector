package com.example.xd.myareaselector;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.xd.myareaselector.model.Province;
import com.example.xd.myareaselector.utils.AssetsUtil;
import com.example.xd.myareaselector.widget.AddressSelectWindow;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AddressSelectWindow window;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.area);
        editText.setOnFocusChangeListener(this::selectArea);
        editText.setOnClickListener(v->selectArea(v,true));
        AssetsUtil util = new AssetsUtil();
        String json = util.getJsonString("area.json",this);
        List<Province> mProvinceList = util.jsonStringToArray(json);
        window = new AddressSelectWindow(this,mProvinceList,this::doSelectOK);
    }

    private void selectArea(View v,boolean hasFocus){

        window.setWheelPosition("湖北省","荆州市","石首市");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && hasFocus) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            window.showPopWindow();
        }else if(!hasFocus){
            window.disMissPopWindow();
        }

    }

    private void doSelectOK(String province,String city,String zone){
        editText.setText(province+" "+city+" "+zone);
    }
}
