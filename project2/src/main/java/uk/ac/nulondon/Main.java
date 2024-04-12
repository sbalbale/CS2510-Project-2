package uk.ac.nulondon;

public class Main {
    /**
     * The main method of the application.
     * It creates an Image object from a file path and prints the color of each pixel in the image.
     * If an exception occurs during the creation of the Image object or during the printing of the image,
     * the exception is caught and its stack trace is printed.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            Image image = new Image("src/main/resources/8x8 Images/beach.png");
            image.calculateEnergies();
            image.printImage();
            image.exportImage("newImg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
