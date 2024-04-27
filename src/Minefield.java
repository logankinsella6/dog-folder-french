//Written by Aaron Siddiky, siddi186 and Logan Kinsella

import java.util.Queue;
import java.util.Random;

public class Minefield {
    /**
     * Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /*
     * Class Variable Section
     *
     */

    private Cell[][] mfield;
    private int flags;

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     * <p>
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     *
     * @param rows    Number of rows.
     * @param columns Number of columns.
     * @param flags   Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) { //construtor
        mfield = new Cell[rows][columns];
        this.flags = flags;
    }

    /**
     * evaluateField
     *
     * @function: Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     */
    public void evaluateField() {
            for (int i = 0; i < mfield.length; i++) { //iterate through the rows, using i
                for (int j = 0; j < mfield[0].length; j++) { //iterate through the columns using j

                    if (mfield[i][j] != null && mfield[i][j].getStatus().equals("M")) { //checking if the cell is not null, and is a mine, if so then we will continue
                        for (int a = -1; a <= 1; a++) { //we will be doing a 3 x 3 matrix around the mine cell, this matrix are all the adjacent tiles, a is horizontal
                            for (int b = -1; b <= 1; b++) { //iterating vertically through this matrix
                                if (a + i >= 0 && a + i < mfield.length && b + j >= 0 && b + j < mfield.length) { //checking for bounds

                                    if (mfield[i + a][j + b] == null) { //if selected cell is empty, then we will create a new cell that is false,
                                        mfield[i + a][j + b] = new Cell(false, "1");

                                    } else if (!mfield[i + a][j + b].getStatus().equals("M")) { //if the selected cell has a number, add 1, thereby evaluating the numberes on our board
                                        mfield[i + a][j + b].setStatus(String.valueOf(1 + Integer.parseInt(mfield[i + a][j + b].getStatus())));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < mfield.length; i++) { //again iterating through i and j (rows/columns)
                for (int j = 0; j < mfield[0].length; j++) {
                    if (mfield[i][j] == null) { //this is in order to bypass the null pointer exception, if its null then create false cell with 0)
                        mfield[i][j] = new Cell(false, "0");
                    }
                }
            }
        }


    /**
     * createMines
     * <p>
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x     Start x, avoid placing on this square.
     * @param y     Start y, avoid placing on this square.
     * @param mines Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        Random random = new Random();
        int numOfMines = 0;  //instantiate the number of mines as 0
        while (numOfMines < mines) { //while the number of mines is less than the mines, then we will continue
            int ranXCoor = random.nextInt(mfield.length); //
            int ranYCoor = random.nextInt(mfield[0].length);
            if (mfield[ranXCoor][ranYCoor] == null && ranYCoor != y && ranXCoor != x) { //while neither of the new coordinates are the same as the exsiting ones, and they are not null
                mfield[ranXCoor][ranYCoor] = new Cell(false, "M"); //create a new cell at this new random coordinates
                numOfMines++; //increment number of mines, as a new one has been added
            }
        }
    }

    /**
     * guess
     * <p>
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     * @param x    The x value the user entered.
     * @param y    The y value the user entered.
     * @param flag A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if (x < 0 || x >= mfield.length || y < 0 || y >= mfield[0].length) { //checking for bounds, will return true if out of bounds
            System.out.println("Entered coordinates are unfortunately out of bounds!!");
            return true;
        }

        if (flag) { // This is for placing a flag

            if (this.flags > 0 && !mfield[x][y].getRevealed()) { //if flags is greater than 0, and field has not been revealed
                mfield[x][y].setStatus("F"); //set the status to F, because flag has been placed
                mfield[x][y].setRevealed(true); //reveal it now, with the newly set status!
                this.flags--; //decrementing because we have a limited number of flags depending on the game mode!

            } else {
                System.out.println("Sorry! Unfortunately we are not able to place flag here!"); //if not, error message that says we can't palce flag
            }
        } else { //last case is when we wont place
            Cell z = mfield[x][y];
            if (z.getStatus().equals("M")) { //if it is a mine, and it is found, then return true
                return true;
            } else if (z.getStatus().equals("0")) { //if not then reveal zeros of the x,y coordinate cell
                revealZeroes(x, y);
            }
            z.setRevealed(true);
        }
        return false;
    }

    /**
     * gameOver
     * <p>
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        for (int i = 0; i < mfield.length; i++) { //checking if all the cells have been revealed, if they have we will set as false because game is not over until everything is revealed
            for (int j = 0; j < mfield[0].length; j++) {
                if((!mfield[i][j].getStatus().equals("M") && !mfield[i][j].getStatus().equals("F")) && !mfield[i][j].getRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     * <p>
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealZeroes(int x, int y) {

        Stack1Gen stackZeroes = new Stack1Gen();
        stackZeroes.push(new int[]{x, y}); //initializing the stack with the x and y coordinates passed through

        while (!stackZeroes.isEmpty()) { //while the stack is empty
            int[] corr = (int[]) stackZeroes.pop(); //essentially getting the top element off "popping"
            x = corr[0]; //corresponding cell
            y = corr[1];

            if (!mfield[x][y].getRevealed() && mfield[x][y].getStatus().equals("0")) { //checking if its an unrevealed
                mfield[x][y].setRevealed(true); //setting the corresponding cell's revealed attribute
                for (int i = -1; i <= 1; i += 2) { //checking for neighbors using the 3x3 adjacent matrix, the cell is in the middle, and everything else is neighbor
                    if (x + i >= 0 && x + i < mfield.length && !mfield[x + i][y].getRevealed()) { //checking if in bounds and if not revealed
                        stackZeroes.push(new int[]{x + i, y}); //then we just push all these neighbor coordinates into the stack
                    }
                    if (y + i >= 0 && y + i < mfield[0].length && !mfield[x][y + i].getRevealed()) { // the other case, above and below
                        stackZeroes.push(new int[]{x, y + i}); //pushing them into the stack
                    }
                }
            }
        }
    }

    /**
     * revealStartingArea
     * <p>
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * <p>
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<>(); //creating Q1gen Queue, note this method is following BSF algo
        queue.add(new int[]{x, y}); //adding the x y coords

        while (queue.length() > 0) { //while the queue is not empty
            int[] current = queue.remove(); //remove or "pull" the current
            int currentX = current[0];
            int currentY = current[1];


            mfield[currentX][currentY].setRevealed(true);

            if (mfield[currentX][currentY].getStatus().equals("M")) { //if it is a mine, then break)
                break;
            }

            for (int a = -1; a <= 1; a++) { //iterating through the same 3x3 matrix adjacent logic used before

                for (int b = -1; b <= 1; b++) { ////iterating vertically of this matrix
                    int neighborX = currentX + a; //adding it to the existing coordinates horizontally
                    int neighborY = currentY + b; //adding it to the existing coordinates vertically


                    if (neighborX >= 0 && neighborX < mfield.length && //checking for bounds
                            neighborY >= 0 && neighborY < mfield[0].length &&
                            !mfield[neighborX][neighborY].getRevealed()) {
                        queue.add(new int[]{neighborX, neighborY}); //if in bounds, as we know in BSF algo, we want to add the newly added child (2nd level nodes) to the queue and repeat process
                    }
                }
            }
        }
    }


        /**
         * For both printing methods utilize the ANSI colour codes provided!
         *
         *
         *
         *
         *
         * debug
         *
         * @function This method should print the entire minefield, regardless if the user has guessed a square.
         * *This method should print out when debug mode has been selected.
         */
        public void debug () {
            String strStats;
            String color = ANSI_GREY_BACKGROUND; //setting this color to grey bg color
            System.out.print("  ");

            for (int a = 0; a < mfield[0].length; a++) {
                if (a < 10) {
                    System.out.print(" ");
                }
                System.out.print(" " + a);
            }
            //as we've done many times, iterating through the entire minefield
            for (int b = 0; b < mfield.length; b++) { //
                System.out.print("\n" + b);
                if (b < 10) {
                    System.out.print(" ");
                }
                //then assign each one a color, took the liberty of assigning random colors from the ones given.
                for (int j = 0; j < mfield[0].length; j++) {
                    strStats = mfield[b][j].getStatus(); //get the status and assign its respective to its own individaul color!
                    if (strStats.equals("M") || strStats.equals("F")) {
                        color = ANSI_RED_BRIGHT;
                    } else if (strStats.equals("0") || strStats.equals("6")) {
                        color = ANSI_YELLOW;
                    } else if (strStats.equals("1") || strStats.equals("7")) {
                        color = ANSI_GREEN;
                    } else if (strStats.equals("2") || strStats.equals("8")) {
                        color = ANSI_BLUE;
                    } else if (strStats.equals("3")) {
                        color = ANSI_PURPLE;
                    } else if (strStats.equals("4")) {
                        color = ANSI_CYAN;
                    } else if (strStats.equals("5")) {
                        color = ANSI_RED;
                    }
                    System.out.print("  " + color + strStats + ANSI_GREY_BACKGROUND);
                    color = ANSI_GREY_BACKGROUND;
                }
            }
            System.out.println();
        }

        /**
         * toString
         *
         * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
         */
        public String toString () {

            String cellStatus;
            String res = "  ";
            String color = ANSI_GREY_BACKGROUND;

            for(int i = 0; i < mfield[0].length; i++) {
                if(i<10) {
                    res += " ";
                }
                res += " " + i;
            }
            for(int i = 0; i < mfield.length; i++) {
                res += "\n" + i;
                if(i < 10) {
                    res += " ";
                }
                for(int j = 0; j < mfield[0].length; j++) {
                    if(mfield[i][j].getRevealed()) {

                        cellStatus = mfield[i][j].getStatus();
                        if (cellStatus.equals("M") || cellStatus.equals("F")) {
                            color = ANSI_RED;
                        } else if (cellStatus.equals("0") || cellStatus.equals("6")) {
                            color = ANSI_YELLOW;
                        } else if (cellStatus.equals("1") || cellStatus.equals("7")) {
                            color = ANSI_GREEN;
                        } else if (cellStatus.equals("2") || cellStatus.equals("8")) {
                            color = ANSI_BLUE;
                        } else if (cellStatus.equals("3")) {
                            color = ANSI_PURPLE;
                        } else if (cellStatus.equals("4")) {
                            color = ANSI_CYAN;
                        } else if (cellStatus.equals("5")) {
                            color = ANSI_RED;
                        }
                        res += "  " + color + cellStatus + ANSI_GREY_BACKGROUND;
                        color = ANSI_GREY_BACKGROUND;

                    } else {
                        res += "  -";
                    }
                }
            }
            return res;
        }
    }


