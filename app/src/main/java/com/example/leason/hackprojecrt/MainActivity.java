package com.example.leason.hackprojecrt;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekbar;
    private Button btn_search;
    private gps gpsLocate;
    private Spinner spinner_size;
    private String spinnertext[] = {"全選", "大", "中", "小"};
    private String size = null;
    private ArrayAdapter spiinerSizeList;
    private TextView textDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsLocate = new gps(this);

        iniView();
        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                size = spinnertext[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    void iniView() {
        spinner_size = (Spinner) findViewById(R.id.spinner_size);
        spiinerSizeList = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, spinnertext);
        spiinerSizeList.setDropDownViewResource(R.layout.myspinner);
        spinner_size.setAdapter(spiinerSizeList);

        seekbar = (SeekBar) findViewById(R.id.seekbar_distance);
        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(search);
        textDistance = (TextView) findViewById(R.id.text_distance);
        seekbar.setOnSeekBarChangeListener(distance);
        textDistance.setText("距離:" + String.valueOf((seekbar.getProgress() / 20.0)) + " 公里");
    }

    Button.OnClickListener search = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {


            Location location_value = gpsLocate.getLocation();
            Double longitude = location_value.getLongitude();    //取得經度
            Double latitude = location_value.getLatitude();    //取得緯度
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ListForSearch.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("longitude", longitude);
            bundle.putDouble("latitude", latitude);
            bundle.putString("size", size);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    SeekBar.OnSeekBarChangeListener distance = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textDistance.setText("距離:" + String.valueOf((progress / 20.0)) + " 公里");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }


    };
}