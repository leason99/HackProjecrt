package com.example.leason.hackprojecrt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class listforitem extends AppCompatActivity {

    private Handler mHandler;
    private String getData = null;
    private Button btnResearch;
    ListView lstPrefer;
    private TextView textView;
    String location[] = null;
    String size[] = null;
    int id[] = null;
    int icon[] = {R.drawable.l, R.drawable.m, R.drawable.s};
    int siteID;
    String string = null;
    String using[] = null;
    String total[] = null;
    ProgressDialog progressDialog;
    int searchItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listforitem);
        lstPrefer = (ListView) findViewById(R.id.lstPrefer2);
        btnResearch = (Button) findViewById(R.id.research);
        textView = (TextView) findViewById(R.id.textView4);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        btnResearch.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {
                ListForSearch.ListForSearchActivity.finish();
                listforitem.this.finish();
            }
        });
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        searchItemID = bundle.getInt("ID");
        siteID = bundle.getInt("ID");
        string = bundle.getString("intro");
        String secondUri = "http://coder.tw:3000/coinlocker/" + String.valueOf(siteID);
        getString(secondUri);

        mHandler = new Handler() {
            int i = 0;

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: //text.setText(getData);

                        try {
                            //       JSONObject jsonRootObject = new JSONObject(getData);
                            textView.setText(string.substring(1, 70) + ".......");
                            JSONArray jsonArray = new JSONArray(getData);
                            location = new String[jsonArray.length()];
                            using = new String[jsonArray.length()];
                            total = new String[jsonArray.length()];
                            id = new int[jsonArray.length()];
                            size = new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                id[i] = Integer.parseInt(jsonObject.optString("id").toString());
                                location[i] = jsonObject.optString("location").toString();
                                using[i] = jsonObject.optString("using").toString();
                                total[i] = jsonObject.optString("total").toString();
                                size[i] = jsonObject.optString("size").toString();
                            }
                            // 建立自訂的 Adapter
                            MyAdapter adapter = new MyAdapter(listforitem.this);
                            // 設定 ListView 的資料來源
                            lstPrefer.setAdapter(adapter);
                            progressDialog.dismiss();
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
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {

                final String[] getdata = new String[1];
                System.out.println("here");
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
                return getdata[0];
            }
        }.execute(Uri);
        return getdata[0];
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }
        ViewHolder viewHolder; // view lookup cache stored in tag
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
           // convertView = myInflater.inflate(R.layout.itemlist, null);


            if (convertView == null) {
                viewHolder = new ViewHolder();
              //  LayoutInflater inflater = LayoutInflater.from();
                convertView = myInflater.inflate(R.layout.itemlist, parent, false);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.imgLogo);
                switch (size[position]) {
                    case "大":
                        viewHolder.icon.setImageResource(icon[0]);
                        break;
                    case "中":
                        viewHolder.icon.setImageResource(icon[1]);
                        break;
                    case "小":
                        viewHolder.icon.setImageResource(icon[2]);
                        break;
                }
                TextView txtName = ((TextView) convertView.findViewById(R.id.txtName));
                TextView txtengName = ((TextView) convertView.findViewById(R.id.txtengName));
                TextView use = ((TextView) convertView.findViewById(R.id.usign));
                // 設定元件內容

//            imgLogo.setImageResource(icon[2]);
                txtName.setText(location[position]);
                //或 txtName.setText(""+getItem(position));
                txtengName.setText("size :  " + size[position]);
                use.setText("總共:  " + total[position] + "   已使用:" + using[position]);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

        // 取得 mylayout.xml 中的元件
    //    ImageView imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);

            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView icon;

    }

}
