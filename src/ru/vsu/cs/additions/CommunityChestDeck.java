package ru.vsu.cs.additions;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CommunityChestDeck {
    private Map<Integer, String> communityChestSituations;
    private LinkedList<Integer> communityChestSituationsNumbers;

    public CommunityChestDeck() {
        communityChestSituations = new HashMap<>();
        communityChestSituations.put(1, "Банк ошибся в вашу пользу. Получите ₩200");
        communityChestSituations.put(2, "Вас поймали на воровстве идей для бизнеса - отправляйтесь в тюрьму. Отправляйтесь прямо в тюрьму. Не проходите через 'Вперед'. Не получайте ₩200");
        communityChestSituations.put(3, "Оплатите страховку: ₩50");
        communityChestSituations.put(4, "Получите доход от инвестиций: ₩25");
        communityChestSituations.put(5, "Оплатите услуги доктора: ₩50");
        communityChestSituations.put(6, "Получите проценты по вкладу: ₩25");
        communityChestSituations.put(7, "Вы получили наследство: ₩100");
        communityChestSituations.put(8, "Рождественский фонд выплачивает вам: ₩100");
        communityChestSituations.put(9, "Возмещение налога: ₩20");
        communityChestSituations.put(10, "С днем рождения! Получите ₩10 от каждого игрока");
        communityChestSituations.put(11, "Банк выплачивает вам дивиденды: ₩50");
        communityChestSituations.put(12, "Ваш питомец занял первое место в конкурсе. Получите ₩20");
        communityChestSituations.put(13, "Вы удачно продали акции: получите ₩50");
        communityChestSituations.put(14, "Оплатите обучение: ₩50");
        communityChestSituations.put(15, "Выигрыш в лотерею: ₩100");
        communityChestSituations.put(16, "Получите премию за изобретение: ₩150");
        communityChestSituations.put(17, "Оплатите штраф за парковку: ₩30");
        communityChestSituations.put(18, "Получите возврат подоходного налога: ₩75");
        communityChestSituations.put(19, "Оплатите взнос в благотворительный фонд: ₩25");
        communityChestSituations.put(20, "Получите гонорар за консультацию: ₩50");

        communityChestSituationsNumbers = new LinkedList<>(communityChestSituations.keySet());
        Collections.shuffle(communityChestSituationsNumbers);
    }

    public Integer getCommunityChestSituation() {
        Integer situation = communityChestSituationsNumbers.getFirst();
        communityChestSituationsNumbers.removeFirst();
        communityChestSituationsNumbers.addLast(situation);
        return situation;
    }

    public String getSituationByNumber(int number) {
        return communityChestSituations.get(number);
    }
}
