package ru.vsu.cs.cards;

import ru.vsu.cs.additions.ChanceDeck;

public class ChanceCard extends MonopolyCard {
    private ChanceDeck deck = new ChanceDeck();

    public ChanceCard(int cardId) {
        super(cardId);

        super.setName("Chance Card" + cardId);
        super.setToolTipText("Шанс");
    }

    public ChanceDeck getDeck() {
        return deck;
    }

    public void setDeck(ChanceDeck deck) {
        this.deck = deck;
    }

    @Override
    public String toString() {
        return "Карта Шанса";
    }
}
