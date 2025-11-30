package ru.vsu.cs.cards;

import ru.vsu.cs.additions.SubwaySet;
import ru.vsu.cs.game.Player;

public class SubwayCard extends MonopolyCard implements PurchasableCard{
    private final String subwayName;
    private final int[] rentPrices = new int[] {25, 50, 100, 200};
    private SubwaySet subwaySet = null;
    private Player owner = null;
    private final int purchasePrice = 200;
    private int currentRentPrice;

    public SubwayCard(int cardId, String subwayName) {
        super(cardId);
        this.subwayName = subwayName;
        currentRentPrice = rentPrices[0];

        super.setName(this.subwayName);
        super.setToolTipText(String.format("<html>%s<br>Владелец: Никто</html>", this.subwayName));
    }

    public int[] getRentPrices() {
        return rentPrices;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getSubwayName() {
        return subwayName;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public SubwaySet getSubwaySet() {
        return subwaySet;
    }

    public void setSubwaySet(SubwaySet subwaySet) {
        this.subwaySet = subwaySet;
    }

    public int getCurrentRentPrice() {
        return currentRentPrice;
    }

    public void setCurrentRentPrice(int currentRentPrice) {
        this.currentRentPrice = currentRentPrice;
    }

    public void calcCorrectRentPrice() {
        int countCardsHasThisOwner = this.subwaySet.getCountOfCardsWithThisOwner(this.owner);
        this.setCurrentRentPrice(this.rentPrices[countCardsHasThisOwner - 1]);
    }

    @Override
    public String toString() {
        return String.format("%s", this.subwayName);
    }
}