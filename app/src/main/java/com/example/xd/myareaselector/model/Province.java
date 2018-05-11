package com.example.xd.myareaselector.model;



import java.util.ArrayList;
import java.util.List;

public class Province{
   public String province;
   public List<City> cityList = new ArrayList<>();

   public List getCityNameList(){
       List citis = new ArrayList<String>();
       for(City city : cityList){
           citis.add(city.city);
       }
       return citis;
   }
}
