package uk.ac.nulondon;

import java.awt.*;

/**
 * The Pixel class represents a pixel in an image.
 * Each pixel has a color and references to its left and right neighbors.
 */
public class Pixel {
    /**
     * The color of the Pixel.
     */
    private Color color = null;

    /**
     * The left neighbor of the Pixel.
     */
    private Pixel left = null;

    /**
     * The right neighbor of the Pixel.
     */
    private Pixel right = null;

    /**
     * Constructs a new Pixel with the specified color.
     * The left and right neighbors are initially set to null.
     *
     * @param rgb The color of the Pixel.
     */
    public Pixel(Color rgb) {
        this.color = rgb;
        this.left = null;
        this.right = null;
    }

    /**
     * This method returns the color of the Pixel.
     *
     * @return The color of the Pixel.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This method returns the left neighbor of the Pixel.
     *
     * @return The left neighbor of the Pixel.
     */
    public Pixel getLeft() {
        return left;
    }

    /**
     * This method returns the right neighbor of the Pixel.
     *
     * @return The right neighbor of the Pixel.
     */
    public Pixel getRight() {
        return right;
    }

    /**
     * This method sets the left neighbor of this Pixel.
     *
     * @param left The Pixel that will be set as the left neighbor.
     */
    public void setLeft(Pixel left) {
        this.left = left;
    }

    /**
     * This method sets the right neighbor of this Pixel.
     *
     * @param right The Pixel that will be set as the right neighbor.
     */
    public void setRight(Pixel right) {
        this.right = right;
    }

    /**
     * This method provides a string representation of the Pixel object.
     * It returns a string that includes the class name and the color of the pixel.
     *
     * @return A string representation of the Pixel object.
     */
    public String toString() {
        return "Pixel{" +
                "color=" + color +
                '}';
    }

}
