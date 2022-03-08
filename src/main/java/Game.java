import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Game {

    static final char X = 'X';
    static final char O = 'O';

    static char currentTurn = O;

    private final char[][] gameBoard = new char[12][12];

    private final HashSet<Placement> placed = new HashSet<>();
    Placement lastMove;
    Placement lastMockCPMove;
    Placement lastMockMHMove;

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
                printBoard();
                if (currentTurn == O) {
                    System.out.println("O is the winner!");
                } else {
                    System.out.println("X is the winner!");
                }
            } else if (gameDraw) {
                System.out.println("The Game is a Draw!");
            }
            changeTurn();
        }
    }

    private void printBoard() {
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                if (gameBoard[j][i] == 0) {
                    System.out.print(" _ ");
                } else {
                    System.out.print(" " + gameBoard[j][i] + " ");
                }
                if (j == 10) {
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
            if (!legalMove) {
                System.out.println("Illegal move!");
            } else {
                placeSymbol(x, y, O);
            }
        } while (!legalMove);

    }

    //TODO skriv färdigt
    private void computerTurn() {
        System.out.println("computer takes a turn");
        Placement bestMove = findComputerMove(6, -6, 0);
        placeSymbol(bestMove.x, bestMove.y, X);

    }

    private Placement findComputerMove(int alpha, int beta, int counter) {
        int responseValue = -6;
        int value;
        int bestMoveX = 0;
        int bestMoveY = 0;

        if (lastMockCPMove != null && lastMockCPMove.value == 6) {
            return new Placement(bestMoveX, bestMoveY, X);
        } else {

            value = alpha;

            for (Placement p : findMoves(X)) {
                if (value < beta || counter > 10) {
                    break;
                }
                mockPlaceSymbol(p);
                lastMockCPMove = p;
                //TODO find human move
                Placement response = findHumanMove(value, beta, counter++);
                responseValue = response.value;
                unPlaceSymbol(p);
                if (responseValue >= value) {
                    value = responseValue;
                    bestMoveX = p.x;
                    bestMoveY = p.y;
                }
            }
        }

        return new Placement(bestMoveX, bestMoveY, X);
    }


    private Placement findHumanMove(int alpha, int beta, int counter) {
        int responseValue = 6;
        int value;
        int bestMoveX = 0;
        int bestMoveY = 0;

        if (lastMockMHMove != null && lastMockMHMove.value == -6) {
            return new Placement(bestMoveX, bestMoveY, X);
        } else {

            value = beta;

            ArrayList<Placement> possibleMoves = findMoves(O);

            for (int i = 0; i < possibleMoves.size() && value < alpha && counter > 10; i++) {
                mockPlaceSymbol(possibleMoves.get(i));
                lastMockMHMove = possibleMoves.get(i);
                //TODO find cp-move
                Placement response = findComputerMove(alpha, value, counter++);
                responseValue = response.value;
                unPlaceSymbol(possibleMoves.get(i));
                if (responseValue <= value) {
                    value = responseValue;
                    bestMoveX = possibleMoves.get(i).x;
                    bestMoveY = possibleMoves.get(i).y;
                }
            }

            return new Placement(bestMoveX, bestMoveY, O);
        }
    }


    private ArrayList<Placement> findMoves(char chr) {
        ArrayList<Placement> possibleMoves = new ArrayList<>();
        for (Placement p : placed) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (p.x > 0 && p.x < 11 && p.y > 0 && p.y < 11) {
                        if (gameBoard[p.x + i][p.y + j] == 0) {
                            possibleMoves.add(new Placement(p.x + i, p.y + j, chr));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    private void placeSymbol(int x, int y, char c) {
        gameBoard[x][y] = c;
        lastMove = new Placement(x, y, c);
        placed.add(lastMove);
    }

    private void mockPlaceSymbol(Placement p) {
        gameBoard[p.x][p.y] = p.c;
        placed.add(p);
    }

    private void unPlaceSymbol(Placement p) {
        gameBoard[p.x][p.y] = 0;
        placed.remove(p);
    }

    private boolean legalMove(int x, int y) {
        if (x < 1 || x > 10 || y < 1 || y > 10) {
            return false;
        } else if (gameBoard[x][y] != 0) {
            return false;
        } else return checkNeighbours(x, y);
    }

    private void checkGameState() {

        checkHorizontal(lastMove);
        checkVertical(lastMove);
        checkDiagonalLeft(lastMove);
        checkDiagonalRight(lastMove);

        gameWon = lastMove.value >= 6;

        if (!gameWon) {
            boolean foundFreeSlot = false;
            for (int i = 1; i < 11; i++) {
                for (int j = 1; j < 11; j++) {
                    if (gameBoard[i][j] == 0) {
                        foundFreeSlot = true;
                        break;
                    }
                }
            }
            gameDraw = !foundFreeSlot;
        }
    }

    private void checkHorizontal(Placement lastMove) {
        int foundSigns = 0;
        boolean stopLeft = false, stopRight = false;

        for (int i = 0; i < 5; i++) {

            if (!stopRight) {
                if (lastMove.x + i > 11 || gameBoard[lastMove.x + i][lastMove.y] != lastMove.c) {
                    stopRight = true;
                } else {
                    foundSigns++;
                }
            }

            if (!stopLeft) {
                if (lastMove.x - i < 0 || gameBoard[lastMove.x - i][lastMove.y] != lastMove.c) {
                    stopLeft = true;
                } else {
                    foundSigns++;
                }
            }
        }
        if (lastMove.value < foundSigns) {
            lastMove.value = foundSigns;
        }
    }

    private void checkVertical(Placement lastMove) {
        int foundSigns = 0;
        boolean stopTop = false, stopBottom = false;

        for (int i = 0; i < 5; i++) {

            if (!stopBottom) {
                if (lastMove.y + i > 11 || gameBoard[lastMove.x][lastMove.y + i] != lastMove.c) {
                    stopBottom = true;
                } else {
                    foundSigns++;
                }
            }

            if (!stopTop) {
                if (lastMove.x - i < 0 || gameBoard[lastMove.x][lastMove.y - i] != lastMove.c) {
                    stopTop = true;
                } else {
                    foundSigns++;
                }
            }
        }

        if (lastMove.value < foundSigns) {
            lastMove.value = foundSigns;
        }
    }

    private void checkDiagonalRight(Placement lastMove) {
        int foundSigns = 0;
        boolean stopLeft = false, stopRight = false;

        for (int i = 0; i < 5; i++) {

            if (!stopRight) {
                if (lastMove.x + i > 11 || lastMove.y + i > 11 || gameBoard[lastMove.x + i][lastMove.y + i] != lastMove.c) {
                    stopRight = true;
                } else {
                    foundSigns++;
                }
            }

            if (!stopLeft) {
                if (lastMove.x - i < 0 || lastMove.y - i < 0 || gameBoard[lastMove.x - i][lastMove.y - i] != lastMove.c) {
                    stopLeft = true;
                } else {
                    foundSigns++;
                }
            }
        }

        if (lastMove.value < foundSigns) {
            lastMove.value = foundSigns;
        }
    }

    private void checkDiagonalLeft(Placement lastMove) {
        int foundSigns = 0;
        boolean stopLeft = false, stopRight = false;

        for (int i = 0; i < 5; i++) {

            if (!stopRight) {
                if (lastMove.x + i > 11 || lastMove.y - i < 0 || gameBoard[lastMove.x + i][lastMove.y - i] != lastMove.c) {
                    stopRight = true;
                } else {
                    foundSigns++;
                }
            }

            if (!stopLeft) {
                if (lastMove.x - i < 0 || lastMove.y + i > 11 || gameBoard[lastMove.x - i][lastMove.y + i] != lastMove.c) {
                    stopLeft = true;
                } else {
                    foundSigns++;
                }
            }
        }

        if (lastMove.value < foundSigns) {
            lastMove.value = foundSigns;
        }
    }

    /**
     * Father has forgiven me
     *
     * @param x Nodens x-koordinat
     * @param y Nodens y-koordinat
     * @return The state of suffering
     */
    private boolean checkNeighbours(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (gameBoard[x + i][y + j] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void changeTurn(){
        if(currentTurn == X){
            currentTurn = O;
        }else{
            currentTurn = X;
        }
    }


    private static class Placement {
        int x, y, value;
        char c;

        public Placement(int x, int y, char c) {
            this.x = x;
            this.y = y;
            this.c = c;
            this.value = 0;
        }
    }

}
