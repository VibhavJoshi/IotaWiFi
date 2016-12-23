package com.sattva.signalproc;

import android.util.Log;

public class AlgorithmMain
{

/**
 * 
 * @param input - N x 4 input.
 * @return 
 */
	public Object[] AlgoStart(double[][] input)
	{

		Log.e("AlgorithmMain", "inside algoStart  " + input[0][0] + ", " + input[14999][3]);
		int Fs = 1000;
		/** 
		 * Impulse filtering
		 */
		long startTime = System.currentTimeMillis();
		ImpulseFilter ImpFilt = new ImpulseFilter();
		double[][] Ecg_imp = ImpFilt.ImpulseParallel(input, Fs);
		long stopTime1 = System.currentTimeMillis();
		

		/**
		 * Filtering : Low, high, notch
		 */
		filterLoHiNotch filt = new filterLoHiNotch();
		double[][] Ecg_filt = filt.filter(Ecg_imp);
		long stopTime2 = System.currentTimeMillis();

		/**
		 * Perform ICA on filtered data
		 */
		
		FastICA fpica = new FastICA();
		double[][] ica1 = fpica.ICA(Ecg_filt);
		long stopTime3 = System.currentTimeMillis();
		/**
		 * Estimate Maternal QRS
		 */

		mQRSDetection mqrsDet = new mQRSDetection();
		int[] qrsM = mqrsDet.mQRS(ica1, Fs);
		System.out.println("Maternal QRS locations >>>>>>  "+qrsM[0]+ " "+qrsM[1]+" "+qrsM[2]);
		
		long stopTime4 = System.currentTimeMillis();
		/**
		 * Maternal QRS cancellation
		 */
		MQRScancelIParallel canc = new MQRScancelIParallel();

		double[][] fetalSig = canc.cancel(Ecg_filt, qrsM);
		long stopTime5 = System.currentTimeMillis();
		/**
		 * Perform ICA on residue
		 */
		
		double[][] ica2 = fpica.ICA(fetalSig);
		long stopTime6 = System.currentTimeMillis();
		/**
		 * Estimate Fetal QRS
		 */
		
		fQRSDetection fqrsDet = new fQRSDetection();
		int[] qrsF = fqrsDet.fQRS(ica2, Fs, qrsM);
		System.out.println("Fetl QRS Locations >>>>>>>>>  "+qrsF[0]+ " "+qrsF[1]+" "+qrsF[2]);
        long stopTime7 = System.currentTimeMillis();



        System.out.println((stopTime1 - startTime )+" milliseconds for Impulse artifact cancellation");
        System.out.println((stopTime2 - stopTime1 )+" milliseconds for Filtering Hi/Lo/No ");
        System.out.println((stopTime3 - stopTime2 )+" milliseconds for Jade1 execution");
        System.out.println((stopTime4 - stopTime3 )+" milliseconds for MQRS execution");
        System.out.println((stopTime5 - stopTime4 )+" milliseconds for MQRS cancellation");
        System.out.println((stopTime6 - stopTime5 )+" milliseconds for Jade2 execution");
        System.out.println((stopTime7 - stopTime6 )+" milliseconds for FQRS execution");
        System.out.println(stopTime7 - startTime+" milliseconds for total execution");


		return new Object[]{qrsM,qrsF};
	}
}