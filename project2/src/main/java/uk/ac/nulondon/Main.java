package uk.ac.nulondon;

public class Main {
    /**
     * The main entry point for the application.
     *
     * This method handles any exceptions that may occur during the execution of the application.
     * It creates a new Image object from the specified file, creates a new ImageEditor object with the created Image object,
     * starts the image editor, and prints the image to the console.
     * If any exceptions occur during this process, it catches them and prints the stack trace.
     *
     * @param args The command-line arguments. This parameter is not used in this method.
     */
    public static void main(String[] args) {
        // Use a try-catch block to handle any exceptions that may occur
        try {
            // Create a new Image object from the specified file
            Image image = new Image("project2\\src\\main\\resources\\SampleImages\\beach.png");
            // Create a new ImageEditor object with the created Image object
            ImageEditor imageEditor = new ImageEditor(image);
            // Start the image editor
            imageEditor.startEditor();
            // Print the image to the console
            image.printImage();

        // Catch any exceptions that occur and print the stack trace
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
