package ru.vsu.cs.cards;

import ru.vsu.cs.additions.CommunityChestDeck;

public class CommunityChestCard extends MonopolyCard{
    private CommunityChestDeck deck = new CommunityChestDeck();

    public CommunityChestCard(int cardId) {
        super(cardId);

        super.setName("Community Chest Card" + cardId);
        super.setToolTipText("Общественная казна");
    }

    public CommunityChestDeck getDeck() {
        return deck;
    }

    public void setDeck(CommunityChestDeck deck) {
        this.deck = deck;
    }

    @Override
    public String toString() {
        return "Карта Общественной Казны";
    }
}
