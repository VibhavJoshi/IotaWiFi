package com.sattva.iotawifi;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Vibhav on 16/11/16.
 */
public class ApplicationUtils {

    public static Queue<String> dynamicDataStore = new LinkedList<String>();
    public static int bufferLength = 15000;
    public static int chan_select = 0;
    public static int plot_flag = 1;
    public static int convert_flag = 1;

    public static double[][] input_array = new double[15000][4];

    public static final double[][] test_input_array = new double[15000][4];

    public static long startMS;


    public static Queue<String> overFlow = new LinkedList<>();





}
