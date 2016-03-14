package com.example.leason.hackprojecrt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by leason on 2016/3/12.
 */
public class gps implements LocationListener {
    private LocationManager lms;
    private  LocationManager status;
    private Context context;
gps(Context x) {
    context=x;
     status = (LocationManager) (context.getSystemService(Context.LOCATION_SERVICE));
}
   public Location getLocation(){

       if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
           //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
           return  locationServiceInitial();

       } else {
           Toast.makeText(context, "請開啟定位服務", Toast.LENGTH_LONG).show();
           context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
           return null;
       }



   }

    @Override
    public void onLocationChanged(Location location) {    //當地點改變時
        // TODO Auto-generated method stub3
        getLocation(location);
    }

    @Override
    public void onProviderDisabled(String arg0) {    //當GPS或網路定位功能關閉時
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {    //當GPS或網路定位功能開啟
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {    //定位狀態改變
        //status=OUT_OF_SERVICE 供應商停止服務
        //status=TEMPORARILY_UNAVAILABLE 供應商暫停服務
    }

    private void getLocation(Location location) {    //將定位資訊顯示在畫面中
        if (location != null) {
           // TextView longitude_txt = (TextView) findViewById(R.id.longitude);
           // TextView latitude_txt = (TextView) findViewById(R.id.latitude);

            Double longitude = location.getLongitude();    //取得經度
            Double latitude = location.getLatitude();    //取得緯度

            //longitude_txt.setText(String.valueOf(longitude));
            //latitude_txt.setText(String.valueOf(latitude));
        } else {
            Toast.makeText(context, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    private String bestProvider = LocationManager.GPS_PROVIDER;    //最佳資訊提供者

    private Location locationServiceInitial() {
        lms = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);    //取得系統定位服務
        Criteria criteria = new Criteria();    //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Location location = lms.getLastKnownLocation(bestProvider);
        while(location  == null) {
            lms.requestLocationUpdates("gps", 60000, 1, this);
        }
        getLocation(location);
        return location;
    }



}
