package com.example.plotsensors;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class ThirdActivity extends AppCompatActivity implements SensorEventListener {

    Sensor s;
    SensorManager sm;
    long lastTime;
    long currentTime;
    boolean firstRound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        lastTime = System.currentTimeMillis();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        sm.registerListener(this, s, 1000000);

        ImageView img = (ImageView) findViewById(R.id.sky);
        img.setBackgroundResource(R.drawable.light_anim);

        ((AnimationDrawable)img.getBackground()).start();
    }

    public void launchMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentTime = System.currentTimeMillis();
        if(currentTime-lastTime > 99 || firstRound == true ) {
            lastTime = currentTime;
            firstRound = false;

            float value = event.values[0];
            Log.v("MYTAG", "Light = " + value);
            LightPlot ref = findViewById(R.id.lightPlot);
            ref.addPoint(value);
            float mean = ref.addMeanPoint(value);

            ImageView img = (ImageView) findViewById(R.id.sky);
            if (mean < 200) {
                img.setBackgroundResource(R.drawable.light_moon);
            } else {
                img.setBackgroundResource(R.drawable.light_sun);
            }

            ref.addSDPoint(value);
            ref.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, s, 100000);
    }

}
