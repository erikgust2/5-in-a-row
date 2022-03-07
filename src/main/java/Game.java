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

    boolean gameWon;
    boolean gameDraw;

    Scanner scanner = new Scanner(System.in);

    public void run() {
        gameBoard[4][5] = X;
        while (!gameWon && !gameDraw) {
            if (currentTurn == O) {
                playerTurn();
            } else {
                //computerTurn();
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
            if(!legalMove) {
                System.out.println("Illegal move!");
            } else {
                placeSymbol(x, y, O);
            }
        } while (!legalMove);
    }

    //TODO skriv färdigt
    private void computerTurn() {
        System.out.println("computer takes a turn");
        findComputerMove(-1,1);

    }
    private void findComputerMove(int alpha, int beta) {
        int responseValue;
        int value;
        int bestMove = 1;
        Move winningMove;

        value = alpha;
        for(Placement p : findMoves(X)){
            mockPlaceSymbol(p);
            //TODO find human move
            findHumanMove(value, beta);
            unPlaceSymbol(p);
        }

    }
    private void findHumanMove(int alpha, int beta) {
        int responseValue;
        int value;
        int bestMove = 1;
        Move winningMove;

        value = alpha;
        for(Placement p : findMoves(O)){
            mockPlaceSymbol(p);
            //TODO find cp-move
            findComputerMove(value, beta);
            unPlaceSymbol(p);
        }

    }


    private HashSet<Placement> findMoves(char chr){
        HashSet<Placement> possibleMoves = new HashSet<>();
        for(Placement p: placed){
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    if(gameBoard[p.x+i][p.y+j] == 0 && p.x > 0 && p.x < 12 && p.y > 0 && p.y < 12) {
                        possibleMoves.add(new Placement(p.x+i, p.y+j, chr));
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

        gameWon = checkHorizontal(lastMove) || checkVertical(lastMove) || checkDiagonalLeft(lastMove) || checkDiagonalRight(lastMove);

        if(!gameWon) {
            boolean foundFreeSlot = false;
            for(int i = 1; i < 11; i++) {
                for(int j = 1; j < 11; j++) {
                    if(gameBoard[i][j] == 0) {
                        foundFreeSlot = true;
                        break;
                    }
                }
            }
            gameDraw = !foundFreeSlot;
        }
    }

    private boolean checkHorizontal(Placement lastMove) {
        int foundSigns = 0;
        boolean stopLeft = false, stopRight = false;

        for(int i = 0; i < 5; i++) {

            if(!stopRight) {
                if(gameBoard[lastMove.x + i][lastMove.y] != lastMove.c) {
                    stopRight = true;
                } else {
                    foundSigns++;
                }
            }

            if(!stopLeft) {
                if(gameBoard[lastMove.x - i][lastMove.y] != lastMove.c) {
                    stopLeft = true;
                } else {
                    foundSigns++;
                }
            }
        }

        return foundSigns >= 6;
    }

    private boolean checkVertical(Placement lastMove) {
        int foundSigns = 0;
        boolean stopTop = false, stopBottom = false;

        for(int i = 0; i < 5; i++) {

            if(!stopBottom) {
                if(gameBoard[lastMove.x][lastMove.y + i] != lastMove.c) {
                    stopBottom = true;
                } else {
                    foundSigns++;
                }
            }

            if(!stopTop) {
                if(gameBoard[lastMove.x][lastMove.y - i] != lastMove.c) {
                    stopTop = true;
                } else {
                    foundSigns++;
                }
            }
        }

        return foundSigns >= 6;
    }

    private boolean checkDiagonalRight(Placement lastMove) {
        int foundSigns = 0;
        boolean stopLeft = false, stopRight = false;

        for(int i = 0; i < 5; i++) {

            if(!stopRight) {
                if(gameBoard[lastMove.x + i][lastMove.y + i] != lastMove.c) {
                    stopRight = true;
                } else {
                    foundSigns++;
                }
            }

            if(!stopLeft) {
                if(gameBoard[lastMove.x - i][lastMove.y - i] != lastMove.c) {
                    stopLeft = true;
                } else {
                    foundSigns++;
                }
            }
        }

        return foundSigns >= 6;
    }

    private boolean checkDiagonalLeft(Placement lastMove) {
        int foundSigns = 0;
        boolean stopLeft = false, stopRight = false;

        for(int i = 0; i < 5; i++) {

            if(!stopRight) {
                if(gameBoard[lastMove.x + i][lastMove.y - i] != lastMove.c) {
                    stopRight = true;
                } else {
                    foundSigns++;
                }
            }

            if(!stopLeft) {
                if(gameBoard[lastMove.x - i][lastMove.y + i] != lastMove.c) {
                    stopLeft = true;
                } else {
                    foundSigns++;
                }
            }
        }

        return foundSigns >= 6;
    }

    /**
     * Father has forgiven me
     *
     * @param x Nodens x-koordinat
     * @param y Nodens y-koordinat
     *
     * @return The state of suffering
     */
    private boolean checkNeighbours(int x, int y) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(gameBoard[x+i][y+j] != 0) {
                    return true;
                }
            }
        }
        return false;
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
