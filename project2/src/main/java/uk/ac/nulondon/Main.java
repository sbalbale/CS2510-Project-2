package uk.ac.nulondon;

import java.awt.Color;
import java.util.Arrays;

public class Main {
    /**
     * The main method of the application.
     * It creates an Image object from a file path and prints the color of each
     * pixel in the image.
     * If an exception occurs during the creation of the Image object or during the
     * printing of the image,
     * the exception is caught and its stack trace is printed.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            Image image = new Image("project2\\src\\main\\resources\\SampleImages\\beach.png");
            ImageEditor imageEditor = new ImageEditor(image);
            imageEditor.start();
            // image.printImage();
            // image.imageCalculateBrightness();
            // image.imageCalculateEnergy();
            // image.imageCalculateBluenesses();
            // System.out.println();
            // image.printImage();
            // int[] highlightedSeam = image.findBluestSeam();
            // System.out.println(Arrays.toString(highlightedSeam));
            // image.highlightSeam(highlightedSeam, Color.BLUE);
            // image.exportImage("highlightBlueSeam");
            
            // // image.printImage();
            // System.out.println();
            // image.removeSeam(highlightedSeam);
            // image.exportImage("removeBlueSeam");
            // image.imageCalculateBrightness();
            // image.imageCalculateEnergy();
            // image.imageCalculateBluenesses();

            // // image.printImage();
            // System.out.println();
            // image.insertSeam(image.getLastRemovedSeam());
            // image.exportImage("insertBlueSeam");
            // image.imageCalculateBrightness();
            // image.imageCalculateEnergy();
            // image.imageCalculateBluenesses();

            // image.printImage();
            // System.out.println();
            // highlightedSeam = image.findLowestEnergySeam();
            // System.out.println(Arrays.toString(highlightedSeam));
            // image.highlightSeam(highlightedSeam, Color.RED);
            // image.exportImage("highlightRedSeam");

            // image.printImage();
            // System.out.println();
            // image.removeSeam(highlightedSeam);
            // image.exportImage("removeRedSeam");
            // image.imageCalculateBrightness();
            // image.imageCalculateEnergy();
            // image.imageCalculateBluenesses();

            // image.printImage();
            // System.out.println();
            // image.insertSeam(image.getLastRemovedSeam());
            // image.exportImage("insertRedSeam");
            // image.imageCalculateBrightness();
            // image.imageCalculateEnergy();
            // image.imageCalculateBluenesses();

            image.printImage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
