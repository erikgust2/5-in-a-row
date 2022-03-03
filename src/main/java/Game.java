import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    static final char X = 'X';
    static final char O = 'O';

    static char currentTurn = O;

    private final char[][] gameBoard = new char[10][10];

    private final ArrayList<Placement> placed = new ArrayList<>();

    boolean gameWon;
    boolean gameDraw;

    Scanner scanner = new Scanner(System.in);

    public void run() {
        gameBoard[4][5] = X;
        while (!gameWon && !gameDraw) {
            if (currentTurn == O) {
                playerTurn();
            } else {
                computerTurn();
            }

            checkGameState();

            if (gameWon) {
                if (currentTurn == O) {
                    System.out.println("O is the winner!");
                } else {
                    System.out.println("X is the winner!");
                }
            } else if (gameDraw) {
                System.out.println("The Game is a Draw!");
            }
        }
    }

    private void printBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameBoard[i][j] == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print(gameBoard[i][j]);
                }
                if (i == 9) {
                    System.out.println();
                }
            }
        }
    }

    private void playerTurn() {
        printBoard();
        int x, y;
        boolean legalMove;
        do {
            System.out.println("Please state your move:");

            System.out.print("x? ");
            x = scanner.nextInt();
            System.out.print("y? ");
            y = scanner.nextInt();

            legalMove = legalMove(x, y);
            if(!legalMove) {
                System.out.println("Illegal move!");
            } else {
                placeSymbol(x, y, O);
            }
        } while (!legalMove);
    }

    private void computerTurn() {

    }

    private void checkGameState() {



    }

    private void placeSymbol(int x, int y, char c) {
        gameBoard[x][y] = c;
        placed.add(new Placement(x, y, c));
    }

    private boolean legalMove(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            return false;
        } else if (gameBoard[x][y] != 0) {
            return false;
        } else return checkNeighbours(x, y);
    }

    /**
     * Father forgive me, for I have sinned
     *
     * @param x Nodens x-koordinat
     * @param y Nodens y-koordinat
     *
     * @return The state of suffering
     */
    private boolean checkNeighbours(int x, int y) {
        if (x != 0 && x != 9 && y != 0 && y != 9) {
            return gameBoard[x + 1][y] != 0 || gameBoard[x - 1][y] != 0 || gameBoard[x][y + 1] != 0 || gameBoard[x][y - 1] != 0 ||
                    gameBoard[x + 1][y + 1] != 0 || gameBoard[x - 1][y + 1] != 0 || gameBoard[x - 1][y - 1] != 0 || gameBoard[x + 1][y - 1] != 0;
        } else if (x == 0 && y == 0) {
            return gameBoard[x + 1][y] != 0 || gameBoard[x][y + 1] != 0 || gameBoard[x + 1][y + 1] != 0;
        } else if (x == 0 && y == 9) {
            return gameBoard[x + 1][y] != 0 || gameBoard[x][y - 1] != 0 || gameBoard[x + 1][y - 1] != 0;
        } else if (x == 9 && y == 0) {
            return gameBoard[x - 1][y] != 0 || gameBoard[x][y + 1] != 0 || gameBoard[x - 1][y + 1] != 0;
        } else if (x == 9 && y == 9) {
            return gameBoard[x - 1][y] != 0 || gameBoard[x][y - 1] != 0 || gameBoard[x - 1][y - 1] != 0;
        } else if (x == 0) {
            return gameBoard[x + 1][y] != 0 || gameBoard[x][y + 1] != 0 || gameBoard[x][y - 1] != 0 ||
                    gameBoard[x + 1][y + 1] != 0 || gameBoard[x + 1][y - 1] != 0;
        } else if (x == 9) {
            return gameBoard[x - 1][y] != 0 || gameBoard[x][y + 1] != 0 || gameBoard[x][y - 1] != 0 ||
                    gameBoard[x - 1][y + 1] != 0 || gameBoard[x - 1][y - 1] != 0;
        } else if (y == 0) {
            return gameBoard[x + 1][y] != 0 || gameBoard[x - 1][y] != 0 || gameBoard[x][y + 1] != 0 ||
                    gameBoard[x + 1][y + 1] != 0 || gameBoard[x - 1][y + 1] != 0;
        } else { //y == 9
            return gameBoard[x + 1][y] != 0 || gameBoard[x - 1][y] != 0 || gameBoard[x][y - 1] != 0 ||
                    gameBoard[x - 1][y - 1] != 0 || gameBoard[x + 1][y - 1] != 0;
        }
    }

    private static class Placement {
        int x, y;
        char c;

        public Placement(int x, int y, char c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }
    }

}
