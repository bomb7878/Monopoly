package ru.vsu.cs.additions;

import ru.vsu.cs.cards.SubwayCard;
import ru.vsu.cs.game.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class SubwaySet {
    private ArrayList<SubwayCard> subways;

    public SubwaySet(SubwayCard[] subways) {
        this.subways = new ArrayList<>(Arrays.asList(subways));
    }

    public ArrayList<SubwayCard> getSubways() {
        return subways;
    }

    public int getCountOfCardsWithThisOwner(Player owner) {
        int count = 1;
        for (int i = 1; i < subways.size(); i++) {
            if (subways.get(i).getOwner().equals(owner)) {
                count++;
            }
        }
        return count;
    }
}
