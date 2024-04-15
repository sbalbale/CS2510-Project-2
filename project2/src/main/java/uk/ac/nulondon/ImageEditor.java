package uk.ac.nulondon;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;

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
     * Constructor for the ImageEditor class.
     *
     * @param image The Image object that this ImageEditor will edit.
     */
    public ImageEditor(Image image) {
        // Assign the provided Image object to the image field of this ImageEditor instance
        this.image = image;
    }

    /**
     * Undoes the highlighting of a seam in the image.
     *
     * This method removes the currently highlighted seam from the image, 
     * retrieves the last removed seam from the image, 
     * and then inserts this last removed seam back into the image. 
     * It then sets the currently highlighted seam to null. 
     * If there is a last removed seam in the image, it sets the last removed seam to null.
     */
    public void undoHighlightedSeam() {
        // Remove the currently highlighted seam from the image
        image.removeSeam(highlightedSeam);
        // Retrieve the last removed seam from the image
        lastRemovedSeam = image.getLastRemovedSeam();
        // Insert the last removed seam back into the image
        image.insertSeam(lastRemovedSeam);
        // Set the currently highlighted seam to null
        highlightedSeam = null;
        // If there is a last removed seam in the image, set the last removed seam to null
        lastRemovedSeam = image.getLastRemovedSeam() == null ? null : image.getLastRemovedSeam();
        image.updateValues();
    }

    /**
     * Starts the image editor.
     *
     * This method provides a command-line interface for editing the image.
     * It continuously prompts the user for commands until the user enters 'q' to quit.
     * The available commands are:
     * - 'b': Find the bluest seam in the image and highlight it in blue.
     * - 'e': Find the seam with the lowest energy in the image and highlight it in red.
     * - 'd': Delete the currently highlighted seam from the image.
     * - 'u': Undo the last seam deletion.
     * - 'q': Quit the editor and save the current state of the image.
     *
     * After each command, the method updates the image values, prints the image to the console,
     * and exports the image to a temporary file.
     */
    public void startEditor() {
        // Create a new Scanner object for reading user input
        Scanner scanner = new Scanner(System.in);
        // Declare a String variable for storing the user's command
        String input;
        // Declare and initialize a counter for the temporary images
        int tempImgCount = 0;

        // Start an infinite loop for the command-line interface
        while (true) {
            // Update the energy and blueness values of the pixels in the image
            image.updateValues();
            // Print the image to the console
            // image.printImage();
            // Print a separator line
            System.out.println("---------------------------------");
            // Print the command menu
            System.out.println("Edit image:");
            System.out.println("b: Find bluest seam");
            System.out.println("e: Find lowest energy seam");
            System.out.println("d: Delete highlighted seam");
            System.out.println("u: Undo last seam deletion");
            System.out.println("q: Quit");
            System.out.println("Enter command: ");
            // Read the user's command
            input = scanner.nextLine();

            // Process the user's command
            switch (input) {
                case "b":
                    // If there is a currently highlighted seam, undo the highlighting
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    // Find the bluest seam in the image and highlight it
                    highlightedSeam = image.findBluestSeam();
                    image.highlightSeam(highlightedSeam, Color.BLUE);
                    // Export the image to a temporary file
                    image.exportImage("tempIMG_" + tempImgCount);
                    // Increment the counter for the temporary images
                    tempImgCount++;
                    // Print the bluest seam to the console
                    // image.printSeam(highlightedSeam);
                    // Print a message to the console
                    System.out.println("Bluest seam found. Press 'd' to delete the seam.");
                    break;
                case "e":
                    // If there is a currently highlighted seam, undo the highlighting
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    // Find the seam with the lowest energy in the image and highlight it
                    highlightedSeam = image.findLowestEnergySeam();
                    image.highlightSeam(highlightedSeam, Color.RED);
                    // Export the image to a temporary file
                    image.exportImage("tempIMG_" + tempImgCount);
                    // Increment the counter for the temporary images
                    tempImgCount++;
                    // Print the seam with the lowest energy to the console
                    // image.printSeam(highlightedSeam);
                    // Print a message to the console
                    System.out.println("Lowest energy seam found. Press 'd' to delete the seam.");
                    break;
                case "d":
                    // If there is a currently highlighted seam, delete it
                    if (highlightedSeam != null) {
                        image.removeSeam(highlightedSeam);
                        lastRemovedSeam = image.getLastRemovedSeam();
                        highlightedSeam = null;
                        // Export the image to a temporary file
                        image.exportImage("tempIMG_" + tempImgCount);
                        // Increment the counter for the temporary images
                        tempImgCount++;
                    } else {
                        // If there is no currently highlighted seam, print a message to the console
                        System.out.println("No seam highlighted. Please highlight a seam before deleting.");
                    }
                    break;
                case "u":
                    // If there is a currently highlighted seam, undo the highlighting
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    // If there is a last removed seam, undo the removal
                    if (lastRemovedSeam != null) {
//                        image.printSeam(lastRemovedSeam);
                        image.insertSeam(lastRemovedSeam);
                        lastRemovedSeam = image.getLastRemovedSeam() == null ? null : image.getLastRemovedSeam();
                        // Export the image to a temporary file
                        image.exportImage("tempIMG_" + tempImgCount);
                        // Increment the counter for the temporary images
                        tempImgCount++;
                    } else {
                        // If there is no last removed seam, print a message to the console
                        System.out.println("No seam to undo. Please delete a seam before undoing.");
                    }
                    break;
                case "q":
                    // If there is a currently highlighted seam, undo the highlighting
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    // Export the image to a new file
                    image.exportImage("newImg");
                    // Close the Scanner object
                    scanner.close();
                    // Exit the method
                    return;
                default:
                    // If the user's command is not recognized, print a message to the console
                    if (highlightedSeam != null) {
                        undoHighlightedSeam();
                    }
                    System.out.println("Invalid command. Please enter a valid command.");
                    break;
            }
        }
    }
}
