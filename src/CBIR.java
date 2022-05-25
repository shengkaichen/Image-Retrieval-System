/*
* Project 2
*/

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CBIR extends JFrame
{
	private JLabel photographLabel = new JLabel(); // container to hold a large
	private JButton[] button; // creates an array of JButtons
	private JCheckBox[] checkBox;
	private JCheckBox chckbxRelevance;
	private GridLayout gridLayout1;
	private GridLayout gridLayout2;
	private JPanel bottomPanel;
	private JPanel topPanel;
	private JPanel buttonPanel;
	private JButton Next;
	private JButton Previous;
	private JLabel counterLabel;

	private int[] buttonOrder = new int[101]; // creates an array to keep up
												// with the image order
	private double[] imageSize = new double[101]; // keeps up with the image
	private double[][] intensityMatrix = new double[100][27];
	private double[][] colorCodeMatrix = new double[100][68];
	private double[] selectedPic = new double[30];
	int imageNo = 1;
	int imageCount = 1; // keeps up with the number of images displayed since
						// the first page.
	int pageNo = 1;
	boolean ManhattanDistance = true;


	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				CBIR app = new CBIR();
				app.setVisible(true);
			}
		});
	}

	public CBIR()
	{
		// The following lines set up the interface including the layout of the
		// buttons and JPanels.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Content-Based Image Retrieval System");

		topPanel = new JPanel();
		bottomPanel = new JPanel();
		buttonPanel = new JPanel();

		gridLayout1 = new GridLayout(4, 5, 5, 5);
		gridLayout2 = new GridLayout(2, 1, 5, 5);

		getContentPane().setLayout(gridLayout2);
		bottomPanel.setLayout(gridLayout1);
		getContentPane().add(topPanel);
		getContentPane().add(bottomPanel);

		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[] { 600, 0 };
		gbl_topPanel.rowHeights = new int[] { 354, 0 };
		gbl_topPanel.columnWeights = new double[] { 1.0, 1.0 };
		gbl_topPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		topPanel.setLayout(gbl_topPanel);

		// Display photo setting
		photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
		photographLabel.setHorizontalTextPosition(JLabel.LEFT);
		photographLabel.setHorizontalAlignment(JLabel.LEFT);
		photographLabel.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints gbc_photographLabel = new GridBagConstraints();
		gbc_photographLabel.insets = new Insets(0, 0, 0, 5);
		gbc_photographLabel.gridx = 0;
		gbc_photographLabel.gridy = 0;
		topPanel.add(photographLabel, gbc_photographLabel);

		GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
		gbc_buttonPanel.anchor = GridBagConstraints.WEST;
		gbc_buttonPanel.gridx = 1;
		gbc_buttonPanel.gridy = 0;
		topPanel.add(buttonPanel, gbc_buttonPanel);

		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[] { 155, 120, 120, 90, 0 };
		gbl_buttonPanel.rowHeights = new int[] { 0, 45, 45, 206, 33, 0 };
		gbl_buttonPanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_buttonPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		buttonPanel.setLayout(gbl_buttonPanel);

		JButton intensity = new JButton("Intensity");
		GridBagConstraints gbc_intensity = new GridBagConstraints();
		gbc_intensity.fill = GridBagConstraints.BOTH;
		gbc_intensity.insets = new Insets(0, 0, 5, 5);
		gbc_intensity.gridx = 0;
		gbc_intensity.gridy = 1;
		buttonPanel.add(intensity, gbc_intensity);
		intensity.addActionListener(new intensityHandler());

		JButton colorCode = new JButton("Color Code");
		GridBagConstraints gbc_colorCode = new GridBagConstraints();
		gbc_colorCode.fill = GridBagConstraints.BOTH;
		gbc_colorCode.insets = new Insets(0, 0, 5, 5);
		gbc_colorCode.gridx = 0;
		gbc_colorCode.gridy = 2;
		buttonPanel.add(colorCode, gbc_colorCode);
		colorCode.addActionListener(new colorCodeHandler());
		
		chckbxRelevance = new JCheckBox("Relevance");
		GridBagConstraints gbc_chckbxRelevance = new GridBagConstraints();
		gbc_chckbxRelevance.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxRelevance.gridx = 0;
		gbc_chckbxRelevance.gridy = 4;
		buttonPanel.add(chckbxRelevance, gbc_chckbxRelevance);
		chckbxRelevance.addItemListener(new chckbxRelevanceHandler());

		Previous = new JButton("Previous");
		GridBagConstraints gbc_Previous = new GridBagConstraints();
		gbc_Previous.fill = GridBagConstraints.BOTH;
		gbc_Previous.insets = new Insets(0, 0, 0, 5);
		gbc_Previous.gridx = 1;
		gbc_Previous.gridy = 4;
		buttonPanel.add(Previous, gbc_Previous);
		Previous.addActionListener(new previousPageHandler());

		Next = new JButton("Next");
		GridBagConstraints gbc_Next = new GridBagConstraints();
		gbc_Next.insets = new Insets(0, 0, 0, 5);
		gbc_Next.fill = GridBagConstraints.BOTH;
		gbc_Next.gridx = 2;
		gbc_Next.gridy = 4;
		buttonPanel.add(Next, gbc_Next);
		Next.addActionListener(new nextPageHandler());

		counterLabel = new JLabel("Page  " + pageNo + " of 5");
		GridBagConstraints gbc_counterLabel = new GridBagConstraints();
		gbc_counterLabel.gridx = 3;
		gbc_counterLabel.gridy = 4;
		buttonPanel.add(counterLabel, gbc_counterLabel);

		setSize(1100, 750);

		// this centers the frame on the screen
		setLocationRelativeTo(null);

		/*
		 * This for loop goes through the images in the database and stores them
		 * as icons and adds the images to JButtons and then to the JButton array
		 */
		button = new JButton[101];
		for (int i = 1; i < 101; i++)
		{
			ImageIcon icon = new ImageIcon(getClass().getResource("images/" + i + ".jpg"));
			if (icon != null)
			{
				button[i] = new JButton(new ImageIcon(
						icon.getImage().getScaledInstance(256, 100, java.awt.Image.SCALE_SMOOTH)));
				button[i].addActionListener(new IconButtonHandler(i, icon));
				buttonOrder[i] = i;
			}
		}
		
		checkBox = new JCheckBox[101];
		for (int i = 1; i < 101; i++)
		{
			checkBox[i] = new JCheckBox(" Relevance");
			checkBox[i].addItemListener(new chckbxHandler());
		}

		readIntensityFile();
		readColorCodeFile();
		displayFirstPage();
	}

	/*
	 * This method opens the intensity text file containing the intensity matrix
	 * with the histogram bin values for each image. The contents of the matrix
	 * are processed and stored in a two dimensional array called
	 * intensityMatrix.
	 */
	public void readIntensityFile()
	{
		try
		{
			Scanner read = new Scanner(new File("intensity.txt"));
			for (int i = 0; i < intensityMatrix.length; i++)
			{
				for (int j = 0; j < intensityMatrix[i].length; j++)
				{
					if (read.hasNextInt())
					{
						intensityMatrix[i][j] = read.nextInt();
					}
				}
			}
			read.close();
		} catch (FileNotFoundException EE)
		{
			System.out.println("The file intensity.txt does not exist");
		}
	}

	/*
	 * This method opens the color code text file containing the color code
	 * matrix with the histogram bin values for each image. The contents of the
	 * matrix are processed and stored in a two dimensional array called
	 * colorCodeMatrix.
	 */
	private void readColorCodeFile()
	{
		try
		{
			Scanner read = new Scanner(new File("colorCodes.txt"));
			for (int i = 0; i < colorCodeMatrix.length; i++)
			{
				for (int j = 0; j < colorCodeMatrix[i].length; j++)
				{
					if (read.hasNextInt())
					{
						colorCodeMatrix[i][j] = read.nextInt();
					}
				}
			}
			read.close();
		} catch (FileNotFoundException EE)
		{
			System.out.println("The file intensity.txt does not exist");
		}

	}

	/*
	 * This method displays the first twenty images in the panelBottom. The for
	 * loop starts at number one and gets the image number stored in the
	 * buttonOrder array and assigns the value to imageButNo. The button
	 * associated with the image is then added to panelBottom1. The for loop
	 * continues this process until twenty images are displayed in the
	 * panelBottom1
	 */
	private void displayFirstPage()
	{
		int imageButNo = 0;
		bottomPanel.removeAll();
		for (int i = 1; i < 21; i++)
		{
			imageButNo = buttonOrder[i];
			bottomPanel.add(button[imageButNo]);
			imageCount++;
		}
		bottomPanel.revalidate();
		bottomPanel.repaint();
		bottomPanel.getPreferredSize();
	}

	/*
	 * This class implements an ActionListener for each iconButton. When an icon
	 * button is clicked, the image on the the button is added to the
	 * photographLabel and the picNo is set to the image number selected and
	 * being displayed.
	 */
	private class IconButtonHandler implements ActionListener
	{
		int pNo = 0;
		ImageIcon iconUsed;

		IconButtonHandler(int i, ImageIcon j)
		{
			pNo = i;
			iconUsed = j; // sets the icon to the one used in the button
		}

		public void actionPerformed(ActionEvent e)
		{
			photographLabel.setIcon(iconUsed);
			imageNo = pNo;
		}
	}

	/*
	 * This class implements an ActionListener for the nextPageButton. The last
	 * image number to be displayed is set to the current image count plus 20.
	 * If the endImage number equals 101, then the next page button does not
	 * display any new images because there are only 100 images to be displayed.
	 * The first picture on the next page is the image located in the
	 * buttonOrder array at the imageCount
	 */
	private class nextPageHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int imageButNo = 0;
			int endImage = imageCount + 20;
			if (endImage <= 101)
			{
				bottomPanel.removeAll();
				for (int i = imageCount; i < endImage; i++)
				{
					imageButNo = buttonOrder[i];
					bottomPanel.add(button[imageButNo]);
					imageCount++;
				}
				bottomPanel.revalidate();
				bottomPanel.repaint();
			}
			if (e.getSource() == Next)
			{
				pageNo++;
				if (pageNo <= 5 && pageNo >= 1)
				{
					counterLabel.setText("Page  " + pageNo + " of 5");
				} else if (pageNo > 5)
				{
					pageNo = 5;
				} else if (pageNo < 1)
				{
					pageNo = 1;
				}
			}
		}

	}

	/*
	 * This class implements an ActionListener for the previousPageButton. The
	 * last image number to be displayed is set to the current image count minus
	 * 40. If the endImage number is less than 1, then the previous page button
	 * does not display any new images because the starting image is 1. The
	 * first picture on the next page is the image located in the buttonOrder
	 * array at the imageCount
	 */
	private class previousPageHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int imageButNo = 0;
			int startImage = imageCount - 40;
			int endImage = imageCount - 20;
			if (startImage >= 1)
			{
				bottomPanel.removeAll();
				/*
				 * The for loop goes through the buttonOrder array starting with
				 * the startImage value and retrieves the image at that place
				 * and then adds the button to the panelBottom1.
				 */
				for (int i = startImage; i < endImage; i++)
				{
					imageButNo = buttonOrder[i];
					bottomPanel.add(button[imageButNo]);
					imageCount--;
				}

				bottomPanel.revalidate();
				bottomPanel.repaint();
			}
			if (e.getSource() == Previous)
			{
				pageNo--;
				if (pageNo <= 5 && pageNo >= 1)
				{
					counterLabel.setText("Page  " + pageNo + " of 5");
				} else if (pageNo > 5)
				{
					pageNo = 5;
				} else if (pageNo < 1)
				{
					pageNo = 1;
				}
			}
		}

	}

	/*
	 * This class implements an ActionListener when the user selects the
	 * intensityHandler button. The image number that the user would like to
	 * find similar images for is stored in the variable pic. pic takes the
	 * image number associated with the image selected and subtracts one to
	 * account for the fact that the intensityMatrix starts with zero and not
	 * one. The size of the image is retrieved from the imageSize array. The
	 * selected image's intensity bin values are compared to all the other
	 * image's intensity bin values and a score is determined for how well the
	 * images compare. The images are then arranged from most similar to the
	 * least.
	 */
	private class intensityHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{		
			// Image size with picture number
			for(int i = 0; i < intensityMatrix.length; i++)
			{
				imageSize[i] = intensityMatrix[i][1];
			}
			
			double[] distance = new double[100];
			double[] newDistance = new double[100];
			int pic = (imageNo - 1);
			double picSize = imageSize[pic];
			int comImage = 0;
			double comImageSize = imageSize[comImage];
			int picNo[] = new int[100];
			double picNoAndDistance [][] = new double[100][2];
			double newPicNoAndDistance [][] = new double[100][2];
			
			if(Boolean.TRUE.equals(ManhattanDistance))
			{
				/*
				 * This for loop calculates Manhattan Distance.
				 */
				for(int i = 0; i < intensityMatrix.length; i++)
				{
					double sum = 0.0;
					for(int j = 2; j < intensityMatrix[i].length; j++)
					{
						double picIntensity = Math.round((
								intensityMatrix[pic][j] / picSize) * 10000.0) / 10000.0;
						double comIntensity = Math.round((
								intensityMatrix[comImage][j] / comImageSize) * 10000.0) / 10000.0;
						sum += Math.abs(picIntensity - comIntensity);
					}
					distance[i] = sum;
					comImage += 1;
					comImageSize = imageSize[comImage];
					picNo[i] = i + 1;
				}
				
				// Store each picture number and the difference with query image
				for(int i = 0; i < picNoAndDistance.length; i++)
				{
					picNoAndDistance[i][0] = distance[i];
					picNoAndDistance[i][1] = picNo[i];
				}
		        	        
		        // Ascending order of picNoAndDistance
				Arrays.sort(picNoAndDistance, new Comparator<double[]>()
				{
		            @Override
		            public int compare(double[] a, double[] b) {
		                return Double.compare(a[0], b[0]);
		            }
		        });
				
				// New order
		     	buttonOrder = new int[100];
		     	for(int i = 0; i < buttonOrder.length; i++)
				{
		     		buttonOrder[i] = (int) picNoAndDistance[i][1];
				}
		     	
				int imageButNo = 0;
				bottomPanel.removeAll();
				for (int i = 1; i < 21; i++)
				{
					// Add image
					imageButNo = buttonOrder[i];
					bottomPanel.add(button[imageButNo]);
					imageCount++;

					// Add check box
					bottomPanel.add(checkBox[imageButNo]);
					checkBox[imageButNo].setEnabled(false);
					checkBox[imageButNo].setVisible(false);
					checkBox[imageButNo].setVerticalAlignment(SwingConstants.BOTTOM);
				}
				bottomPanel.revalidate();
				bottomPanel.repaint();
			}
			else
			{
				/*
				 * Using Euclidean distance to calculate the distance
				 */
				RelevanceFeedback RelevanceFeedback = new RelevanceFeedback();
				double normalizedMatrix[][] = RelevanceFeedback.getNormalization(intensityMatrix, imageSize);
				double normalizedWeight[] = RelevanceFeedback.getNormalizedWeight(normalizedMatrix);
				
				for(int i = 0; i < normalizedMatrix.length; i++)
				{
					double sum = 0.0;
					for(int j = 0; j < normalizedMatrix[i].length; j++)
					{
						double picIntensity = Math.round((
								normalizedMatrix[pic][j] / picSize) * 10000.0) / 10000.0;
						double comIntensity = Math.round((
								normalizedMatrix[comImage][j] / comImageSize) * 10000.0) / 10000.0;
						sum += normalizedWeight[i] * Math.abs(picIntensity - comIntensity);
					}
					newDistance[i] = sum;
					comImage += 1;
					comImageSize = imageSize[comImage];
					picNo[i] = i + 1;
				}
				// Store each picture number and the difference with query image
				for(int i = 0; i < picNoAndDistance.length; i++)
				{
					newPicNoAndDistance[i][0] = newDistance[i];
					newPicNoAndDistance[i][1] = picNo[i];
				}
		        	        
		        // Ascending order of picNoAndDistance
				Arrays.sort(picNoAndDistance, new Comparator<double[]>()
				{
		            @Override
		            public int compare(double[] a, double[] b) {
		                return Double.compare(a[0], b[0]);
		            }
		        });
				
				// New order
		     	buttonOrder = new int[100];
		     	for(int i = 0; i < buttonOrder.length; i++)
				{
		     		buttonOrder[i] = (int) picNoAndDistance[i][1];
				}
		     	
				int imageButNo = 0;
				bottomPanel.removeAll();
				for (int i = 1; i < 21; i++)
				{
					// Add image
					imageButNo = buttonOrder[i];
					bottomPanel.add(button[imageButNo]);
					imageCount++;

					// Add check box
					bottomPanel.add(checkBox[imageButNo]);
					checkBox[imageButNo].setEnabled(false);
					checkBox[imageButNo].setVisible(false);
					checkBox[imageButNo].setVerticalAlignment(SwingConstants.BOTTOM);
				}
				bottomPanel.revalidate();
				bottomPanel.repaint();
			}
		}
	}

	/*
	 * This class implements an ActionListener when the user selects the
	 * colorCode button. The image number that the user would like to find
	 * similar images for is stored in the variable pic. pic takes the image
	 * number associated with the image selected and subtracts one to account
	 * for the fact that the intensityMatrix starts with zero and not one. The
	 * size of the image is retrieved from the imageSize array. The selected
	 * image's intensity bin values are compared to all the other image's
	 * intensity bin values and a score is determined for how well the images
	 * compare. The images are then arranged from most similar to the least.
	 */
	private class colorCodeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{		
			// Image size with picture number
			for(int i = 0; i < colorCodeMatrix.length; i++)
			{
				imageSize[i] = colorCodeMatrix[i][1];
			}
			
			double[] distance = new double[100];
			double[] newDistance = new double[100];
			int pic = (imageNo - 1);
			double picSize = imageSize[pic];
			int comImage = 0;
			double comImageSize = imageSize[comImage];
			int picNo[] = new int[100];
			double picNoAndDistance [][] = new double[100][2];
			double newPicNoAndDistance [][] = new double[100][2];
			
			if(Boolean.TRUE.equals(ManhattanDistance))
			{
				/*
				 * This for loop calculates Manhattan Distance.
				 */
				for(int i = 0; i < colorCodeMatrix.length; i++)
				{
					double sum = 0.0;
					for(int j = 2; j < colorCodeMatrix[i].length; j++)
					{
						double picIntensity = Math.round((
								colorCodeMatrix[pic][j] / picSize) * 10000.0) / 10000.0;
						double comIntensity = Math.round((
								colorCodeMatrix[comImage][j] / comImageSize) * 10000.0) / 10000.0;
						sum += Math.abs(picIntensity - comIntensity);
					}
					distance[i] = sum;
					comImage += 1;
					comImageSize = imageSize[comImage];
					picNo[i] = i + 1;
				}
				
				// Store each picture number and the difference with query image
				for(int i = 0; i < picNoAndDistance.length; i++)
				{
					picNoAndDistance[i][0] = distance[i];
					picNoAndDistance[i][1] = picNo[i];
				}
		        	        
		        // Ascending order of picNoAndDistance
				Arrays.sort(picNoAndDistance, new Comparator<double[]>()
				{
		            @Override
		            public int compare(double[] a, double[] b) {
		                return Double.compare(a[0], b[0]);
		            }
		        });
				
				// New order
		     	buttonOrder = new int[100];
		     	for(int i = 0; i < buttonOrder.length; i++)
				{
		     		buttonOrder[i] = (int) picNoAndDistance[i][1];
				}
		     	
				int imageButNo = 0;
				bottomPanel.removeAll();
				for (int i = 1; i < 21; i++)
				{
					// Add image
					imageButNo = buttonOrder[i];
					bottomPanel.add(button[imageButNo]);
					imageCount++;

					// Add check box
					bottomPanel.add(checkBox[imageButNo]);
					checkBox[imageButNo].setEnabled(false);
					checkBox[imageButNo].setVisible(false);
					checkBox[imageButNo].setVerticalAlignment(SwingConstants.BOTTOM);
				}
				bottomPanel.revalidate();
				bottomPanel.repaint();
			}
			else
			{
				/*
				 * Using Euclidean distance to calculate the distance
				 */
				RelevanceFeedback RelevanceFeedback = new RelevanceFeedback();
				double normalizedMatrix[][] = RelevanceFeedback.getNormalization(colorCodeMatrix, imageSize);
				double normalizedWeight[] = RelevanceFeedback.getNormalizedWeight(normalizedMatrix);
				
				for(int i = 0; i < normalizedMatrix.length; i++)
				{
					double sum = 0.0;
					for(int j = 0; j < normalizedMatrix[i].length; j++)
					{
						double picIntensity = Math.round((
								normalizedMatrix[pic][j] / picSize) * 10000.0) / 10000.0;
						double comIntensity = Math.round((
								normalizedMatrix[comImage][j] / comImageSize) * 10000.0) / 10000.0;
						sum += normalizedWeight[i] * Math.abs(picIntensity - comIntensity);
					}
					newDistance[i] = sum;
					comImage += 1;
					comImageSize = imageSize[comImage];
					picNo[i] = i + 1;
				}
				// Store each picture number and the difference with query image
				for(int i = 0; i < picNoAndDistance.length; i++)
				{
					newPicNoAndDistance[i][0] = newDistance[i];
					newPicNoAndDistance[i][1] = picNo[i];
				}
		        	        
		        // Ascending order of picNoAndDistance
				Arrays.sort(picNoAndDistance, new Comparator<double[]>()
				{
		            @Override
		            public int compare(double[] a, double[] b) {
		                return Double.compare(a[0], b[0]);
		            }
		        });
				
				// New order
		     	buttonOrder = new int[100];
		     	for(int i = 0; i < buttonOrder.length; i++)
				{
		     		buttonOrder[i] = (int) picNoAndDistance[i][1];
				}
		     	
				int imageButNo = 0;
				bottomPanel.removeAll();
				for (int i = 1; i < 21; i++)
				{
					// Add image
					imageButNo = buttonOrder[i];
					bottomPanel.add(button[imageButNo]);
					imageCount++;

					// Add check box
					bottomPanel.add(checkBox[imageButNo]);
					checkBox[imageButNo].setEnabled(false);
					checkBox[imageButNo].setVisible(false);
					checkBox[imageButNo].setVerticalAlignment(SwingConstants.BOTTOM);
				}
				bottomPanel.revalidate();
				bottomPanel.repaint();
			}
		}
	}
	
	private class chckbxRelevanceHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if(e.getStateChange() == ItemEvent.SELECTED) //checkbox has been selected
			{
				for(int i = 1; i < checkBox.length; i++)
				{
					checkBox[i].setEnabled(true);
					checkBox[i].setVisible(true);
					ManhattanDistance = false;
				}
	        }
			else //checkbox has been deselected
			{
				for(int i = 1; i < checkBox.length; i++)
				{
					checkBox[i].setEnabled(false);
					checkBox[i].setVisible(false);
					ManhattanDistance = true;
				}
	        };
		}
	}
	private class chckbxHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if(e.getStateChange() == ItemEvent.SELECTED) //checkbox has been selected
			{
				for(int i = 1; i < selectedPic.length; i++)
				{
					selectedPic[i] = buttonOrder[i];
				}
	        }
			else //checkbox has been deselected
			{
			    JOptionPane.showMessageDialog(null, e.toString(), "Please don't "
			    		+ "deselected any check box", JOptionPane.ERROR_MESSAGE);
	        };
		}
	}
	
}