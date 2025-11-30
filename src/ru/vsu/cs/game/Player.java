package ru.vsu.cs.game;

import ru.vsu.cs.additions.PlayerShapeFactory;
import ru.vsu.cs.cards.MonopolyCard;
import ru.vsu.cs.cards.StreetCard;
import ru.vsu.cs.cards.SubwayCard;
import ru.vsu.cs.cards.UtilityCard;
import ru.vsu.cs.util.SwingUtils;
import ru.vsu.cs.window.MonopolyWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final int id;
    private final String playerName;
    private int money;
    private final Color color;
    private final ArrayList<StreetCard> streetsInventory;
    private final ArrayList<SubwayCard> subwaysInventory;
    private final ArrayList<UtilityCard> utilitiesInventory;
    private int position = 0;
    private boolean isBankrupt = false;
    private int moveBan = 0;

    public Player(int id, String playerName) {
        this.id = id;
        this.playerName = playerName;
        this.money = 1500;
        this.color = PlayerShapeFactory.getColorById(id);
        this.streetsInventory = new ArrayList<>();
        this.subwaysInventory = new ArrayList<>();
        this.utilitiesInventory = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getMoney() {
        return money;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public void increaseMoney(int amount) {
        money += amount;
    }

    public Color getColor() {
        return color;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public ArrayList<StreetCard> getStreetsInventory() {
        return streetsInventory;
    }

    public ArrayList<SubwayCard> getSubwaysInventory() {
        return subwaysInventory;
    }

    public ArrayList<UtilityCard> getUtilitiesInventory() {
        return utilitiesInventory;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMoveBan() {
        return moveBan;
    }

    public void setMoveBan(int moveBan) {
        this.moveBan = moveBan;
    }

    public void buyCard(StreetCard card, int price) {
        this.decreaseMoney(price);
        streetsInventory.add(card);
        card.setOwner(this);

        card.setToolTipText(String.format(
                        "<html>%s<br>Владелец: %s</html>",
                        card.getStreetName(),
                        this.getPlayerName()
                )
        );
    }

    public void buyCard(SubwayCard card, int price) {
        this.decreaseMoney(price);
        subwaysInventory.add(card);
        card.setOwner(this);

        card.setToolTipText(String.format(
                        "<html>%s<br>Владелец: %s</html>",
                        card.getSubwayName(),
                        this.getPlayerName()
                )
        );
    }

    public void buyCard(UtilityCard card, int price) {
        this.decreaseMoney(price);
        utilitiesInventory.add(card);
        card.setOwner(this);

        card.setToolTipText(String.format(
                        "<html>%s<br>Владелец: %s</html>",
                        card.getCardName(),
                        this.getPlayerName()
                )
        );
    }

    public boolean mortgageCard(StreetCard card) {
        if (card.getHouseCount() == 0 && card.getHotelCount() == 0 &&
                !card.isMortgaged() && streetsInventory.contains(card)) {
            card.setMortgaged(true);
            this.increaseMoney(card.getCollateralPrice());
            return true;
        }

        return false;
    }

    // Метод для выкупа карты из залога
    public boolean withdrawFromCollateralCard(StreetCard card) {
        double redemptionPrice = 0;

        if (card.isMortgaged() && streetsInventory.contains(card)) {
            redemptionPrice = card.getRedemptionPrice();
        }


        if (redemptionPrice > 0 && this.money >= redemptionPrice) {
            this.decreaseMoney((int) redemptionPrice);

            card.setMortgaged(false);

            return true;
        }
        return false;
    }

    // Метод для продажи дома/отеля
    public boolean sellBuildings(StreetCard street) {
        if (streetsInventory.contains(street) && !street.isMortgaged()) {
            if (street.getHotelCount() > 0) { // Продажа отеля
                street.setHotelCount(0);
                street.setHouseCount(4); // Отель превращается в 4 дома
                this.increaseMoney(street.getHotelPrice() / 2);
                street.calcCorrectRentPrice();
                return true;
            } else if (street.getHouseCount() > 0) { // Продажа дома
                street.setHouseCount(street.getHouseCount() - 1);
                this.increaseMoney(street.getHousePrice() / 2);
                street.calcCorrectRentPrice();
                return true;
            }
        }
        return false;
    }

    // Проверка, может ли игрок оплатить
    public boolean canPay(int amount) {
        if (this.money >= amount) {
            return true;
        }

        return hasSellableAssets();
    }

    // Проверка наличия активов для продажи
    private boolean hasSellableAssets() {
        // Проверяем дома/отели
        for (StreetCard street : streetsInventory) {
            if (!street.isMortgaged() && (street.getHouseCount() > 0 || street.getHotelCount() > 0)) {
                return true;
            }
        }

        // Проверяем незаложенные улицы
        for (StreetCard street : streetsInventory) {
            if (!street.isMortgaged()) return true;
        }
        return false;
    }

    public void handlePlayerBankruptcy(MonopolyWindow monopolyWindow) {
        MonopolyCard currentCard = monopolyWindow.getPanelById(this.getPosition());
        if (currentCard != null) {
            currentCard.removePlayer(this);
        }

        // Возвращаем все имущество банку
        for (StreetCard street : this.getStreetsInventory()) {
            street.setOwner(null);
            street.setMortgaged(false);
            street.setHouseCount(0);
            street.setHotelCount(0);
            street.calcCorrectRentPrice();
            this.getStreetsInventory().remove(street);
        }
        streetsInventory.clear();

        for (SubwayCard subway : this.getSubwaysInventory()) {
            subway.setOwner(null);
            this.getSubwaysInventory().remove(subway);
        }
        subwaysInventory.clear();

        for (UtilityCard utility : this.getUtilitiesInventory()) {
            utility.setOwner(null);
            this.getUtilitiesInventory().remove(utility);
        }
        utilitiesInventory.clear();

        SwingUtils.showInfoMessageBox(this.getPlayerName() + " объявил банкротство и выбывает из игры!");
    }

    public ArrayList<String> getStreetsInventoryDescription() {
        ArrayList<String> description = new ArrayList<>();
        for (StreetCard card : streetsInventory) {
            description.add(card.toString());
        }
        return description;
    }

    public ArrayList<String> getAllInventoriesDescription() {
        ArrayList<String> description = new ArrayList<>();
        for (StreetCard card : streetsInventory) {
            description.add(card.toString());
        }
        for (SubwayCard card : subwaysInventory) {
            description.add(card.toString());
        }
        for (UtilityCard card : utilitiesInventory) {
            description.add(card.toString());
        }
        return description;
    }

    public StreetCard findStreetInInventoryByName(String cardName) {
        for (StreetCard card : streetsInventory) {
            if (card.getStreetName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    public SubwayCard findSubwayInInventoryByName(String cardName) {
        for (SubwayCard card : subwaysInventory) {
            if (card.getSubwayName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    public UtilityCard findUtilityInInventoryByName(String cardName) {
        for (UtilityCard card : utilitiesInventory) {
            if (card.getCardName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    public StreetCard findStreetInInventoryByColor(String color) {
        for (StreetCard card : streetsInventory) {
            if (card.getColor().equals(color)) {
                return card;
            }
        }
        return null;
    }

    // Метод для рисования фигуры игрока
    public void drawShape(Graphics2D g2d, int x, int y, int size) {
        Shape shape = PlayerShapeFactory.createShape(id, x, y, size);
        g2d.setColor(color);
        g2d.fill(shape);

        // обводка для лучшей видимости
        g2d.setColor(Color.BLACK);
        g2d.draw(shape);
    }

    // Метод для получения фигуры
    public Shape getShape(int x, int y, int size) {
        return PlayerShapeFactory.createShape(id, x, y, size);
    }

    @Override
    public String toString() {
        return this.getPlayerName();
    }
}