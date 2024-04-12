package uk.ac.nulondon;

public class Main {
    public static void main(String[] args) {
        try {
            Image image = new Image("src/main/resources/8x8 Images/beach.png");
            image.printImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
