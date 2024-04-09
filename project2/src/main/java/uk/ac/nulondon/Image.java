package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Image {

    private ArrayList<ArrayList<ArrayList<Integer>>> image = new ArrayList<ArrayList<ArrayList<Integer>>>();
    public int count = 0;
    public int undoCount = 0;
    public int operationCount = 0;
    public int selectedColumn = -1;
    public String filePath = "";



    public Image(String filePath) throws IOException {
        // Constructor
        // This is where you would open the file and read the image then turn the image into a 2d array of tuples
        File input = new File(filePath);  // Create file object
        BufferedImage img = ImageIO.read(input);  // Read image from file
        int width = img.getWidth();  // Get width of image
        int height = img.getHeight();  // Get height of image
        this.filePath = filePath;
        System.out.println("Width: " + width + " Height: " + height);  // Print width and height of image



        // This loops through the image pixel by pixel
        // we will read in the original color then change
        // the color and store into new buffer
        for (int row = 0; row < height; row++) {
            ArrayList<ArrayList<Integer>> rowArray = new ArrayList<ArrayList<Integer>>();
            this.image.add(rowArray);
            for (int col = 0; col < width; col++) {
//                System.out.println("Reading pixel at: " + row + " " + col);

                // Retrieving contents of a pixel
                int pixel = img.getRGB(col, row);

                // Creating a Color object from pixel value
                Color originalColor = new Color(pixel);

                // Retrieving the Red Green and Blue values
                int red = originalColor.getRed();
                int green = originalColor.getGreen();
                int blue = originalColor.getBlue();

                // Alter the color and then store in new image buffer
                ArrayList<Integer> pixelArray = new ArrayList<Integer>();
                pixelArray.add(red);
                pixelArray.add(green);
                pixelArray.add(blue);

                this.image.get(row).add(pixelArray);
            }
        }
    }

    public int randomColumn() {
//        pick random column
        Random rand = new Random();
        return rand.nextInt(this.image.get(0).size());
    }

    public void printImage() {
        // This is where you would print the image to the console
        for (int row = 0; row < this.image.size(); row++) {
            for (int col = 0; col < this.image.get(row).size(); col++) {
                System.out.print(this.image.get(row).get(col));
            }
            System.out.println();
        }
    }

    public int findBluestColumn() {
        // This is where you would find the bluest column in the image
        int bluestColumn = 0;
        int bluestColumnValue = 0;
        for (int col = 0; col < this.image.get(0).size(); col++) {
            int columnValue = 0;
            for (int row = 0; row < this.image.size(); row++) {
                columnValue += this.image.get(row).get(col).get(2);
            }
            if (columnValue > bluestColumnValue) {
                bluestColumnValue = columnValue;
                bluestColumn = col;
            }
        }
        return bluestColumn;
    }

    public void changeColumnColor(int column, int red, int green, int blue) {
        // This is where you would change the color of a column
        for (int row = 0; row < this.image.size(); row++) {
            for (int col = 0; col < this.image.get(row).size(); col++) {
                if (col == column) {
                    this.image.get(row).get(col).set(0, red);
                    this.image.get(row).get(col).set(1, green);
                    this.image.get(row).get(col).set(2, blue);
                }
            }
        }
    }

    public void removeColumn(int column) {
        // This is where you would remove a column from the image
        for (int row = 0; row < this.image.size(); row++) {
            this.image.get(row).remove(column);
        }
    }

    public void saveImage(String filePath) throws IOException {
        ++this.count;
        // This is where you would save the image to a file
        int width = this.image.get(0).size();
        int height = this.image.size();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int red = this.image.get(row).get(col).get(0);
                int green = this.image.get(row).get(col).get(1);
                int blue = this.image.get(row).get(col).get(2);
                Color newColor = new Color(red, green, blue);
                newImage.setRGB(col, row, newColor.getRGB());
            }
        }
        File output = new File("Output/" + filePath + ".png");
        ImageIO.write(newImage, "png", output);
    }

    public void undo() throws IOException {
//        if (this.operationCount == 0) {
//            System.out.println("No operations to undo");
//            return;
//        }
        // This is where you would undo the last operation
        this.undoCount++;
//        Using d calculations to get the correct image
//        System.out.println("count: " + this.count + " undoCount: " + this.undoCount + " calculated count: " + (this.count-(2*this.undoCount)-this.undoCount));
//        Image img = new Image("Output/tempIMG_0" + (this.count-(2*this.undoCount)-this.undoCount) + ".png");

//        Using confirm calculations
        if((this.count-(2*this.undoCount)-this.undoCount) == -1){
            Image img = new Image(this.filePath);
            this.image = img.image;
        }
        else{
            Image img = new Image("Output/tempIMG_0" + (this.count-(2*this.undoCount)-this.undoCount) + ".png");
            this.image = img.image;
        }


    }
    public void backOne() throws IOException {
        // This is where you would undo the last operation
        if (this.count - 2 < 0){
            Image img = new Image(this.filePath);
            this.image = img.image;
        }
        else{
            Image img = new Image("Output/tempIMG_0" + (this.count-2) + ".png");
            this.image = img.image;
        }


    }

}