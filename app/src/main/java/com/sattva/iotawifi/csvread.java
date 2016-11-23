package com.sattva.iotawifi;


import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class csvread {
    public void csv() {

        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader("sdcard/Download/inp_28.csv"));
            int count = 0;
            String[] nextLine;
            while (((nextLine = reader.readNext()) != null) && count < 15000) {

                // nextLine[] is an array of values from the line

                for (int i = 0; i < 4; i++) {
                    ApplicationUtils.test_input_array[count][i] = Double.parseDouble(nextLine[i]);
                }
                count = count + 1;

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}