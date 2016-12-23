package com.sattva.iotawifi;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class PlottingIntentService extends IntentService {


    double fHR;
    int f_value_counter, f_diff;


    public PlottingIntentService() {
        super("PlottingIntentService");
    }


    Intent send_intent = new Intent("to_plot_data");



    @Override
    protected void onHandleIntent(Intent intent)
    {

        f_diff = 0;

        for(f_value_counter  = 8; f_value_counter < ApplicationUtils.FQRS.length - 1; f_value_counter++)
        {


            fHR = (60.0 * 8000.0)/(ApplicationUtils.FQRS[f_value_counter] - ApplicationUtils.FQRS[f_value_counter - 8]);


            f_diff = ApplicationUtils.FQRS[f_value_counter] - ApplicationUtils.FQRS[f_value_counter - 1];

            try {
                Thread.sleep(f_diff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            send_intent.putExtra("y", (float)fHR);
            send_intent.putExtra("x", (float)ApplicationUtils.FQRS[f_value_counter]);


            LocalBroadcastManager.getInstance(this).sendBroadcast(send_intent);
        }


        Log.e("PlottingIntentService", "plotting for 15k done at" + (System.currentTimeMillis() - ApplicationUtils.startMS));

    }





}
