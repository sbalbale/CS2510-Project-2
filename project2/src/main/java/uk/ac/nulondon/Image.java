package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The Image class represents an image as a graph structure where each node
 * (pixel) keeps track of only its left and right neighbors.
 * The image stores a list of the first column of pixels.
 */
public class Image {
    /**
     * The file path of the image.
     */
    public String filePath = "";
    public String outputFilePath = "newImg";

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
        File input = new File(filePath);
        BufferedImage img = ImageIO.read(input);
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.filePath = filePath;
        Pixel chaser = null;
        System.out.println("Width: " + width + " Height: " + height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = new Color(img.getRGB(col, row));
                Pixel pixel = new Pixel(col, row, color);
                if (col == 0) {
                    firstColumn.add(pixel);
                } else {
                    chaser.setRight(pixel);
                    pixel.setLeft(chaser);
                }
                chaser = pixel;
            }
        }
        imageCalculateBrightness();
        imageCalculateEnergy();
        imageCalculateBluenesses();
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
        this.outputFilePath = outputFilePath;
        BufferedImage newImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        try {
            for (int row = 0; row < 8; row++) {
                Pixel pixel = firstColumn.get(row);
                for (int col = 0; col < 8; col++) {
                    newImage.setRGB(col, row, pixel.getColor().getRGB());
                    pixel = pixel.getRight();
                }
            }
            File output = new File("project2\\Output\\" + outputFilePath + ".png");
            ImageIO.write(newImage, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method prints the color of each pixel in the image.
     * It iterates over the first column of pixels and for each pixel,
     * it traverses to the right, printing the color of each pixel until
     * it reaches a null pixel (indicating the end of the row).
     */
    public void printImage() {
        for (Pixel pixel : firstColumn) {
            while (pixel != null) {
                System.out.print(pixel.toString() + " ");
                // System.out.print("{ Brightness: " + pixel.getBrightness() + " Energy: " +
                // pixel.getEnergy() + " } ");
                // System.out.print("{ Blueness: " + pixel.getBlueness() + " } ");
                pixel = pixel.getRight();
            }
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
    public int calculateBrightness(Pixel pixel) {
        int r = pixel.getColor().getRed();
        int g = pixel.getColor().getGreen();
        int b = pixel.getColor().getBlue();
        int brightness = (r + g + b) / 3;
        return brightness;
    }

    public void imageCalculateBrightness() {
        for (Pixel pixel : firstColumn) {
            while (pixel != null) {
                int brightness = calculateBrightness(pixel);
                pixel.setBrightness(brightness);
                pixel = pixel.getRight();
            }
        }
    }

    /**
     * Calculates the energy of a given pixel.
     *
     * This method calculates the energy of a pixel based on the brightness of its
     * neighbors.
     * The brightness of a pixel is defined as the average of its RGB values.
     * The energy is calculated as the square root of the sum of the squares of the
     * horizontal and vertical energies.
     *
     * @param pixelE the pixel for which to calculate the energy
     * @return the energy of the pixel
     */
    public int calculateEnergy(Pixel pixelE) {
        int x = pixelE.getX();
        int y = pixelE.getY();
        int brE = pixelE.getBrightness();

        Pixel pixelA = y > 0 && x > 0 ? getPixelAt(firstColumn.get(y - 1), x - 1) : pixelE;
        Pixel pixelB = y > 0 ? getPixelAt(firstColumn.get(y - 1), x) : pixelE;
        Pixel pixelC = y > 0 ? getPixelAt(firstColumn.get(y - 1), x + 1) : pixelE;
        Pixel pixelD = x > 0 ? pixelE.getLeft() : pixelE;
        Pixel pixelF = getPixelAt(pixelE, x + 1);
        Pixel pixelG = y < firstColumn.size() - 1 && x > 0 ? getPixelAt(firstColumn.get(y + 1), x - 1) : pixelE;
        Pixel pixelH = y < firstColumn.size() - 1 ? getPixelAt(firstColumn.get(y + 1), x) : pixelE;
        Pixel pixelI = y < firstColumn.size() - 1 ? getPixelAt(firstColumn.get(y + 1), x + 1) : pixelE;

        int brA = pixelA != null ? pixelA.getBrightness() : brE;
        int brB = pixelB != null ? pixelB.getBrightness() : brE;
        int brC = pixelC != null ? pixelC.getBrightness() : brE;
        int brD = pixelD != null ? pixelD.getBrightness() : brE;
        int brF = pixelF != null ? pixelF.getBrightness() : brE;
        int brG = pixelG != null ? pixelG.getBrightness() : brE;
        int brH = pixelH != null ? pixelH.getBrightness() : brE;
        int brI = pixelI != null ? pixelI.getBrightness() : brE;

        int horizEnergy = (brA + 2 * brD + brG) - (brC + 2 * brF + brI);
        int vertEnergy = (brA + 2 * brB + brC) - (brG + 2 * brH + brI);

        return (int) Math.sqrt(horizEnergy * horizEnergy + vertEnergy * vertEnergy);
    }

    /**
     * Calculates the energies of all Pixels in the Image.
     * This method iterates over all Pixels in the Image and calls the energy method
     * on each Pixel.
     */
    public void imageCalculateEnergy() {
        for (Pixel pixel : firstColumn) {
            while (pixel != null) {
                int energy = calculateEnergy(pixel);
                pixel.setEnergy(energy);
                pixel = pixel.getRight();
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
    public int calculateBlueness(Pixel pixel) {
        int blueness = pixel.getColor().getBlue();
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
        for (Pixel pixel : firstColumn) {
            while (pixel != null) {
                int blueness = calculateBlueness(pixel);
                pixel.setBlueness(blueness);
                pixel = pixel.getRight();
            }
        }
    }

    /**
     * Finds the path from top to bottom with the maximum total blueness.
     *
     * This method uses dynamic programming to find the "bluest" seam in the image.
     * The blueness of a seam is defined as the sum of the blueness of all pixels in
     * the seam.
     *
     * @return an array representing the bluest seam. Each element is the
     *         x-coordinate of the pixel in the seam for the corresponding
     *         y-coordinate.
     */
    public int[] findBluestSeam() {
        int height = firstColumn.size();
        int[] seam = new int[height];
        int[] previousValues = new int[height];
        int[][] previousSeams = new int[height][height];

        // Initialize the first row
        Pixel pixel = firstColumn.get(0);
        int i = 0;
        while (pixel != null) {
            previousValues[i] = pixel.getBlueness();
            previousSeams[i][0] = i;
            pixel = pixel.getRight();
            i++;
        }

        // Process the remaining rows
        for (int y = 1; y < height; y++) {
            pixel = firstColumn.get(y);
            int[] currentValues = new int[i];
            int[][] currentSeams = new int[i][height];
            int x = 0;
            while (pixel != null) {
                int left = x > 0 ? previousValues[x - 1] : -1;
                int middle = previousValues[x];
                int right = x < i - 1 ? previousValues[x + 1] : -1;
                int max = Math.max(Math.max(left, middle), right);
                currentValues[x] = pixel.getBlueness() + max;
                if (max == left) {
                    System.arraycopy(previousSeams[x - 1], 0, currentSeams[x], 0, y);
                } else if (max == middle) {
                    System.arraycopy(previousSeams[x], 0, currentSeams[x], 0, y);
                } else {
                    System.arraycopy(previousSeams[x + 1], 0, currentSeams[x], 0, y);
                }
                currentSeams[x][y] = x;
                pixel.setBlueness(currentValues[x]);
                pixel = pixel.getRight();
                x++;
            }
            previousValues = currentValues;
            previousSeams = currentSeams;
        }

        // Find the bluest seam
        int maxIndex = indexOfLargest(previousValues);
        seam = previousSeams[maxIndex];
        return seam;
    }

    /**
     * Finds the path from top to bottom with the minimum total energy.
     *
     * This method uses dynamic programming to find the "lowest energy" seam in the
     * image.
     * The energy of a seam is defined as the sum of the energy of all pixels in the
     * seam.
     *
     * @return an array representing the lowest energy seam. Each element is the
     *         x-coordinate of the pixel in the seam for the corresponding
     *         y-coordinate.
     */
    public int[] findLowestEnergySeam() {
        int height = firstColumn.size();
        int[] seam = new int[height];
        int[] previousValues = new int[height];
        int[][] previousSeams = new int[height][height];

        // Initialize the first row
        Pixel pixel = firstColumn.get(0);
        int i = 0;
        while (pixel != null) {
            previousValues[i] = pixel.getEnergy();
            previousSeams[i][0] = i;
            pixel = pixel.getRight();
            i++;
        }

        // Process the remaining rows
        for (int y = 1; y < height; y++) {
            pixel = firstColumn.get(y);
            int[] currentValues = new int[i];
            int[][] currentSeams = new int[i][height];
            int x = 0;
            while (pixel != null) {
                int left = x > 0 ? previousValues[x - 1] : Integer.MAX_VALUE;
                int middle = previousValues[x];
                int right = x < i - 1 ? previousValues[x + 1] : Integer.MAX_VALUE;
                int min = Math.min(Math.min(left, middle), right);
                currentValues[x] = pixel.getEnergy() + min;

                // Update the seam for the current pixel
                if (min == left) {
                    System.arraycopy(previousSeams[x - 1], 0, currentSeams[x], 0, y);
                } else if (min == middle) {
                    System.arraycopy(previousSeams[x], 0, currentSeams[x], 0, y);
                } else {
                    System.arraycopy(previousSeams[x + 1], 0, currentSeams[x], 0, y);
                }
                currentSeams[x][y] = x;
                pixel.setEnergy(currentValues[x]);
                pixel = pixel.getRight();
                x++;
            }

            // Prepare for the next row
            previousValues = currentValues;
            previousSeams = currentSeams;
        }

        // Find the lowest energy seam
        int minIndex = indexOfSmallest(previousValues);
        seam = previousSeams[minIndex];

        return seam;
    }
    
    /**
     * Highlights a given seam in the image with a specified color.
     *
     * @param seam an array representing the seam to be highlighted. Each element is the
     *             x-coordinate of the pixel in the seam for the corresponding y-coordinate.
     * @param color the color to use for highlighting the seam.
     */
    public void highlightSeam(int[] seam, Color color) {
        for (int y = 0; y < seam.length; y++) {
            int x = seam[y];
            Pixel pixel = getPixelAt(firstColumn.get(y), x);
            if (pixel != null) {
                pixel.setColor(color);
            }
        }
    }
    
    /**
     * Gets the pixel at a specific x-coordinate.
     *
     * This method starts at a given pixel and moves to the right until it finds the
     * pixel with the specified x-coordinate.
     *
     * @param start the pixel to start at
     * @param x     the x-coordinate of the pixel to find
     * @return the pixel at the specified x-coordinate
     */
    private Pixel getPixelAt(Pixel start, int x) {
        Pixel pixel = start;
        while (pixel != null && pixel.getX() != x) {
            pixel = pixel.getRight();
        }
        return pixel;
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
    public static int indexOfLargest(int[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
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
    public static int indexOfSmallest(int[] array) {
        // add this
        if (array.length == 0)
            return -1;
        int index = 0;
        int min = array[index];
        for (int i = 1; i < array.length; i++) {
            if (array[i] <= min) {
                min = array[i];
                index = i;
            }
        }
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
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
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
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }
}