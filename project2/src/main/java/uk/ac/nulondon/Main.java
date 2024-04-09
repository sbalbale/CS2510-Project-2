package uk.ac.nulondon;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class Main {

    /**
     * Print the options for the user.
     */

    public static void printMenu() {
//        print options
        System.out.println("Please make a selection");
        System.out.println("b - remove the column with the most blue");
        System.out.println("r - remove a random column");
        System.out.println("u - undo the last operation");
//        System.out.println("d - confirm removal of the column");
        System.out.println("q - quit the program");
    }


    /**
     * Print a response to the user, given their selection
     * @param selection the value of the user's choice
     */
//    public static char printResponse(char selection) {
//        if (confirmResponse(selection)) {
//            switch (selection) {
//                case 'b':
//                    System.out.println("Removing the column with the most blue. \n");
//                    return 'b';
//                case 'r':
//                    System.out.println("Removing a random column. \n");
//                    return 'r';
//                case 'u':
//                    System.out.println("Undoing the last operation. \n");
//                    return 'u';
//                case 'd':
//                    System.out.println("Confirming removal. \n");
//                    return 'd';
//                case 'q':
//                    System.out.println("Quitting program. \n");
//                    return 'q';
//                default:
//                    System.out.println("That is not a valid option. \n");
//                    return 'a';
//            }
//        }
//        return 'a';
//    }

    public static boolean confirmResponse(char selection){
//        take user input
        Scanner scan = new Scanner(System.in);
        char choice;

//      give message for response based on selection
        String message = null;
        switch (selection) {
            case 'b':
                message = "remove the column with the most blue";
                break;
            case 'r':
                message = "remove a random column";
                break;
            case 'u':
                message = "undo the provious operation";
                break;
//            case 'd':
//                message = "remove the highlighted column";
//                break;
            case 'q':
                message = "quit the program";
                break;
            default:
                message = "do this";
                break;
        }

        System.out.println("Are you sure you want to " + message + "? (y/n)");
        // try and get user input, if input is ever invalid this will set choice to quit
        try {
            choice = scan.next().toLowerCase().charAt(0);
        } catch (InputMismatchException e) {
            System.out.println("Input should be a character");
            choice = 'n';
        }

        if(choice == 'y') {
            System.out.println("Thank you for confirming. \n");
            return true;
        }
        System.out.println("I will not do that. \n");
        return false;

    }


    public static int imageOperations(Image img, char choice) throws IOException {
//        do the image operations
//        if (confirmResponse(choice)) {
            switch (choice) {
//                remove the column with the most blue
                case 'b':
                    img.operationCount++;
                    System.out.println("Removing the column with the most blue. \n");
                    System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
//                find bluest column
                    img.selectedColumn = img.findBluestColumn();
//                    System.out.println("Bluest column is: " + img.selectedColumn);
//                change color of bluest column
                    img.changeColumnColor(img.selectedColumn, 0, 0, 255);
//                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
//                save image
                    img.saveImage("tempIMG_0" + img.count);
//                    confirm response
                    if (confirmResponse(choice)) {
                        if (img.selectedColumn == -1) {
                            System.out.println("No column has been selected. \n");
                            break;
                        }
                        System.out.println("Confirming removal. \n");
                        System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
//                remove column
                        img.removeColumn(img.selectedColumn);
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
//                save image
                        img.saveImage("tempIMG_0" + img.count);

                    }
                    else{
                        System.out.println("Canceling removal. \n");
                        System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
                        img.backOne();
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
                        img.saveImage("tempIMG_0" + img.count);
                    }
                    break;

                case 'r':
                    img.operationCount++;
//                    remove a random column
                    System.out.println("Removing a random column. \n");
                    System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
//                find random column
                    img.selectedColumn = img.randomColumn();
//                    System.out.println("Randomly selected column is: " + img.selectedColumn);
//                change color of random column
                    img.changeColumnColor(img.selectedColumn, 255, 0, 0);
//                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
//                save image
                    img.saveImage("tempIMG_0" + img.count);
//                    confirm response
                    if (confirmResponse(choice)) {
                        if (img.selectedColumn == -1) {
                            System.out.println("No column has been selected. \n");
                            break;
                        }
                        System.out.println("Confirming removal. \n");
                        System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
//                remove column
                        img.removeColumn(img.selectedColumn);
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
//                save image
                        img.saveImage("tempIMG_0" + img.count);

                    }
                    else{
                        System.out.println("Canceling removal. \n");
                        System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
                        img.backOne();
//                print image
//                        img.printImage();
//                        System.out.println("Image has been printed");
                        img.saveImage("tempIMG_0" + img.count);
                    }
                    break;

                case 'u':
//                    handle canceled undo
                    if (!confirmResponse(choice)) {
                        return img.selectedColumn;
                    }
//                    undo the last operation
                    System.out.println("Undoing the last operation. \n");
                    System.out.println("Saving image to tempIMG_0" + img.count + ".png");
//                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
//                undo last operation
                    img.undo();
//                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
//                save image
                    img.saveImage("tempIMG_0" + img.count);
//                    confirm response
                    break;

//                case 'd':
//                    if (img.selectedColumn == -1) {
//                        System.out.println("No column has been selected. \n");
//                        break;
//                    }
//                    System.out.println("Confirming removal. \n");
//                    System.out.println("Saving image to tempIMG_0" + img.count + ".png");
////                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
////                remove column
//                    img.removeColumn(img.selectedColumn);
////                print image
//                    img.printImage();
//                    System.out.println("Image has been printed");
////                save image
//                    img.saveImage("tempIMG_0" + img.count);
//                    break;

                case 'q':

                    break;

                default:
                    System.out.println("That is not a valid option. \n");
                    break;
            }
            return img.selectedColumn;
//        }
//        return -1;
    }

    public static void main(String[] args) throws IOException {

        // keep track of if we want to keep the program running
        boolean shouldQuit = false;

//      take user input
        Scanner scan = new Scanner(System.in);
        char choice = 0;
        String filePath = "";


        while (filePath.equals("")) {
            System.out.println("Please enter the file path of the image you would like to edit: ");
            filePath = scan.nextLine();
            System.out.println("You have entered: " + filePath + "\n");
            System.out.println("Is this correct? (y/n)");
            if (scan.next().toLowerCase().charAt(0) == 'n') {
                filePath = "";
            }
        }


        // Create an Image object
        Image img = null;
        try {
            img = new Image(filePath);
//            img.saveImage("tempIMG_0" + img.count );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // while shouldQuit is false, keep going
        while(!shouldQuit) {

            // display options to the user
            printMenu();

            // try and get user input, if input is ever invalid this will set choice to quit
            try {
                choice = scan.next().toLowerCase().charAt(0);
            } catch (InputMismatchException e) {
                System.out.println("Input should be a character");
                choice = 'q';
            }


//          Do image operations
            imageOperations(img, choice);

            // if choice is quit confirm then, exit the while-loop
            if(choice == 'q' && confirmResponse(choice)) {
                System.out.println("Quitting program. \n");
                shouldQuit = true;
            }
        }
//      Save the program on quit
        img.saveImage("newImg");
        scan.close();


    }

}
