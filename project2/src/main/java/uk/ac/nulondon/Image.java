package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    public int br(Pixel pixel) {
        int r = pixel.getColor().getRed();
        int g = pixel.getColor().getGreen();
        int b = pixel.getColor().getBlue();
        int brightness = (r + g + b) / 3;
        // System.out.println("Brightness: " + brightness);
        if (brightness != pixel.getBrightness()) {
            pixel.setBrightness(brightness);
        }
        return brightness;
    }

    /**
     * Calculates the energy of a given Pixel.
     * The energy is calculated based on the brightness of the Pixel and its
     * neighbors.
     * The calculated energy is then set as the energy of the Pixel.
     *
     * @param pixel The Pixel whose energy is to be calculated.
     */
    public void energy(Pixel pixel) {
        int brE = br(pixel);
        int brA = brE;
        int brB = brE;
        int brC = brE;
        int brD = brE;
        int brF = brE;
        int brG = brE;
        int brH = brE;
        int brI = brE;
        int x = pixel.getX();
        int y = pixel.getY();
        if (y != 0) {
            Pixel b = firstColumn.get(y - 1);
            brB = br(b);
            while (x != b.getX()) {
                b = b.getRight();
            }
            Pixel a = b.getLeft();
            Pixel c = b.getRight();
            if (a != null) {
                brA = br(a);
            }
            if (c != null) {
                brB = br(b);
            }
        }
        Pixel e = pixel;
        Pixel d = e.getLeft();
        Pixel f = e.getRight();
        if (d != null) {
            brD = br(d);
        }
        if (f != null) {
            brF = br(f);
        }
        if (y != 7) {
            Pixel h = firstColumn.get(y + 1);
            brH = br(h);
            while (x != h.getX()) {
                h = h.getRight();
            }
            Pixel g = h.getLeft();
            Pixel i = h.getRight();
            if (g != null) {
                brG = br(g);
            }
            if (i != null) {
                brI = br(i);
            }
        }
        int horizEnergy = (brA + 2 * brD + brG) - (brC + 2 * brF + brI);
        int vertEnergy = (brA + 2 * brB + brC) - (brG + 2 * brH + brI);
        int energy = (int) Math.sqrt(Math.pow(horizEnergy, 2) + Math.pow(vertEnergy, 2));
        // System.out.println("HorizEnergy: " + horizEnergy);
        // System.out.println("VertEnergy: " + vertEnergy);
        // System.out.println("Energy: " + energy);
        if (energy != pixel.getEnergy()) {
            pixel.setEnergy(energy);
        }
    }

    /**
     * Calculates the energies of all Pixels in the Image.
     * This method iterates over all Pixels in the Image and calls the energy method
     * on each Pixel.
     */
    public void calculateEnergies() {
        for (Pixel pixel : firstColumn) {
            while (pixel != null) {
                energy(pixel);
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