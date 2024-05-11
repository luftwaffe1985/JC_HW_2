package ReworkedCode;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_PC = '0';
    private static final char DOT_EMPTY = '*';
    private static int WIN_COUNT;
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
                if (checkState(DOT_PC, "PC won!"))
                    break;
                ;
            }
            System.out.println("Once again? (Y - yes): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Initializing game objects and setting the coordinates
     */
    static void initialize() {
        System.out.println(
                "Enter the dimensions of the playing field along the X axis and Y axis \n(separated by space (not less than 3): ");
        fieldSizeX = scanner.nextInt();
        fieldSizeY = scanner.nextInt();

        if (fieldSizeX < 3) {
            fieldSizeX = 3;
            System.out.println("The X axis margin size value is set to 3");
        } else {
            System.out.println("The X axis margin size value is set to " + fieldSizeX);
        }
        if (fieldSizeY < 3) {
            fieldSizeX = 3;
            System.out.println("The X axis margin size value is set to 3");
        } else {
            System.out.println("The X axis margin size value is set to " + fieldSizeY);
        }

        System.out.println("Enter the length of the continuous series\n that will be considered winning: ");
        WIN_COUNT = scanner.nextInt();
        if (WIN_COUNT > fieldSizeX || WIN_COUNT > fieldSizeY) {
            WIN_COUNT = 3;
            System.out.println("The series length is set to 3");
        } else {
            System.out.println("The series length is set to " + WIN_COUNT);
        }

        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Current playing field status printing
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
            System.out.println("Enter the X and Y coordinates of the move\n(from 1 to 3 separated by space");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Checking if the cell is empty
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Checking the validity of the coordinates of the move
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
        field[x][y] = DOT_PC;
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
        return (checkCartesian(dot) || checkDiagonal(dot));
    }

    /**
     * Game status check
     * 
     * @param dot player's tocken
     * @param s   winning slogan
     * @return1
     */
    static boolean checkState(char dot, String s) {
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        } else if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Game continues
    }

    /**
     * Method for verifying victory using Cartesian directions
     * 
     * @param dot player's tocken
     * @return
     */
    static boolean checkCartesian(char dot) {
        int countWin = 0;
        // Horizontal win
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == dot) {
                    countWin++;
                    if (countWin == WIN_COUNT)
                        return true;
                } else {
                    countWin = 0;
                }
            }
        }
        // Vertical win
        countWin = 0;
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[j][i] == dot) {
                    countWin++;
                    if (countWin == WIN_COUNT)
                        return true;
                } else {
                    countWin = 0;
                }
            }
        }
        return false;
    }

    /**
     * Diagonal win
     * 
     * @param dot player's tocken
     * @return
     */
    static boolean checkDiagonal(char dot) {
        // for (int i = 0; i < WIN_COUNT; i++) { //starting from the leftmost column

        int countWin1;
        int countWin2;
        // if more rows than columns
        if (fieldSizeX >= fieldSizeY) {
            // First diagonal from bottom right to top / Looking through full rows
            int row;
            for (int i = fieldSizeX; i >= WIN_COUNT; i--) {
                if (i >= fieldSizeY) {
                    row = i - 1;
                    countWin1 = 0;
                    for (int col = 0; col < fieldSizeY; col++) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        row--;
                    }
                    // Second diagonal from bottom left to top \ full rows
                    row = i - 1;
                    countWin1 = 0;
                    for (int col = fieldSizeY - 1; col >= 0; col--) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else
                            countWin1 = 0;
                        row--;
                    }
                    // Tops
                } else {
                    // First Top
                    row = i - 1;
                    countWin1 = 0;
                    countWin2 = 0;
                    for (int col = 0; col < i; col++) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        // Mirror for the Top
                        if (field[fieldSizeX - row - 1][(fieldSizeY - col - 1)] == dot) {
                            countWin2++;
                            if (countWin2 == WIN_COUNT)
                                return true;
                        } else {
                            countWin2 = 0;
                        }
                        row--;
                    }
                    // Second Top
                    row = i - 1;
                    countWin1 = 0;
                    countWin2 = 0;
                    for (int col = fieldSizeY - 1; col >= fieldSizeY - i; col--) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        // Mirror for the Top
                        if (field[fieldSizeX - row - 1][fieldSizeY - col - 1] == dot) {
                            countWin2++;
                            if (countWin2 == WIN_COUNT)
                                return true;
                        } else {
                            countWin2 = 0;
                        }
                        row--;
                    }
                }
            }
            // if there are less rows than columns
        } else {
            // First diagonal (full rows)
            int col;
            for (int i = 0; i <= fieldSizeY - WIN_COUNT; i++) {
                if (i <= fieldSizeY - fieldSizeX) {
                    col = i;
                    countWin1 = 0;
                    for (int row = 0; row < fieldSizeX; row++) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        col++;
                    }
                    // Second diagonal (full rows)
                    col = i;
                    countWin1 = 0;

                    for (int row = fieldSizeX - 1; row >= 0; row--) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        col++;
                    }

                } else {
                    // Tops
                    col = i;
                    countWin1 = 0;
                    countWin2 = 0;
                    for (int row = 0; row < fieldSizeY - i; row++) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        if (field[(fieldSizeX - row - 1)][fieldSizeY - col - 1] == dot) { // Checking mirrors
                            countWin2++;
                            if (countWin2 == WIN_COUNT)
                                return true;
                        } else {
                            countWin2 = 0;
                        }
                        col++;
                    }
                    col = i;
                    countWin1 = 0;
                    countWin2 = 0;
                    for (int row = fieldSizeX - 1; row >= fieldSizeX - (fieldSizeY - i); row--) {
                        if (field[row][col] == dot) {
                            countWin1++;
                            if (countWin1 == WIN_COUNT)
                                return true;
                        } else {
                            countWin1 = 0;
                        }
                        if (field[fieldSizeX - row - 1][fieldSizeY - col - 1] == dot) { // Checking mirrors
                            countWin2++;
                            if (countWin2 == WIN_COUNT)
                                return true;
                        } else {
                            countWin2 = 0;
                        }

                        col++;
                    }
                }
            }
        }
        return false;
    }
}
