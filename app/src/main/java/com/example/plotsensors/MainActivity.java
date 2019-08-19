package com.example.plotsensors;

import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sm1, sm2;
    Sensor s1, s2;
    Button b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSensorInfo();
    }

    public void setSensorInfo() {
        boolean accelerometer;
        boolean light;

        TextView text1 = (TextView) findViewById(R.id.info1);
        TextView text2 = (TextView) findViewById(R.id.info2);

        sm1 = (SensorManager) getSystemService(SENSOR_SERVICE);
        s1 = sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometer = sm1.registerListener(this, s1, 100000);
        if(accelerometer) {
            text1.setText("Status: Accelerometer is Present\nInfo: Max Range = " +
                    s1.getMaximumRange() + "\nResolution= " + s1.getResolution() +
                    "\nMin Delay=" + s1.getMinDelay());
        } else {
            text1.setText("Status: Accelerometer is Not Present");
        }

        sm2 = (SensorManager) getSystemService(SENSOR_SERVICE);
        s2 = sm2.getDefaultSensor(Sensor.TYPE_LIGHT);
        light = sm2.registerListener(this, s2, 100000);
        if(light) {
            text2.setText("Status: Light Sensor is Present\nInfo: Max Range = " +
                    s2.getMaximumRange() + "\nResolution= " + s2.getResolution() +
                    "\nMin Delay=" + s2.getMinDelay());
        } else {
            text2.setText("Status: Light Sensor is Not Present");
        }
    }

    public void launchSecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void launchThirdActivity(View view) {
        startActivity(new Intent(this, ThirdActivity.class));
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm1.unregisterListener(this);
        sm2.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm1.registerListener(this, s1, 100000);
        sm2.registerListener(this, s2, 100000);
    }
}
