package uk.ac.nulondon;

import java.util.*;

public class UserMenuDemo {

    /**
     * Print the options for the user.
     */
    public static void printMenu() {
        System.out.println("Please make a selection");
        System.out.println("b - remove the column with the most blue");
        System.out.println("r - remove a random column");
        System.out.println("u - undo the last operation");
        System.out.println("q - quit the program");
    }

    /**
     * Print a response to the user, given their selection
     * @param selection the value of the user's choice
     */
    public static void printResponse(int selection) {
        switch(selection) {
            case 1:
                System.out.println("Excellent choice, A it is.");
                break;
            case 2:
                System.out.println("Choice B, well ok then.");
                break;
            case 3:
                System.out.println("C, you picked, C?");
                break;
            case 4:
                System.out.println("Thanks for playing.");
                break;
            default:
                System.out.println("That is not a valid option.");
                break;

        }
    }


    public static void main(String[] args) {

        // keep track of if we want to keep the program running
        boolean shouldQuit = false;

        Scanner scan = new Scanner(System.in);
        int choice = 0;

        // while shouldQuit is false, keep going
        while(!shouldQuit) {

            // display options to the user
            printMenu();

            // try and get user input, if input is ever invalid this will set choice to quit
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input should be an int");
                choice = 4;
            }

            printResponse(choice);

            // if choice is quit, exit the while-loop
            if(choice == 4) {
                shouldQuit = true;
            }
        }
        scan.close();
    }
}

