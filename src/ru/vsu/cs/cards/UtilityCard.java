package ru.vsu.cs.cards;

import ru.vsu.cs.game.Player;

public class UtilityCard extends MonopolyCard  implements PurchasableCard{
    private final String cardName;
    private final int purchasePrice = 150;
    private Player owner = null;
    private UtilityCard anotherCard = null;

    public UtilityCard(int cardId, String cardName) {
        super(cardId);
        this.cardName = cardName;

        super.setName(this.cardName);
        super.setToolTipText(String.format("<html>%s<br>Владелец: Никто</html>", this.cardName));
    }

    public String getCardName() {
        return cardName;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public UtilityCard getAnotherCard() {
        return anotherCard;
    }

    public void setAnotherCard(UtilityCard anotherCard) {
        this.anotherCard = anotherCard;
    }

    public int calcRentPrice(int diceDrop) {
        if (this.getAnotherCard().getOwner() != null) {
            return 10 * diceDrop;
        } else {
            return 4 * diceDrop;
        }
    }

    @Override
    public String toString() {
        return String.format("%s", cardName);
    }
}
