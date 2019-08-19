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

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity implements SensorEventListener{
    Sensor s;
    SensorManager sm;
    ArrayList<Float> array;
    ArrayList<Float> total_array;
    long lastTime;
    long currentTime;
    boolean firstRound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lastTime = System.currentTimeMillis();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this, s, 1000000);

        ImageView img = (ImageView) findViewById(R.id.car);
        img.setBackgroundResource(R.drawable.car_slow);
//        img.setBackgroundResource(R.drawable.car_anim);

//        ((AnimationDrawable)img.getBackground()).start();
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

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double value = Math.sqrt(x * x + y * y + z * z);

            Log.v("MYTAG", "Accelerometer = " + value);
            AccelPlot ref = findViewById(R.id.accelPlot);
            ref.addPoint((float) value);
            float mean = ref.addMeanPoint((float) value);

            ImageView img = (ImageView) findViewById(R.id.car);
            if (mean < 12) {
                img.setBackgroundResource(R.drawable.car_slow);
            } else if (mean < 18) {
                img.setBackgroundResource(R.drawable.car_med);
            } else {
                img.setBackgroundResource(R.drawable.car_fast);
            }

            ref.addSDPoint((float) value);
            ref.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();;
        sm.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, s, 100000);
    }
}
