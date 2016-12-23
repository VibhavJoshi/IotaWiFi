package com.sattva.iotawifi;


import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class csvread {

    String[] nextLine;
    CSVReader reader;
    int count;

    public void csv(int sample_set)
    {
        try {

            reader = new CSVReader(new FileReader("sdcard/Download/inp_28.csv"));

            for(int s_int = 0; s_int < sample_set; s_int ++)
            {
                feed_test_input_array();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    public void feed_test_input_array()
    {
        count = 0;
        try {
            while (((nextLine = reader.readNext()) != null) && count < 15000) {

                // nextLine[] is an array of values from the line

                for (int i = 0; i < 4; i++) {
                    ApplicationUtils.test_input_array[count][i] = Double.parseDouble(nextLine[i]);
                }
                count++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}