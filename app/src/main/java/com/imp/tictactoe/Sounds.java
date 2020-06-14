package com.imp.tictactoe;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.ImageView;

class Sounds {

    // Sound Settings
    static boolean enableSound;

    final MediaPlayer change;
    final MediaPlayer game;
    static MediaPlayer game_music;
    final MediaPlayer start_game;
    final MediaPlayer tap;
    final MediaPlayer game_end;

    Sounds(Activity activity) {
        change = MediaPlayer.create(activity, R.raw.change);
        game = MediaPlayer.create(activity, R.raw.game);
        game.setLooping(true);
        start_game = MediaPlayer.create(activity, R.raw.start_game);
        tap = MediaPlayer.create(activity, R.raw.tap);
        game_end = MediaPlayer.create(activity, R.raw.game_end);
    }

    static void play(MediaPlayer sound) {
        if (enableSound) {
            sound.start();
        }
    }

    static void stop(MediaPlayer sound) {
        if (enableSound) {
            sound.pause();
        }
    }

    static void mute(ImageView muteButton) {
        if(enableSound) {
            stop(Sounds.game_music);
            enableSound = false;
            muteButton.setImageResource(R.drawable.sound_off);
        } else {
            enableSound = true;
            play(Sounds.game_music);
            muteButton.setImageResource(R.drawable.sound_on);
        }
    }
}