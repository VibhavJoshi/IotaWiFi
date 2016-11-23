package com.sattva.signalproc;
import java.util.Arrays;

public class mQRSDetection
{
	public int[] mQRS(double[][] input, int Fs)
	{
		
		int length = input.length;
		
		double[] Channel1 = new double[length];
		double[] Channel2 = new double[length];
		double[] Channel3 = new double[length];
		double[] Channel4 = new double[length];
		
		for (int i =0; i<length; i++)
		{
			Channel1[i] = input[i][0];
			Channel2[i] = input[i][1];
			Channel3[i] = input[i][2];
			Channel4[i] = input[i][3];
		}
		
		int QRS1[] = MaternalQRS(Channel1, Fs);
		int QRS2[] = MaternalQRS(Channel2, Fs);
		int QRS3[] = MaternalQRS(Channel3, Fs);
		int QRS4[] = MaternalQRS(Channel4, Fs);
		
		qrsmSelection  qrsM = new qrsmSelection();
		int[] QRSM = qrsM.qrs(QRS1, QRS2, QRS3, QRS4);
		
		
		
		return QRSM;
	}

	private int[] MaternalQRS(double[] channel, int fs) 
	{
		int length = channel.length;
		double Derivative[] = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		// differentiate and square
		Convolution(channel, Derivative);
		
		/**
		 * FIltering 0.8- 3Hz
		 */

		double bH[] = {0.9975, -0.9975};
		double aH[] = {1, -0.9950};
		double zH = -0.9975;
		FilterLoHi(channel, aH, bH, zH);

		double bL[] = {0.0093, 0.0093};
		double aL[] = {1, -0.9813};
		double zL = 0.9854;
		FilterLoHi(channel, aL, bL, zL);
		// No integrator is present -- removed that part of code
		
		/**
		 * Find the 90% and 10% value to find the threshold
		 */
		double temp[] = new double[length];

		for (int i = 0; i<length; i++)
			{
				temp[i] = channel[i];
			}
		Arrays.sort(temp);
		
		int MaxLoc = (int)Math.ceil(length*0.9);
		int MinLoc = (int)Math.ceil(length*0.1);

		double MaxVal = temp[MaxLoc];
		double MinVal = temp[MinLoc];

		double Threshold = (MaxVal - MinVal)/10;

		for (int i = 0; i<length; i++)
		{
			if (channel[i] < Threshold)
			{
				channel[i] = 0;
			}
		}
		/**
		 * Peak Detection , not sure about return type have to change it
		 * Just return the first column of the Maxtab. No need the magnitudes.
		 */
		int maxtab[] = PeakDetection(channel, Threshold);
		
		
		
		
		return maxtab;
	}

	private void FilterLoHi(double[] channel, double[] a, double[] b, double z) 
	{
		
		int length = channel.length;
		int nfact = 3;
		int Len_ext = 2*nfact + length;
		double Mirror[] = new double[Len_ext];

		MirrorInput(channel, Mirror, nfact);
		Filter2(Mirror, b, a, Mirror[0]*z, Len_ext);
		Reverse(Mirror, Len_ext);
		Filter2(Mirror, b, a, Mirror[0]*z, Len_ext);
		Reverse(Mirror, Len_ext);

		for (int i = 0 ; i<length; i++)
		{
			channel[i] = Mirror[nfact+i];
		}
		
	}
	private void Filter2(double[] Inp, double[] b, double[] a, double z, int len) 
	{
		
		double tempI, tempF;
		tempI = Inp[0];

		Inp[0] = b[0]*Inp[0] + z;

		for (int i = 1; i< len; i++)
		{
			tempF = Inp[i];
			Inp[i] = b[0]*Inp[i] + b[1]*tempI - a[1]*Inp[i-1];
			tempI  = tempF;
		}
	}
	private void MirrorInput(double[] Inp, double[] Inp_ext, int nfact) 
	{

    	int length = Inp.length;
    	int nIt = length + 2*(nfact);
    	int nIf = length + (nfact-1);
    	int nShift = 2*length + nfact - 2;
    	for (int i = 0; i< nIt; i++)
    	{
    		if (i < nfact)
    		{
    			Inp_ext[i] = 2*Inp[0] - Inp[nfact-i];
    		}
    		else if (i > nIf)
    		{
    			Inp_ext[i] =2*Inp[length - 1] - Inp[nShift - i];
    		}
    		else
    		{
    			Inp_ext[i] = Inp[i - nfact];
    		}
    	}

		
	}
    private void Reverse(double[] Inp, int len) 
    {
		
    	double temp = 0;
    	for (int i = 0; i<len/2; i++)
    	{
    		temp = Inp[i];
    		Inp[i] = Inp[len - i - 1];
    		Inp[len- i - 1] = temp;
    	}
		
	}
	/**
	 * Peak detection for array
	 */
	public int[] PeakDetection(double[]inp, double delta)
	{
		double min = 100000 , max = -100000;
		double minpos = 0; 
		double maxpos = 0;
		double lookformax=1;
		double thisVar=0;
		int count1 = 0;int count2 = 0;
		double[][] maxtab = new double[inp.length][2];
		double[][] mintab = new double[inp.length][2];
		
		for(int y=0;y<inp.length;y++)
			
		{
			thisVar=inp[y];
			
			//check max and min are greater and lesser to x[y][0] respectively
			if(thisVar>max)
			{
				max = thisVar;	
				maxpos = y;
			}
			if(thisVar<min)
			{
				min = thisVar;
				minpos = y;
			}
			
			if (lookformax==1)
			{
				if(thisVar < (max-delta))
				{
					maxtab[count1][0] = maxpos;			//first col has positions
					maxtab[count1][1] = max;	//second col has values
					
					count1=count1+1;						//next row		 
					min=thisVar;	minpos=y;
					lookformax=0;
				
				}
			}
			else if (lookformax ==0)
			{
				if(thisVar > (min+delta))
				{
					mintab[count2][0] = minpos;
					mintab[count2][1] = min;
					
					count2=count2+1;
					max=thisVar;	maxpos=y;
					lookformax=1;
					
				}
			}
		}
		int count = 0;
		if (maxtab[0][0] >= 0 && maxtab[1][0] > 0)
		{
			count = count + 1;
		}
		for (int i = 1; i< maxtab.length; i++)
		{
			if (maxtab[i][0] > 0)
			{
				count = count+1;
			}
			else
			{
				break;
			}
		}
		int[] maxtab_fin = new int[count];
		for (int i = 0; i<count; i++)
		{		
				maxtab_fin[i] = (int)(Math.floor(maxtab[i][0])); // in case , we get decimal.	
		}
		return maxtab_fin;
	}
	/**
	 * Finds derivative and then squares the output
	 */
	private void Convolution(double[] a, double[] b) 
	{
		// TODO Auto-generated method stub
		int m = a.length;
		int n = b.length;
		int k = m+n-1;
		double ext[] = new double[k];
		for (int i = 0; i<k; i++)
		{
			if (i >= n/2 && i<n/2+m)
				ext[i] = a[i-n/2];
			else
				ext[i] = 0;
		}

		double sum;
		for (int i = 0; i<m; i++)
		{
			sum = 0;
			for (int j = 0; j<n; j++)
			{
				sum = sum + b[n-1 - j]* ext[j+i]/64;
			}
			a[i] = sum*sum;
		}

		
	}
}