package Checkers;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;

import org.checkerframework.checker.units.qual.C;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.sql.SQLOutput;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = {181, 136, 99};
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final float[][][] coloursRGB = new float[][][]{
            // default - white & black
            {{WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]}, {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}},
            // green
            {{105, 138, 76}, // when on white cell
                    {105, 138, 76} // when on black cell
            },
            // blue
            {{196, 224, 232}, {170, 210, 221}}};

    public static int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH * CELLSIZE;

    public static final int FPS = 60;

    private Cell[][] board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
    private Cell selectedCell = null;
    private Cell rememberLastCell = null;
    private char playerCurrentTurn = 'w';
    ArrayList<Cell> trackLastClick = new ArrayList<Cell>();
    private ArrayList<Cell> availableMoves = new ArrayList<>();
    private ArrayList<Cell> captureAvailableMoves = new ArrayList<>();

    public Cell getSelectedCell() {
        return this.selectedCell;
    }

    public App() {

    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);

        // Set up the data structures used for storing data in the game
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2] = new Cell(i2, i);
                if ((i + i2) % 2 == 1) {
                    if (i < 3) {
                        CheckersPiece piece = new CheckersPiece('w');

                        board[i][i2].setPiece(piece);
                    } else if (i > 4) {
                        CheckersPiece piece = new CheckersPiece('b');

                        board[i][i2].setPiece(piece);
                    }


                }


            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed() {

    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Check if the user clicked on a piece which is theirs - make sure only
        // whoever's current turn it is, can click on pieces
        if (e.getX() < 0 || e.getX() >= WIDTH || e.getY() < 0 || e.getY() >= HEIGHT) {
            return;
        }
        Cell clicked = board[e.getY() / CELLSIZE][e.getX() / CELLSIZE];

        if (clicked.getPiece() != null && clicked.getPiece().getColor() == playerCurrentTurn) {
            selectedCell = clicked;
            availableMoves = determineAvailableMoves(selectedCell); // Use class-level variable
            trackLastClick.add(clicked);
            highlightAvailableMoves();
            System.out.println("Selected Cell " + selectedCell);
            System.out.println("Avaliable Movies " + availableMoves);
            System.out.println("getx and get y " + e.getX() / CELLSIZE + " " + e.getY() / CELLSIZE);

        }


        if (availableMoves.contains(clicked) && clicked.getPiece() == null) {
            Cell to = board[e.getY() / CELLSIZE][e.getX() / CELLSIZE];
            movePiece(selectedCell, to);
            System.out.println("clicked x : " + to.getX() + " Y " + to.getY());

        } else {
            // If no piece is selected, check if the clicked cell contains a piece belonging to the current player
            if (clicked.getPiece() != null && clicked.getPiece().getColor() == playerCurrentTurn) {
                selectedCell = clicked;
                availableMoves = determineAvailableMoves(selectedCell);
                highlightAvailableMoves();
            }
        }
        // if (clicked.getPiece() != null && clicked.getPiece().getColor() ==
        // playerCurrentTurn) {
        // if (selectedCell == clicked) {
        // selectedCell = null;
        // } else {
        // selectedCell = clicked;
        // }
        // }
        // TODO: Check if user clicked on an available move - move the selected piece
        // there.
        // TODO: Remove captured pieces from the board
        // TODO: Check if piece should be promoted and promote it
        // TODO: Then it's the other player's turn.
    }

    // Method to move a piece from one cell to another

//    public void movePiece(Cell selectedCellParm, Cell to) {
//        if (selectedCellParm == null || to == null) {
//            System.out.println("Error: Cannot move piece - selectedCell or destination cell is null.");
//            return;
//        }
//
//        //Cell isOpponentCell = board[to.getX()+1][to.getY()-1];
//        System.out.println("Opponent Cell X " + to.getX() + " Y " + to.getY());
//
//
////        if(isOpponentCell.getPiece() != null)
////        {
////            if(isOpponentCell.getPiece().getColor() != playerCurrentTurn)
////            {
////                System.out.println("OPPO Cell X " + isOpponentCell.getX() + " Y " + isOpponentCell.getY());
////                isOpponentCell.setPiece(null);
////            }
////
////        }
//        // Move the piece to the destination cell
//        to.setPiece(selectedCellParm.getPiece());
//        // selectedCellParm.setPiece(null);
//        // Clear the selected cell after moving
//        availableMoves.clear();
//        selectedCell = null;
//        int size = trackLastClick.size();
//        System.out.println("trackedLastClick " + size);
//
//        Cell secondLastItem = trackLastClick.get(size - 1);
//        secondLastItem.setPiece(null);
//        trackLastClick.clear();
//        switchPlayerTurn();
//
//    }

    // this code work
//    public void movePiece(Cell selectedCell, Cell to) {
//        if (selectedCell == null || to == null) {
//            return;
//        }
//
//        int fromX = selectedCell.getX();
//        int fromY = selectedCell.getY();
//        int toX = to.getX();
//        int toY = to.getY();
//
//        // Move the piece to the destination cell
//        to.setPiece(selectedCell.getPiece());
//        selectedCell.setPiece(null); // Clear the source cell after moving
//
//        // Capture opponent's piece if it's a valid capture move
//        int captureX = (fromX + toX) / 2; // Calculate x-coordinate of the captured piece
//        int captureY = (fromY + toY) / 2; // Calculate y-coordinate of the captured piece
//        Cell capturedCell = board[captureY][captureX];
//        boolean isKing = selectedCell.getPiece().isKing();
//        if (Math.abs(fromX - toX) == 2 && Math.abs(fromY - toY) == 2 && capturedCell.getPiece() != null) {
//            // Check if the piece is a king or if it's capturing forward or backward
//            if (isKing || (selectedCell.getPiece().getColor() == 'w' && toY < fromY) || (selectedCell.getPiece().getColor() == 'b' && toY > fromY)) {
//                capturedCell.setPiece(null); // Remove the captured piece from the board
//            }
//        }
//
//        if (to.getY() == 0 || to.getY() == BOARD_WIDTH - 1) {
//            // Promote the piece to a king
//            to.getPiece().pormoteToKing();
//        }
//
//        // Clear the selected cell after moving
//        availableMoves.clear();
//        selectedCell = null;
//        trackLastClick.clear();
//
//        switchPlayerTurn();
//    }

    public void movePiece(Cell selectedCell, Cell to) {
        if (selectedCell == null || to == null) {
            System.out.println("Error: Cannot move piece - selectedCell or destination cell is null.");
            return;
        }

        // Get the coordinates of the source and destination cells
        int fromX = selectedCell.getX();
        int fromY = selectedCell.getY();
        int toX = to.getX();
        int toY = to.getY();

        // Move the piece to the destination cell
        to.setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null); // Clear the source cell after moving

        // Calculate the coordinates of the captured piece, if any
        int captureX = (fromX + toX) / 2; // Calculate x-coordinate of the captured piece
        int captureY = (fromY + toY) / 2; // Calculate y-coordinate of the captured piece

        // Check if it's a valid capture move and there is a piece to capture
        if (Math.abs(fromX - toX) == 2 && Math.abs(fromY - toY) == 2) {
            Cell capturedCell = board[captureY][captureX];
            if (capturedCell.getPiece() != null && capturedCell.getPiece().getColor() != playerCurrentTurn) {
                capturedCell.setPiece(null); // Remove the captured piece from the board
            }
        }



        // Promote the piece to a king if it reaches the opposite end of the board
        if (toY == 0 || toY == BOARD_WIDTH - 1) {
            to.getPiece().pormoteToKing();
        }

        // Clear the selected cell after moving
        availableMoves.clear();
        selectedCell = null;
        trackLastClick.clear();

        // Switch player turn
        switchPlayerTurn();
    }


    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_WIDTH;
    }




//    public void movePiece(Cell selectedCellParm, Cell to) {
//        if (selectedCellParm == null || to == null) {
//            System.out.println("Error: Cannot move piece - selectedCell or destination cell is null.");
//            return;
//        }
//
//        // Check if the destination cell is in the list of available moves
//        if (!availableMoves.contains(to)) {
//            System.out.println("Error: Destination cell is not a valid move.");
//            return;
//        }
//
//        // Move the piece to the destination cell
//        to.setPiece(selectedCellParm.getPiece());
//
//        // Capture the opponent's piece if it's a capturing move
//        int xDiff = Math.abs(selectedCellParm.getX() - to.getX());
//        int yDiff = Math.abs(selectedCellParm.getY() - to.getY());
//        if (xDiff == 2 && yDiff == 2) {
//            // Capture the opponent's piece
//            int capturedX = (selectedCellParm.getX() + to.getX()) / 2;
//            int capturedY = (selectedCellParm.getY() + to.getY()) / 2;
//            board[capturedX][capturedY].setPiece(null);
//        }
//
//        // Clear the selected cell after moving
//        availableMoves.clear();
//        selectedCell = null;
//        switchPlayerTurn();
//    }

    // Method to switch player turn
    public void switchPlayerTurn() {
        if (playerCurrentTurn == 'w') {
            playerCurrentTurn = 'b';
        } else {
            playerCurrentTurn = 'w';
        }
    }


//    public ArrayList<Cell> determineAvailableMoves(Cell selectedCell) {
//        ArrayList<Cell> availableMoves = new ArrayList<>();
//
//        int x = selectedCell.getX();
//        int y = selectedCell.getY();
//        char color = selectedCell.getPiece().getColor();
//
//        // Check for available moves based on color
//        int dy = (color == 'w') ? 1 : -1; // Adjust direction based on color
//
//        // For non-king pieces, check two possible moves
//        // Check left diagonal move
//        int newXLeftTwo = x - dy;
//        int newXLeftThree = newXLeftTwo - dy;
//
//        int newYLeftTwo = y + dy;
//        int newYLeftThree = newYLeftTwo + dy;
//
//        int newXRightTwo = x + dy;
//        int newXRightThree = newXRightTwo + dy;
//
//
//        int newYRightTwo = y + dy;
//        int newYRightThree = newYRightTwo + dy;
//
//        if (isValidMove(newXLeftTwo, newYLeftTwo) && isEmpty(newXLeftTwo, newYLeftTwo)) {
//
//            availableMoves.add(board[newYLeftTwo][newXLeftTwo]);
//        }
//
//
//        if (isValidMove(newXLeftThree, newYLeftThree) && isEmpty(newXLeftThree, newYLeftThree) && !isEmpty(newXLeftTwo, newYLeftTwo)) {
//
//            availableMoves.add(board[newYLeftThree][newXLeftThree]);
//        }
//        if (isValidMove(newXRightThree, newYRightThree) && isEmpty(newXRightThree, newYRightThree) && !isEmpty(newXRightTwo, newYRightTwo)) {
//
//            availableMoves.add(board[newYRightThree][newXRightThree]);
//        }
//
//
//        if (isValidMove(newXRightTwo, newYRightTwo) && isEmpty(newXRightTwo, newYRightTwo)) {
//
//            availableMoves.add(board[newYRightTwo][newXRightTwo]);
//        }
//
//
//        return availableMoves;
//    }

        public ArrayList<Cell> determineAvailableMoves(Cell selectedCell) {
            ArrayList<Cell> availableMoves = new ArrayList<>();

            int x = selectedCell.getX();
            int y = selectedCell.getY();
            char color = selectedCell.getPiece().getColor();
            int dy = (color == 'w') ? 1 : -1; // Adjust direction based on color

            // Check left diagonal move for capturing
            int leftCaptureX = x - 1;
            int leftCaptureY = y + dy;
            if (isValidMove(leftCaptureX, leftCaptureY) && !isEmpty(leftCaptureX, leftCaptureY) &&
                    board[leftCaptureY][leftCaptureX].getPiece().getColor() != color) {
                int leftMoveX = x - 2;
                int leftMoveY = y + 2 * dy;
                if (isValidMove(leftMoveX, leftMoveY) && isEmpty(leftMoveX, leftMoveY)) {
                    availableMoves.add(board[leftMoveY][leftMoveX]);
                }
            }

            // Check right diagonal move for capturing
            int rightCaptureX = x + 1;
            int rightCaptureY = y + dy;
            if (isValidMove(rightCaptureX, rightCaptureY) && !isEmpty(rightCaptureX, rightCaptureY) &&
                    board[rightCaptureY][rightCaptureX].getPiece().getColor() != color) {
                int rightMoveX = x + 2;
                int rightMoveY = y + 2 * dy;
                if (isValidMove(rightMoveX, rightMoveY) && isEmpty(rightMoveX, rightMoveY)) {
                    availableMoves.add(board[rightMoveY][rightMoveX]);
                }
            }


            if (selectedCell.getPiece().isKing()) {
                for (int dx = -1; dx <= 1; dx += 2) {
                    for (int dyMove = -1; dyMove <= 1; dyMove += 2) {
                        int newX = x + dx;
                        int newY = y + (dy * dyMove);
                        while (isValidMove(newX, newY) && isEmpty(newX, newY)) {
                            availableMoves.add(board[newY][newX]);
                            newX += dx;
                            newY += (dy * dyMove);
                        }
                        // Check if the move is valid and there's an opponent's piece to capture
                        if (isValidMove(newX, newY) && !isEmpty(newX, newY) && board[newY][newX].getPiece().getColor() != color) {
                            newX += dx;
                            newY += (dy * dyMove);
                            if (isValidMove(newX, newY) && isEmpty(newX, newY)) {
                                availableMoves.add(board[newY][newX]);
                            }
                        }
                    }
                }
            }
            else{
                // Add regular moves (without capturing) in left and right directions
                int leftX = x - 1;
                int leftY = y + dy;
                int rightX = x + 1;
                int rightY = y + dy;

                System.out.println("it goes here");
                Cell leftCell = board[leftX][leftY];
                Cell rightCell = board[rightX][rightY];

                // Check if the piece can jump over its own piece and capture opponent's piece
                if (isValidMove(leftX, leftY) && isValidMove(leftX - 1, leftY + dy) && !isEmpty(leftX, leftY) &&
                        board[leftY][leftX].getPiece().getColor() == playerCurrentTurn &&
                        isValidMove(leftX - 1, leftY + 1 * dy) && isEmpty(leftX - 1, leftY + 1 * dy)) {
                    availableMoves.add(board[leftY + 1 * dy][leftX - 1]);
                }

                if (isValidMove(rightX, rightY) && isValidMove(rightX + 1, rightY + dy) && !isEmpty(rightX, rightY) &&
                        board[rightY][rightX].getPiece().getColor() == playerCurrentTurn &&
                        isValidMove(rightX + 1, rightY + 1 * dy) && isEmpty(rightX + 1, rightY + 1 * dy)) {
                    availableMoves.add(board[rightY + 1 * dy][rightX + 1]);
                }


                if (isValidMove(leftX, leftY) && isEmpty(leftX, leftY)) {
                    availableMoves.add(board[leftY][leftX]);
                }

                if (isValidMove(rightX, rightY) && isEmpty(rightX, rightY)) {
                    availableMoves.add(board[rightY][rightX]);
                }
            }

            return availableMoves;
        }

    private boolean isValidMove(int x, int y) {
        if (x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_WIDTH) {
            return true;
        } else {
            return false;
        }


    }


    private boolean isEmpty(int x, int y) {

        if (board[y][x].getPiece() == null) {
            return true;
        } else {
            return false;
        }
        //return board[x][y].getPiece() == null;

    }

    public void highlightAvailableMoves() {
        if (selectedCell != null) {
            for (Cell cell : availableMoves) {
                // Calculate the screen coordinates based on the cell positions
                float screenX = cell.getX() * CELLSIZE + CELLSIZE / 2;
                float screenY = cell.getY() * CELLSIZE + CELLSIZE / 2;


                // Highlight the cell in blue
                fill(coloursRGB[2][(cell.getX() + cell.getY()) % 2][0], coloursRGB[2][(cell.getX() + cell.getY()) % 2][1], coloursRGB[2][(cell.getX() + cell.getY()) % 2][2]);
                ellipse(screenX, screenY, CELLSIZE * 0.6f, CELLSIZE * 0.6f);
            }
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        this.noStroke();
        background(180);
        // draw the board
        // i = y coordinate
        // i2 = x coordinate
        int blackPieces = 0;
        int whitePieces = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int i2 = 0; i2 < BOARD_WIDTH; i2++) {

                if (this.board[i][i2].getPiece() != null) {
                    if (this.board[i][i2].getPiece().getColor() == 'w') {
                        whitePieces += 1;
                    }
                    if (this.board[i][i2].getPiece().getColor() == 'b') {
                        blackPieces += 1;
                    }
                }
                board[i][i2].draw(this);
            }
        }
        highlightAvailableMoves();
        // ellipse(mouseX, mouseY, CELLSIZE*0.8f, CELLSIZE*0.8f);

        // draw highlighted cells

        // check if the any player has no more pieces. The winner is the player who
        // still has pieces remaining

        stroke(0);
        fill(255);
        if (whitePieces == 0 || blackPieces == 0) {
            rect(WIDTH * 0.33f, HEIGHT * 0.35f, 140, 40);
        }
        textSize(24f);
        fill(0);
        if (whitePieces == 0) {
            text("Black wins!", WIDTH * 0.35f, HEIGHT * 0.43f);
        } else if (blackPieces == 0) {
            text("White wins!", WIDTH * 0.35f, HEIGHT * 0.43f);
        }
    }

    /**
     * Set fill colour for cell background
     *
     * @param colourCode   The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may
     *                     have different shades
     */
    public void setFill(int colourCode, int blackOrWhite) {
        this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
    }

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }

}
