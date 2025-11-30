package ru.vsu.cs.game;

import ru.vsu.cs.window.MonopolyWindow;
import ru.vsu.cs.window.SelectPlayersCountWindow;

import java.util.ArrayList;

public class Game {
    public static void main(String[] args) throws Exception {
        SelectPlayersCountWindow selectPlayersCountWindow = new SelectPlayersCountWindow();
        selectPlayersCountWindow.setVisible(true);

        String[] playersNames = new String[0];
        if (selectPlayersCountWindow.isConfirmed()) {
            playersNames = selectPlayersCountWindow.getPlayersNames();
        }
        int playersCount = selectPlayersCountWindow.getPlayersCount();
        ArrayList<Player> players = new ArrayList<>(playersCount);
        for (int i = 1; i < playersCount + 1; i++) {
            players.add(new Player(i, playersNames[i - 1]));
        }

        MonopolyWindow monopolyWindow = new MonopolyWindow();
        monopolyWindow.winMain();
        monopolyWindow.setPlayers(players);

        GameLogic logic = new GameLogic(monopolyWindow, players);
        logic.startGame();
    }
}
