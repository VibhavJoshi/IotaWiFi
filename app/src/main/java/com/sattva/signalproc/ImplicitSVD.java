package com.sattva.signalproc;





import java.util.Arrays;

public class ImplicitSVD {
	public Object[] impcitsvd(double[][] A)
	{
//		System.out.println(" A  Row 1:"+A[0][0]+" 2: "+A[0][1]+" 3: "+A[0][2]+" 4: "+A[0][3]);
//		System.out.println(" A  Row 11:"+A[10][0]+" 2: "+A[10][1]+" 3: "+A[10][2]+" 4: "+A[10][3]);
//		System.out.println(" A  Row 51:"+A[50][0]+" 2: "+A[50][1]+" 3: "+A[50][2]+" 4: "+A[50][3]);
		
		int row = A.length;
		int col = A[0].length;
		//convert to float
		float[][] Af = new float[row][col];
		for (int i =0; i<row; i++)
		{
			for (int j=0; j<col; j++)
			{
				Af[i][j] = (float)A[i][j];
			}
		}
		// Initialize sigma, beta, U, y
		double sigma, beta;
		double[] u = new double[row];
		double[] yl = new double[col];
		
		// initialize Q;
		double[][] QL = new double[row][row];
		for (int i = 0; i<row; i++)
		{
			QL[i][i] = 1;
		}
		double[] yql = new double[row];
		
		
		// initialize for right householder
		double[] ur = new double[col];
		double[][] QR = new double[col][col];
		double[] yr = new double[row];
		double[] yqr = new double[col];
		for (int i = 0; i<col; i++)
		{
			QR[i][i] = 1;
		}
		// initialize R
		double[][] R = new double[col][col];
		
		
		
//		long start = System.currentTimeMillis();
		
		/**
		 * 
		 * Aimplicit = QL(1:col,:)'*R*QR
		 * 
		 */
		// Start Bidiagonalization
		for (int k = 0; k<col; k++)
		{
			// start left householder
			int z = 0;
			sigma = 0;
			for (int i = k; i <row; i++)
			{
				sigma = sigma + A[i][k]*A[i][k];
			}
			sigma = Math.sqrt(sigma);
			
			beta = 1/(sigma*(sigma + Math.abs(A[k][k])));
			
			u[k] = (A[k][k]/Math.abs(A[k][k])) * (sigma + Math.abs(A[k][k]));
			
			for (int i = k+1; i<row; i++)
			{
				u[i] = A[i][k];
			}
			
			A = R_QRtransL(beta,u,A,yl,row,col,k);
			

			
			QL = Q_QrtransL(beta,u,QL,yql,row,k);
			
			
			// end left house holger
			z = z+1;
			// Start Right householder
			if (k < col-2)
			{
				sigma = 0;
				for (int i = k+1; i <col; i++)
				{
					sigma = sigma + A[k][i]*A[k][i];
				}
				sigma = Math.sqrt(sigma);
				
				beta = 1/(sigma*(sigma + Math.abs(A[k][k+1])));
				
				ur[k+1] = (A[k][k+1]/Math.abs(A[k][k+1])) * (sigma + Math.abs(A[k][k+1]));
				
				for (int i = k+2; i<col; i++)
				{
					ur[i] = A[k][i];
				}
				
				A = R_QRtransR(beta,ur,A,yr,row,col,k);
				
				
				
				QR = Q_QRtransR(beta,ur,QR,yqr,col,k);
				
				
				z = z+1;
			} // end right householder
			
		} // end Bidiagonalization
	
		
		
		// Extract R
		
		for (int i =0; i<col; i++)
		{
			for (int j = 0; j<col; j++)
			{
				R[j][i] = A[j][i];
			}
		}

		
		
		
		matrixFunctions  Matrix = new matrixFunctions();
		double[][] U = Matrix.submatrix(Matrix.transpose(QL), 0, row-1, 0, col-1);
		QR = Matrix.transpose(QR);
		
		
		
		// DIagonalization of bidiagonal form
				int p = 0;
				//long startTime1 = System.currentTimeMillis();
				int q = 0;
				float eps = -1;
				if (eps < 0){
					eps = 1;
					while (eps + 1 > 1){
						eps = eps* (float)0.5;
					}
					eps = eps *64;
				}
				// Algorithm 1b. Golub-Reinsh SVD step 2;
				while ( q < col-1){
					
					for (int i = 0; i<col-1; i++){
						if (Math.abs(R[i][i+1])<= eps*(Math.abs(R[i][i])+Math.abs(R[i+1][i+1]))){
							R[i][i+1] = 0;
						}
					}
					float D_max = 0;
					for (int i = 0; i<col; i++){
						if (D_max < (float)R[i][i]){
							D_max = (float)R[i][i];
						}
					} // end for
					
					while ((p < col-1) && (Math.abs(R[p][p+1]) <= eps*D_max)){
						p = p+1;
					}
					if (p == col -1){
						 break;
					}
					int n = p+1;
					while (n <col && Math.abs(R[n-1][n]) > eps*D_max){
						n = n+1;
					}
					q = col -n;
					if (q == col-1){
						break;
					}
					
					// Algo 1b - step : d
					
					
					// obtain B22 submatrix
					float[][] C = new float[2][2];
					
					C[0][0] = (float)R[(col-q-1) - 1][(col-q-1)-1];
					C[0][1] = (float)R[(col-q-1)- 1][(col-q-1)];
					C[1][0] = (float)R[(col-q-1)][(col-q-1)-1];
					C[1][1] = (float)R[(col-q-1)][(col-q-1)];
					
					
					C = Matrix.multiF(Matrix.transposeF(C), C);
					// Algo 1c Step 2 ends
					float alpha = 0;
					beta = 0;
					
					 // calculate mu

						float b = -(C[0][0] + C[1][1])/2;
						float c = C[0][0]*C[1][1] - C[0][1]*C[1][0];
						float d = 0;
						
						if (b*b-c >0){
							d = (float)Math.sqrt(b*b-c);
						}else{
							b = (C[0][0] - C[1][1])/2;
							c = -C[0][1]*C[1][0];
							if (b*b-c>0){
								d = (float)Math.sqrt(b*b-c);
							}
						}
						
						float lambda1 = -b+d;
						float lambda2 = -b-d;
						
						float d1 = Math.abs(lambda1 - C[1][1]);
						
						float d2 = Math.abs(lambda2 - C[1][1]);
						
						float mu = lambda2;
						if (d1 < d2){
							mu = lambda1;
						}
						 // Algo 1c. Step 3 ends
						
						
						alpha = (float)R[p][p]*(float)R[p][p] - mu;
						beta = (float)R[p][p]*(float)R[p][p+1];
						// Algo 1c. Step 4 ends
						
					 // end mu
					
					//start givens rotation
					for (int k = p; k<= col-q-2; k++){
						
						R = GivensR(R,col,k,alpha,beta);
						QR = GivensR(QR,col,k,alpha,beta);
						
						alpha = (float)R[k][k];
						beta = (float)R[k+1][k];
						
						R = GivensL(R,col,k,alpha,beta);
						U = GivensR(U,row,k,alpha,beta);
						if ( k < col-q -2){
							alpha = (float)R[k][k+1];
							beta = (float)R[k][k+2];
						}
						
					} // end givens rotation

				} // end while loop
				
				
				float[] D = new float[R.length];
//				double[][] DfinalM = new double[col][col];
				double[] Dfinal = new double[R.length];
				for (int i = 0; i<R.length; i++){
					D[i] = (float)(R[i][i]);
					//Dfinal[i] = B[i][i];
				}
				//long endTime1 = System.currentTimeMillis();
		        //System.out.println("It took " + (endTime1 - startTime1) + " milliseconds");
				int[] ind = ArrayUtils.argsort(D);
		        Arrays.sort(D);
		        for (int i =0; i<D.length; i++){
		        	Dfinal[i] = D[D.length-1-i];
		        	//DfinalM[i][i] = D[D.length-1-i];
		        }
				
		        double[][] Ufinal = new double[row][col];
		        double[][] Vfinal = new double[col][col];

		        for(int i = 0; i<ind.length; i++){
		        	for (int j =0; j<row; j++){
		            	Ufinal[j][ind.length-1-i] = U[j][ind[i]];
		        	}
		        }
		        
		        for(int i = 0; i<ind.length; i++){
		        	for (int j =0; j<col; j++){
		            	Vfinal[j][ind.length-1-i] = QR[j][ind[i]];
		        	}
		        }
		        
//		        long stop = System.currentTimeMillis();
		       // System.out.println("It took " + (stop - start) + " milliseconds for svd bidiagonal code");
		        
		        //double[][] Atide = Matrix.multi(Ufinal, Matrix.multi(DfinalM, Matrix.transpose(Vfinal)));
				
		        
		        return new Object[]{Dfinal, Ufinal, Vfinal};
		        
		        
		     
	} // end main
	


	private double[][] R_QRtransR(double beta, double[] ur, double[][] A, double[] yr, int row, int col, int k) {
		
		// compute yr = beta*A*ur
		
		for (int l = 0; l <row; l++)
		{
			yr[l] = 0;
			for (int j = k+1; j<col; j++)
			{
				yr[l] = yr[l] + beta*ur[j]*A[l][j];
			}
		}
		
		// compute A = (A - yr*ur);
		
		for (int i = k; i<row; i++)
		{
			for (int j = k+1; j<col; j++)
			{
				A[i][j] = A[i][j] - ur[j]*yr[i];
			}
		}
		return A;
	}



	private double[][] Q_QRtransR(double beta, double[] ur, double[][] QR, double[] yqr, int col, int k) {
		
		// compute yqr^T = beta * ur^T * Qtilde
		
		for (int l = 0; l < col; l++)
		{
			yqr[l] = 0;
			for (int j = k+1; j<col; j++)
			{
				yqr[l] = yqr[l] + beta*ur[j]*QR[j][l];
			}
		}
		
		
		// compute Qtilde = Qtilde - ur*yqr^T
		
		for (int i = 0; i<col; i++)
		{
			for (int j = k+1; j<col; j++)
			{
				QR[j][i] = QR[j][i] - ur[j]*yqr[i];
			}
		}
		return QR;
	}



	private double[][] R_QRtransL(double beta, double[] u, double[][] A, double[] yl, int row, int col, int k) {
		
			// compute y^T = u^T * A
					for (int l = 0; l <col; l++)
					{
						yl[l] = 0;
						for (int j = k; j<row; j++)
						{
							yl[l] = yl[l] + beta*u[j]*A[j][l];
						}
					}
					

					// compute A = (A - u*y^T);
					
					for (int i = k; i<col; i++)
					{
						for (int j = k; j<row; j++)
						{
							A[j][i] = A[j][i] - u[j]*yl[i];
						}
					}
					
		return A;
	}



	private double[][] Q_QrtransL(double beta, double[] u, double[][] QL, double[] yql, int row, int k) {
		
		// compute yq = beta*Q*u
					for (int l = 0; l <row; l++)
					{
						yql[l] = 0;
						for (int j = k; j<row; j++)
						{
							yql[l] = yql[l] + beta*u[j]*QL[j][l];
						}
					}
					// compute Q = Q - yql * u^T;
					
					for (int i = 0; i<row; i++)
					{
						for (int j = k; j<row; j++)
						{
							QL[j][i] = QL[j][i] - yql[i] * u[j];
						}
					}
					
		return QL;
	}

	private static double[][] GivensL( double[][] B, int n, int k, float a, double b ){
		
		float r = (float)Math.sqrt(a*a+b*b);
		float c = a/r;
		double s = -b/r;
		
		double S0, S1;
		
		for (int i = 0; i<n; i++){
			S0 = B[k+0][i];
			S1 = B[k+1][i];
			
			B[k][i] = c*S0 -s*S1;
			B[k+1][i] = s*S0 + c*S1;
			
		}
		
		
		return B;
	} // end givensL
	
	private static double[][] GivensR( double[][] B, int n, int k, float a, double b ){
		
		float r = (float)Math.sqrt(a*a+b*b);
		float c = a/r;
		double s = -b/r;
		
		double S0, S1;
		for (int i = 0; i < n; i++){
			S0 = B[i][k];
			S1 = B[i][k+1];
			B[i][k] = c * S0 - s*S1;	// check sign of s
			B[i][k+1] = s*S0 + c*S1;	// -ve in this or above line
			
		}
		
		
		return B;
	} // end givensL

} // end classs
