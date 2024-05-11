package SeminarCode;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char[][] field;

    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "You won!"))
                    break;
                ;
                aiTurn();
                printField();
                if (checkState(DOT_AI, "PC won!"))
                    break;
                ;
            }
            System.out.println("Once again? (Y - yes): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Initializing game obects
     */
    static void initialize() {
        fieldSizeX = 3;
        fieldSizeY = 3;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Game field current status printing
     */
    static void printField() {
        System.out.print("+");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print("-" + (x + 1));
        }
        System.out.println("-");

        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }
        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Player's move (human)
     */
    static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Enter X и Y coordinates for the move\n(from 1 to 3 separated by space");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Check if the cell is empty
     * 
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Checking the validity of coordinates of the move
     * 
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Player's move (PC)
     */
    static void aiTurn() {
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Draw check-up
     * 
     * @return
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    /**
     * Win check-up method
     * 
     * @param dot player's tocken
     * @return
     */
    static boolean checkWin(char dot) {
        // Horizontal check
        if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot)
            return true;
        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot)
            return true;
        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot)
            return true;

        // Vertical check
        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot)
            return true;
        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot)
            return true;
        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot)
            return true;

        // Diagonal check
        if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot)
            return true;
        if (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot)
            return true;

        return false;
    }

    /**
     * Game status check
     * 
     * @param dot player's tocken
     * @param s   victory slogan
     * @return
     */
    static boolean checkState(char dot, String s) {
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        } else if (checkDraw()) {
            System.out.println("It's a draw!");
            return true;
        }
        return false; // Game continues
    }
}