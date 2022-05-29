package com.example.a383349.tictactoejason;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TicTacToeJason extends Activity implements View.OnClickListener {
    //create the board
    Button[][] grid = new Button[3][3];
    int gameBoard[][] = new int[3][3];
    final int BLANK = 0;
    final int X_MOVE = 1;
    final int O_MOVE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_jason);
        //create all the buttons
        grid[0][0] = (Button) this.findViewById(R.id.button1);
        grid[0][1] = (Button) this.findViewById(R.id.button2);
        grid[0][2] = (Button) this.findViewById(R.id.button3);
        grid[1][0] = (Button) this.findViewById(R.id.button4);
        grid[1][1] = (Button) this.findViewById(R.id.button5);
        grid[1][2] = (Button) this.findViewById(R.id.button6);
        grid[2][0] = (Button) this.findViewById(R.id.button7);
        grid[2][1] = (Button) this.findViewById(R.id.button8);
        grid[2][2] = (Button) this.findViewById(R.id.button9);
        resetBoard();
        //add a action listener for all of the buttons.
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                grid[x][y].setOnClickListener(this);
            }
        }
    }

    public void onClick(View v) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "";
        int[][] gameBoardBlank = new int[3][3];
        //b is the buttons pressed
        Button b = (Button) v;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                //find which button was pressed
                if (b.equals(grid[x][y])) {
                    //check if the square they pressed was blank
                    if (gameBoard[x][y] == BLANK) {
                        //if so, mark both boards
                        b.setText("X");
                        b.setEnabled(false);
                        gameBoard[x][y] = X_MOVE;
                        if (checkWin(X_MOVE, gameBoard) == true) { // check if x wins, if so say so
                            text = "X wins!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            resetBoard();
                            //reset board
                            gameBoard = gameBoardBlank;
                            return;
                        }
                        else if (checkTie(gameBoard) == true) { // check if it was a tie
                            text = "It was a tie!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            resetBoard();
                            //reset board
                            gameBoard = gameBoardBlank;
                            return;
                        }
                    }
                }
            }
        }
        //make the ai move
        String aiMove = findBestMove(gameBoard);
        int move1 = Character.getNumericValue(aiMove.charAt(0));
        int move2 = Character.getNumericValue(aiMove.charAt(2));
        gameBoard[move1][move2] = O_MOVE;
        grid[move1][move2].setText("O");
        grid[move1][move2].setEnabled(false);
        if (checkWin(O_MOVE, gameBoard) == true) { // check if o wins, if so say so
            text = "O wins!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            resetBoard();
            //reset the board
            gameBoard = gameBoardBlank;
        } else if (checkTie(gameBoard) == true) { // check if it was a tie
            text = "It was a tie!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            resetBoard();
            //reset the board
            gameBoard = gameBoardBlank;
        }
    }
    //resets the board by resenting all the buttons to blank.
    public void resetBoard(){
        for(int x = 0; x<3;x++){
            for(int y = 0; y<3;y++){
                grid[x][y].setText("");
                grid[x][y].setEnabled(true);

            }
        }
    }
    //check to see if game is a tie by checking if all the squares are full
    public boolean checkTie(int board[][]) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == BLANK) {
                    return false; // check if all of the squares are filled, if so it is a tie
                }
            }
        }
        return true;
    }

    public boolean checkWin(int player, int board[][]) {
        // check all the win conditions
        if (board[0][0] == player && board[0][1] == player && board[0][2] == player) {
            return true;
        }

        if (board[1][0] == player && board[1][1] == player && board[1][2] == player) {
            return true;
        }
        if (board[2][0] == player && board[2][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][0] == player && board[1][0] == player && board[2][0] == player) {
            return true;
        }
        if (board[0][1] == player && board[1][1] == player && board[2][1] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][2] == player && board[2][2] == player) {
            return true;
        }
        if (board[2][0] == player && board[1][1] == player && board[0][2] == player) {
            return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        return false;
    }
    //ai function
    public int minimax(int board[][], int depth, boolean isMaximizingPlayer) {
        //first start off by checking if anyone won
        if (checkWin(O_MOVE, board)) {
            return 10;
        } else if (checkWin(X_MOVE, board)) {
            return -10;
        } else if (checkTie(board)) {
            return 0;
        }
        //check who's turn it is
        //if it's o's turn then continue
        if (isMaximizingPlayer) {
            int bestVal = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 3; k++) {
                    //loop through all blank squares on the board
                    if (board[i][k] == BLANK) {
                        //try the move
                        board[i][k] = O_MOVE;
                        //recall the function, so that the ai will place moves until someone won or its a tie then get the score of the previous move
                        int value = minimax(board, depth + 1, false);
                        //get the best value
                        bestVal = Math.max(bestVal, value);
                        //reset the square you placed in
                        board[i][k] = BLANK;

                    }
                }
            }
            return bestVal;
        }
        //exact same case as above, but just for the human
        else {
            int bestVal = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 3; k++) {
                    if (board[i][k] == BLANK) {
                        board[i][k] = X_MOVE;
                        int value = minimax(board, depth + 1, true);
                        bestVal = Math.min(bestVal, value);
                        board[i][k] = BLANK;

                    }
                }
            }
            return bestVal;

        }
    }

    public String findBestMove(int board[][]) {
        int bestVal = -1000;
        int row = -1;
        int col = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //loop through all the blank squares
                if (board[i][j] == BLANK) {
                    //try a move, and call the mininmax function above
                    board[i][j] = O_MOVE;
                    int moveVal = minimax(board, 0, false);
                    //once a move is found, reset that square to be blank
                    board[i][j] = BLANK;
                    //check if that move is the best one so far, if so remember the row and col
                    if (moveVal > bestVal) {
                        row = i;
                        col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        //return row and col
        return (row + "," + col);

    }
}
