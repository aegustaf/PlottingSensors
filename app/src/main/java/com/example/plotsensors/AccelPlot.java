package com.example.plotsensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class AccelPlot extends View {

    ArrayList<Float> array = new ArrayList<>(10);
    ArrayList<Float> mean = new ArrayList<>(10);
    ArrayList<Float> std_dv = new ArrayList<>(10);
    int offset = 0;

    public AccelPlot(Context context) {
        super(context);
    }

    public AccelPlot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccelPlot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AccelPlot(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //create graph
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(30);

        // vertical lines
        for(int i = 0; i < 10; i++) {
            canvas.drawLine((this.getWidth()-100)/10*i+100, 0, (this.getWidth()-100)/10*i+100, this.getHeight()-100, p);
        }
        // horizontal lines
        for(int i = 0; i < 6; i++) {
            canvas.drawLine(100, (this.getHeight() - 100) / 5 * i, this.getWidth(), (this.getHeight() - 100) / 5 * i, p);
        }
        // add y-axis measurements
        for(int i=5; i>=0; i--) {
            int num = 50 - i * 10;
            canvas.drawText(""+num, 50, (this.getHeight()-100)/5*i, p);
        }
        // add x-axis measurements
        for(int i = 0; i < 10; i++) {
            int label = i + offset;
            canvas.drawText(""+label, (this.getWidth()-100)/10*i+100, this.getHeight()-50, p);
        }
        offset++;
        //add label to y-axis
        p.setTextSize(50);
        canvas.drawText("D", 0, 250, p);
        canvas.drawText("A", 0, 350, p);
        canvas.drawText("T", 0, 450, p);
        canvas.drawText("A", 0, 550, p);

        //add label to x-axis
        canvas.drawText("Time (x100 msec)", this.getWidth()/3, this.getHeight(), p);

        ArrayList<Float> x_vals = new ArrayList<>(10);
        ArrayList<Float> y_vals = new ArrayList<>(10);
        ArrayList<Float> y_vals_mean = new ArrayList<>(10);
        ArrayList<Float> y_vals_sd = new ArrayList<>(10);

        Paint p1 = new Paint();
        p1.setColor(Color.BLUE);
        p1.setStyle(Paint.Style.FILL);

        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setStyle(Paint.Style.FILL);

        Paint p3 = new Paint();
        p3.setColor(Color.GREEN);
        p3.setStyle(Paint.Style.FILL);

        // creates data points
        for (int i = 0; i < array.size(); i++) {
            float x_coor = (float) (this.getWidth()-100) / 10 * i +100;
            x_vals.add(x_coor);
            float y_coor = (float) (this.getHeight()-100) - (((this.getHeight()-100) / 50) * array.get(i));
            float y_coor_mean = (float) (this.getHeight()-100) - (((this.getHeight()-100) / 50) * mean.get(i));
            float y_coor_sd = (float) (this.getHeight()-100) - (((this.getHeight()-100) / 50) * std_dv.get(i));
            y_vals.add(y_coor);
            y_vals_mean.add(y_coor_mean);
            y_vals_sd.add(y_coor_sd);
            canvas.drawCircle(x_coor, y_coor, 15, p1);
            canvas.drawCircle(x_coor, y_coor_mean, 15, p2);
            canvas.drawCircle(x_coor, y_coor_sd, 15, p3);

        }
        // create lines connecting data points
        p1.setStyle(Paint.Style.STROKE);
        p1.setStrokeWidth(7);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(7);
        p3.setStyle(Paint.Style.STROKE);
        p3.setStrokeWidth(7);

        if (array.size() >= 2) {
            for (int i = 0; i < array.size(); i++) {
                if(i < array.size()-1) {
                    canvas.drawLine(x_vals.get(i), y_vals.get(i), x_vals.get(i+1), y_vals.get(i+1), p1);
                    canvas.drawLine(x_vals.get(i), y_vals_mean.get(i), x_vals.get(i+1), y_vals_mean.get(i+1), p2);
                    canvas.drawLine(x_vals.get(i), y_vals_sd.get(i), x_vals.get(i+1), y_vals_sd.get(i+1), p3);
                }
            }
        }


    }





    public void clearList() {
        array.clear();
    }

    public void addPoint(float num) {
        if(array.size() < 10) {
            array.add(num);
        } else if(array.size() == 10) {
            array.remove(0);
            array.add(num);
        }
        Log.v("ACCEL_TAG", "Accel is: " + num);
    }

    public float addMeanPoint(float num) {
        if(mean.size() < 3) {
            mean.add(num);
            return num;
        } else if(mean.size() < 10) {
            float total = (array.get(array.size() - 2) + array.get(array.size()-3) + array.get(array.size()-4)) / 3;
            mean.add(total);
            return total;
        } else if(mean.size() == 10) {
            mean.remove(0);
            float total = (array.get(array.size() - 2) + array.get(array.size()-3) + array.get(array.size()-4)) / 3;
            mean.add(total);
            return total;
        }
        return num;
    }

    public void addSDPoint(float num) {
        if(std_dv.size() < 3) {
            std_dv.add(num);
        } else if(std_dv.size() < 10) {
            float calc_mean = (array.get(array.size() - 2) + array.get(array.size()-3) + array.get(array.size()-4)) / 3;
            float first = array.get(array.size()-2) - calc_mean;
            first = first * first;
            float second = array.get(array.size()-3) - calc_mean;
            second = second * second;
            float third = array.get(array.size()-4) - calc_mean;
            third = third * third;
            float new_mean = (first + second + third)/2;
            float return_val = (float) Math.sqrt((double) new_mean);
            Log.v("STAND_DEV", "Standard Deviation is: " + return_val);
            std_dv.add(return_val);
        } else if(mean.size() == 10) {
            std_dv.remove(0);
            float calc_mean = (array.get(array.size() - 2) + array.get(array.size()-3) + array.get(array.size()-4)) / 3;
            float first = array.get(array.size()-2) - calc_mean;
            first = first * first;
            float second = array.get(array.size()-3) - calc_mean;
            second = second * second;
            float third = array.get(array.size()-4) - calc_mean;
            third = third * third;
            float new_mean = (first + second + third)/2;
            float return_val = (float) Math.sqrt((double) new_mean);
            std_dv.add(return_val);
        }

    }
}
