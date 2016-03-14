package com.example.leason.hackprojecrt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class ListForSearch extends AppCompatActivity {
    private Handler mHandler;
    private String getData = null;
    private int icon[]={R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};
    ListView lstPrefer;
    static Activity ListForSearchActivity;
    String name[] = null;
    String address[] = null;
    String intro[] = null;
    String Latitude[] = null;
    String Longitude[] = null;



    int id[] = null;
    String size;
    ProgressDialog waitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_for_search);
        ListForSearchActivity = this;
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        double longitude = bundle.getInt("longitude");
        double latitude = bundle.getInt("latitude");
        size = bundle.getString("size");
        waitDialog = new ProgressDialog(this);
        waitDialog.setMessage("處理中,請稍候...");
        waitDialog.show();
        lstPrefer = (ListView) findViewById(R.id.lstPrefer);
        lstPrefer.setOnItemClickListener(click);
        String firstUrl = "http://coder.tw:3000/coinlocker?";
        getString(firstUrl);
        mHandler = new Handler() {
            int i = 0;
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: //text.setText(getData);

                        try {
                            //  JSONObject jsonRootObject = new JSONObject(getData);
                            JSONArray jsonArray = new JSONArray(getData);
                            name = new String[jsonArray.length()];
                            address = new String[jsonArray.length()];
                            intro = new String[jsonArray.length()];
                            Latitude = new String[jsonArray.length()];
                            Longitude = new String[jsonArray.length()];
                            id = new int[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                id[i] = Integer.parseInt(jsonObject.optString("id").toString());
                                name[i] = jsonObject.optString("name").toString();
                                //  address[i] = jsonObject.optString("address").toString();
                                intro[i] = jsonObject.optString("intro").toString();
                                Latitude[i] = jsonObject.optString("latitude").toString();
                                Longitude[i] = jsonObject.optString("longitude").toString();
                                Location location=new Location("get");
                                location.setLatitude(Double.parseDouble(Latitude[i]));
                                location.setLongitude(Double.parseDouble(Longitude[i]));
                                address[i] = getAddressByLocation(location);
                            }
                            waitDialog.dismiss();
                            // 建立自訂的 Adapter
                            MyAdapter adapter = new MyAdapter(ListForSearch.this);
                            // 設定 ListView 的資料來源
                            lstPrefer.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    String getString(final String Uri) {
        final String[] getdata = new String[1];
        new Thread(new Runnable() {
            public void run() {
                final String[] getdata = new String[1];
                HttpURLConnection conn = null;
                URL url = null;
                try {
                    url = new URL(Uri);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    getdata[0] = reader.readLine();
                    reader.close();
                    getData = getdata[0];

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }).start();
        return getdata[0];
    }


    public String getAddressByLocation(Location location) {
        String returnAddress = null;
        try {
            if (location != null) {
                Double longitude = location.getLongitude();    //取得經度
                Double latitude = location.getLatitude();    //取得緯度
                //建立Geocoder物件: Android 8 以上模疑器測式會失敗
                Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);    //地區:台灣
                //自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
                //List<Address> lstAddress = lstAddress = gc.getFromLocationName("地址", 3);	//輸入地址回傳Location物件
                //	if (!Geocoder.isPresent()){ //Since: API Level 9
                //		returnAddress = "Sorry! Geocoder service not Present.";
                //	}
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return id.length;

        }

        @Override
        public Object getItem(int position) {
            return id[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.sitelist, null);
            ImageView imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);
            TextView txtName = ((TextView) convertView.findViewById(R.id.txtName));
            TextView txtengName = ((TextView) convertView.findViewById(R.id.txtengName));
            imgLogo.setImageResource(icon[position]);
            txtName.setText(name[position]);
            txtengName.setText(address[position]);
            return convertView;
        }
    }

    private ListView.OnItemClickListener click = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setClass(ListForSearch.this, listforitem.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", id[arg2]);
            bundle.putString("size", size);
            bundle.putString("intro", intro[arg2]);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    };
}
