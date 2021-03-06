package com.sattva.signalproc;

import java.util.Arrays;

public class MQRScancelI 
{
	public double[][] cancel(double[][] inp, int[] qrsM)
	{
		matrixFunctions Matrixoperations=new matrixFunctions();
		// input = Nx4
		int fs = 1000;



		
		// all above are inputs to function
		int ndt = inp.length;
		int ns = inp[0].length;
		
		int nqrs = qrsM.length;
		
		double[] RRc = new double[nqrs-1];
		double[] RRs = new double[nqrs-1];
		
		for (int i = 0; i< RRc.length ; i++)
		{
			RRc[i] = qrsM[i+1] - qrsM[i];
			RRs[i] = RRc[i]/fs;
		}
		// error coming due to sorting inside the function
		double RR1s[] = new double[nqrs-1];
		for (int i =0; i < nqrs-1; i++)
		{
			RR1s[i] = RRs[i];
		}
		double RRmean = meansc(RR1s , 4, 4);
		//double RRstd = Stdevation(RRs);
		/**
		 * Initialize the no of points before and after QRS
		 */
		int npp = 0;
		int npd = 0;
		if (Math.ceil(0.2*fs) == 0.2*fs)
		{
			npp = (int)Math.ceil(0.2*fs);
		}
		else{
			npp = (int)Math.ceil(0.2*fs)-1;
		}
		double npd1 = 0.8*(RRmean - 0.1);
		
		if (npd1 > 0.5)
		{
			npd1 = 0.5;
		}
		if (Math.ceil(npd1*fs) == npd1*fs)
		{
			npd = (int)Math.ceil(npd1*fs);
		}else {
			npd = (int)Math.ceil(npd1*fs) - 1;
		}
		int  npt = 1 + npp + npd;
		System.out.println(" ");
		System.out.println("The value of npt >>>>  "+npt);
		/**
		 * Extend signals to manage first QRS
		 */
		int qi = 1;
			
		// Always the first qrsM > 120, so take only else condition
				int npxp = 0;
		if (npp + 1 - qrsM[0] > 0)
		{
			npxp = npp + 1 - qrsM[0];
		}
		//npxp = 3;
		double[][] rep_inp = Matrixoperations.submatrix(inp, 0, 0, 0, ns-1);

		double[][] rep_ext = repmat(rep_inp,npxp,1);
		double[][] ext_init = Matrixoperations.verticalConcat(rep_ext, inp);

		/**
		 *  Extend signals to manage last QRS
		 */
		// Always the last qrsM < len - 140, so take only else condition
		
		int qf = nqrs;
		double[] RRmed1 =new double[5];
		double RRmeansum = 0;
		for (int i = 0; i<5; i++)
		{
			//int z = RRs.length-1;
			RRmed1[i] = RRs[RRs.length-1-i];
			RRmeansum = RRmed1[i] + RRmeansum;
		}
		double RRsmean = RRmeansum/5;
		double RRsmed = median(RRmed1);
		
		double tempd = 0.85*fs*RRsmed;
		int tempi =0;
		if (Math.ceil(tempd) == tempd)
		{
			tempi = (int)Math.ceil(tempd);
		}else
		{
			tempi = (int)Math.ceil(tempd) - 1;
		}
		double npepd =0;
		int npep =0;
		
		if (qrsM[qf-1] + tempi < ndt)
		{
			// find max
			if (0.1*fs > 0.15*fs*RRsmean)
			{
				npepd = 0.1*fs;
			}else{
				npepd = 0.15*fs*RRsmean;
			}
			
			if (Math.ceil(npepd) == npepd)
			{
				npep = (int)Math.ceil(npepd);
			}else{
				npep = (int)Math.ceil(npepd) - 1;
			}

			int row_ext = ext_init.length -1 - npep -1;
			int c2 = ext_init[0].length-1;

			double[][] rep_inp1 = Matrixoperations.submatrix(ext_init, row_ext,row_ext , 0, c2); 	

			
			//npep = (int)Math.floor(npepd);
			double[][] rep_fin = repmat(rep_inp1, npep, 1);
			for (int i = 0; i<npep; i++)
			{
				for (int j=0; j<c2+1; j++)
				{
					ext_init[i+row_ext+2][j] = rep_fin[i][j];
				}
			}
			
		} // end if qrsm[qf]
		/**
		 * no of samples to add to right of the signal
		 */
		int npxd = 0;
		if (qrsM[qf-1] + npd-ndt >0)
		{
			npxd = qrsM[qf-1] + npd - ndt;
		}
		double[][] ext_fin = new double[ext_init.length+npxd][ext_init[0].length];
		for (int i =0; i<ext_init.length; i++)
		{
			for (int j =0; j<ext_init[0].length; j++)
			{
				ext_fin[i][j] = ext_init[i][j];
			}
		}
		if (npxd >0)
		{
			double[][] rep_inp2 = Matrixoperations.submatrix(inp, ndt-1, ndt-1, 0, ns-1);
			double[][] rep_out = repmat(rep_inp2, npxd, 1);
			for (int i =0; i<npxd; i++)
			{
				for (int j=0; j<ns; j++)
				{
					ext_fin[i+ext_init.length][j] = rep_out[i][j];
				}
			}
		}
		
		int ndtx = ext_fin.length;
		int nqe = qf - qi +1;
		System.out.println(" ");
		System.out.println("nqe >>>> "+nqe);
		int[] iqw = new int[nqe];
		for (int i=qi ; i<=qf; i++)
		{
			iqw[i-qi] = qrsM[i-1];
		}
		
		/** 
		 * Start and end of QRS window
		 */
		int[] iw = new int[nqe];
		int[] fw = new int[nqe];
		
		for (int i = 0; i<nqe; i++)
		{
			iw[i] = iqw[i] + npxp - npp;
			fw[i] = iqw[i] + npxp + npd;
		}

		
		double A[][] = new double[npt][nqe];
		
		// add weight function
		double[][] wwgT = WeightFunction(npp, npd, fs);
		double[][] wwg = Matrixoperations.transpose(wwgT);
		double[][] Xc = new double[ndtx][ns];

		
		/**
		 * Start loop for doing SVD and substraction
		 */
		double[][] rowextract = new double[1][ns];
		double[][] row_weighted = new double[1][ns];
		for (int is = 0; is<ns; is++)
		{

			for ( int iq=0; iq<nqe; iq++)
			{
				int iwq = iw[iq];
				int fwq = fw[iq];

				
				rowextract = Matrixoperations.submatrix(ext_fin,iwq-1 , fwq-1, is, is);
				row_weighted = Matrixoperations.ElementWiseMult(rowextract, wwg);
				for (int j =0; j<npt; j++){
					A[j][iq] = row_weighted[j][0];
				}
				
				
			}// end extracting the matrix A
			// for svd, rows > cols
			//Matrix Am = new Matrix(A);

			ImplicitSVD svd = new ImplicitSVD();
			
			long startTime = System.currentTimeMillis();
			
			Object[] SVD = svd.impcitsvd(A);

	        
			double[][] V = (double[][])SVD[2];
			double[][] U = (double[][])SVD[1];
			double[] s = (double[])SVD[0];
			
			
			//double[] s = new double[B.length];

			//double[][] S = svd.getS().getArray();
			
			/**
			 * We did svd for A^T. 
			 * A = ui * vi^T * sigma. 
			 */
	        
			double[][] V_ext = new double[A[0].length][1]; // once we get real data change to A.length to A[0].length
			double[][] U_ext = new double[A.length][1]; // and vice versa. Also, remove transpose in svd;
			double Ar[][] = new double[A.length][A[0].length];
			double[][] A1 = new double[A.length][A[0].length];
			int nds = 3; //no of singular values
			for (int i = 0 ; i< nds; i++)
			{
				V_ext = Matrixoperations.submatrix(V, 0, A[0].length-1, i, i);
				U_ext = Matrixoperations.submatrix(U, 0, A.length-1, i, i);
				A1 = Matrixoperations.multi(U_ext, Matrixoperations.transpose(V_ext));
				for (int r = 0; r < A.length; r++)
				{
					for (int c =0; c< A[0].length; c++)
					{
						Ar[r][c] = Ar[r][c] + A1[r][c]*s[i];
					}
				}
			} // find approx A
			
			
			//double[][] wwg1 = {{0.2}, {0.2}, {0.2},{1}, {0.2}, {0.2}, {0.2}};
			// putting back the approximation into a single channel
			double[][] Artemp = new double[Ar.length][1];
			double[][] Artemp1 = new double[Ar.length][1];
			for (int iq = 0 ; iq< nqe; iq++)
			{
				int iwq = iw[iq];
				int fwq = fw[iq];
				Artemp = Matrixoperations.submatrix(Ar, 0, Ar.length-1, iq, iq);
				Artemp1 = Matrixoperations.ElementWiseDivide(Artemp, wwg);
				for (int i = iwq-1; i<fwq; i++)
				{
					Xc[i][is] = Artemp1[i-iwq+1][0];
				}	
			} // end approx single channel
			
			// smoothening connectioons btw sucessive channels
			double dv =0;
			double pv =0;
			for (int iq = 0; iq<nqe-1 ; iq++)
			{
				int fwq = fw[iq];
				int iwqs = iw[iq+1];
				if (iwqs > fwq)
				{
					dv = Xc[iwqs][is] - Xc[fwq][is];
					pv = dv/(iwqs - fwq);
					for (int it = fwq+1; it <=iwqs-1; it++)
					{
						Xc[it][is] = Xc[fwq][is] + pv*(it - fwq);
					}
					
				}
			} // end smoothening
		
			int count = 0;
			count = count+1;
			
		} // end SVD substraction loop

		System.out.println(" ");
		double[][] Xout = new double[ndt][ns];
		for (int i = 0; i<ndt; i++)
		{
			for (int j = 0; j<ns; j++)
			{
				Xout[i][j] = ext_fin[i+npxp][j] - Xc[i+npxp][j];
			}
		}

        
		int e = 0;
		e = e + 1;
		
		
		return Xout;
	} // end main
	
	private static double[][] WeightFunction(int npp,int npd,int fs)
	{
		int nppc1 = (int)(Math.floor(0.06*fs));
		int npdc1 = (int)(Math.floor(0.06*fs));
		int nppc2 = (int)(Math.floor(0.08*fs));
		int npdc2 = (int)Math.min((Math.floor(0.2*fs)),(npd-npdc1));
	 
	//System.out.println(nppc1+"\t"+npdc1+"\t"+nppc2+"\t"+npdc2);
	

		int ie1 = npp-nppc1-nppc2;
		int ie2 = ie1+nppc2;
		int ii3 = ie2+1; int ie3 = ie2+nppc1+npdc1+1;
		int ie4 = ie3+npdc2;
		int ii5 = ie4+1; int ie5 = npp+npd+1;
		double wwg[][] = new double[1][ie5];
		int  flag=0;
		for(int i=0; i<ie1;i++)
		{
			wwg[0][i] = 0.2;
			flag = i;
		}
		int k=0;
		while(flag<ie2 && k<=nppc2)
		{
			flag = flag+1;
			k = k+1;
			wwg[0][flag] = 0.2+((0.8*(k))/nppc2);			
		}	
		
		for(int i=ii3-1; i<ie3;i++)
		{
			wwg[0][i]=1;
			flag=i;//12
		}
		int u=1;
		
		while(flag<ie4 && u<=npdc2)
		{
			flag=flag+1;
			wwg[0][flag] = (1-((0.8*(u))/npdc2));
			u = u+1;			
		}
		
		for(int i = ii5-1; i<ie5;i++)
		{
			wwg[0][i] = 0.2;
			flag = i;
		}
	int y=9;
	y=y+3;
	
	return wwg;
	
	}
	private static double median(double[] list) {
		// TODO find median
		Arrays.sort(list);
		int len = list.length;
		double median =0;
		if (len %2 == 1){
			median = list[len/2];
		}
		else {
			median = (list[len/2] + list[len/2 -1])/2;
		}
		return median;
	}


	private static double[][] repmat(double[][] inp, int row, int col) {
		// replicate a row vector to many rows. 
		double[][] ext = new double[row][inp[0].length];
		for (int i = 0 ; i <row; i++){
			for (int j = 0; j < inp[0].length; j++){
				ext[i][j] = inp[0][j];
			}
		}
		
		
		return ext;
	}


	private static double meansc(double[] v1, int perci, int percf) {
		double[] v = v1;
		Arrays.sort(v);
		int ii = 1 + v.length*perci/100;
		int fi = v.length - v.length*percf/100;
		double sum =0;
		for (int i = ii-1 ; i < fi; i++ ){
			sum = sum + v[i];
		}
		double mean = sum/(fi-ii+1);
		return mean;
	}

} // end class