//Written by Aaron Sidiky, siddi186 and Logan Kinsella

import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 *
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */
public class main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        Boolean debug = false;
        int rows = 0; //instantiating rows
        int flags = 0; //instatiating flags
        String input; //input from user on mode/difficulty of mode
        Boolean flagBool = false;

        System.out.println("Would you like to play in Debug Mode? y for Yes, anything else for No: ");
        String input2 = scn.nextLine(); //input from user on debug mode
        Boolean gameStatus = true;
        if (input2.equals("Y") || input2.equals("y")) {
            debug = true; //turns on debug mode as true if user input asks for it
        }
            System.out.println("----Aaron and Logan's Minefield Game!----");
            System.out.println("Easy Mode: 5 x 5 - 5 mines - 5 flags (Type 'easy')");
            System.out.println("Medium Mode: 9 x 9 - 12 mines - 12 flags (Type 'medium')");
            System.out.println("Hard Mode: 20 x 20 - 40 mines - 40 flags (Type 'hard')");
            input = scn.nextLine();

            if (input.toLowerCase().equals("easy")) { //sets rows to 5, and flags to 5 if the mode is easy
                rows = 5;
                flags = 5;}
            else if (input.toLowerCase().equals("medium")) { //sets rows to 9, and flags to 12 if the mode is medium
                rows = 9;
                flags = 12;
            } else if (input.toLowerCase().equals("hard")) { //sets rows to 20, and flags to 40 if the mode is hard
                rows = 20;
                flags = 40;
            }
            Minefield m = new Minefield(rows, rows, flags); //creating a new Minefield
            int x = -1;
            int y = -1;
            String flagsinp = ""; //instantiating the input for flag
            while (x == -1 || y == -1) { //loop that we will use while the user is selecting
                System.out.println("----Aaron and Logan's Minefield Game----");
                System.out.println("Please enter a x & y, remember to separate by space. Ex: 2 4.");

                x = scn.nextInt();
                y = scn.nextInt();

                if (x >= rows || x < 0) {
                    System.err.println("The x coordinate: " + x + " is unfortunately out of bounds.");
                    x = -1; //setting it back to -1
                }
                if (y >= rows || y < 0) {
                    System.err.println("The y coordinate: " + y + " is unfortunately out of bounds.");
                    y = -1; //setting it back to -1
                }
            }

            m.createMines(x, y, flags);
            m.evaluateField();
            m.revealStartingArea(x, y);
            m.guess(x, y, false);

            if (debug) { //if debug is true, then open debug
                m.debug();
            }
            System.out.println(m); //print the minefield
            scn = new Scanner(System.in);
            while (!m.gameOver()) { //Beginning of the game loop
                x = -1;
                y = -1;
                while (x == -1 || y == -1) { //allows us to keep this whle loop open while user is still choosing coordinates
                    System.out.println("----Aaron and Logan's Minefield Game----");
                    System.out.println("Enter \"x y and then f to place flag, and n to not place flag! Ex: 1 3 f or 1 2 n\".");
                    x = scn.nextInt();
                    y = scn.nextInt();
                    if (scn.hasNext()) { //checking if there is a flag
                        flagsinp = scn.next();
                    } else {
                        flagsinp = "";
                    }
                    if (x >= rows || x < 0) { //checking out of bounds
                        System.err.println("x value: " + x + " is out of bounds.");
                        x = -1;
                    }
                    if (y >= rows || y < 0) { //checking out of bounds
                        System.err.println("y value: " + y + " is out of bounds.");
                        y = -1;
                    }
                    if (flagsinp.toLowerCase().equals("f") || flagsinp.toLowerCase().equals("flag")) { //if the user wants flag
                        flagBool = true; //set the flagBool to true if user wants to place flag
                        break;
                    } else {
                        flagBool = false; //otherwise we will set the flagBool to false
                        break;
                    }
                }
                if (m.guess(x, y, flagBool)) {
                    System.out.println("----Aaron and Logan's Minefield Game----");
                    if (x < 0 || y < 0 || x >= rows || y >= rows) {
                        System.out.println("Hey! You're out of bounds :(");
                        x = -1;
                        y = -1;
                        continue;
                    } else {
                        System.out.println("You've Hit a MINE!! GAME OVER :( ");
                        break;
                    }
                }
                if (m.gameOver()) { //if no empty cells
                    System.out.println("----Aaron and Logan's Minefield Game----");
                    System.out.println("Congrats!! You won!");
                    break; //ends game
                }
                m.debug(); //prints when game is over
                System.out.println(m);



            }
            m.debug(); //prints when game is over
        }
    }