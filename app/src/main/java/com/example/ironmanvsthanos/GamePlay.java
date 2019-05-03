package com.example.ironmanvsthanos;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GamePlay extends AppCompatActivity {

    MediaPlayer avengersSoundTrack;

    //T ==> Thanos, I ==> Ironman, E ==> empty grid
    char currentPlayer = 'T';

    //keep track of what grids are filled
    char[] gridPos = {'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E'};

    //all winning possibilities
    int[][] winningCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
            {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};


    //determines when to terminate the game
    boolean isGameActive = true;

    //onClick() definitions
    public void displayImage(View view) {


        MediaPlayer fatSound_gridMoves = MediaPlayer.create(this, R.raw.fatsound);
        TextView displayWinner = findViewById(R.id.displayWinner);
        ImageView winnerImage = findViewById(R.id.winnerImage);
        Button resetGameBoard = findViewById(R.id.resetBoard);

        //play sound on button click
        fatSound_gridMoves.start();

        //typecast the image view to an image
        ImageView gridImage = (ImageView) view;

        //get the position of the image object by using its tag
        int imagePos = Integer.parseInt(gridImage.getTag().toString());

        //keep playing the game for as long as there are - E - empty grids to be filled
        if (gridPos[imagePos] == 'E' && isGameActive) {

            //keep track of what grids are filled
            gridPos[imagePos] = currentPlayer;

            //move image to the left out of view by 1600 pixels
            gridImage.setTranslationX(-1500);

            //toggle between thanos and ironman heads
            if (currentPlayer == 'T') {
                gridImage.setImageResource(R.drawable.thanos_head);
                currentPlayer = 'I';
            }
            else {
                gridImage.setImageResource(R.drawable.ironman_head);
                currentPlayer = 'T';
            }

            //move image to the view from the right
            gridImage.animate().translationXBy(1500).rotation(3600).setDuration(300);

            //detect game wins by comparing each winning combination with
            //if a == b and b == c, then a == c - must be equal
            for (int[] winningPosition : winningCombinations) {
                if (gridPos[winningPosition[0]] == gridPos[winningPosition[1]] && gridPos[winningPosition[1]] == gridPos[winningPosition[2]] &&
                        gridPos[winningPosition[0]] != 'E') {

                    //quit game when someone has won
                    isGameActive = false;

                    //display winner
                    if(currentPlayer == 'T'){
                        displayWinner.setVisibility(View.VISIBLE);
                        displayWinner.setText("Iron Man Wins!");
                        displayWinner.setTextColor(Color.parseColor("#f53f00"));
                        winnerImage.setImageResource(R.drawable.ironmansnaps);
                        winnerImage.setVisibility(View.VISIBLE);
                        resetGameBoard.setVisibility(View.VISIBLE);
                        avengersSoundTrack.start();
                    }
                    else if(currentPlayer == 'I'){
                        displayWinner.setVisibility(View.VISIBLE);
                        displayWinner.setText("Thanos Wins!");
                        displayWinner.setTextColor(Color.parseColor("#5e35b1"));
                        winnerImage.setImageResource(R.drawable.thanos);
                        winnerImage.setVisibility(View.VISIBLE);
                        resetGameBoard.setVisibility(View.VISIBLE);
                        avengersSoundTrack.start();
                    }
                    else {
                        //Toast.makeText(this, "Game Is A Tie!\nNo One Has All The Stones", Toast.LENGTH_LONG).show();
                        displayWinner.setText("Game is A Tie!");
                        resetGameBoard.setVisibility(View.VISIBLE);
                    }

                }

            }
        }
    }


    public void resetBoard(View view){

        GridLayout gameBoard = findViewById(R.id.gameBoard);
        TextView displayWinner = findViewById(R.id.displayWinner);
        ImageView winnerImage = findViewById(R.id.winnerImage);
        Button resetGameBoard = findViewById(R.id.resetBoard);

        //make the views disappear when the user decides to play again
        displayWinner.setVisibility(View.INVISIBLE);
        winnerImage.setVisibility(View.INVISIBLE);
        resetGameBoard.setVisibility(View.INVISIBLE);

        //stop playing all audio files on game restart
        if(avengersSoundTrack.isPlaying()){
            avengersSoundTrack.stop();
            avengersSoundTrack = MediaPlayer.create(this, R.raw.avengerssoundtrack);
        }


        //put the default image back on the screen
        winnerImage.setImageResource(R.drawable.thanosironmans);

        //remove all the image views in the gridlayout
        for(int i = 0; i < gameBoard.getChildCount(); i++){

            //get the imageView object
            ImageView gridImage = (ImageView) gameBoard.getChildAt(i);

            //remove the imageView source by resetting git back to null
            gridImage.setImageDrawable(null);

            //reset the game back to its original state
            currentPlayer = 'T';
            for(int j = 0; j < gridPos.length; j++){
                gridPos[j] = 'E';
            }
            isGameActive = true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        //set the title of the game play activity
        this.setTitle("Ironman vs. Thanos Gameplay");

        avengersSoundTrack = MediaPlayer.create(this, R.raw.avengerssoundtrack);
    }
}
