package com.sattva.signalproc;


import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImpulseFilter
{
	/**
	 * Input = N x 4 signal
	 */
	static boolean i0Flag=false;
	static boolean i1Flag=false;
	static boolean i2Flag=false;
	static boolean i3Flag=false;
	matrixFunctions Matrix = new matrixFunctions();
	public double[][] Impulse(double[][] input, int Fs)
	{

				final int ndt = input.length;
				final int ns = input[0].length;
				
				/**
				 * 
				 * Set the first ten samples to the median of the following three
				 */
				final int npti = 10;
				final int nptim = 3;
				double[][] new_inp=input;
				
				for(int t=0; t<ns; t++)
				{	
					double[] tempmed=new double[3];	
						
						int count = 0;
						for(int u = npti; u<npti+nptim; u++)
						{
							
							tempmed[count]=input[u][t];
							++count;
						}
				
					double temp = median(tempmed);
					for(int s=0;s<npti;s++){
							new_inp[s][t] = temp;
						}
				}

				final int thE=4;
				final int wm = (int)Math.floor((0.06*Fs));
				final int pvsc = 2;

				double[] Xin = new double[ndt];
				double[][] Impuleremoved = new double[ndt][ns];
				int cols,i;
				
//				System.out.println("  Row 1 >>>> 1:"+Xin[0]);
//				System.out.println("  Row 11 >>>> 1:"+Xin[10]);
//				System.out.println("  Row 21 >>>> 1:"+Xin[20]);
				for(cols=0; cols<ns; cols++)
				{
						for (i = 0; i < ndt; i++)
						{
							Xin[i] = new_inp[i][cols];
						}
//						System.out.println(cols+"  Row 1 >>>> 1:"+Xin[0]);
//						System.out.println(cols+"  Row 11 >>>> 1:"+Xin[10]);
//						System.out.println(cols+"  Row 21 >>>> 1:"+Xin[20]);
						double[] X = impulseElimination (Xin,thE,wm,pvsc);
//						System.out.println(cols+" Impluse elemination Row 1 >>>> 1:"+X[0]);
//						System.out.println(cols+" Impluse elemination Row 11 >>>> 1:"+X[10]);
//						System.out.println(cols+" Impluse elemination Row 21 >>>> 1:"+X[20]);
						for (i = 0; i < ndt; i++){
							Impuleremoved[i][cols] = X[i];
						}
				}	
//				System.out.println("Impulse removed"+Impuleremoved[0][0]+"< 2>"+Impuleremoved[0][1]+"< 3>"+Impuleremoved[0][2]);
//				System.out.println("Impulse removed"+Impuleremoved[10][0]+"< 2>"+Impuleremoved[10][1]+"< 3>"+Impuleremoved[10][2]);
//				System.out.println("Impulse removed"+Impuleremoved[20][0]+"< 2>"+Impuleremoved[20][1]+"< 3>"+Impuleremoved[20][2]);
				return Impuleremoved;		
	}//main ends
	
	public double[][] ImpulseParallel(double[][] input, int Fs)
	{

			
			
				final int ndt = input.length;
					final int ns = input[0].length;
					/**
					 * 
					 * Set the first ten samples to the median of the following three
					 */
					final int npti = 10;
					final int nptim = 3;
					double[][] new_inp=input;
					
					for(int t=0; t<ns; t++)
					{	
						double[] tempmed=new double[3];	
							
							int count = 0;
							for(int u = npti; u<npti+nptim; u++)
							{
								
								tempmed[count]=input[u][t];
								++count;
							}
					
						double temp = median(tempmed);
						for(int s=0;s<npti;s++){
								new_inp[s][t] = temp;
							}
					}
	
					final int thE=4;
					final int wm = (int)Math.floor((0.06*Fs));
					final int pvsc = 2;
	
					final double[][] Impuleremoved = new double[ndt][ns];
					ExecutorService exec = Executors.newFixedThreadPool(ns);
					try
					{
//					System.out.println("Initiating Parallel Processing at : "+(new java.text.SimpleDateFormat("H:mm:ss:SSS")).format(java.util.Calendar.getInstance().getTime()));
					for(int cols=0; cols<ns; cols++)
					{
						final double[][] fA = new_inp;
						final int fcols = cols;
						final double[] Xin = new double[ndt];
//						System.out.println(fcols+" th Impluse Calculation is initiated :"+(new java.text.SimpleDateFormat("H:mm:ss:SSS")).format(java.util.Calendar.getInstance().getTime()));
						
						
						exec.submit(new Runnable()
						{
				            @Override
				            public void run() 
				            {
				               for (int ir = 0; ir < ndt; ir++)
								{
									Xin[ir] = fA[ir][fcols];
								}
								
								double[] X = impulseElimination (Xin,thE,wm,pvsc);
								
								for (int is = 0; is < ndt; is++){
									Impuleremoved[is][fcols] = X[is];
								}
								if(fcols == 0)
									 i0Flag = true;
								 else if(fcols == 1)
									 i1Flag = true;
								 else if(fcols == 2)
									 i2Flag = true;
								 else if(fcols == 3)
									 i3Flag = true;
				            }
						});

					}	
					while(true)
					 {
						 Thread.sleep(10);
						 if(i0Flag && i1Flag && i2Flag && i3Flag)
						 {
							 break;
						 }
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally 
				{
				    exec.shutdown();
				}

				return Impuleremoved;		
	}//main ends
			  
	/**
	 * Impulse Elimantion fucntion

	 */
				private double[] impulseElimination(double[] input, int threshold, int window, int pvsc)
			    {
			        int len = input.length;

			        double[]xc = input;
			        window = (int)(2*Math.floor(window/2)) + 1;

			        /**
			         * xmed-->medianfilter function code is referred to
			         */

			        double xmed[] = medianfilter1MIT(xc,window);



			        double[]xad = new double[len];
			        for(int i =0; i<len; i++)
			        {
			            xad[i] = Math.abs(xc[i]-xmed[i]);
			        }


			        double[] index = Matrix.FindingElementDouble(xad, 0, 2);
			        double[] temp_xad = new double[index.length];

			        for(int r=0;r<index.length;r++)
			        {
			            temp_xad[r]= xad[r];
			        }

			        double xadm= maxsc(temp_xad,pvsc);

			        double element_forfind=threshold*xadm;
			        double[] sub = new double[xad.length];
			        for(int i=0;i<xad.length;i++)
			        {
			            sub[i] = (xad[i]-(element_forfind));
			        }
			        double kv[] = Matrix.FindingElementDouble(sub, 0, 2);



			        if(kv != null)
			        {
			            int i = 0;	//i=1 in matlab

			            while(i<kv.length)	//<= in matlab
			            {
			                double iivx=kv[i];
			                while(i<kv.length-1 && kv[i+1]==(kv[i])+1){
			                    i=i+1;
			                }
			                double ifvx = kv[i];
			                double xck = (xc[(int)Math.max(iivx-1,1)]+xc[(int) (Math.min(ifvx+1,len))])/2;

			                for(int ind=(int)iivx ; ind<= (int)ifvx; ind++)
			                {
			                    xc[ind-1] = xck;
			                }
			                i=i+1;

			            }//outer while loop


			        }//if(k!=null) loop


			        int h=9;
			        h=h+3;

			        return xc;

			    }

				/**
				 * THis is matlab function converted
				 */
			    private double[] medianfilter1MIT(double[] input, int win)
			    {

			        int iter = 1;

			        int len = input.length;
			        int m2 = (int)Math.floor(win/2);
			        int min = m2;
			        min = Math.min(min,len);

			        double[] xi = new double[min];

			        for (int i = 0; i < min; i++){
			            xi[i] = input[i];
			        }

			        double xi_med = median(xi);
			        double[] xf = new double[min];

			        for (int i = 0; i<min; i++){
			            xf[i] = input[len-(min - i)];
			        }

			        double xf_med = median(xf);
			        double[] inp_ext = new double[len + 2*min];

			        for (int i = 0; i<min; i++){
			            inp_ext[i] = xi_med;
			            inp_ext[len+min+i] = xf_med;
			        }

			        for (int i =0; i<len; i++){
			            inp_ext[min+i] = input[i];
			        }


			        // median1D function starts here

			        int len1 = inp_ext.length;
			        int window;
			        if (win%2 == 0){ // if even
			            window = win/2;
			        }else{
			            window = (win-1)/2;
			        }

			        double inp_ext1[] = new double[2*window+len1];
			        for (int i = 0; i<len1; i++){
			            inp_ext1[i+window] = inp_ext[i];
			        }

			        double output[] = new double[len1];
			        double[] med_ext = new double[win];
			        for (int i = 0; i<len1; i=i+iter){
			            int count = 0;
			            for (int k = i; k<i+win; k++){
			                //ind[count] = k;
			                med_ext[count] = inp_ext1[k];
			                count = count + 1;
			            }

			            output[i] = median(med_ext);

			            //z = z + 1;
			        }
			        double[] med = new double[len];
			        for (int i =0; i<len; i++){
			            med[i] = output[i+min];
			        }

			        return med;

			    } //end function
			    
			    /**
			     * Find median
			     */
				private static double median(double[] list) 
				{
					// TODO Auto-generated method stub
					Arrays.sort(list);
					final int len = list.length;
					double median =0;
					if (len %2 == 1){
						median = list[len/2];
					}
					else {
						median = (list[len/2] + list[len/2 -1])/2;
					}
					return median;
					
				}
				
				private static double maxsc(double[]v,int prec)
				
				{
					Arrays.sort(v);
					final int fi=v.length-(int)Math.floor(v.length*prec/100);
					return v[fi-1];
				}
}