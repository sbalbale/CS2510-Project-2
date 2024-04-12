package uk.ac.nulondon;

import java.awt.*;

public class Pixel {
    private Color color = null;
    private Pixel left = null;
    private Pixel right = null;



    public Pixel(Color rgb) {
//      where a pixel keeps track of only its left and right neighbors
//      and our image stores a list of the first column of pixels.
        this.color = rgb;
        this.left = null;
        this.right = null;
    }

    public Color getColor() {
        return color;
    }
    public Pixel getLeft() {
        return left;
    }
    public Pixel getRight() {
        return right;
    }
    public void setLeft(Pixel left) {
        this.left = left;
    }
    public void setRight(Pixel right) {
        this.right = right;
    }

    public String toString() {
        return "Pixel{" +
                "color=" + color +
                '}';
    }

}
