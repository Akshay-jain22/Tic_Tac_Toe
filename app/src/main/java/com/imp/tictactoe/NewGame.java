package com.imp.tictactoe;

public class NewGame {

    // Points
    private final int DRAW_POINTS = 0;
    private final int LOST_POINTS = 0;
    private final int WON_POINTS = 1;

    // Games Played
    private int games_played = 0;

    // Players
    private final Player player1;
    private final Player player2;

    Player X_player;
    Player O_player;


    NewGame(String player1Name, String player2Name) {
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);

        X_player = player1;
        O_player = player2;
    }


    void switchPlayers() {
        Player temp = X_player;
        X_player = O_player;
        O_player = temp;
    }


    void won(Player player) {
        player.add(WON_POINTS);
    }


    public void draw() {
        player1.add(DRAW_POINTS);
        player2.add(DRAW_POINTS);
    }


    Player getTopScorer() {
        if (player1.getPoints() > player2.getPoints()) {
            return player1;
        } else {
            return player2;
        }
    }


    Player getRunner() {
        if (player1.getPoints() > player2.getPoints()) {
            return player2;
        } else {
            return player1;
        }
    }


    void replay() {
        ++games_played;
        switchPlayers();
    }

}



class Player {

    // Player Data
    private final String name;
    private int points;


    Player(String name) {
        this.name = name;
        this.points = 0;
    }


    String getName() {
        return name;
    }


    void add(int points) {
        this.points += points;
    }


    int getPoints() {
        return points;
    }

}
