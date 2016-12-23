package com.sattva.signalproc;

import android.util.Log;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

//package hb;
public class qrsFSelectionQueue
{		
	int temp1 = 0;
	int temp2 = 0;
	int temp3 = 0;
	double diff =0;
	double RRdiff0, RRdiff1, RRdiff2;
	public int[] qrs(int[] qrs1, int[] qrs2, int[] qrs3, int[] qrs4, int[] qrsM)
	{
		/**
		 * Channel selection part
		 */
		
		int len1 = qrs1.length;
		int len2 = qrs2.length;
		int len3 = qrs3.length;
		int len4 = qrs4.length;
		
		double ind1=0;
		double ind2=0;
		double ind3=0;
		double ind4=0;
		// to get the start index in each channel
		int strtInd1 = -1;
		int strtInd2 = -1;
		int strtInd3 = -1;
		int strtInd4 = -1;
		if (len1 > 3)
		{
			int l = len1-3;
			double var1[] = new double[l];
			double t1,t2,t3, mean;
			for (int i = 0; i<l; i++)
			{
				t1 = qrs1[i+1] - qrs1[i];
				t2 = qrs1[i+2] - qrs1[i+1];
				t3 = qrs1[i+3] - qrs1[i+2];
				
				mean = (t1+t2+t3)/3;
				
				var1[i] = Math.sqrt( ((t1-mean)*(t1-mean) + (t2-mean)*(t2-mean) + (t3-mean)*(t3-mean)) /3  );
				if (var1[i] < 50)
				{
					ind1 = ind1+1;
					if (strtInd1 == -1)
					{
						strtInd1 = i; 
					}
				}
			}
			ind1 = ind1/l;
		}
		
		if (len2 > 3)
		{
			int l = len2-3;
			double var2[] = new double[l];
			double t1,t2,t3, mean;
			for (int i = 0; i<l; i++)
			{
				t1 = qrs2[i+1] - qrs2[i];
				t2 = qrs2[i+2] - qrs2[i+1];
				t3 = qrs2[i+3] - qrs2[i+2];
				
				mean = (t1+t2+t3)/3;
				
				var2[i] = Math.sqrt( ((t1-mean)*(t1-mean) + (t2-mean)*(t2-mean) + (t3-mean)*(t3-mean)) /3  );
				if (var2[i] < 50)
				{
					ind2 = ind2+1;
					if (strtInd2 == -1)
					{
						strtInd2 = i; 
					}
				}
			}
			ind2 = ind2/l;
		}
		
		if (len3 > 3)
		{
			int l = len3-3;
			double var3[] = new double[l];
			double t1,t2,t3, mean;
			for (int i = 0; i<l; i++)
			{
				t1 = qrs3[i+1] - qrs3[i];
				t2 = qrs3[i+2] - qrs3[i+1];
				t3 = qrs3[i+3] - qrs3[i+2];
				
				mean = (t1+t2+t3)/3;
				
				var3[i] = Math.sqrt( ((t1-mean)*(t1-mean) + (t2-mean)*(t2-mean) + (t3-mean)*(t3-mean)) /3  );
				if (var3[i] < 50)
				{
					ind3 = ind3+1;
					if (strtInd3 == -1)
					{
						strtInd3 = i; 
					}
				}
			}
			ind3 = ind3/l;
		}
		
		if (len4 > 3)
		{
			int l = len4-3;
			double var4[] = new double[l];
			double t1,t2,t3, mean;
			for (int i = 0; i<l; i++)
			{
				t1 = qrs4[i+1] - qrs4[i];
				t2 = qrs4[i+2] - qrs4[i+1];
				t3 = qrs4[i+3] - qrs4[i+2];
				
				mean = (t1+t2+t3)/3;
				
				var4[i] = Math.sqrt( ((t1-mean)*(t1-mean) + (t2-mean)*(t2-mean) + (t3-mean)*(t3-mean)) /3  );
				if (var4[i] < 50)
				{
					ind4 = ind4+1;
					if (strtInd4 == -1)
					{
						strtInd4 = i; 
					}
				}
			}
			ind4 = ind4/l;
		}
		// FInd the maximum value of 'ind'
		// Have to add mean RR value also to this computation to get better estimate of 'ch'
		double ind = ind1;
		int len = len1;
		int ch = 1;
		
		if (ind2 >ind)
		{
			ind = ind2;
			ch = 2;
			len = len2;
			
		}
		else if (ind3>ind)
		{
			ind = ind3;
			ch = 3;
			len = len3;
		}
		else if (ind4>ind)
		{
			ind = ind4;
			ch = 4;
			len = len4;
		}
		/**
		 * Get the start Index and qrs values to find the final QRS.
		 */
		int[] QRS = new int[len];
		int StartIndex = -1;
		if (ch ==1)
		{
			StartIndex = strtInd1;

			for (int i = 0; i<len; i++)
			{
				QRS[i] = qrs1[i];
			}
		}
		else if (ch == 2)
		{
			StartIndex = strtInd2;
			for (int i = 0; i<len; i++)
			{
				QRS[i] = qrs2[i];
			}
		}
		else if (ch == 3)
		{
			StartIndex = strtInd3;
			for (int i = 0; i<len; i++)
			{
				QRS[i] = qrs3[i];
			}
		}
		else if (ch == 4)
		{
			StartIndex = strtInd4;
			for (int i = 0; i<len; i++)
			{
				QRS[i] = qrs4[i];
			}
		}
		
		Log.e("qrsFSelectionQueu", "StartIndex = " + StartIndex + ", ch = " + ch);
		/**
		 * Final Selection of QRS
		 */
		
		
		int dec = 0;
		int iter=0; 
		int inc = 0;;
		int lengthQRS = QRS.length;
		
		LinkedList<Integer> QRSFinal = new LinkedList<Integer>();
		Queue<Integer> miss = new LinkedList<Integer>();
		Deque<Integer> miss1 = new LinkedList<Integer>();
		
		
		int length_final = 0;
		int start = 0;
		
		int i = 0;
		int count = 0;
		int count0 = 0;
		
		int missFlag = 0;
		int countMiss = 0;
		int findFlag = 0;
		if (StartIndex >-1)
		{
			i = StartIndex;
			QRSFinal.addLast(QRS[i]);
			QRSFinal.addLast(QRS[i+1]);
			
			i = i+1;
			count = 2;
			
			while( i<lengthQRS-2)
			{
				count0 = count;
				if (missFlag == 0)
				{
					if (count < 9)
					{
						RRdiff0 = 0;
						for (int j = 0; j< count-1; j++)
						{
							RRdiff0 = RRdiff0 + (QRS[j+1] - QRS[j]);
						}
						RRdiff0 = RRdiff0/(count-1);
					}
					else
					{
						RRdiff0 = 0;
						for (int j = count-9; j<count-1; j++)
						{
							RRdiff0 = RRdiff0 + (QRS[j+1] - QRS[j]);
						}
						RRdiff0 = RRdiff0/8;
						
					}
				}
				else 
				{
					RRdiff0 = 0;
					for (int j=1; j<=countMiss; j++)
					{
						RRdiff0 = RRdiff0 + (QRS[count-j] - QRS[count-j-1]);
					}
					RRdiff0 = RRdiff0/countMiss;

				}
				
				RRdiff1 = QRS[i+1] - QRSFinal.getLast();
				RRdiff2 = QRS[i+2] - QRSFinal.getLast();
				
				
				if ( (RRdiff1 > 300) || (RRdiff2 >300))
				{
					if ( (RRdiff1 > RRdiff0*0.9) && (RRdiff1 < 1.1*RRdiff0))
					{
						QRSFinal.addLast(QRS[i+1]);
						i = i+1;
						count = count+1;
					}
					else if( (RRdiff2 > RRdiff0*0.9) && (RRdiff2 < 1.1*RRdiff0))
					{
						QRSFinal.addLast(QRS[i+2]);
						i = i+2;
						count =count+1;
					}
					else
					{
						findFlag = 0;
						inc = 0;
						while (findFlag == 0)
						{
							if ( (QRS[i+2+inc] - QRS[i+1+inc]) > RRdiff0*0.9 && (QRS[i+2+inc] - QRS[i+1+inc]) < RRdiff0*1.1)
							{
								QRSFinal.addLast(QRS[i+1+inc]);
								QRSFinal.addLast(QRS[i+2+inc]);
								i = i+2+inc;
								count = count+2;
								findFlag = 1;
							}
							else
							{
//								findFlag = 0;
								inc = inc+1;
								if ((i+inc+2) == lengthQRS-1)
								{
									i = i+2+inc;
									findFlag = 1;
								}
							}
						}
					}

				}
				else
				{
					i = i+1;
				}
				
				if (count0 < count)
				{
					if (getDiff(QRSFinal) >1.66*RRdiff0)
					{
						miss.add(count-2);
						missFlag = 1;
						countMiss = 1;
					}
					else
					{
						if (missFlag == 1)
						{
							countMiss = countMiss + 1;
							if (countMiss == 10)
							{
								missFlag = 0;
							}
							
						}
					}
				}
				
				
				
			}
			
		}
		int z= 0;
	
		// Add the last 1-2 peaks
		System.out.println("Forward tracking is over");
		if ( (i+1) == lengthQRS)
		{
			iter = 1;
		}
		else if ( (i+2) == lengthQRS)
		{
			iter = 2;
		}
		
		for (int it = 1; it<iter; it++)
		{
			count0 = count;
			if (missFlag == 0)
			{
				if (count < 9)
				{
					RRdiff0 = 0;
					for (int j = 0; j< count-1; j++)
					{
						RRdiff0 = RRdiff0 + (QRS[j+1] - QRS[j]);
					}
					RRdiff0 = RRdiff0/(count-1);
				}
				else
				{
					RRdiff0 = 0;
					for (int j = count-9; j<count-1; j++)
					{
						RRdiff0 = RRdiff0 + (QRS[j+1] - QRS[j]);
					}
					RRdiff0 = RRdiff0/8;
					
				}
			}
			else 
			{
				RRdiff0 = 0;
				for (int j=1; j<=countMiss; j++)
				{
					RRdiff0 = RRdiff0 + (QRS[count-j] - QRS[count-j-1]);
				}
				RRdiff0 = RRdiff0/countMiss;

			}
			
			if ( (QRS[i+it] - QRSFinal.getLast()) > RRdiff0*0.9 && (QRS[i+it] - QRSFinal.getLast()) <1.1*RRdiff0 )
			{
				QRSFinal.addLast(QRS[i+it]);
				count = count +1;
			}
			
			if (count0 < count)
			{
				if (getDiff(QRSFinal) >1.66*RRdiff0)
				{
					miss.add(count-2);
					missFlag = 1;
					countMiss = 1;
				}
				else
				{
					if (missFlag == 1)
					{
						countMiss = countMiss + 1;
						if (countMiss == 10)
						{
							missFlag = 0;
						}
						
					}
				}
			}
			
		}
		System.out.println("Adding last 1-2 peaks is over");
		/**
		 * Add missed peaks.
		 */
		int lenMiss = miss.size();
		int indMiss = -1;
		int factor = 0;
		int overlapFlag = 0;
		int qrsInter ;
		int elementadded = 0;
		if (lenMiss >0)
		{
			indMiss = miss.remove() + elementadded;
			int flag = 0;
			for (i = 0; i<lenMiss; i++)
			{
				flag = 0;
				factor = Math.round((QRSFinal.get(indMiss) - QRSFinal.get(indMiss-1)) / (QRSFinal.get(indMiss-1) - QRSFinal.get(indMiss-2)) );
				overlapFlag = 0;
				if (factor == 2)
				{
					qrsInter = FindOverlapMqrsLoc(qrsM,QRSFinal.get(indMiss), QRSFinal.get(indMiss+1));
					if (qrsInter >0)
					{
						if (indMiss<9)
						{
							RRdiff0 = 0;
							for (int j = 0; j<indMiss; j++)
							{
								RRdiff0 = RRdiff0 + (QRSFinal.get(j+1) - QRSFinal.get(j));
							}
							RRdiff0 = RRdiff0/indMiss;
						}
						else
						{
							RRdiff0 = 0;
							for (int j =1; j<9; j++)
							{
								RRdiff0 = RRdiff0 + (QRSFinal.get(indMiss-j) -QRSFinal.get(indMiss-j-1));
							}
							RRdiff0 = RRdiff0/8;
						}
					}
					RRdiff1 = qrsInter - QRSFinal.get(indMiss-1);
					if ( (RRdiff1 >0.9*RRdiff0 ) && (RRdiff1 < 1.1*RRdiff0))
					{
						QRSFinal.add(indMiss, qrsInter);
						overlapFlag = 1;
						flag = 1;
					}
				}
				
				if (overlapFlag == 0)
				{
					for (int f= factor-1; f>1; f--)
					{
						qrsInter = QRSFinal.get(indMiss-1) + ((QRSFinal.get(indMiss-1) - QRSFinal.get(indMiss-2)))*f;
						QRSFinal.add(indMiss, qrsInter);
						flag = 1;
					}
				}
			
			
				if (flag == 1)
				{
					elementadded = elementadded + (factor-1);
				}
			
			
			}
		}

		
		
		/**
		 *  Backtrack initial peaks
		 */
		
		int count1 = 0;
		missFlag = 0;
		int it;
		countMiss = 0;
		if (StartIndex >0)
		{
			it = StartIndex;
			while (it >1)
			{
				if (missFlag == 0)
				{
					if (count >8)
					{
						RRdiff0 = 0;
						for (int j = 1; j<9; j++)
						{
							RRdiff0 = RRdiff0 + (QRSFinal.get(j)- QRSFinal.get(j-1));
						}
						RRdiff0 = RRdiff0/8;
						
					}
					else
					{
						RRdiff0 = 0;
						for (int j = 1; j<= count; j++)
						{

							Log.e("qrsFSelectionQueu", "j = " + j + ", " + "QRSFlength = " + QRSFinal.size());
							RRdiff0 = RRdiff0 + (QRSFinal.get(j)- QRSFinal.get(j-1));
						}
						RRdiff0 = RRdiff0/count;
					}
				}
				else
				{
					if (countMiss >0)
					{
						RRdiff0 = 0;
						for (int j = 1 ; j<countMiss; j++)
						{
							RRdiff0 = RRdiff0 + (QRSFinal.get(j) - QRSFinal.get(j-1));
						}
						RRdiff0 = RRdiff0/countMiss;
					}
				}
				
				RRdiff1 = QRSFinal.getFirst() - QRS[it-1];
				RRdiff2 = QRSFinal.getFirst() - QRS[it-2];
				
				
				if ( (RRdiff1 > 300) || (RRdiff2 >300))
				{
					if ( (RRdiff1 > RRdiff0*0.9) && (RRdiff1 < 1.1*RRdiff0))
					{
						QRSFinal.addFirst(QRS[it-1]);
						it = it-1;
						count1 = count1+1;
					}
					else if( (RRdiff2 > RRdiff0*0.9) && (RRdiff2 < 1.1*RRdiff0))
					{
						QRSFinal.addFirst(QRS[it-2]);
						it = it-2;
						count1 =count1+1;
					}
					else
					{
						findFlag = 0;
						dec = 0;
						while (findFlag == 0)
						{
							if ( (QRS[it-1-dec] - QRS[it-2-dec]) > RRdiff0*0.9 && (QRS[it-1-dec] - QRS[it-2-dec]) < RRdiff0*1.1)
							{
								QRSFinal.addFirst(QRS[it-1-dec]);
								QRSFinal.addFirst(QRS[it-2-dec]);
								it = it-2-dec;
								count1 = count1+2;
								findFlag = 1;
							}
							else
							{
//								findFlag = 0;
								dec = dec+1;
								if ((it-dec-2) <0)
								{
									it = it-2-dec;
									findFlag = 1;
								}
							}
						}
					}

				}
				else
				{
					it = it-1;
				}

				// Find loc of missed peaks
				
				if ( (QRSFinal.get(2) - QRSFinal.get(1)) >1.66*RRdiff0)
				{
					miss1.addFirst(count1);
					missFlag = 1;
					countMiss = 1;
				}
				else
				{
					if (missFlag == 1)
					{
						countMiss = countMiss + 1;
						if (countMiss == 10)
						{
							missFlag = 0;
						}
						
					}
				}
			}
			
			for (int it1 = it; it1>1; it1--)
			{
				if ( (QRSFinal.getFirst() - QRS[it1-1]) >300)
				{
					QRSFinal.addFirst(QRS[it1-1]);
					count1 = count1 +1;
				}
			}
			
		}
		
		/** 
		 * Add missed peaks
		 */
		int lenMiss1 = miss1.size();
		int indMiss1 = -1;
		factor = 0;
		overlapFlag = 0;
		elementadded = 0;
		if (lenMiss1 >0)
		{
			indMiss1 = (count1 - miss1.removeFirst()+3) + elementadded;
			int flag = 0;
			for (i = 0; i<lenMiss1; i++)
			{
				flag = 0;
				factor = Math.round((QRSFinal.get(indMiss1) - QRSFinal.get(indMiss1-1)) / (QRSFinal.get(indMiss1-1) - QRSFinal.get(indMiss1-2)) );
				overlapFlag = 0;
				if (factor == 2)
				{
					qrsInter = FindOverlapMqrsLoc(qrsM,QRSFinal.get(indMiss1), QRSFinal.get(indMiss1+1));
					if (qrsInter >0)
					{
						if (indMiss1<9)
						{
							RRdiff0 = 0;
							for (int j = 0; j<indMiss1; j++)
							{
								RRdiff0 = RRdiff0 + (QRSFinal.get(j+1) - QRSFinal.get(j));
							}
							RRdiff0 = RRdiff0/indMiss1;
						}
						else
						{
							RRdiff0 = 0;
							for (int j =1; j<9; j++)
							{
								RRdiff0 = RRdiff0 + (QRSFinal.get(indMiss1-j) -QRSFinal.get(indMiss1-j-1));
							}
							RRdiff0 = RRdiff0/8;
						}
					}
					RRdiff1 = qrsInter - QRSFinal.get(indMiss1-1);
					if ( (RRdiff1 >0.9*RRdiff0 ) && (RRdiff1 < 1.1*RRdiff0))
					{
						QRSFinal.add(indMiss1, qrsInter);
						overlapFlag = 1;
						flag = 1;
					}
				}
				
				if (overlapFlag == 0)
				{
					for (int f= factor-1; f>1; f--)
					{
						qrsInter = QRSFinal.get(indMiss1-1) + ((QRSFinal.get(indMiss1-1) - QRSFinal.get(indMiss1-2)))*f;
						QRSFinal.add(indMiss1, qrsInter);
						flag = 1;
					}
				}
			
			
				if (flag == 1)
				{
					elementadded = elementadded + (factor-1);
				}
			
			
			}
		}

		
		int lengthQRSF = QRSFinal.size();
		int[] qrsF = new int[lengthQRSF];
		for (i = 0; i<lengthQRSF; i++)
		{
			qrsF[i] = QRSFinal.removeFirst();
		}
		return qrsF;
	}

	private int FindOverlapMqrsLoc(int[] qrsM, int a, int b) 
	{
		// TODO Auto-generated method stub
		int lenM = qrsM.length;
		for (int k = 0 ; k<lenM; k++)
		{
			if (qrsM[k] >a)
			{
				if (qrsM[k] < b && qrsM[k+1]<b)
				{
					return -1;
				}
				else
				{
					return qrsM[k];
				
				}
				
			}
			
			
		}
		
		return -1;	
	}

	private double getDiff(Deque<Integer> QRSFinal) {
		// TODO Auto-generated method stub

		temp3  = QRSFinal.removeLast();
		temp2 = QRSFinal.removeLast();
		temp1 = QRSFinal.removeLast();
		diff = temp2 - temp1;
		QRSFinal.addLast(temp1);
		QRSFinal.addLast(temp2);
		QRSFinal.addLast(temp3);
		
		return diff;
	}
}