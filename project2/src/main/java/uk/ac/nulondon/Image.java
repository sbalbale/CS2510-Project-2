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
     * The image is read into a graph structure where each node (pixel) keeps track of only its left and right neighbors.
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
     * The method takes a string parameter outputFilePath which is the path where the output image will be saved.
     * It creates a new BufferedImage object with a size of 8x8 pixels and a type of BufferedImage.TYPE_INT_RGB.
     * It then iterates over each row and column of the image, retrieves the color of the pixel and sets the RGB value of the corresponding pixel in the BufferedImage object.
     * Finally, it writes the BufferedImage object to the output file. If an IOException occurs during this process, the exception is caught and its stack trace is printed.
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
            File output = new File("Output/" + outputFilePath + ".png");
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
            Pixel chaser = pixel;
            while (chaser != null) {
                System.out.print(chaser.toString() + " ");
//                System.out.print("{ Brightness: " + chaser.getBrightness() + " Energy: " + chaser.getEnergy() + " } ");
                chaser = chaser.getRight();
            }
            System.out.println();
        }
    }

    /**
     * Calculates the brightness of a given Pixel.
     * The brightness is calculated as the average of the red, green, and blue color components.
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
        System.out.println("Brightness: " + brightness);
        if (brightness != pixel.getBrightness()) {
            pixel.setBrightness(brightness);
        }
        return brightness;
    }

    /**
     * Calculates the energy of a given Pixel.
     * The energy is calculated based on the brightness of the Pixel and its neighbors.
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
        System.out.println("HorizEnergy: " + horizEnergy);
        System.out.println("VertEnergy: " + vertEnergy);
        System.out.println("Energy: " + energy);
        if (energy != pixel.getEnergy()) {
            pixel.setEnergy(energy);
        }
    }

    /**
     * Calculates the energies of all Pixels in the Image.
     * This method iterates over all Pixels in the Image and calls the energy method on each Pixel.
     */
    public void calculateEnergies() {
        for (Pixel pixel : firstColumn) {
            Pixel chaser = pixel;
            while (chaser != null) {
                energy(chaser);
                chaser = chaser.getRight();
            }
        }
    }


//    function findBluestSeam() finds the seam with the most amount of blue pixels
//    it checks finds the blue values of each pixel in the first column and then finds the pixel with the highest blue value
//    it then moves to the next row and finds the pixel who when added to the previous pixel either above, up and to the left or up and to the right has the highest blue value
//    it continues this process until it reaches the last row and then traces back to find the seam with the most blue pixels
//    it saves the bluest value of the pixel in the pixel object and the seams path in a list of integers.
//    it returns an array of integers representing the indices of the pixels in the seam
    public int[] findBluestSeam(){
        Pixel bluestPixel = null;
        int maxBlue = 0;
        for(Pixel pixel : firstColumn){
            if(pixel.getColor().getBlue() > maxBlue){
                maxBlue = pixel.getColor().getBlue();
                bluestPixel = pixel;
            }
        }
        Pixel chaser = bluestPixel;
        int[] seam = new int[8];
        seam[0] = bluestPixel.getX();
        for(int i = 1; i < 8; i++){
            int x = chaser.getX();
            int y = chaser.getY();
            int bluest = 0;
            Pixel next = null;
            if(y != 0){
                Pixel a = firstColumn.get(y - 1);
                while(x != a.getX()){
                    a = a.getRight();
                }
                if(a.getColor().getBlue() > bluest){
                    bluest = a.getColor().getBlue();
                    next = a;
                }
            }
            Pixel b = chaser.getLeft();
            if(b != null && b.getColor().getBlue() > bluest){
                bluest = b.getColor().getBlue();
                next = b;
            }
            Pixel c = chaser.getRight();
            if(c != null && c.getColor().getBlue() > bluest){
                bluest = c.getColor().getBlue();
                next = c;
            }
            seam[i] = next.getX();
            chaser = next;
        }
        return seam;

    }






}
