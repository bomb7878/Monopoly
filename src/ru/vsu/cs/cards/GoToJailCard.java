package ru.vsu.cs.cards;

import ru.vsu.cs.additions.WindowComponentsUtils;
import ru.vsu.cs.game.GameLogic;
import ru.vsu.cs.game.Player;

import javax.swing.*;

public class GoToJailCard extends MonopolyCard {
    private JTextPane textPane;
    private JailCard jail;

    public GoToJailCard(int cardId, JailCard jail) {
        super(cardId);
        this.jail = jail;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
        WindowComponentsUtils.addTextPaneToGoToJailCardAndSetTextAtHer(this, textPane);
    }

    public void onPlayerLanding(Player player, GameLogic gameLogic) {
        gameLogic.movePlayerToJail(player);
    }

    @Override
    public String toString() {
        return "Карта Заключения в Тюрьму";
    }
}
