package com.imp.tictactoe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class Game extends AppCompatActivity {

    // Game Data
    private boolean gameActive = true, draw = true;
    private int activePlayer = 0;
    private int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    /*
     *   State meanings:
     *   0 - X
     *   1 - O
     *   2 - Null
     */

    private NewGame game;
    private Sounds sounds;


    // Views & Widgets
    private ImageView turnLogo;
    private TextView turnText;
    private ImageView[] grids = new ImageView[9];

    // PopUp Views & Widgets
    protected View popUpScoreboardView;
    protected PopupWindow popUpScoreboard;
    TextView topScorerName;
    TextView topScorerScore;
    TextView runnerName;
    TextView runnerScore;
    Button endGame;
    Button replay;


    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sounds = new Sounds(this);
        Sounds.play(sounds.start_game);

        turnLogo = findViewById(R.id.turnLogo);
        turnText = findViewById(R.id.turnText);

        grids[0] = findViewById(R.id.grid1);
        grids[1] = findViewById(R.id.grid2);
        grids[2] = findViewById(R.id.grid3);
        grids[3] = findViewById(R.id.grid4);
        grids[4] = findViewById(R.id.grid5);
        grids[5] = findViewById(R.id.grid6);
        grids[6] = findViewById(R.id.grid7);
        grids[7] = findViewById(R.id.grid8);
        grids[8] = findViewById(R.id.grid9);

        activePlayer = 0;

        String player1name = getIntent().getStringExtra("player1name");
        String player2name = getIntent().getStringExtra("player2name");

        game = new NewGame(player1name, player2name);

        turnLogo.setImageResource(R.drawable.ic_cross);
        turnText.setText(game.X_player.getName());

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        popUpScoreboardView = inflater.inflate(R.layout.pop_up_scoreboard, null);
        popUpScoreboard = new PopupWindow(popUpScoreboardView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        topScorerName = popUpScoreboardView.findViewById(R.id.top_scorer_name);
        topScorerScore = popUpScoreboardView.findViewById(R.id.top_scorer_score);
        runnerName = popUpScoreboardView.findViewById(R.id.runner_name);
        runnerScore = popUpScoreboardView.findViewById(R.id.runner_score);
        endGame = popUpScoreboardView.findViewById(R.id.end_game_button);
        replay = popUpScoreboardView.findViewById(R.id.replay_button);

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(sounds.change);
                popUpScoreboard.dismiss();
                shouldEndGame = true;
                finish();
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpScoreboard.dismiss();
                clearGame();
                gameActive = true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        Sounds.play(Sounds.game_music);
    }


    @Override
    protected void onPause() {
        super.onPause();

        Sounds.stop(Sounds.game_music);
    }


    // The activity will finish only when "End Game" button is pressed
    // shouldEndGame is set to true in endGame button's onClickListener
    boolean shouldEndGame = false;
    @Override
    public void finish() {
        if(shouldEndGame) {
            super.finish();
        } else {
            popUpScoreboard();
        }
    }


    protected void clearGame() {
        Arrays.fill(gameState, 2);

        for (ImageView imageView : grids) {
            imageView.setImageResource(0);
        }

        // This will switch players also
        game.replay();

        activePlayer = 0;
        turnLogo.setVisibility(View.VISIBLE);
        turnLogo.setImageResource(R.drawable.ic_cross);
        turnText.setText(game.X_player.getName());
        turnText.setTextColor(getResources().getColor(R.color.colorAccent));

        Sounds.play(sounds.start_game);
    }


    @SuppressLint("SetTextI18n")
    public void gridTap(View view) {
        ImageView tapImage = (ImageView) view;
        int tappedImage = parseInt(tapImage.getTag().toString());

        if (!gameActive) {
            popUpScoreboard();
        } else {
            Sounds.play(sounds.tap);
            if (gameState[tappedImage] == 2) {
                draw = true;
                gameState[tappedImage] = activePlayer;

                // activePlayer is being changed before checking for win
                if (activePlayer == 0) {
                    tapImage.setImageResource(R.drawable.ic_cross);
                    activePlayer = 1;
                    turnLogo.setImageResource(R.drawable.ic_zero);
                    turnText.setText(game.O_player.getName());
                    turnText.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tapImage.setImageResource(R.drawable.ic_zero);
                    activePlayer = 0;
                    turnLogo.setImageResource(R.drawable.ic_cross);
                    turnText.setText(game.X_player.getName());
                    turnText.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
            // Check if any player has won
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[0]] != 2) {
                    gameActive = false;
                    if (gameState[winPosition[0]] == 0) turnLogo.setImageResource(R.drawable.ic_cross);
                    else turnLogo.setImageResource(R.drawable.ic_zero);

                    // activePlayer has changed. Therefore send the opposite player
                    turnText.setText(((activePlayer == 1) ? game.X_player.getName() : game.O_player.getName()) + " wins");
                    turnText.setTextColor(getResources().getColor((activePlayer == 1) ? R.color.colorAccent : R.color.colorPrimary));
                    game.won((activePlayer == 1) ? game.X_player : game.O_player);

                    turnLogo.setVisibility(View.GONE);
                    draw = false;
                    popUpScoreboard();

                    Sounds.play(sounds.game_end);
                }
            }
        }

        if (gameActive && draw) {
            for (int i : gameState)
                if (i == 2) {
                    draw = false;
                    break;
                }
            if (draw) {
                turnText.setText("DRAW : ");
                turnLogo.setImageResource(R.drawable.ic_draw);
                gameActive = false;

                game.draw();
                popUpScoreboard();
                Sounds.play(sounds.game_end);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    void popUpScoreboard() {
        Sounds.play(sounds.change);

        topScorerName.setText(game.getTopScorer().getName());
        topScorerScore.setText(Integer.toString(game.getTopScorer().getPoints()));
        runnerName.setText(game.getRunner().getName());
        runnerScore.setText(Integer.toString(game.getRunner().getPoints()));

        popUpScoreboard.setAnimationStyle(R.style.pop_animation);
        popUpScoreboard.showAtLocation(popUpScoreboardView, Gravity.CENTER, 0, 0);
    }

}


