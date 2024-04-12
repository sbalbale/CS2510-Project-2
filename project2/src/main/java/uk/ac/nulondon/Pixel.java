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
     * The blueness of the Pixel.
     */
    private int blueness = -1;

    /**
     * The energy of the Pixel.
     */
    private int energy = -1;

    /**
     * The brightness of the Pixel.
     */
    private int brightness = -1;

    /**
     * The x-coordinate of the Pixel.
     */
    private int x;

    /**
     * The y-coordinate of the Pixel.
     */
    private int y;

    /**
     * Constructs a new Pixel with the specified color.
     * The left and right neighbors are initially set to null.
     *
     * @param rgb The color of the Pixel.
     */
    public Pixel(int x, int y, Color rgb) {
        this.x = x;
        this.y = y;
        this.color = rgb;
        this.left = null;
        this.right = null;
    }

    /**
     * This method returns the x-coordinate of the Pixel.
     *
     * @return The x-coordinate of the Pixel.
     */
    public int getX() {
        return x;
    }
    /**
     * This method returns the y-coordinate of the Pixel.
     *
     * @return The y-coordinate of the Pixel.
     */
    public int getY() {
        return y;
    }

    /**
     * This method sets the color of the Pixel.
     *
     * @param color The color to set.
     */
    public void setColor(Color color) {
        this.color = color;
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
                "loc=(" + x + "," + y + ")" +
                ", color=(R:" + color.getRed()  + ", G:" + color.getGreen() + ", B:" + color.getBlue() + ")" +
                ", brightness=" + brightness +
                ", energy=" + energy +
                '}';
    }

    /**
     * Sets the brightness of this Pixel.
     *
     * @param brightness The brightness value to set.
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    /**
     * Returns the brightness of this Pixel.
     *
     * @return The brightness of this Pixel.
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Sets the energy of this Pixel.
     *
     * @param energy The energy value to set.
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * Returns the energy of this Pixel.
     *
     * @return The energy of this Pixel.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the blueness of this Pixel.
     *
     * @param blueness The blueness value to set.
     */
    public void setBlueness(int blueness) {
        this.blueness = blueness;
    }

    /**
     * Returns the blueness of this Pixel.
     *
     * @return The blueness of this Pixel.
     */
    public int getBlueness() {
        return blueness;
    }


}
