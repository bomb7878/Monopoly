package ru.vsu.cs.cards;

import ru.vsu.cs.additions.WindowComponentsUtils;
import ru.vsu.cs.game.Player;

import javax.swing.*;

public class StartAndFinishCard  extends MonopolyCard {
    private JTextPane textPane;

    public StartAndFinishCard(int cardId) {
        super(cardId);
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
        WindowComponentsUtils.addTextPaneToStartAndFinishCardAndSetTextAtHer(this, textPane);
    }

    public void playerStartNewCircle(Player player) {
        player.increaseMoney(200);
    }

    @Override
    public String toString() {
        return "Карта Начала Круга";
    }
}
