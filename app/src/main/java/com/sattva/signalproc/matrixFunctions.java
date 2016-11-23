package com.sattva.signalproc;

public class matrixFunctions {
	/* Some comments about the class Matrix
	  * Matrix operations are performed on 2D arrays
	  *
	*/
	
		/* Some comments about multi function
		  * 
		  * @param 2D array- A and B both , of type double
		  * computes the matrix multiplication of A and B 
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double[][] multi(double[][]a,double[][]b)
		{
		
		int aCol=a[0].length;
		int aRow=a.length;
		int bCol=b[0].length;
		int bRow=b.length;
		double c[][]=new double[aRow][bCol];
		if(aCol==bRow) //Matrix multiplication condition mxn and nxp produces mxp;
		{
			
		
		for (int i = 0; i < aRow; i++)
	    {
	        for (int j = 0; j < bCol; j++)
	        {
	            for (int k = 0; k < bRow; k++)
	            {
	                c[i][j] = c[i][j] + a[i][k] * b[k][j];
	            }
	        }
	    }
		
	    }
		else
		{System.out.println("Enter a valid Matrix");}
	    return c;
		}
		
		
		public float[][] multiF(float[][]a,float[][]b)
		{
		
		int aCol=a[0].length;
		int aRow=a.length;
		int bCol=b[0].length;
		int bRow=b.length;
		float c[][]=new float[aRow][bCol];
		if(aCol==bRow) //Matrix multiplication condition mxn and nxp produces mxp;
		{
			
		
		for (int i = 0; i < aRow; i++)
	    {
	        for (int j = 0; j < bCol; j++)
	        {
	            for (int k = 0; k < bRow; k++)
	            {
	                c[i][j] = c[i][j] + a[i][k] * b[k][j];
	            }
	        }
	    }
		
	    }
		else
		{System.out.println("Enter a valid Matrix");}
	    return c;
		}
		

		public double[][] multiFD(float[][]a,float[][]b)
		{
		
		int aCol=a[0].length;
		int aRow=a.length;
		int bCol=b[0].length;
		int bRow=b.length;
		double c[][]=new double[aRow][bCol];
		if(aCol==bRow) //Matrix multiplication condition mxn and nxp produces mxp;
		{
			
		
		for (int i = 0; i < aRow; i++)
	    {
	        for (int j = 0; j < bCol; j++)
	        {
	            for (int k = 0; k < bRow; k++)
	            {
	                c[i][j] = c[i][j] + a[i][k] * b[k][j];
	            }
	        }
	    }
		
	    }
		else
		{System.out.println("Enter a valid Matrix");}
	    return c;
		}
		
		public float[][] multi_index(float[][]Ql,float[][]UTl,int i,int n )
		{
		
		int row = UTl.length;
		
		//double c[][]=new double[row][Col];
		float[] templ = new float[row];
		float norm = 0;	
		
			for (int k = 0; k<n; k++){
				for (int t =0 ; t<row; t++){
					templ[t] = UTl[t][k];
				}
				for (int l = i ; l<row; l++){
					norm = 0;
					for (int m = i; m<row; m++){
						norm = norm + templ[m]*Ql[l][m];
					}
					UTl[l][k] = norm;
				}
			}
	    

	    return UTl;
		
		}
		

		/* Some comments about sum function
		  * 
		  * @param 2D array 'A' of type double
		  * computes the sum of the elements in the matrix
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double sum(double[][]a)
		{
			int n=a.length;
			int m=a[0].length;
			double sum=0.0;
			for (int i = 0; i < n; i++)
		    {
		        for (int j = 0; j < m; j++)
		        {
		            {
		                sum = sum + a[i][j];
		            }
		        }
		    }
		   // System.out.println("The sum of all elements is:"+sum);
		    return sum;
			}
		

		/* Some comments about 'Mean' function
		  * 
		  * @param 2D array 'A' of type double
		  * calculates the mean of the matrix and subtracts the mean from each element
		  *	returns the resultant matrix which can be used for further calc
		*/	
		public double[][] mean(double[][]a)
		{
			
			int n=a.length;
			int m=a[0].length;
			double[][]res=new double[n][m];
			
			for(int i=0;i<n;i++)
			 {double mean,s=0.0;
				for(int j=0;j<m;j++)
				{
					s=s+a[i][j];
				}
			
			mean=s/(m);
			
			 //System.out.print("mean of row "+i+" is = "+mean+"\t");
				for(int j=0;j<m;j++)
				{
					res[i][j]=a[i][j]-mean;//subtract the mean from the all elements
					// System.out.print(res[i][j] + "\t");
				}
			//	System.out.println();
			}
			
			return res;
		}
		

		/* Some comments about 'Identity' function
		  * 
		  * @param size of the required Identity Matrix
		  * Produces an Identity Matrix of size specified by the input- 'size'
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double[][] identity(int size)
		{
		
		double[][]id=new double[size][size];
		for(int i=0; i<size; i++)
			{
				for(int j=0;j<size;j++)
				{
				if(i==j) 	//diagonal elements are set to unity
				id[i][j]= 1;
				
				else
				id[i][j]=0;
			}
		}
		
		return id;
		}

		public float[][] identityF(int size)
		{
		
			float[][]id=new float[size][size];
		for(int i=0; i<size; i++)
			{
				for(int j=0;j<size;j++)
				{
				if(i==j) 	//diagonal elements are set to unity
				id[i][j]= 1;
				
				else
				id[i][j]=0;
			}
		}
		
		return id;
		}
		/* Some comments about 'ElementWiseMult' function
		  * 
		  * @param 2D Arrays A and B of type double
		  * Computes the element wise multiplication of A and B
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double[][] ElementWiseMult(double[][]a,double[][]b)
		{	
			int aCol=a[0].length;
			int aRow=a.length;
			int bCol=b[0].length;
			int bRow=b.length;
				double [][] dot=new double[aRow][aCol];
				if(aCol==bCol && aRow==bRow) //The dimensions of Matrix A and B must match
				{
				//System.out.println("The element wise mult matrix is: ");
				for(int i=0;i<aRow;i++)
				{
					for(int j=0; j<aCol;j++)
						{
						dot[i][j]= a[i][j]*b[i][j];
						//System.out.print(dot[i][j]+" ");
						}
				//System.out.println();
				}
				return dot;
				}
				else{System.out.println("dim mismatch for element wise * ");return dot;}
			}
		
		public double[][] ElementWiseDivide(double[][]a,double[][]b)
		{	
			int aCol=a[0].length;
			int aRow=a.length;
			int bCol=b[0].length;
			int bRow=b.length;
				double [][] dot=new double[aRow][aCol];
				if(aCol==bCol && aRow==bRow) //The dimensions of Matrix A and B must match
				{
				//System.out.println("The element wise mult matrix is: ");
				for(int i=0;i<aRow;i++)
				{
					for(int j=0; j<aCol;j++)
						{
						dot[i][j]= a[i][j]/b[i][j];
						//System.out.print(dot[i][j]+" ");
						}
				//System.out.println();
				}
				return dot;
				}
				else{System.out.println("dim mismatch for element wise * ");return dot;}
			}
		
		/* Some comments about 'diag' function
		  * 
		  * @param 1D Array A 
		  * Results in computing the diagonal matrix by inserting the input values in the diagonal locations
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double[][] diag(double[]a)
		{
			int n=a.length;
			int v=0;
			
			double[][]diag=new double[n][n];
			for(int i=0; i<n; i++)
				{
					for(int j=0;j<n;j++)
					{
					if(i==j)
					{diag[i][j]= a[v];
					v++;}
					
					else
					diag[i][j]=0;
				}
			}
			//System.out.println("The diagonal matrix is:");
			//for(int i=0;i<n;i++)
			 {
				//for(int j=0;j<n;j++)
				{
					 //System.out.print(diag[i][j] + " ");
				}
				//System.out.println();
			}
			return diag;
			}
		
		
		/* Some comments about 'Trace' function
		  * 
		  * @param 2D Array A of type double
		  * Computes the sum of the diagonal elements
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double trace(double[][]a)

		{
			int n=a.length;
			int m=a[0].length;
			double sum=0.0;
			if(n==m){
			for(int i=0; i<n; i++)
				{
					for(int j=0;j<m;j++)
					{
					if(i==j)
					sum=sum+a[i][j];
				}
			}
			//System.out.println("Trace="+sum);
			}
			else
			{System.out.println("Enter a square matrix");}
			return sum;
			}
		

		/* Some comments about 'Transpose' function
		  * 
		  * @param 2D Array A of type double
		  * Produces the transpose of the input matrix
		  *	returns the resultant matrix which can be used for further calc
		*/
	public double[][] transpose(double[][] a)
		{
			int n=a.length;
			int m=a[0].length;
			double[][] transpose=new double[m][n];
			for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				{transpose[j][i]=a[i][j];}
			
			return transpose;
		}

	public float[][] transposeF(float[][] a)
	{
		int n=a.length;
		int m=a[0].length;
		float[][] transpose=new float[m][n];
		for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
			{transpose[j][i]=a[i][j];}
		
		return transpose;
	}
	/* Some comments about 'submatrix' function
	 * 
	 * @param 2D Array A of type double, the boundary of the submatrix 
	 * ie, from r1 to r2 and from c1 to c2
	 * Computes the element wise multiplication of A and B
	 *	returns the resultant matrix which can be used for further calc
	*/
		public double[][] submatrix(double[][]a, int r1, int r2, int c1,int c2)
		{
			int n=a.length;
			int m=a[0].length;
			int u=-1;int v=0;
			double[][] sub=new double[(r2-r1)+1][(c2-c1)+1]; //size of the sub matrix
			if(((r1>=0 && r1<=r2) && (r2<n && r2>=r1))&&((c1>=0 &&c1<=c2 ) && (c2<m && c2>=c1)))//The boundary conditions must lie within the size of the Matrix
		{ 	
			/*
			for(int i=r1-1;i<r2;i++)
			{ ++u;v=0;//indices pointing at the sub matrix locations
				for(int j=c1-1;j<c2;j++)
				{
					sub[u][v]=a[i][j];
					++v;
				}
			}*/
			for (int i = 0; i < r2-r1 +1; i++){
				for (int j = 0; j < c2-c1+1; j++){
					sub[i][j] = a[r1+i][c1+j];
					//System.out.print("Enter final"+sub[i][j]);
				}
			}
			
			//System.out.println("Enter final");
		
		}
			else{System.out.println("Enter valid matrix dimention");
			}

			return sub;
		}

		public float[][] submatrixF(float[][]a, int r1, int r2, int c1,int c2)
		{
			int n=a.length;
			int m=a[0].length;
			int u=-1;int v=0;
			float[][] sub=new float[(r2-r1)+1][(c2-c1)+1]; //size of the sub matrix
			if(((r1>=0 && r1<=r2) && (r2<n && r2>=r1))&&((c1>=0 &&c1<=c2 ) && (c2<m && c2>=c1)))//The boundary conditions must lie within the size of the Matrix
		{ 	
			for (int i = 0; i < r2-r1 +1; i++){
				for (int j = 0; j < c2-c1+1; j++){
					sub[i][j] = a[r1+i][c1+j];
				}
			}
		
		}
			else{System.out.println("Enter valid matrix dimention");
			}

			return sub;
		}

		
		
		public int[][] submatrixI(int[][]a, int r1, int r2, int c1,int c2)
		{
			int n=a.length;
			int m=a[0].length;
			int u=-1;int v=0;
			int[][] sub=new int[(r2-r1)+1][(c2-c1)+1]; //size of the sub matrix
			if(((r1>=0 && r1<=r2) && (r2<n && r2>=r1))&&((c1>=0 &&c1<=c2 ) && (c2<m && c2>=c1)))//The boundary conditions must lie within the size of the Matrix
		{ 	
			/*
			for(int i=r1-1;i<r2;i++)
			{ ++u;v=0;//indices pointing at the sub matrix locations
				for(int j=c1-1;j<c2;j++)
				{
					sub[u][v]=a[i][j];
					++v;
				}
			}*/
			for (int i = 0; i < r2-r1 +1; i++){
				for (int j = 0; j < c2-c1+1; j++){
					sub[i][j] = a[r1+i][c1+j];
					//System.out.print("Enter final"+sub[i][j]);
				}
			}
			
			//System.out.println("Enter final");
		
		}
			else{System.out.println("Enter valid matrix dimention");
			}

			return sub;
		}


		/* Some comments about 'sqrt' function
		  * 
		  * @param 2D Array A of type double
		  * Computes the square roots of the elements in the Matrix
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double[][] sqrt(double a[][])
	{	
		int n=a.length;
		int m=a[0].length;
		double[][] sqrt=new double[n][m];
		for(int i=0; i<n;i++)
			{
			for(int j=0;j<m;j++)
				{
					sqrt[i][j]= Math.sqrt(a[i][j]);
					
					//System.out.print(sqrt[i][j]+"  ");
				}
	//System.out.println();
			}
	return sqrt;
	}
		

		/* Some comments about 'inverse' function
		  * 
		  * @param 2D Array A of type double
		  * Computes the inverse of the matrix and intrinsically calls gaussian function 
		  *	returns the resultant matrix which can be used for further calc
		*/
		public double[][] inverse(double[][]a)
	{
		int n = a.length;
	    double x[][] = new double[n][n];
	    double b[][] = new double[n][n];
	    int index[] = new int[n];
	    for (int i=0; i<n; ++i) b[i][i] = 1;

	 // Transform the matrix into an upper triangle
	    gaussian(a, index);

	 // Update the matrix b[i][j] with the ratios stored
	    for (int i=0; i<n-1; ++i)
	      for (int j=i+1; j<n; ++j)
	        for (int k=0; k<n; ++k)
	          b[index[j]][k]
	            -= a[index[j]][i]*b[index[i]][k];

	 // Perform backward substitutions
	    for (int i=0; i<n; ++i) {
	      x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
	      for (int j=n-2; j>=0; --j) {
	        x[j][i] = b[index[j]][i];
	        for (int k=j+1; k<n; ++k) {
	          x[j][i] -= a[index[j]][k]*x[k][i];
	        }
	        x[j][i] /= a[index[j]][j];
	      }
	    }
	    //output
	    //System.out.println("\n"+"The inverse of matrix a is:");
	    for(int i=0;i<n;i++){
	    	for(int j=0;j<n;j++)
	    	{
	    		//System.out.print(x[i][j]+"  ");
	    	}
	   // System.out.println();
	    }
	  return x;
	  }
		public void gaussian(double a[][],int index[]) 
		{
		    int n = index.length;
		    double c[] = new double[n];

		 // Initialize the index
		    for (int i=0; i<n; ++i) index[i] = i;

		 // Find the re-scaling factors, one from each row
		    for (int i=0; i<n; ++i) 
		    {
		      double c1 = 0;
		      for (int j=0; j<n; ++j) 
		      {
		        double c0 = Math.abs(a[i][j]);
		        if (c0 > c1) c1 = c0;
		      }
		      c[i] = c1;
		    }

		 // Search the pivoting element from each column
		    int k = 0;
		    for (int j=0; j<n-1; ++j) 
		    {
		      double pi1 = 0;
		      for (int i=j; i<n; ++i) 
		      {
		        double pi0 = Math.abs(a[index[i]][j]);
		        pi0 /= c[index[i]];
		        if (pi0 > pi1) 
		        {
		          pi1 = pi0;
		          k = i;
		        }
		      }

		   // Interchange rows according to the pivoting order
		      int itmp = index[j];
		      index[j] = index[k];
		      index[k] = itmp;
		      for (int i=j+1; i<n; ++i) 
		      {
		        double pj = a[index[i]][j]/a[index[j]][j];

		     // Record pivoting ratios below the diagonal
		        a[index[i]][j] = pj;

		     // Modify other elements accordingly
		        for (int l=j+1; l<n; ++l)
		          a[index[i]][l] -= pj*a[index[j]][l];
		      }
		    }

	}
		
		/* Some comments about 'Matdiv' function
		  * 
		  * @param 2D Arrays inv and B of type double
		  *  inv array is the inverse of a matrix
		  *  To compute the matrix division:B/A, => B*(1/A) =>B*(A inverse)
		  *  This function calculates the matrix division of B by inv (inv=A inverse)
		  *	returns the resultant matrix which can be used for further calc
		*/	
		public double[][] Matdiv(double[][]inv,double[][]b)
		{
			int bRow=b.length;
			int bCol=b[0].length;
			int aRow=inv.length;
			int aCol=inv[0].length;
			double[][]div =new double[aRow][aCol]; 
			if(bRow==aRow && bCol==aCol)
			{System.out.println("\n"+"The division of b by a is:");
			  for(int i=0;i<bRow;i++)
			  {
				  for( int j=0;j<bCol;j++)
				  {
					  div[i][j]=inv[i][j]*b[i][j]; 
					  //System.out.print(div[i][j]+"  ");
				  }
			  //System.out.println();
			  }return div;
			}
			else System.out.println("The dimensions do not match");
		return div;
		}
		
		public double[][] ElementDiv(double[][]a,double b){
			int aCol=a[0].length;
			int aRow=a.length;
			
				double [][] div=new double[aRow][aCol];
				
				for(int i=0;i<aRow;i++)
				{
					for(int j=0; j<aCol;j++)
						{
						div[i][j]= a[i][j]/b;
						
						}
				}
				return div;
				}
				
		public double[][] ones(int n,int m)
		{
		
		double[][]one=new double[n][m];
		for(int i=0; i<n; i++)
			{
				for(int j=0;j<m;j++)
				{
				one[i][j]=1;
			}
		}
		
		return one;
		}
		
		
		public double[][] ElementWiseSub(double[][]a,double b)
		{	
			int aCol=a[0].length;
			int aRow=a.length;
			
				double [][] sub=new double[aRow][aCol];
				
				for(int i=0;i<aRow;i++)
				{
					for(int j=0; j<aCol;j++)
						{
						sub[i][j]= a[i][j]-b;
						
						}
				}
				return sub;
				
			
		}

		public double[][] numberMultmatrix(double[][]a,double b)
		{	
			int aCol=a[0].length;
			int aRow=a.length;
			
				double [][] multiply=new double[aRow][aCol];
				
				for(int i=0;i<aRow;i++)
				{
					for(int j=0; j<aCol;j++)
						{
						multiply[i][j]= a[i][j]*b;
						
						}
				}
				return multiply;
				
			
		}
		
		public double[][] SubtractMatrices(double[][]a,double[][] b)
		{	
			int aCol=a[0].length;
			int aRow=a.length;
			int bCol=b[0].length;
			int bRow=b.length;
			double [][] sub=new double[aRow][aCol];
			if(aRow==bRow && aCol==bCol){
				
				
				for(int i=0;i<aRow;i++)
				{
					for(int j=0; j<aCol;j++)
						{
						sub[i][j]= a[i][j]-b[i][j];
						
						}
				}	return sub;}
			else{System.out.println("Cannot sub matrices, dim mismatch");return sub;}
				
			
		}
		

		public double[][]  sorting(double[]a1)
		{	//double[][] old_ind=new double[a1.length][1];
			double[][] finalArray=new double[a1.length][2];
			
		for(int t=0;t<a1.length;t++)
		{
			finalArray[t][1]=t;
			finalArray[t][0]=a1[t];
			
		}
			
			for(int i=0; i<a1.length -1; i++){
		          
	            //Inner loop to perform comparision and swapping between adjacent numbers
	            //After each iteration one index from last is sorted
	            for(int j= 1; j<a1.length -i; j++){
	              
	                //If current number is greater than swap those two
	                if(a1[j-1] > a1[j]){
	                	

	                    double temp = finalArray[j][1];
	                    finalArray[j][1] = finalArray[j-1][1];
	                    finalArray[j-1][1] = temp;
	                  
	                	
	                	
	                	
	                    temp = finalArray[j][0];
	                    finalArray[j][0] = finalArray[j-1][0];
	                    finalArray[j-1][0] = temp;
	                       
	                }
	            }
			}
			 return finalArray;
			 
		}
		
		
		public double[][] verticalConcat(double[][] a, double[][] b){
			
			
				if (a.length >0 && b.length == 0){
					return a;
				} else if(a.length ==0 && b.length>0) {
					return b;
				}else if (a[0].length == b[0].length){
				double[][] c = new double[a.length+b.length][a[0].length];
				for (int i = 0; i < a.length+b.length ; i++){
					for (int j =0 ; j< a[0].length; j++){
					if ( i < a.length){
						c[i][j] = a[i][j]; 
					}else{
						c[i][j] = b[i-a.length][j];
					}
					}
				} 
				
				return c;
			} // end both present
			 else{
				return null;
			 }
			
			}
			
		
		/**
		 * Peak detection for array
		 */
		public double[][] PeakDetection(double[]inp, double delta)
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
				else if (lookformax ==0){
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
			if (maxtab[0][0] >= 0 && maxtab[1][0] > 0){
				count = count + 1;
			}
			for (int i = 1; i< maxtab.length; i++){
				if (maxtab[i][0] > 0){
					count = count+1;
				}
				else{
					break;
				}
			}
			double[][] maxtab_fin = new double[count][2];
			for (int i = 0; i<count; i++){
				for (int j = 0; j<2; j++){
					maxtab_fin[i][j] = maxtab[i][j];
				}
			}
			return maxtab_fin;
		}	
			
		/**
		 * 
		 * @param inp
		 * @param element
		 * @param CaseNumber::  1 for equal, 2 for greater, 3 for lesser
		 * @return  Add return exceptions , i.e if nothing is found
		 * 			then return null
		 */
		public int[] FindingElement(int inp[][], double element, int CaseNumber)
		{	int num = CaseNumber; 
			int output[] = new int[100];
			int t = 0;
			int count = 0;
			switch(num)
			{
			
			case 1: 
					for(int y = 0; y<inp.length; y++){
						if(inp[y][0] == element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
					}break;
			
			case 2:	
					for(int y = 0; y<inp.length; y++)
					{
						if(inp[y][0] > element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
					}	
					break;
			
			case 3:	 
				    for(int y = 0; y<inp.length; y++)
					{
						if(inp[y][0] < element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
						
					}break;
			
			
			}
			
			if (count >0){
			for (int i = 1; i< output.length; i++){
				if (output[i] > 0){
					count = count+1;
				}
				else{
					break;
				}
			}
			} else if (count == 0){
				for (int i = 0; i< output.length; i++){
					if (output[i] > 0){
						count = count+1;
					}
					else{
						break;
					}
				}
			}
			if (count > 0){
				int[] out = new int[count];
				for (int i = 0; i < count; i++){
					out[i] = output[i];
				}
				return out;
			}else{
				return null;
			}
			
		}	
		
		public int[] FindingElementVector(int inp[], double element, int CaseNumber)
		{	int num = CaseNumber; 
			int output[] = new int[100];
			int t = 0;
			int count = 0;
			switch(num)
			{
			
			case 1: 
					for(int y = 0; y<inp.length; y++){
						if(inp[y] == element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
					}break;
			
			case 2:	
					for(int y = 0; y<inp.length; y++)
					{
						if(inp[y] > element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
					}	
					break;
			
			case 3:	 
				    for(int y = 0; y<inp.length; y++)
					{
						if(inp[y] < element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
						
					}break;
			
					
			case 4:	
				for(int y = 0; y<inp.length; y++)
				{
					if(inp[y] >= element)
					{
						output[t] = y;
						++t;
						if (y == 0){
							count = count + 1;
						}
					}
				}	
				break;	
			case 5:	 
			    for(int y = 0; y<inp.length; y++)
				{
					if(inp[y] <= element)
					{
						output[t] = y;
						++t;
						if (y == 0){
							count = count + 1;
						}
					}
					
				}break;
			
			}
			
			if (count >0){
			for (int i = 1; i< output.length; i++){
				if (output[i] > 0){
					count = count+1;
				}
				else{
					break;
				}
			}
			} else if (count == 0){
				for (int i = 0; i< output.length; i++){
					if (output[i] > 0){
						count = count+1;
					}
					else{
						break;
					}
				}
			}
			if (count > 0){
				int[] out = new int[count];
				for (int i = 0; i < count; i++){
					out[i] = output[i];
				}
				return out;
			}else{
				return null;
			}
			
		}

		
		public double[] FindingElementDouble(double inp[], double element, int CaseNumber)
		{	int num = CaseNumber; 
			int output[] = new int[100000];
			int t = 0;
			int count = 0;
			switch(num)
			{
			
			case 1: 
					for(int y = 0; y<inp.length; y++){
						if(inp[y] == element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
					}break;
			
			case 2:	
					for(int y = 0; y<inp.length; y++)
					{
						if(inp[y] > element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
					}	
					break;
			
			case 3:	 
				    for(int y = 0; y<inp.length; y++)
					{
						if(inp[y] < element)
						{
							output[t] = y;
							++t;
							if (y == 0){
								count = count + 1;
							}
						}
						
					}break;
			
					
			case 4:	
				for(int y = 0; y<inp.length; y++)
				{
					if(inp[y] >= element)
					{
						output[t] = y;
						++t;
						if (y == 0){
							count = count + 1;
						}
					}
				}	
				break;	
			case 5:	 
			    for(int y = 0; y<inp.length; y++)
				{
					if(inp[y] <= element)
					{
						output[t] = y;
						++t;
						if (y == 0){
							count = count + 1;
						}
					}
					
				}break;
			
			}
			
			if (count >0){
			for (int i = 1; i< output.length; i++){
				if (output[i] > 0){
					count = count+1;
				}
				else{
					break;
				}
			}
			} else if (count == 0){
				for (int i = 0; i< output.length; i++){
					if (output[i] > 0){
						count = count+1;
					}
					else{
						break;
					}
				}
			}
			if (count > 0){
				double[] out = new double[count];
				for (int i = 0; i < count; i++){
					out[i] = output[i];
				}
				return out;
			}else{
				return null;
			}
			
		}
		
		
		
		
		// FOR SVD functions
		

		public float[][] GenerateRightHouseholderMatrix(float[] w1, int col) {
			// TODO Auto-generated method stub
			float[][] Q1 = new float[col][col];
			for (int j = 0; j<col; j++){
				for (int k = 0; k <=j; k++){
					if (k<j){
						Q1[k][j] = -2*w1[k]*w1[j];
						Q1[j][k] = Q1[k][j];
					}
					if (k == j){
						Q1[k][j] = 1 - 2* w1[k]*w1[j]; 
					}
				}
			}
			return Q1;
		}

		public float[][] GenerateLeftHouseholderMatrix(float[] wl, int i, int row) {
			// TODO Auto-generated method stub
			float[][] Ql = new float[row][row];
			for (int j = i; j<row; j++){
				for (int k = i; k <=j; k++){
					if (k<j){
						Ql[k][j] = -2*wl[k]*wl[j];
						Ql[j][k] = Ql[k][j];
					}
					if (k == j){
						Ql[k][j] = 1 - 2* wl[k]*wl[j]; 
					}
				}
			}
			return Ql;
		}

		public float[] Get_householdervector(float[][] Al, int row, int i) {
			// TODO Auto-generated method stub
			float[] wl = new float[row];
			float norml = 0;
			for (int j =0; j<row; j++){
				if(j<=i){
					wl[j] = 0;
				}else{
					wl[j] = Al[j][i];
					norml = norml + wl[j]*wl[j];
				}
			}
			wl[i] = Al[i][i] - (float)Math.sqrt(norml+Al[i][i]*Al[i][i]);
			norml = (float)Math.sqrt(norml + wl[i]*wl[i]);
			
			for (int j = i; j<row; j++){
				wl[j] = wl[j]/norml;
			}
			
			return wl;
		}

		public float[][] GivensL( float[][] B, int n, int k, float a, float b ){
			
			float r = (float)Math.sqrt(a*a+b*b);
			float c = a/r;
			float s = -b/r;
			
			float S0, S1;
			
			for (int i = 0; i<n; i++){
				S0 = B[k+0][i];
				S1 = B[k+1][i];
				
				B[k][i] = c*S0 -s*S1;
				B[k+1][i] = s*S0 + c*S1;
				
			}
			
			
			return B;
		} // end givensL
		
		public float[][] GivensR( float[][] B, int n, int k, float a, float b ){
			
			float r = (float)Math.sqrt(a*a+b*b);
			float c = a/r;
			float s = -b/r;
			
			float S0, S1;
			for (int i = 0; i < n; i++){
				S0 = B[i][k];
				S1 = B[i][k+1];
				B[i][k] = c * S0 - s*S1;	// check sign of s
				B[i][k+1] = s*S0 + c*S1;	// -ve in this or above line
				
			}
			
			
			return B;
		} // end givensL


		public double[][] prakdet(double[] inp,int flag) {
			// TODO Auto-generated method stub
			// flag == 0; for maternal
			// flag == 1 for fetal
			
			int len = inp.length;
			double[][] maxtab = new double[len][2];
			int init;
			int fin;
			if (flag == 0){
				//maxtab = new double[Math.round(len/400)][2];
				init = 150;
				fin = 200;
			}
			else{
				//maxtab = new double[Math.round(len/150)][2];
				init = 150;
				fin = 200;
			}
			
			double temp;
			
			int count = 0;
			for (int i = init; i<len-fin; i++){
				temp = inp[i];
				if (temp >0 && inp[i+1] == 0){
					count = count+1;
				}
				if (temp > maxtab[count][1]){
					maxtab[count][0] = i;
					maxtab[count][1] = temp;
				}
			}
			count = 0;
			for (int  i = 0; i<maxtab.length; i++){
				if (maxtab[i][0] >0){
					count = count + 1;
				}
			}
			
			double[][] maxtab1 = new double[count][2];
			for (int i =0; i<count; i++){
				maxtab1[i][0] = maxtab[i][0];
				maxtab1[i][1] = maxtab[i][1];
			}
			
			
			
			return maxtab1;
		}
		


}//close class
		
		
	


		




