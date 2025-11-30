package ru.vsu.cs.additions;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChanceDeck {
    private Map<Integer, String> chanceSituations;
    private LinkedList<Integer> chanceSituationsNumbers;

    public ChanceDeck() {
        chanceSituations = new HashMap<>();
        chanceSituations.put(1, "Отправляйтесь на Варшавское Шоссе. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(2, "Отправляйтесь на Смоленскую площадь. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(3, "Отправляйтесь на Рязанский Проспект. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(4, "Отправляйтесь на ул. Сретенка. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(5, "Отправляйтесь на Площадь Маяковского. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(6, "Отправляйтесь на Электростанцию. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(7, "Отправляйтесь на Водопровод. Если вы проходите через 'Вперед', получите ₩200");
        chanceSituations.put(8, "Вас настигла Налоговая Инспекция - отправляйтесь в тюрьму. Отправляйтесь прямо в тюрьму. Не проходите через 'Вперед'. Не получайте ₩200");
        chanceSituations.put(9, "Ставка сыграла в вашу пользу: получите 100₩");
        chanceSituations.put(10, "Вернитесь на три шага назад");
        chanceSituations.put(11, "Оплатите ремонт улиц: за каждый дом ₩25, за каждый отель ₩100");
        chanceSituations.put(12, "Оплатите ремонт зданий: за каждый дом ₩40, за каждый отель ₩115");
        chanceSituations.put(13, "Штраф за превышение скорости: ₩15");
        chanceSituations.put(14, "Вы заняли второе место в конкурсе красоты. Получите ₩10");
        chanceSituations.put(15, "У вашей банковской карты истёк срок пользования оплатите налог: ₩50");
        chanceSituations.put(16, "Банковские дивиденды: ₩25");
        chanceSituations.put(17, "Вы получили наследство: ₩100");
        chanceSituations.put(18, "Вам повезло: получите сумму равную суммарной ренте всех ваших улиц!");
        chanceSituations.put(19, "Возмещение налога: ₩20");
        chanceSituations.put(20, "Ваша мама приболела, получите ₩10 от каждого игрока в дар");

        chanceSituationsNumbers = new LinkedList<>(chanceSituations.keySet());
        Collections.shuffle(chanceSituationsNumbers);
    }

    public Integer getChanceSituation() {
        Integer situation = chanceSituationsNumbers.getFirst();
        chanceSituationsNumbers.removeFirst();
        chanceSituationsNumbers.addLast(situation);
        return situation;
    }

    public String getSituationByNumber(int number) {
        return chanceSituations.get(number);
    }
}
