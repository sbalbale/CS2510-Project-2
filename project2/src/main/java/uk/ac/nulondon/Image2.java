package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class Image2 {
//    create a constructor that takes a file path and reads the image to a adjacency list representation using maps and linked lists
    private int nodeCount;
    Map<Integer, List<Pixel>> image;

    public String filePath = "";

    public Image2(String filePath) throws IOException {
        File input = new File(filePath);
        BufferedImage img = ImageIO.read(input);
        int width = img.getWidth();
        int height = img.getHeight();
        this.filePath = filePath;
        this.nodeCount = width * height;
        image = new HashMap<>();

        // initialize the adjacency list map
        // with node values from 0 to v
        for (int i = 0; i < nodeCount; i++) {
            image.put(i, new LinkedList<>());
        }
        System.out.println("Width: " + width + " Height: " + height);

        for (int row = 0; row < height; row++) {
            ArrayList<Pixel> rowArray = new ArrayList<>();
            this.image.add(rowArray);
            for (int col = 0; col < width; col++) {
                int pixel = img.getRGB(col, row);
                Color originalColor = new Color(pixel);
                int red = originalColor.getRed();
                int green = originalColor.getGreen();
                int blue = originalColor.getBlue();
                this.image.get(row).add(new Pixel(red, green, blue));
            }
        }
    }
}
