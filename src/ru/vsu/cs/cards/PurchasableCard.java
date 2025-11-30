package ru.vsu.cs.cards;

import ru.vsu.cs.game.Player;

public interface PurchasableCard {
    Player getOwner();
    void setOwner(Player player);
    int getPurchasePrice();
}