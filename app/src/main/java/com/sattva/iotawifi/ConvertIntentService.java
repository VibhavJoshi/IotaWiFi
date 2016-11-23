package com.sattva.iotawifi;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.sattva.signalproc.AlgorithmMain;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vibhav on 16/11/16.
 */
public class ConvertIntentService extends IntentService {

    public ConvertIntentService() {
        super("ConvertIntentService");
    }


    int start_index;
    int end_index;
    double out;
    double Vref = 4.5;
    double check = Math.pow(2, 23);
    double check_divide = 2 * check;
    double gain = 24;
    String temp_test;
    String prev_temp_test;
    int input_array_counter;
    int absolute_sample_diff = 0;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH-mm-ss");
    String strDate = sdf.format(c.getTime());
    String fileName = "input-array-" +  strDate;




    @Override
    protected void onHandleIntent(Intent intent)

    {

        Log.e("ConvertIntentService", "entered ConvertIntent at: " + (System.currentTimeMillis() - ApplicationUtils.startMS));

        populate_input_array();

        Log.e("SocketIntentService", "Populated input array at: " + (System.currentTimeMillis() - ApplicationUtils.startMS));

        AlgorithmMain Algo = new AlgorithmMain();

        //Object[] Final = Algo.AlgoStart(ApplicationUtils.input_array);

        Object[] Final = Algo.AlgoStart(ApplicationUtils.test_input_array);//this is for testing entire app. the one above to be used in production.
        Log.e("SocketIntentService", "Algo completed at: " + (System.currentTimeMillis() - ApplicationUtils.startMS));

        int[] MQRS = (int[]) Final[0];
        int[] FQRS = (int[]) Final[1];

        Log.e("ConvertIntentService", "MQRS[0]: " + MQRS[0]);
        Log.e("ConvertIntentService", "FQRS[0: " + FQRS[0]);

        ApplicationUtils.convert_flag = 1;


    }


    public void populate_input_array()
    {

        temp_test = ApplicationUtils.dynamicDataStore.remove();

        while (temp_test.length() != 25)
        {
            temp_test = ApplicationUtils.dynamicDataStore.remove();
        }

        feed_input_array(temp_test, input_array_counter);

        for(input_array_counter++; input_array_counter < ApplicationUtils.input_array.length;input_array_counter++)
        {
            prev_temp_test = temp_test;
            temp_test = ApplicationUtils.dynamicDataStore.remove();
            start_index = (int)prev_temp_test.charAt(0);
            end_index = (int)temp_test.charAt(0);
            absolute_sample_diff = end_index - start_index;


            //(temp_test.length())!=25 || (absolute_sample_diff != 1) || (absolute_sample_diff != -9)
            if(!((temp_test.length() == 25) && ((absolute_sample_diff == 1) || (absolute_sample_diff ==-9))))
            {

                //Log.e("SocketIntentService", "Sample loss");

                while ((temp_test.length() != 25))
                {
                    Log.e("SocketIntentService", "Type: partial sample");
                    temp_test = ApplicationUtils.dynamicDataStore.remove();
                    end_index = (int)temp_test.charAt(0);
                    absolute_sample_diff = end_index - start_index;
                    input_array_counter++;

                }

                if(absolute_sample_diff <= 0)
                {
                    absolute_sample_diff = 10 + absolute_sample_diff;
                }

                Log.e("SocketIntentService", "Samples Lost = " + (absolute_sample_diff - 1));

                feed_input_array(temp_test,input_array_counter);
                interpolate(input_array_counter, input_array_counter - absolute_sample_diff);

            }
            else
            {
                feed_input_array(temp_test, input_array_counter);
            }
        }

    }

    public void interpolate(int current_input_index, int start_index)
    {

        Log.e("SocketIntentService", "Interpolating...");
        for(int k = start_index + 1; k< current_input_index; k++)
        {

            for(int input_channel_index = 0; input_channel_index < 4; input_channel_index++)
            {
                ApplicationUtils.input_array[k][input_channel_index] = ApplicationUtils.input_array[start_index][input_channel_index] + (ApplicationUtils.input_array[end_index][input_channel_index] - ApplicationUtils.input_array[start_index][input_channel_index])/(end_index - start_index)*k;

            }
        }
    }



    public void feed_input_array(String in_string, int input_index)
    {
        for(int input_channel_index = 0; input_channel_index < 4; input_channel_index++)
        {
            ApplicationUtils.input_array[input_index][input_channel_index] = stringToDouble(in_string.substring(6*input_channel_index+1,6*input_channel_index+7));
        }
    }


    public double double_conv_double(double input) {

        if (input >= check) {
            out = (input - check_divide) * Vref / (check - 1) / gain;
        } else {
            out = input / (check - 1) / gain * Vref;
        }
        return out;
    }


    public double stringToDouble(String input)
    {
        return double_conv_double(new BigInteger(input,16).doubleValue());
    }

}

