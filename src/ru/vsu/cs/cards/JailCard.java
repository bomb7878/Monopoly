package ru.vsu.cs.cards;

import ru.vsu.cs.additions.WindowComponentsUtils;
import ru.vsu.cs.game.Player;

import javax.swing.*;
import java.awt.*;

public class JailCard extends MonopolyCard{
    private JTextPane textPane;

    public JailCard(int cardId) {
        super(cardId);

        super.setName("Jail");
        super.setToolTipText("<html>Тюрьма<br>заключены: " + (this.getPlayersOnCard().isEmpty() ? "Никто" : this.getPlayersOnCard()) + "</html>" );
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
        WindowComponentsUtils.addTextPaneToJailCardAndSetTextAtHer(this, textPane);
    }

    public void imprisonPlayer(Player player) {
        player.setMoveBan(3);
    }

    @Override
    public String toString() {
        return "Карта Тюрьмы";
    }
}
