package ru.vsu.cs.cards;

import ru.vsu.cs.additions.WindowComponentsUtils;

import javax.swing.*;

public class FreeParkingCard extends MonopolyCard{
    private JTextPane textPane;

    public FreeParkingCard(int cardId) {
        super(cardId);

        super.setName("Free Parking");
        super.setToolTipText(
                "<html>Бесплатная<br>Парковка<br>Паркуются: " + (this.getPlayersOnCard().isEmpty() ? "Никто" : this.getPlayersOnCard()) + "</html>"
        );
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
        WindowComponentsUtils.addTextPaneToFreeParkingCardAndSetTextAtHer(this, textPane);
    }

    @Override
    public String toString() {
        return "Карта Бесплатной Парковки";
    }
}
