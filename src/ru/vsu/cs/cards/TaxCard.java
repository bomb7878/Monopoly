package ru.vsu.cs.cards;

public class TaxCard extends MonopolyCard {
    private final String taxName;
    private final int tax;

    public TaxCard(int cardId, String name, int tax) {
        super(cardId);
        this.taxName = name;
        this.tax = tax;

        super.setName(this.taxName);
        super.setToolTipText(this.taxName);
    }

    public int getTax() {
        return tax;
    }

    public String getTaxName() {
        return taxName;
    }

    @Override
    public String toString() {
        return this.getTaxName();
    }
}
