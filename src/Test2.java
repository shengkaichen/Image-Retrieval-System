

import java.util.Arrays;

public class Test2
{
	public static void main(String[] args)
	{
		// image 3 + 4  
		double tem1[][] = {{0.25,0.375,0.375,0.25},{0.1,0.5,0.4,0},{0.4,0.4,0.2,0.4}, {0.4,0.4,0.2,0.2}};
		
		double tem2[]= {1, 1, 1,1};
		
		RelevanceFeedback RelevanceFeedback = new RelevanceFeedback();
		System.out.println(Arrays.deepToString(RelevanceFeedback.getNormalization(tem1, tem2)));
		System.out.println(Arrays.toString(RelevanceFeedback.getNormalizedWeight(RelevanceFeedback.getNormalization(tem1, tem2))));
		
		boolean myBoolean = true;
		if(Boolean.TRUE == new Boolean(true)){
            System.out.println("==");
        }

        if(Boolean.TRUE.equals(myBoolean)){
            System.out.println("equals");;
        }

	}
}
