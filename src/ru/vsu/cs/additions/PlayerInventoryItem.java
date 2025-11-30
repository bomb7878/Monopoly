package ru.vsu.cs.additions;

import ru.vsu.cs.cards.PurchasableCard;

public class PlayerInventoryItem {
    private final String displayText;  // Текст для пользователя
    private final String debugText;    // Текст для внутренней логики
    private final String type;         // Тип карты
    private final PurchasableCard card;// Ссылка на саму карту

    public static final String STREET_TYPE = "STREET";
    public static final String SUBWAY_TYPE = "SUBWAY";
    public static final String UTILITY_TYPE = "UTILITY";

    public PlayerInventoryItem(String displayText, String type, PurchasableCard card) {
        if(!type.equals(STREET_TYPE) && !type.equals(SUBWAY_TYPE) && !type.equals(UTILITY_TYPE)) {
            throw new IllegalArgumentException("Invalid type: " + type);
        } else {
            this.type = type;
        }
        this.displayText = displayText;
        this.debugText = this.type + ": " + displayText;
        this.card = card;
    }

    @Override
    public String toString() {
        return displayText;
    }

    public String getDebugText() { return debugText; }
    public String getType() { return type; }
    public Object getCard() { return card; }
}
