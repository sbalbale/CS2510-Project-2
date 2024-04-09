package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.sqrt;

public class Image {

    private ArrayList<ArrayList<Pixel>> image = new ArrayList<>();
    public int count = 0;
    public int undoCount = 0;
    public int operationCount = 0;
    public int selectedColumn = -1;
    public String filePath = "";

    public Image(String filePath) throws IOException {
        File input = new File(filePath);
        BufferedImage img = ImageIO.read(input);
        int width = img.getWidth();
        int height = img.getHeight();
        this.filePath = filePath;
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

    public int randomColumn() {
//        pick random column
        Random rand = new Random();
        return rand.nextInt(this.image.get(0).size());
    }

    public void printImage() {
        for (ArrayList<Pixel> row : this.image) {
            for (Pixel pixel : row) {
                System.out.print(pixel.toString());
            }
            System.out.println();
        }
    }

    public int findBluestColumn() {
        int bluestColumn = 0;
        int bluestColumnValue = 0;
        for (int col = 0; col < this.image.get(0).size(); col++) {
            int columnValue = 0;
            for (ArrayList<Pixel> row : this.image) {
                columnValue += row.get(col).getBlue();
            }
            if (columnValue > bluestColumnValue) {
                bluestColumnValue = columnValue;
                bluestColumn = col;
            }
        }
        return bluestColumn;
    }

    public void changeColumnColor(int column, int red, int green, int blue) {
        for (ArrayList<Pixel> row : this.image) {
            Pixel pixel = row.get(column);
            pixel.setRed(red);
            pixel.setGreen(green);
            pixel.setBlue(blue);
        }
    }

    public void removeColumn(int column) {
        for (ArrayList<Pixel> row : this.image) {
            row.remove(column);
        }
    }

    public void saveImage(String filePath) throws IOException {
        ++this.count;
        int width = this.image.get(0).size();
        int height = this.image.size();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel pixel = this.image.get(row).get(col);
                Color newColor = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                newImage.setRGB(col, row, newColor.getRGB());
            }
        }
        File output = new File("Output/" + filePath + ".png");
        ImageIO.write(newImage, "png", output);
    }

    public void undo() throws IOException {
        this.undoCount++;
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
        if (this.count - 2 < 0){
            Image img = new Image(this.filePath);
            this.image = img.image;
        }
        else{
            Image img = new Image("Output/tempIMG_0" + (this.count-2) + ".png");
            this.image = img.image;
        }
    }

//  The brightness of a Pixel is the average of the RGB values of the color of a
//  given pixel.
    public int brightness(Pixel pixel) {
        return (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;
    }

//    𝐻𝑜𝑟𝑖𝑧𝐸𝑛𝑒𝑟𝑔𝑦(𝐸) = (𝑏𝑟(𝐴) + 2𝑏𝑟(𝐷) + 𝑏𝑟(𝐺)) − (𝑏𝑟(𝐶) + 2𝑏𝑟(𝐹) + 𝑏𝑟(𝐼))
//    𝑉𝑒𝑟𝑡𝐸𝑛𝑒𝑟𝑔𝑦(𝐸) = (𝑏𝑟(𝐴) + 2𝑏𝑟(𝐵) + 𝑏𝑟(𝐶)) − (𝑏𝑟(𝐺) + 2𝑏𝑟(𝐻) + 𝑏𝑟(𝐼))
//    Energy(E) = sqrt(HorizontalEnergy(E)^2 + VerticalEnergy(E)^2)
    public double energy(int row, int col) {
        int width = this.image.get(0).size();
        int height = this.image.size();
        int left = (col - 1 + width) % width;
        int right = (col + 1) % width;
        int up = (row - 1 + height) % height;
        int down = (row + 1) % height;
        Pixel A = this.image.get(up).get(left);
        Pixel B = this.image.get(up).get(col);
        Pixel C = this.image.get(up).get(right);
        Pixel D = this.image.get(row).get(left);
        Pixel E = this.image.get(row).get(col);
        Pixel F = this.image.get(row).get(right);
        Pixel G = this.image.get(down).get(left);
        Pixel H = this.image.get(down).get(col);
        Pixel I = this.image.get(down).get(right);
        int horizontalEnergy = Math.abs(brightness(A) + 2 * brightness(D) + brightness(G) - brightness(C) - 2 * brightness(F) - brightness(I));
        int verticalEnergy = Math.abs(brightness(A) + 2 * brightness(B) + brightness(C) - brightness(G) - 2 * brightness(H) - brightness(I));
        return sqrt(horizontalEnergy * horizontalEnergy + verticalEnergy * verticalEnergy);
    }



}