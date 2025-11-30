package ru.vsu.cs.cards;

import ru.vsu.cs.additions.StreetColorSet;
import ru.vsu.cs.game.Player;
import ru.vsu.cs.util.SwingUtils;

public class StreetCard extends MonopolyCard implements PurchasableCard{
    private final String streetName;
    private final String color;
    private StreetColorSet colorSet = null; // набор улиц с таким же цветом (для проверок возможности покупки домиков и т.д.)
    private final int purchasePrice;
    private final int[] rentPrices;
    private final int rentPriceWithColorSet; // стоимость аренды при полном комплекте этой цветовой группы(всегда равна удвоенной аренде улицы без домиков)
    private int currentRentPrice; // Стоимость аренды улицы, взимается из счёта наступившего на улицу игрока при условии, что эта улица не его и у неё есть владелец
    private final int housePrice;
    private int houseCount = 0;
    private final int hotelPrice;
    private int hotelCount = 0;
    private final int collateralPrice;
    private final double redemptionPrice; // стоимость выкупа улицы после залога
    private Player owner; // игрок - владелец улицы
    private boolean isMortgaged = false;

    /**
     * @param cardId          - идентификатор карты
     * @param streetName      - название улицы
     * @param color           - цвет, к которому принадлежит карта
     * @param PurchasePrice   - покупная стоимость улицы
     * @param rentPrices      - массив арендных плат за улицу
     * @param housePrice      - стоимость домика на улице
     * @param hotelPrice      - стоимость отеля на улице
     * @param collateralPrice - стоимость залога улицы
     */
    public StreetCard(int cardId, String streetName, String color,
                      int PurchasePrice, int[] rentPrices,
                      int housePrice, int hotelPrice, int collateralPrice) {
        super(cardId);
        this.streetName = streetName;
        this.color = color;
        this.purchasePrice = PurchasePrice;
        this.rentPrices = rentPrices;
        this.rentPriceWithColorSet = rentPrices[0] * 2;
        this.currentRentPrice = rentPrices[0];
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
        this.collateralPrice = collateralPrice;
        this.redemptionPrice = collateralPrice * 1.1;
        this.owner = null;

        super.setName(this.streetName);
        super.setToolTipText(String.format("<html>%s<br>Владелец: Никто</html>", this.streetName));
    }

    public String getStreetName() {
        return streetName;
    }

    public String getColor() {
        return color;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public int[] getRentPrices() {
        return rentPrices;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public int getCollateralPrice() {
        return collateralPrice;
    }

    public int getRentPriceWithColorSet() {
        return rentPriceWithColorSet;
    }

    public StreetColorSet getColorSet() {
        return colorSet;
    }

    public void setColorSet(StreetColorSet colorSet) {
        this.colorSet = colorSet;
    }

    public int getCurrentRentPrice() {
        return currentRentPrice;
    }

    public void setCurrentRentPrice(int currentRentPrice) {
        this.currentRentPrice = currentRentPrice;
    }

    public double getRedemptionPrice() {
        return redemptionPrice;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(int houseCount) {
        this.houseCount = houseCount;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount) {
        this.hotelCount = hotelCount;
    }

    public void incrementHouseCount() {
        if (this.colorSet.isStreetsHaveSingleOwner()) {
            if (this.colorSet.canBuildHouses(this)) {
                if (this.hotelCount == 0 && this.houseCount < 4) {
                    this.houseCount++;
                } else {
                    SwingUtils.showInfoMessageBox("Вы уже улучшили улицу на максимум");
                }
            } else {
                SwingUtils.showInfoMessageBox("Сначала увеличьте кол-во домиков на других улицах этой группы");
            }
        }
    }

    public void incrementHotelCount() {
        if (this.colorSet.isStreetsHaveSingleOwner()) {
            if (this.colorSet.canBuildHotels() && this.hotelCount == 0) {
                this.houseCount = 0;
                this.hotelCount++;
            } else if(!this.colorSet.canBuildHotels()) {
                SwingUtils.showInfoMessageBox("Сначала поставьте по 4 домика на всех улицах этого набора");
            } else if(this.hotelCount == 1){
                SwingUtils.showInfoMessageBox("Вы уже улучшили улицу на максимум");
            }
        }
    }

    public void calcCorrectRentPrice() {
        if (this.isMortgaged) {
            this.setCurrentRentPrice(0); // Заложенная улица не приносит доход
        } else if (this.hotelCount > 0) {
            this.setCurrentRentPrice(this.rentPrices[5]);
        } else if (this.houseCount > 0) {
            this.setCurrentRentPrice(this.rentPrices[houseCount]);
        } else if (this.colorSet != null && this.colorSet.isStreetsHaveSingleOwner()) {
            this.setCurrentRentPrice(this.rentPriceWithColorSet);
        } else {
            this.setCurrentRentPrice(this.rentPrices[0]);
        }
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        isMortgaged = mortgaged;
        // Обновляем подсказку
        if (mortgaged) {
            super.setToolTipText(String.format(
                    "<html>%s<br>Владелец: %s<br>ЗАЛОЖЕНА</html>",
                    this.streetName,
                    this.owner != null ? this.owner.getPlayerName() : "Никто"
            ));
        } else {
            super.setToolTipText(String.format(
                    "<html>%s<br>Владелец: %s</html>",
                    this.streetName,
                    this.owner != null ? this.owner.getPlayerName() : "Никто"
            ));
        }
    }

    @Override
    public String toString() {
        String status = isMortgaged ? " [ЗАЛОЖЕНА]" : "";
        return String.format("%s %sая группа%s", this.streetName, this.color.substring(0, this.color.length() - 2), status);
    }
}
