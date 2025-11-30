package ru.vsu.cs.additions;

import ru.vsu.cs.cards.StreetCard;
import ru.vsu.cs.game.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class StreetColorSet {
    private final ArrayList<StreetCard> streets;

    public StreetColorSet(StreetCard[] streets) {
        this.streets = new ArrayList<>(Arrays.asList(streets));
    }

    public ArrayList<StreetCard> getStreets() {
        return streets;
    }

    public int getAcceptableHousesCount() {
        int[] streetsHouseCount = new int[streets.size()];
        for (int i = 0; i < streetsHouseCount.length; i++) {
            streetsHouseCount[i] = streets.get(i).getHouseCount();
        }
        return Arrays.stream(streetsHouseCount).min().getAsInt() + 1;
    }

    public boolean canBuildHouses(StreetCard street) {
        return street.getHouseCount() < getAcceptableHousesCount();
    }

    public boolean canBuildHotels() {
        int[] streetsHouseCount = new int[streets.size()];
        for (int i = 0; i < streetsHouseCount.length; i++) {
            streetsHouseCount[i] = streets.get(i).getHouseCount();
        }

        int[] streetsHotelCount = new int[streets.size()];
        for (int i = 0; i < streetsHotelCount.length; i++) {
            streetsHotelCount[i] = streets.get(i).getHotelCount();
        }
        return Arrays.stream(streetsHouseCount).min().getAsInt() == 4
                || Arrays.stream(streetsHotelCount).max().getAsInt() == 1;
    }

    public boolean isStreetsHaveSingleOwner() {
        Player firstOwner = streets.getFirst().getOwner();
        boolean isStreetsHaveSingleOwner = false;
        for (int i = 1; i < streets.size(); i++) {
            if (streets.get(i).getOwner() != null && streets.get(i).getOwner().equals(firstOwner)) {
                isStreetsHaveSingleOwner = true;
            } else {
                isStreetsHaveSingleOwner = false;
            }
        }
        return isStreetsHaveSingleOwner;
    }
}
