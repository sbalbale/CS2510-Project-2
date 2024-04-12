package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Image class represents an image as a graph structure where each node (pixel) keeps track of only its left and right neighbors.
 * The image stores a list of the first column of pixels.
 */
public class Image {
    /**
     * The file path of the image.
     */
    public String filePath = "";

    /**
     * A list of the first column of pixels in the image.
     * Each pixel in this list is the start of a row in the image.
     */
    private ArrayList<Pixel> firstColumn = new ArrayList<>();

    /**
     * Constructs a new Image from the specified file path.
     * The image is read into a graph structure where each node (pixel) keeps track of only its left and right neighbors.
     * The image stores a list of the first column of pixels.
     *
     * @param filePath The path of the image file.
     * @throws IOException If an error occurs while reading the image file.
     */
    public Image(String filePath) throws IOException {
        File input = new File(filePath);
        BufferedImage img = ImageIO.read(input);
        int width = img.getWidth();
        int height = img.getHeight();
        this.filePath = filePath;
        Pixel chaser = null;

        System.out.println("Width: " + width + " Height: " + height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = new Color(img.getRGB(col, row));
                Pixel pixel = new Pixel(color);
                System.out.println("Pixel: " + pixel);
                System.out.println("Chaser: " + chaser);
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
     * This method prints the color of each pixel in the image.
     * It iterates over the first column of pixels and for each pixel,
     * it traverses to the right, printing the color of each pixel until
     * it reaches a null pixel (indicating the end of the row).
     */
    public void printImage() {
        for (Pixel pixel : firstColumn) {
            Pixel chaser = pixel;
            while (chaser != null) {
                System.out.print(chaser.getColor() + " ");
                chaser = chaser.getRight();
            }
            System.out.println();
        }
    }
}
