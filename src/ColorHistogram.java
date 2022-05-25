/*
* Project 2
*/

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ColorHistogram
{
	int imageCount = 1;
	int intensityMatrix[][] = new int[100][25];
	int colorCodeMatrix[][] = new int[100][64];
	int imageSize[] = new int[100];
	/*
	 * Each image is retrieved from the file. The height and width are found for
	 * the image and the getIntensity and getColorCode methods are called.
	 */
	public ColorHistogram()
	{
		BufferedImage image = null;
		while(imageCount < 101)
		{
			try
			{
				image = ImageIO.read(new File("src/images/" + imageCount + ".jpg"));
				getIntensity(image, imageCount, image.getHeight(), image.getWidth());
				getColorCode(image, imageCount, image.getHeight(), image.getWidth());
				
				for(int i = 0; i < imageSize.length; i++)
				{
					imageSize[i] = (image.getHeight() * image.getWidth());
				}

			}catch (IOException e)
			{
				System.out.println("Error occurred when reading the file.");
			}
			imageCount += 1;
		}		
		writeIntensity();
		writeColorCode();	
	}
	
	// intensity method
	public void getIntensity(BufferedImage image, int imageCount, int height, int width)
	{
		ArrayList<Double> intensityValue = new ArrayList<Double>();
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				// Get RGB color on each pixel
				Color pixel = new Color(image.getRGB(i, j));
				int r = pixel.getRed();
				int g = pixel.getGreen();
				int b = pixel.getBlue();
				double I = 0.299 * r + 0.587 * g + 0.114 * b;
				intensityValue.add(I);
			}
		}
		
		int intensityBins[] = new int[25];
		for (int i = 0; i < intensityValue.size(); i++)
		{
			for (int j = 0; j < intensityBins.length; j++)
			{
				if (intensityValue.get(i) >= j * 10 && intensityValue.get(i) < (j + 1) * 10)
				{
					intensityBins[j] += 1;
				}
			}
		}
		
		for (int i = 0; i < intensityBins.length; i++)
		{
			intensityMatrix[imageCount - 1][i] = intensityBins[i];
		}
	}

	// color code method
	public void getColorCode(BufferedImage image, int imageCount, int height, int width)
	{
		ArrayList<String> colorCodeValue = new ArrayList<String>();
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				// Get RGB color on each pixel
				Color pixel = new Color(image.getRGB(i, j));
				int r = pixel.getRed();
				int g = pixel.getGreen();
				int b = pixel.getBlue();
				
				// Transfer each color of RGB value to 8-bit
				double bitPerColor = 8.0;
				String r8bit = Integer.toBinaryString(r);
				String g8bit = Integer.toBinaryString(g);
				String b8bit = Integer.toBinaryString(b);

				// Convert concatonate 0's in front to get desired bit count
				int rDifference = (int) bitPerColor - r8bit.length();
				int gDifference = (int) bitPerColor - g8bit.length();
				int bDifference = (int) bitPerColor - b8bit.length();
				for (int k = rDifference; k > 0; k--)
				{
					r8bit = "0" + r8bit;
				}

				for (int k = gDifference; k > 0; k--)
				{
					g8bit = "0" + g8bit;
				}

				for (int k = bDifference; k > 0; k--)
				{
					b8bit = "0" + b8bit;
				}
				String x = r8bit + g8bit + b8bit;
				colorCodeValue.add(x);
			}
		}
		
		// Transfer the ArrayList to Array	
		String colorCodeArray[] = new String[colorCodeValue.size()];
		colorCodeArray = colorCodeValue.toArray(colorCodeArray);

		String colorCode2dArray[][] = new String[colorCodeArray.length][24];
		for (int r = 0; r < colorCode2dArray.length; r++)
		{
			colorCode2dArray[r] = colorCodeArray[r].split("");

		}
		
		// Calculate the color code value for each pixel
		int newColorCode2dArray[] = new int[colorCode2dArray.length];
		for (int r = 0; r < newColorCode2dArray.length; r++)
		{
			int colorCode = 0;
			for (int c = 0; c < 6; c++)
			{	
				if (c == 0)
				{
					if (colorCode2dArray[r][0].equals("1"))
					{
						colorCode += 32;
					}
				}
				else if (c==1)
				{
					if (colorCode2dArray[r][1].equals("1"))
					{
						colorCode += 16;
					}
				}
				else if (c==2)
				{
					if (colorCode2dArray[r][8].equals("1"))
					{
						colorCode += 8;
					}
				}
				else if (c==3)
				{
					if (colorCode2dArray[r][9].equals("1"))
					{
						colorCode += 4;
					}
				}
				else if (c==4)
				{
					if (colorCode2dArray[r][16].equals("1"))
					{
						colorCode += 2;
					}
				}
				else if (c==5)
				{
					if (colorCode2dArray[r][17].equals("1"))
					{
						colorCode += 1;
					}
				}
			}
			newColorCode2dArray[r] = colorCode;
		}

		int colorCodeBins[] = new int[64];
		for (int i = 0; i < newColorCode2dArray.length; i++)
		{
			for (int j = 0; j < colorCodeBins.length; j++)
			{
				if (newColorCode2dArray[i] >= j && newColorCode2dArray[i] < j + 1)
				{
					colorCodeBins[j] += 1;
				}
			}
		}
		
		for (int i = 0; i < colorCodeBins.length; i++)
		{
			colorCodeMatrix[imageCount - 1][i] = colorCodeBins[i];
		}
	}

	// This method writes the contents of the intensity matrix to a file called
	// intensity.txt
	public void writeIntensity()
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter("intensity.txt"));
			for (int i = 0; i < intensityMatrix.length; i++)
			{
				bw.write((i + 1) + " " + imageSize[i] + " ");
				
				for (int j = 0; j < intensityMatrix[i].length; j++)
				{
					bw.write(intensityMatrix[i][j] + " ");
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// This method writes the contents of the colorCode matrix to a file named
	// colorCodes.txt.
	public void writeColorCode()
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter("colorCodes.txt"));
			for (int i = 0; i < colorCodeMatrix.length; i++)
			{
				bw.write((i + 1) + " " + imageSize[i] + " ");
				
				for (int j = 0; j < colorCodeMatrix[i].length; j++)
				{
					bw.write(colorCodeMatrix[i][j] + " ");
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException
	{
		new ColorHistogram();
	}

}
