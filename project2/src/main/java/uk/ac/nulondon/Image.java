package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Image {
//    create a constructor that takes a file path and reads the image to graph structure
//    where a node keeps track of only its left and right neighbors and our image stores a list of the first column of pixels.
//    information should be kept in a pixel class.

    public String filePath = "";


    private ArrayList<Pixel> firstColumn = new ArrayList<>();

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
