

public class RelevanceFeedback
{
	double normalizedMatrix_T[][];
	double normalizedWeight[];
	
	/*
	 * Using Gaussian normalization to Normalize intensityMatrix or colorCodeMatrix
	 */
	public double[][] getNormalization(double[][] colorMatrix, double[] picSize)
	{
		normalizedMatrix_T = new double[colorMatrix[0].length][colorMatrix.length];
		double normalizedMatrix[][] = new double[colorMatrix.length][colorMatrix[0].length];
		double avg[] = new double[colorMatrix[0].length];
		double std[] = new double[colorMatrix[0].length];

		// Recalculate each features in an image by dividing each value to its size
		for(int i = 0; i < colorMatrix.length; i++)
		{
			for(int j = 0; j < colorMatrix[i].length; j++)
			{
				colorMatrix[i][j] /= picSize[i];
			}
		}
		
		// Transpose colorMatrix[][] into colorValue_T[][]
        for (int i = 0; i < colorMatrix.length; i++)
        {
        	for (int j = 0; j < colorMatrix[0].length; j++)
        	{
        		normalizedMatrix_T[j][i] = colorMatrix[i][j];
        	}    
        }

        // Average of each feature
		for(int i = 0; i < normalizedMatrix_T.length; i++)
		{
	        double colTotal = 0;
	        
			for(int j = 0; j < normalizedMatrix_T[i].length; j++)
			{	
				colTotal += normalizedMatrix_T[i][j];
			}
			avg[i] = colTotal / normalizedMatrix_T[i].length;
		}
		
		// Standard deviation of each feature
		for(int i = 0; i < normalizedMatrix_T.length; i++)
		{
	        double colTotal= 0;
	        
			for(int j = 0; j < normalizedMatrix_T[i].length; j++)
			{	
				colTotal += Math.pow(normalizedMatrix_T[i][j] - avg[i], 2);
			}
			std[i] = Math.sqrt(colTotal / (normalizedMatrix_T[i].length - 1));
		}

		// Calculate normalized features
		
		
		for(int i = 0; i < normalizedMatrix_T.length; i++)
		{
			for(int j = 0; j < normalizedMatrix_T[i].length; j++)
			{	
				normalizedMatrix_T[i][j] = (normalizedMatrix_T[i][j] - avg[i]) / std[i];
				normalizedMatrix[j][i] = normalizedMatrix_T[i][j];
			}
		}
		return normalizedMatrix;
	}

	/*
	 * Get normalized weight from selected image
	 */
	public double[] getNormalizedWeight(double[][] selectedImage)
	{
		normalizedWeight = new double[selectedImage.length];
		double newNormalizedMatrix[][] = new double
				[selectedImage[0].length][selectedImage.length];
		double avg[] = new double[selectedImage.length]; // average
		double std[] = new double[selectedImage.length]; // standard deviation
		double updwt[] = new double[selectedImage.length]; // updated weight
		double norwt[] = new double[selectedImage.length];// normalized weight
		double sumupdwt = 0.0;
		
		// Transpose
		for(int i = 0; i < newNormalizedMatrix.length; i++)
		{
			for(int j = 0; j < newNormalizedMatrix[i].length; j++)
			{
				newNormalizedMatrix[j][i] = selectedImage[i][j];
			}
		}

        // Average of each feature
		for(int i = 0; i < newNormalizedMatrix.length; i++)
		{
	        double colTotal = 0;
	        
			for(int j = 0; j < newNormalizedMatrix[i].length; j++)
			{	
				colTotal += newNormalizedMatrix[i][j];
			}
			avg[i] = colTotal / newNormalizedMatrix[i].length;
		}
		
		// Standard deviation of each feature
		for(int i = 0; i < newNormalizedMatrix.length; i++)
		{
	        double colTotal= 0;
	        
			for(int j = 0; j < newNormalizedMatrix[i].length; j++)
			{	
				colTotal += Math.pow(newNormalizedMatrix[i][j] - avg[i], 2);
			}
			std[i] = Math.sqrt(colTotal / (newNormalizedMatrix[i].length - 1));
		}
		
		// updated weight of each feature
		for(int i = 0; i < newNormalizedMatrix.length; i++)
		{
	        updwt[i] = 1 / std[i];
	        sumupdwt += updwt[i];
		}

		// normalized weight of each feature
		for(int i = 0; i < newNormalizedMatrix.length; i++)
		{
			norwt[i] = updwt[i] / sumupdwt;
			normalizedWeight[i] = norwt[i];
		}
		return normalizedWeight;
	}
}
