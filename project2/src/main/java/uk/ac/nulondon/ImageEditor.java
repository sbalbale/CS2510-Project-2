package uk.ac.nulondon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import java.awt.Color;
import java.lang.reflect.Array;

/**
 * The ImageEditor class provides a user interface for editing an image.
 * It stores the image to be edited, the currently highlighted seam in the image,
 * the last seam that was removed from the image, the output file path for the edited image,
 * and a count of the number of operations performed on the image.
 */
public class ImageEditor {
    /**
     * The image to be edited.
     */
    private Image image;

    /**
     * The currently highlighted seam in the image.
     * This is an array of integers representing the x-coordinates of the pixels in the seam.
     */
    private ArrayList<Pixel> highlightedSeam;

    /**
     * The last seam that was removed from the image.
     * This is an ArrayList of Pixel objects representing the pixels that were in the removed seam.
     */
    private ArrayList<Pixel> lastRemovedSeam;

    /**
     * The output file path for the edited image.
     * By default, it is set to "newImg", meaning the edited image will be saved as "newImg" in the project directory.
     */
    private String outputFilePath = "newImg";

    /**
     * The number of operations performed on the image.
     * This is incremented each time a seam is highlighted, a seam is removed, or a seam is reinserted.
     */
    private int operationCount = 0;

    /**
     * Constructor for the ImageEditor class.
     *
     * @param image The Image object that this ImageEditor will edit.
     */
    public ImageEditor(Image image) {
        this.image = image;
    }

    public void undoHighlightedSeam() {
        image.removeSeam(highlightedSeam);
        lastRemovedSeam = image.getLastRemovedSeam();
        image.insertSeam(lastRemovedSeam);
        highlightedSeam = null;
        if (image.getLastRemovedSeam() != null) {
            lastRemovedSeam = null;
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String input;
        int tempImgCount = 0;
    
        while (true) {
            image.updateValues();
            image.printImage();
            System.out.println("---------------------------------");
            System.out.println("Edit image:");
            System.out.println("b: Find bluest seam");
            System.out.println("e: Find lowest energy seam");
            System.out.println("d: Delete highlighted seam");
            System.out.println("u: Undo last seam deletion");
            System.out.println("q: Quit");
            System.out.println("Enter command: ");
            input = scanner.nextLine();
    
            switch (input) {
                case "b":
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    highlightedSeam = image.findBluestSeam();
                    image.highlightSeam(highlightedSeam, Color.BLUE);
                    image.exportImage("tempIMG_" + tempImgCount + ".png");
                    tempImgCount++;
                    image.printSeam(highlightedSeam);
                    System.out.println("Bluest seam found. Press 'd' to delete the seam.");
                    break;
                case "e":
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    highlightedSeam = image.findLowestEnergySeam();
                    image.highlightSeam(highlightedSeam, Color.RED);
                    image.exportImage("tempIMG_" + tempImgCount + ".png");
                    tempImgCount++;
                    image.printSeam(highlightedSeam);
                    System.out.println("Lowest energy seam found. Press 'd' to delete the seam.");
                    break;
                case "d":
                    if (highlightedSeam != null) {
                        image.removeSeam(highlightedSeam);
                        lastRemovedSeam = image.getLastRemovedSeam();
                        highlightedSeam = null;
                        image.exportImage("tempIMG_" + tempImgCount + ".png");
                        tempImgCount++;
                    } else {
                        System.out.println("No seam highlighted. Please highlight a seam before deleting.");
                    }
                    break;
                case "u":
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    if (lastRemovedSeam != null) {
                        image.insertSeam(lastRemovedSeam);
                        lastRemovedSeam = null;
                        image.exportImage("tempIMG_" + tempImgCount + ".png");
                        tempImgCount++;
                    } else {
                        System.out.println("No seam to undo. Please delete a seam before undoing.");
                    }
                    break;
                case "q":
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    image.exportImage("newImg.png");
                    return;
                default:
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    System.out.println("Invalid command. Please enter a valid command.");
                    break;
            }
        }
    }
}
