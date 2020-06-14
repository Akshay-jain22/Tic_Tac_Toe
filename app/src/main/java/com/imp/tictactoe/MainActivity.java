package com.imp.tictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Sounds sounds;

    // Popup Views and Widgets
    PopupWindow newGamePopUp;
    View newGamePopUpView;
    Button playButton;
    EditText player1;
    EditText player2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sounds = new Sounds(this);
        settings();

        final ImageView info = findViewById(R.id.info);
        final ImageView mute = findViewById(R.id.mute);
        final Button newGame = findViewById(R.id.newGame);
        newGame.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(sounds.change);
                Intent intent2 = new Intent(MainActivity.this, Info.class);
                startActivity(intent2);
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(sounds.tap);
                Sounds.mute(mute);
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(sounds.change);
                startNewGame();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        Sounds.stop(Sounds.game_music);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Sounds.stop(Sounds.game_music);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Sounds.play(Sounds.game_music);
    }


    @SuppressLint({"InflateParams", "SetTextI18n"})
    void startNewGame() {
        newGamePopUpView = getLayoutInflater().inflate(R.layout.pop_up_new_game, null);
        newGamePopUp = new PopupWindow(newGamePopUpView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        playButton = newGamePopUpView.findViewById(R.id.play_button);
        player1 = newGamePopUpView.findViewById(R.id.player1_edit_text);
        player2 = newGamePopUpView.findViewById(R.id.player2_edit_text);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(sounds.change);
                if(player1.getText().toString().equals("") || player2.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Player names cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    newGamePopUp.dismiss();
                    Intent intent1 = new Intent(MainActivity.this, Game.class);
                    intent1.putExtra("player1name", player1.getText().toString());
                    intent1.putExtra("player2name", player2.getText().toString());
                    startActivity(intent1);
                }
            }
        });

        player1.setText("Player 1");
        player2.setText("Player 2");
        newGamePopUp.setAnimationStyle(R.style.pop_animation);
        newGamePopUp.showAtLocation(newGamePopUpView, Gravity.CENTER, 0, 0);
    }


    void settings() {
        Sounds.enableSound = true;

        Sounds.game_music = MediaPlayer.create(this, R.raw.game_music);
        Sounds.game_music.setLooping(true);
    }

}
