package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Image class represents an image as a graph structure where each node
 * (pixel) keeps track of only its left and right neighbors.
 * The image stores a list of the first column of pixels.
 */
public class Image {
    /**
     * The file path of the image.
     */
    private String filePath = "";

    /**
     * The width of the image.
     */
    private int width;

    /**
     * The height of the image.
     */
    private int height;

    /**
     * A list of the first column of pixels in the image.
     * Each pixel in this list is the start of a row in the image.
     */
    private ArrayList<Pixel> firstColumn = new ArrayList<>();

    /**
     * This is a 2D ArrayList that stores the removed seams from the image.
     * Each ArrayList<Pixel> inside the outer ArrayList represents a seam that has been removed.
     * A seam is a connected path of pixels in the image from top to bottom or from left to right.
     * The Pixel objects in the inner ArrayLists are the pixels that were removed from the image when the corresponding seam was removed.
     */
    private ArrayList<ArrayList<Pixel>> removedSeams = new ArrayList<>();

    /**
     * Constructs a new Image from the specified file path.
     * The image is read into a graph structure where each node (pixel) keeps track
     * of only its left and right neighbors.
     * The image stores a list of the first column of pixels.
     *
     * @param filePath The path of the image file.
     * @throws IOException If an error occurs while reading the image file.
     */
    public Image(String filePath) throws IOException {
        // Create a new File object from the provided file path
        File input = new File(filePath);
        // Read the image from the file
        BufferedImage img = ImageIO.read(input);
        // Set the width of the image
        this.width = img.getWidth();
        // Set the height of the image
        this.height = img.getHeight();
        // Store the file path of the image
        this.filePath = filePath;
        // Declare a Pixel object to keep track of the current pixel
        Pixel chaser = null;
        // Print the width and height of the image to the console
        // System.out.println("Width: " + width + " Height: " + height);
        // Loop over each row of pixels in the image
        for (int row = 0; row < height; row++) {
            // Loop over each column of pixels in the image
            for (int col = 0; col < width; col++) {
                // Get the color of the current pixel
                Color color = new Color(img.getRGB(col, row));
                // Create a new Pixel object for the current pixel
                Pixel pixel = new Pixel(col, row, color);
                // If the current pixel is in the first column, add it to the firstColumn list
                if (col == 0) {
                    firstColumn.add(pixel);
                } else {
                    // Otherwise, set the right neighbor of the previous pixel to the current pixel
                    // and set the left neighbor of the current pixel to the previous pixel
                    chaser.setRight(pixel);
                    pixel.setLeft(chaser);
                }
                // Set the current pixel as the previous pixel for the next iteration
                chaser = pixel;
            }
        }
        // Update the energy and blueness values of the pixels in the image
        updateValues();
    }

    /**
     * Exports the image represented by this Image object to a file.
     * The method takes a string parameter outputFilePath which is the path where
     * the output image will be saved.
     * It creates a new BufferedImage object with a size of 8x8 pixels and a type of
     * BufferedImage.TYPE_INT_RGB.
     * It then iterates over each row and column of the image, retrieves the color
     * of the pixel and sets the RGB value of the corresponding pixel in the
     * BufferedImage object.
     * Finally, it writes the BufferedImage object to the output file. If an
     * IOException occurs during this process, the exception is caught and its stack
     * trace is printed.
     *
     * @param outputFilePath The path where the output image will be saved.
     */
    public void exportImage(String outputFilePath) {
        // Create a new BufferedImage object with the width and height of the image
        BufferedImage newImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        try {
            // Loop over each row of pixels in the image
            for (int row = 0; row < this.height; row++) {
                // Get the first pixel in the current row
                Pixel pixel = firstColumn.get(row);
                // Loop over each column of pixels in the image
                for (int col = 0; col < this.width; col++) {
                    // Set the color of the current pixel in the new image
                    newImage.setRGB(col, row, pixel.getColor().getRGB());
                    // Move to the next pixel in the row
                    pixel = pixel.getRight();
                }
            }
            // Create a new File object for the output file
            File output = new File(filePath.split("src")[0] + "Output\\" + outputFilePath + ".png");

            // Write the new image to the output file
            ImageIO.write(newImage, "png", output);
        // Catch any IOExceptions that occur and print the stack trace
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Prints the image to the console.
     * It iterates over each pixel in the first column of the image, and for each pixel, 
     * it prints the pixel and all its right neighbors in the same row.
     * Each pixel is printed using its toString method, followed by a space.
     * After all pixels in a row have been printed, it prints a new line.
     */
    public void printImage() {
        // Loop over each pixel in the first column of the image
        for (Pixel pixel : firstColumn) {
            // While the current pixel is not null
            while (pixel != null) {
                // Print the string representation of the current pixel to the console
                System.out.print(pixel.toString() + " ");
                // Move to the next pixel in the row
                pixel = pixel.getRight();
            }
            // Print a newline to the console
            System.out.println();
        }
    }
    
    /**
     * Calculates the brightness of a given Pixel.
     * The brightness is calculated as the average of the red, green, and blue color
     * components.
     * The calculated brightness is then set as the brightness of the Pixel.
     *
     * @param pixel The Pixel whose brightness is to be calculated.
     * @return The calculated brightness.
     */
    private int calculateBrightness(Pixel pixel) {
        // Get the red component of the pixel's color
        int r = pixel.getColor().getRed();
        // Get the green component of the pixel's color
        int g = pixel.getColor().getGreen();
        // Get the blue component of the pixel's color
        int b = pixel.getColor().getBlue();
        // Calculate the brightness of the pixel as the average of the red, green, and blue components
        int brightness = (r + g + b) / 3;
        // Return the calculated brightness
        return brightness;
    }

    /**
     * Calculates the brightness of each pixel in the image.
     * It iterates over each pixel in the first column of the image, and for each pixel, 
     * it calculates the brightness and sets the pixel's brightness to the calculated value.
     * The brightness is calculated using the calculateBrightness method.
     */
    public void imageCalculateBrightness() {
        // Loop over each pixel in the first column of the image
        for (Pixel pixel : firstColumn) {
            // While the current pixel is not null
            while (pixel != null) {
                // Calculate the brightness of the current pixel
                int brightness = calculateBrightness(pixel);
                // Set the brightness of the current pixel
                pixel.setBrightness(brightness);
                // Move to the next pixel in the row
                pixel = pixel.getRight();
            }
        }
    }

    /**
     * Calculates the energy of a pixel in an image. The energy of a pixel is a measure of its importance in the image.
     * The energy is calculated based on the brightness of the pixel and its eight neighboring pixels.
     * This method is used in the context of seam carving, an image resizing technique that removes seams from an image.
     *
     * @param pixelE The current pixel for which the energy is being calculated.
     * @param pixelB The pixel above the current pixel.
     * @param pixelH The pixel below the current pixel.
     * @return The energy of the current pixel, calculated as the square root of the sum of the squares of the horizontal and vertical energy.
     */
    private int calculateEnergy(Pixel pixelE, Pixel pixelB, Pixel pixelH) {
        // Get the x and y coordinates and the brightness of the current pixel
        int x = pixelE.getX();
        int y = pixelE.getY();
        int brE = pixelE.getBrightness();      

        // Identify the eight neighboring pixels of the current pixel
        Pixel pixelA = y > 0 && x > 0 ? pixelB.getLeft() : pixelE;
        Pixel pixelC = y > 0 ? pixelB.getRight() : pixelE;
        Pixel pixelD = x > 0 ? pixelE.getLeft() : pixelE;
        Pixel pixelF = pixelE.getRight();
        Pixel pixelG = y < firstColumn.size() - 1 && x > 0 ? pixelH.getLeft() : pixelE;
        Pixel pixelI = y < firstColumn.size() - 1 ? pixelH.getRight() : pixelE;

        // Get the brightness of each neighboring pixel. If a neighboring pixel is null, use the brightness of the current pixel
        int brA = pixelA != null ? pixelA.getBrightness() : brE;
        int brB = pixelB != null ? pixelB.getBrightness() : brE;
        int brC = pixelC != null ? pixelC.getBrightness() : brE;
        int brD = pixelD != null ? pixelD.getBrightness() : brE;
        int brF = pixelF != null ? pixelF.getBrightness() : brE;
        int brG = pixelG != null ? pixelG.getBrightness() : brE;
        int brH = pixelH != null ? pixelH.getBrightness() : brE;
        int brI = pixelI != null ? pixelI.getBrightness() : brE;

        // Calculate the horizontal and vertical energy of the current pixel
        int horizEnergy = (brA + 2 * brD + brG) - (brC + 2 * brF + brI);
        int vertEnergy = (brA + 2 * brB + brC) - (brG + 2 * brH + brI);

        // Return the total energy of the current pixel
        return (int) Math.sqrt(horizEnergy * horizEnergy + vertEnergy * vertEnergy);
    }

    /**
     * Calculates the energies of all Pixels in the Image.
     * This method iterates over all Pixels in the Image and calls the energy method
     * on each Pixel.
     */
    public void imageCalculateEnergy() {
        // Loop over each pixel in the first column of the image
        for (int i = 0; i < firstColumn.size(); i++) {
            // Get the current pixel, and the pixels directly above and below it
            Pixel pixelE = firstColumn.get(i);
            Pixel pixelB = i > 0 ? firstColumn.get(i - 1) : pixelE;
            Pixel pixelH = i < firstColumn.size() - 1 ? firstColumn.get(i + 1) : pixelE;
            // While the current pixel is not null
            while (pixelE != null) {
                // Calculate the energy of the current pixel
                int energy = calculateEnergy(pixelE, pixelB, pixelH);
                // Set the energy of the current pixel
                pixelE.setEnergy(energy);
                // Move to the next pixel in the row
                pixelE = pixelE.getRight();
                pixelB = pixelB.getRight();
                pixelH = pixelH.getRight();
            }
        }
    }

    /**
     * Calculates the blueness of a given pixel.
     *
     * This method retrieves the blue component of the pixel's color and returns it
     * as the pixel's blueness.
     * The blueness is an integer between 0 (no blue) and 255 (maximum blue).
     *
     * @param pixel the pixel for which to calculate the blueness
     * @return the blueness of the pixel
     */
    private int calculateBlueness(Pixel pixel) {
        // Get the blue component of the pixel's color
        int blueness = pixel.getColor().getBlue();
        // Return the blue component
        return blueness;
    }

    /**
     * Calculates and sets the blueness of all pixels in the image.
     *
     * This method iterates over the pixels in the first column of the image, and
     * for each pixel, it calculates the blueness
     * and sets it as the pixel's blueness. It then moves to the right and repeats
     * the process for the next pixel, until it
     * has processed all pixels in the image.
     */
    public void imageCalculateBluenesses() {
        // Loop over each pixel in the first column of the image
        for (Pixel pixel : firstColumn) {
            // While the current pixel is not null
            while (pixel != null) {
                // Calculate the blueness of the current pixel
                int blueness = calculateBlueness(pixel);
                // Set the blueness of the current pixel
                pixel.setBlueness(blueness);
                // Move to the next pixel in the row
                pixel = pixel.getRight();
            }
        }
    }

    /**
     * Finds the bluest seam in the image.
     *
     * This method uses dynamic programming to find the seam with the maximum total blueness.
     * It first initializes the first row of the dynamic programming table with the blueness of each pixel in the first row.
     * Then, for each remaining row, it calculates the maximum total blueness that can be achieved by selecting a pixel in that row,
     * given that the pixel in the previous row that was selected is either the pixel to the left, the pixel directly above, or the pixel to the right.
     * It keeps track of the seam that achieves the maximum total blueness for each pixel.
     * Finally, it returns the seam that achieves the maximum total blueness in the last row.
     *
     * @return An ArrayList of Pixel objects representing the bluest seam in the image.
     */
    public ArrayList<Pixel> findBluestSeam() {
        // Get the height of the image
        int height = firstColumn.size();
        // Initialize an ArrayList to store the pixels in the bluest seam
        ArrayList<Pixel> seam = new ArrayList<>();
        // Initialize an array to store the blueness values of the previous row of pixels
        int[] previousValues = new int[height];
        // Initialize a 2D array to store the pixels in the bluest seam up to each pixel in the previous row
        Pixel[][] previousSeams = new Pixel[height][height];

        // Initialize the first row of pixels
        Pixel pixel = firstColumn.get(0);
        int i = 0;
        while (pixel != null) {
            // Set the blueness value of the current pixel in the previousValues array
            previousValues[i] = pixel.getBlueness();
            // Set the current pixel as the bluest seam up to itself in the previousSeams array
            previousSeams[i][0] = pixel;
            // Move to the next pixel in the row
            pixel = pixel.getRight();
            i++;
        }

        // Process the remaining rows of pixels
        for (int y = 1; y < height; y++) {
            pixel = firstColumn.get(y);
            int[] currentValues = new int[i];
            Pixel[][] currentSeams = new Pixel[i][height];
            int x = 0;
            while (pixel != null) {
                // Calculate the maximum blueness value of the three pixels above the current pixel
                int left = x > 0 ? previousValues[x - 1] : -1;
                int middle = x < previousValues.length ? previousValues[x]: -1;
                int right = x < i - 1 ? previousValues[x + 1] : -1;
                int max = Math.max(Math.max(left, middle), right);
                // Add the blueness value of the current pixel to the maximum blueness value
                currentValues[x] = pixel.getBlueness() + max;

                // Update the bluest seam up to the current pixel
                if (max == left) {
                    System.arraycopy(previousSeams[x - 1], 0, currentSeams[x], 0, y);
                } else if (max == middle) {
                    System.arraycopy(previousSeams[x], 0, currentSeams[x], 0, y);
                } else {
                    System.arraycopy(previousSeams[x + 1], 0, currentSeams[x], 0, y);
                }
                currentSeams[x][y] = pixel;
                // Update the blueness value of the current pixel
                pixel.setBlueness(currentValues[x]);
                // Move to the next pixel in the row
                pixel = pixel.getRight();
                x++;
            }

            // Prepare for the next row
            previousValues = currentValues;
            previousSeams = currentSeams;
        }

        // Find the bluest seam in the last row of pixels
        int maxIndex = indexOfLargest(previousValues);
        Pixel[] pixelSeam = previousSeams[maxIndex];
        // Add the pixels in the bluest seam to the seam ArrayList
        seam.addAll(Arrays.asList(pixelSeam));
        // Return the seam ArrayList
        return seam;
    }

    /**
     * Finds the seam with the lowest total energy in the image.
     *
     * This method uses dynamic programming to find the seam with the lowest total energy.
     * It first initializes the first row of the dynamic programming table with the energy of each pixel in the first row.
     * Then, for each remaining row, it calculates the minimum total energy that can be achieved by selecting a pixel in that row,
     * given that the pixel in the previous row that was selected is either the pixel to the left, the pixel directly above, or the pixel to the right.
     * It keeps track of the seam that achieves the minimum total energy for each pixel.
     * Finally, it returns the seam that achieves the minimum total energy in the last row.
     *
     * @return An ArrayList of Pixel objects representing the seam with the lowest total energy in the image.
     */
    public ArrayList<Pixel> findLowestEnergySeam() {
        // Get the height of the image
        int height = firstColumn.size();
        // Initialize an ArrayList to store the pixels in the lowest energy seam
        ArrayList<Pixel> seam = new ArrayList<>();
        // Initialize an array to store the energy values of the previous row of pixels
        int[] previousValues = new int[height];
        // Initialize a 2D array to store the pixels in the lowest energy seam up to each pixel in the previous row
        Pixel[][] previousSeams = new Pixel[height][height];
    
        // Initialize the first row of pixels
        Pixel pixel = firstColumn.get(0);
        int i = 0;
        while (pixel != null) {
            // Set the energy value of the current pixel in the previousValues array
            previousValues[i] = pixel.getEnergy();
            // Set the current pixel as the lowest energy seam up to itself in the previousSeams array
            previousSeams[i][0] = pixel;
            // Move to the next pixel in the row
            pixel = pixel.getRight();
            i++;
        }
    
        // Process the remaining rows of pixels
        for (int y = 1; y < height; y++) {
            pixel = firstColumn.get(y);
            int[] currentValues = new int[i];
            Pixel[][] currentSeams = new Pixel[i][height];
            int x = 0;
            while (pixel != null) {
                // Calculate the total energy for all possible seams ending at this pixel
                int minEnergy = Integer.MAX_VALUE;
                for (int dx = -1; dx <= 1; dx++) {
                    if (x + dx >= 0 && x + dx < previousValues.length) {
                        int energy = pixel.getEnergy() + previousValues[x + dx];
                        if (energy < minEnergy) {
                            minEnergy = energy;
                            // Copy the seam from the previous row
                            System.arraycopy(previousSeams[x + dx], 0, currentSeams[x], 0, y);
                        }
                    }
                }
                currentValues[x] = minEnergy;
                currentSeams[x][y] = pixel;
                // Move to the next pixel in the row
                pixel = pixel.getRight();
                x++;
            }
    
            // Prepare for the next row
            previousValues = currentValues;
            previousSeams = currentSeams;
        }
    
        // Find the lowest energy seam in the last row of pixels
        int minIndex = indexOfSmallest(previousValues);
        Pixel[] pixelSeam = previousSeams[minIndex];
        // Add the pixels in the lowest energy seam to the seam ArrayList
        seam.addAll(Arrays.asList(pixelSeam));
        // Return the seam ArrayList
        return seam;
    }

    /**
     * Highlights a given seam in the image with a specified color.
     *
     * @param seam an ArrayList of Pixel objects representing the seam to be highlighted.
     * @param color the color to use for highlighting the seam.
     */
    public void highlightSeam(ArrayList<Pixel> seam, Color color) {
        // Loop over each pixel in the seam
        for (Pixel pixel : seam) {
            // If the current pixel is not null
            if (pixel != null) {
                // Set the color of the current pixel to the specified color
                pixel.setColor(color);
            }
        }
    }
    
    /**
     * Removes a given seam from the image and stores the removed pixels.
     *
     * @param seam an ArrayList of Pixel objects representing the seam to be removed.
     */
    public void removeSeam(ArrayList<Pixel> seam) {
        // Initialize an ArrayList to store the pixels that will be removed
        ArrayList<Pixel> removedPixels = new ArrayList<>();
        // Loop over each pixel in the seam
        for (Pixel pixel : seam) {
            // If the current pixel is not null
            if (pixel != null) {
                // Add the current pixel to the removedPixels ArrayList
                removedPixels.add(pixel);
                // Remove the current pixel from the image
                removePixel(pixel);
            }
        }
        // Decrease the width of the image by 1
        this.width--;
        // Add the removedPixels ArrayList to the removedSeams ArrayList
        removedSeams.add(removedPixels);
    }

    /**
     * Inserts a given seam into the image.
     *
     * @param seam an ArrayList of Pixel objects representing the seam to be inserted.
     */
    public void insertSeam(ArrayList<Pixel> seam) {
        // Loop over each pixel in the seam
        for (int y = 0; y < seam.size(); y++) {
            // Get the current pixel
            Pixel pixel = seam.get(y);
            // If the current pixel is not null
            if (pixel != null) {
                // Insert the current pixel into the image
                insertPixel(pixel);
            }
        }
        // Increase the width of the image by 1
        this.width++;
        // Remove the last seam from the removedSeams ArrayList
        removedSeams.remove(removedSeams.size() - 1);
    }
    
    /**
     * Inserts a pixel into the image.
     *
     * This method inserts a pixel into the image by updating the left and right neighbors of the pixel
     * to point to the new pixel, effectively inserting the pixel into the linked list that represents the image.
     * If the pixel being inserted is the first pixel in its row (i.e., it's in the firstColumn list),
     * the method updates the firstColumn list to point to the new pixel.
     *
     * @param pixel The pixel to be inserted. If the pixel is null, the method does nothing.
     */
    private void insertPixel(Pixel pixel) {
        // If the pixel to be inserted is not null
        if (pixel != null) {
            // Get the pixel to the left of the current pixel
            Pixel left = pixel.getLeft();
            // Get the pixel to the right of the current pixel
            Pixel right = pixel.getRight();
            // Set the left pixel of the current pixel
            pixel.setLeft(left);
            // Set the right pixel of the current pixel
            pixel.setRight(right);
            // If there is a pixel to the left of the current pixel
            if (left != null) {
                // Set the right pixel of the left pixel to the current pixel
                left.setRight(pixel);
            }
            // If there is a pixel to the right of the current pixel
            if (right != null) {
                // Set the left pixel of the right pixel to the current pixel
                right.setLeft(pixel);
            }
            // If the right pixel is the first pixel in the column
            if (right == firstColumn.get(pixel.getY())) {
                // Set the first pixel in the column to the current pixel
                firstColumn.set(pixel.getY(), pixel);
            }
            // Set the color of the current pixel to its initial color
            pixel.setColor(pixel.getInitialColor());
        }
    }

    /**
     * Removes a pixel from the image.
     *
     * This method removes a pixel from the image by updating the left and right neighbors of the pixel
     * to point to each other, effectively removing the pixel from the linked list that represents the image.
     * If the pixel being removed is the first pixel in its row (i.e., it's in the firstColumn list),
     * the method updates the firstColumn list to point to the right neighbor of the pixel.
     *
     * @param pixel The pixel to be removed. If the pixel is null, the method does nothing.
     */
    private void removePixel(Pixel pixel) {
        // If the pixel to be removed is not null
        if (pixel != null) {
            // Get the pixel to the left of the current pixel
            Pixel left = pixel.getLeft();
            // Get the pixel to the right of the current pixel
            Pixel right = pixel.getRight();
            // If there is a pixel to the left of the current pixel
            if (left != null) {
                // Set the right pixel of the left pixel to the right pixel of the current pixel
                left.setRight(right);
            }
            // If there is a pixel to the right of the current pixel
            if (right != null) {
                // Set the left pixel of the right pixel to the left pixel of the current pixel
                right.setLeft(left);
            }
            // If the current pixel is the first pixel in its column
            if (pixel == firstColumn.get(pixel.getY())) {
                // Set the first pixel in the column to the right pixel of the current pixel
                firstColumn.set(pixel.getY(), right);
            }
        }
    }

    /**
     * Returns the last seam that was removed from the image.
     *
     * @return an ArrayList of Pixel objects representing the last seam that was removed. 
     *         If no seams have been removed, this method returns null.
     */
    public ArrayList<Pixel> getLastRemovedSeam() {
        // Check if the removedSeams list is empty
        if (removedSeams.isEmpty()) {
            // If it is, return null
            return null;
        }
        // Otherwise, return the last seam that was removed from the image
        return removedSeams.get(removedSeams.size() - 1);
    }

    /**
     * Updates the values of brightness, energy, and blueness for each pixel in the image.
     * It first calculates the brightness of each pixel using the imageCalculateBrightness method,
     * then calculates the energy of each pixel using the imageCalculateEnergy method,
     * and finally calculates the blueness of each pixel using the imageCalculateBluenesses method.
     */
    public void updateValues() {
        // Calculate the brightness of each pixel in the image
        imageCalculateBrightness();
        // Calculate the energy of each pixel in the image
        imageCalculateEnergy();
        // Calculate the blueness of each pixel in the image
        imageCalculateBluenesses();
    }

    /**
     * UTILITY FUNCTIONS
     * THESE FUNCTIONS ARE USED TO HELP IMPLEMENT AND BUG FIX THE MAIN FUNCTIONS
     * 
     * 
     */

    /**
     * Returns the index of the largest value in the given array.
     *
     * This function iterates over the array, keeping track of the maximum value
     * found so far and its index.
     * If it finds a value that is larger than the current maximum, it updates the
     * maximum and its index.
     * After iterating over the entire array, it returns the index of the maximum
     * value.
     *
     * @param array the array to search
     * @return the index of the largest value in the array
     */
    private static int indexOfLargest(int[] array) {
        // Initialize the index of the maximum value to 0
        int maxIndex = 0;
        // Loop over each element in the array starting from the second element
        for (int i = 1; i < array.length; i++) {
            // If the current element is greater than the maximum value
            if (array[i] > array[maxIndex]) {
                // Update the index of the maximum value to the current index
                maxIndex = i;
            }
        }
        // Return the index of the maximum value
        return maxIndex;
    }

    /**
     * Returns the index of the smallest value in the given array.
     *
     * This function iterates over the array, keeping track of the minimum value
     * found so far and its index.
     * If it finds a value that is smaller than or equal to the current minimum, it
     * updates the minimum and its index.
     * After iterating over the entire array, it returns the index of the minimum
     * value.
     *
     * If the array is empty, it returns -1.
     *
     * @param array the array to search
     * @return the index of the smallest value in the array, or -1 if the array is
     *         empty
     */
    private static int indexOfSmallest(int[] array) {
        // Check if the array is empty
        if (array.length == 0)
            // If it is, return -1
            return -1;
        // Initialize the index of the minimum value to 0
        int index = 0;
        // Initialize the minimum value to the first element in the array
        int min = array[index];
        // Loop over each element in the array starting from the second element
        for (int i = 1; i < array.length; i++) {
            // If the current element is less than or equal to the minimum value
            if (array[i] <= min) {
                // Update the minimum value to the current element
                min = array[i];
                // Update the index of the minimum value to the current index
                index = i;
            }
        }
        // Return the index of the minimum value
        return index;
    }

    /**
     * Prints the elements of the given array.
     *
     * This function iterates over the array and prints each element followed by a
     * space.
     * After printing all elements, it prints a newline.
     *
     * This function is useful for debugging, as it allows you to easily inspect the
     * contents of an array.
     *
     * @param array the array to print
     */
    public void printArray(int[] array) {
        // Loop over each element in the array
        for (int i = 0; i < array.length; i++) {
            // Print the current element followed by a space
            System.out.print(array[i] + " ");
        }
        // Print a newline
        System.out.println();
    }

    /**
     * Prints the elements of the given 2D array.
     *
     * This function iterates over the rows and columns of the array and prints each
     * element followed by a space.
     * After printing all elements in a row, it prints a newline.
     *
     * This function is useful for debugging, as it allows you to easily inspect the
     * contents of a 2D array.
     *
     * @param array the 2D array to print
     */
    public void printArray(int[][] array) {
        // Loop over each row in the array
        for (int i = 0; i < array.length; i++) {
            // Loop over each column in the current row
            for (int j = 0; j < array[i].length; j++) {
                // Print the current element followed by a space
                System.out.print(array[i][j] + " ");
            }
            // Print a newline to move to the next row
            System.out.println();
        }
    }

    /**
     * Prints the coordinates of the pixels in the given seam.
     *
     * This method iterates over the pixels in the given seam and prints their
     * coordinates in the format (x,y). The coordinates are separated by spaces.
     * After all the coordinates have been printed, it prints a newline.
     *
     * @param seam an ArrayList of Pixel objects representing the seam to be printed.
     */
    public void printSeam(ArrayList<Pixel> seam) {
        // Loop over each pixel in the seam
        for (Pixel pixel : seam) {
            // If the current pixel is not null
            if (pixel != null){
                // Print the coordinates of the current pixel in the format (x,y)
                System.out.print("(" + pixel.getX() + "," + pixel.getY() + ") ");
            }
        }
        // Print a newline
        System.out.println();
    }
}