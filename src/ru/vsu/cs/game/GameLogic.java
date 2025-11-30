package ru.vsu.cs.game;

import ru.vsu.cs.cards.*;
import ru.vsu.cs.util.SwingUtils;
import ru.vsu.cs.window.BankruptcyManagementWindow;
import ru.vsu.cs.window.DiceDropWindow;
import ru.vsu.cs.window.MonopolyWindow;
import ru.vsu.cs.window.PlayerWindow;

import javax.swing.*;
import java.util.ArrayList;

public class GameLogic {
    private final MonopolyWindow monopolyWindow;
    private final ArrayList<Player> players;
    private int currentPlayerIndex = 0;
    private boolean gameStarted = false;

    public GameLogic(MonopolyWindow monopolyWindow, ArrayList<Player> players) {
        this.monopolyWindow = monopolyWindow;
        this.players = players;
    }

    public void startGame() {
        gameStarted = true;

        MonopolyCard startCard = monopolyWindow.getPanelById(0);
        for (Player player : players) {
            player.setPosition(0);
            if (startCard != null) {
                startCard.addPlayer(player);
            }
        }

        startPlayerTurn(players.get(currentPlayerIndex));
    }

    public void startPlayerTurn(Player player) {
        if (player.isBankrupt()) {
            endPlayerTurn(player);
            return;
        } else if (player.getMoveBan() > 0) {
            player.setMoveBan(player.getMoveBan() - 1);
            interactionWithPlayerWhichInJail(player);
            return;
        }
        PlayerWindow playerWindow = new PlayerWindow(monopolyWindow, player, this, player.getPosition());
        playerWindow.setVisible(true);
    }

    public void processPlayerMove(Player player, int currentPosition, int diceDrop) {
        MonopolyCard oldCard = monopolyWindow.getPanelById(currentPosition);
        if (oldCard != null) {
            oldCard.removePlayer(player);
        }

        int newPlayerLocation = movePlayer(player, currentPosition, diceDrop);

        MonopolyCard newCard = monopolyWindow.getPanelById(newPlayerLocation);
        if (newCard != null) {
            newCard.addPlayer(player);
        }

        handleLandingOnCard(player, newPlayerLocation);
    }

    private int movePlayer(Player player, int currentPosition, int diceDrop) {
        int newPlayerLocation = currentPosition + diceDrop;
        if (newPlayerLocation > 38) {
            newPlayerLocation -= 39; // -1 потому что позиции от 0 до 38
            player.increaseMoney(200); // Проход через старт
        }
        player.setPosition(newPlayerLocation);
        return newPlayerLocation;
    }

    private void handleLandingOnCard(Player player, int position) {
        MonopolyCard card = monopolyWindow.getPanelById(position);

        if (card instanceof StreetCard) {
            handleStreetCard(player, (StreetCard) card);
        } else if (card instanceof ChanceCard) {
            handleChanceCard(player, (ChanceCard) card);
        } else if (card instanceof CommunityChestCard) {
            handleCommunityChestCard(player, (CommunityChestCard) card);
        } else if (card instanceof SubwayCard) {
            handleSubwayCard(player, (SubwayCard) card);
        } else if (card instanceof TaxCard) {
            handleTaxCard(player, (TaxCard) card);
        } else if (card instanceof UtilityCard) {
            handleUtilityCard(player, (UtilityCard) card);
        } else if (card instanceof GoToJailCard) {
            ((GoToJailCard) card).onPlayerLanding(player, this);
        } else if (card instanceof StartAndFinishCard) {
            ((StartAndFinishCard) card).playerStartNewCircle(player);
        }
    }

    private void handleStreetCard(Player player, StreetCard street) {
        if (street.getOwner() != null && street.getOwner() != player && !street.isMortgaged()) {
            street.calcCorrectRentPrice();
            int rent = street.getCurrentRentPrice();

            if (player.getMoney() >= rent) {
                processPayment(player, street.getOwner(), rent, "арендную плату за " + street.getStreetName());
            } else {
                handleBankruptcy(player, rent, street.getOwner());
            }
        }
    }

    private void handleSubwayCard(Player player, SubwayCard subway) {
        if (subway.getOwner() != null && subway.getOwner() != player) {
            subway.calcCorrectRentPrice();
            int rent = subway.getCurrentRentPrice();

            if (player.getMoney() >= rent) {
                processPayment(player, subway.getOwner(), rent, "арендную плату за " + subway.getSubwayName());
            } else {
                handleBankruptcy(player, rent, subway.getOwner());
            }
        }
    }

    private void handleUtilityCard(Player player, UtilityCard utility) {
        if (utility.getOwner() != null && utility.getOwner() != player) {
            DiceDropWindow diceDropWindow2 = new DiceDropWindow();
            diceDropWindow2.setVisible(true);

            if (diceDropWindow2.isConfirmed()) {
                int diceDrop2 = diceDropWindow2.getDiceDrop();
                int rent = utility.calcRentPrice(diceDrop2);

                if (player.getMoney() >= rent) {
                    processPayment(player, utility.getOwner(), rent, "арендную плату за " + utility.getCardName());
                } else {
                    handleBankruptcy(player, rent, utility.getOwner());
                }
            }
        }
    }

    private void handleTaxCard(Player player, TaxCard tax) {
        if (player.getMoney() >= tax.getTax()) {
            player.decreaseMoney(tax.getTax());
            SwingUtils.showInfoMessageBox(
                    "Игрок: " + player.getPlayerName() + " оплатил(-а) " + tax.getTaxName()
            );
        } else {
            handleBankruptcy(player, tax.getTax(), null);
        }
    }

    private void handleChanceCard(Player player, ChanceCard chance) {
        int numberOfSituation = chance.getDeck().getChanceSituation();
        SwingUtils.showInfoMessageBox(
                chance.getDeck().getSituationByNumber(numberOfSituation),
                "Вот Что Вам Выпало"
        );

        if (numberOfSituation == 1) {
            movePlayerToThisCard(player, "Варшавское Шоссе");
        } else if (numberOfSituation == 2) {
            movePlayerToThisCard(player, "Смоленская площадь");
        } else if (numberOfSituation == 3) {
            movePlayerToThisCard(player, "Рязанский Проспект");
        } else if (numberOfSituation == 4) {
            movePlayerToThisCard(player, "ул. Сретенка");
        } else if (numberOfSituation == 5) {
            movePlayerToThisCard(player, "Площадь Маяковского");
        } else if (numberOfSituation == 6) {
            movePlayerToThisCard(player, "Электростанция");
        } else if (numberOfSituation == 7) {
            movePlayerToThisCard(player, "Водопровод");
        } else if (numberOfSituation == 8) {
            movePlayerToJail(player);
        } else if (numberOfSituation == 9) {
            player.increaseMoney(100);
        } else if (numberOfSituation == 10) {
            movePlayerBackwards(player, 3);
        } else if (numberOfSituation == 11) {
            int totalHouseCount = player.getStreetsInventory().stream()
                    .map(StreetCard::getHouseCount)
                    .reduce(0, Integer::sum);
            int totalHotelCount = player.getStreetsInventory().stream()
                    .map(StreetCard::getHotelCount)
                    .reduce(0, Integer::sum);
            playerPayToBank(player, totalHouseCount * 25 + totalHotelCount * 100);
        } else if (numberOfSituation == 12) {
            int totalHouseCount = player.getStreetsInventory().stream()
                    .map(StreetCard::getHouseCount)
                    .reduce(0, Integer::sum);
            int totalHotelCount = player.getStreetsInventory().stream()
                    .map(StreetCard::getHotelCount)
                    .reduce(0, Integer::sum);
            playerPayToBank(player, totalHouseCount * 40 + totalHotelCount * 115);
        } else if (numberOfSituation == 13) {
            playerPayToBank(player, 15);
        } else if (numberOfSituation == 14) {
            player.increaseMoney(10);
        } else if (numberOfSituation == 15) {
            playerPayToBank(player, 50);
        } else if (numberOfSituation == 16) {
            player.increaseMoney(25);
        } else if (numberOfSituation == 17) {
            player.increaseMoney(100);
        } else if (numberOfSituation == 18) {
            int sum = player.getStreetsInventory().stream()
                    .map(StreetCard::getCurrentRentPrice)
                    .reduce(0, Integer::sum);
            player.increaseMoney(sum);
        } else if (numberOfSituation == 19) {
            playerPayToBank(player, 20);
        } else if (numberOfSituation == 20) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getMoney() >= 10) {
                    players.get(i).decreaseMoney(10);
                    player.increaseMoney(10);
                }
            }
        }
    }

    private void movePlayerBackwards(Player player, int steps) {
        MonopolyCard oldCard = monopolyWindow.getPanelById(player.getPosition());
        if (oldCard != null) {
            oldCard.removePlayer(player);
        }

        int newPosition = player.getPosition() - steps;
        if (newPosition < 0) {
            newPosition += 39;
        }
        player.setPosition(newPosition);

        MonopolyCard newCard = monopolyWindow.getPanelById(newPosition);
        if (newCard != null) {
            newCard.addPlayer(player);
        }

        // Обрабатываем landing на новой клетке
        handleLandingOnCard(player, newPosition);
    }

    private void movePlayerToThisCard(Player player, String streetName) {
        MonopolyCard situationCard = monopolyWindow.getStreetByStreetName(streetName);
        if (situationCard == null) {
            SwingUtils.showInfoMessageBox("Не удалось найти карту: " + streetName);
            return;
        }

        int targetIndex = situationCard.getCardId();
        int currentPosition = player.getPosition();

        // Вычисляем количество шагов для достижения целевой клетки
        int steps;
        if (targetIndex >= currentPosition) {
            // Двигаемся вперед
            steps = targetIndex - currentPosition;
        } else {
            // Двигаемся вперед через старт
            steps = (39 - currentPosition) + targetIndex;
        }

        // Удаляем игрока с текущей позиции
        MonopolyCard oldCard = monopolyWindow.getPanelById(currentPosition);
        if (oldCard != null) {
            oldCard.removePlayer(player);
        }

        // Перемещаем игрока
        int newPlayerLocation = movePlayer(player, currentPosition, steps);

        // Добавляем игрока на новую позицию
        MonopolyCard newCard = monopolyWindow.getPanelById(newPlayerLocation);
        if (newCard != null) {
            newCard.addPlayer(player);
        }

        // Обрабатываем landing на новой клетке
        handleLandingOnCard(player, newPlayerLocation);
    }

    private void handleCommunityChestCard(Player player, CommunityChestCard communityChest) {
        int numberOfSituation = communityChest.getDeck().getCommunityChestSituation();
        SwingUtils.showInfoMessageBox(
                communityChest.getDeck().getSituationByNumber(numberOfSituation),
                "Вот Что Вам Выпало"
        );

        if (numberOfSituation == 1) {
            player.increaseMoney(200);
        } else if (numberOfSituation == 2) {
            movePlayerToJail(player);
        } else if (numberOfSituation == 3 || numberOfSituation == 5 || numberOfSituation == 14) {
            playerPayToBank(player, 50);
        } else if (numberOfSituation == 4 || numberOfSituation == 6 || numberOfSituation == 19) {
            player.increaseMoney(25);
        } else if (numberOfSituation == 7 || numberOfSituation == 8 || numberOfSituation == 15) {
            player.increaseMoney(100);
        } else if (numberOfSituation == 9) {
            playerPayToBank(player, 20);
        } else if (numberOfSituation == 10) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getMoney() >= 10) {
                    players.get(i).decreaseMoney(10);
                    player.increaseMoney(10);
                }
            }
        } else if (numberOfSituation == 11 || numberOfSituation == 13 || numberOfSituation == 20) {
            player.increaseMoney(50);
        } else if (numberOfSituation == 12) {
            player.increaseMoney(20);
        } else if (numberOfSituation == 16) {
            player.increaseMoney(150);
        } else if (numberOfSituation == 17) {
            playerPayToBank(player, 30);
        } else if (numberOfSituation == 18) {
            player.increaseMoney(75);
        }
    }

    public void movePlayerToJail(Player player) {
        // Удаляем с текущей позиции
        MonopolyCard currentCard = monopolyWindow.getPanelById(player.getPosition());
        if (currentCard != null) {
            currentCard.removePlayer(player);
        }

        // Устанавливаем позицию тюрьмы (10)
        player.setPosition(10);
        player.setMoveBan(3);

        // Добавляем на карту тюрьмы
        MonopolyCard jailCard = monopolyWindow.getPanelById(10);
        if (jailCard != null) {
            jailCard.addPlayer(player);
        }

        SwingUtils.showInfoMessageBox(player.getPlayerName() + " отправлен в тюрьму!");
    }

    private void playerPayToBank(Player player, int amount) {
        if (player.getMoney() >= amount) {
            player.decreaseMoney(amount);
        } else {
            handleBankruptcy(player, amount, null);
        }
    }

    private void processPayment(Player from, Player to, int amount, String reason) {
        from.decreaseMoney(amount);
        if (to != null) {
            to.increaseMoney(amount);
        }
        SwingUtils.showInfoMessageBox(
                from.getPlayerName() + " платит " + (to != null ? to.getPlayerName() : "банку") + " " + amount + "₩ за " + reason
        );
    }

    private void handleBankruptcy(Player debtor, int debt, Player creditor) {
        SwingUtils.showInfoMessageBox(
                debtor.getPlayerName() + " не может заплатить " + debt + "₩!" + "\nНеобходимо продать имущество или объявить банкротство."
        );

        // Открываем окно для управления банкротством
        BankruptcyManagementWindow bankruptcyWindow = new BankruptcyManagementWindow(
                null, debtor, debt, monopolyWindow
        );
        bankruptcyWindow.setVisible(true);

        if (bankruptcyWindow.isResolvedSuccessfully()) {
            // Игрок смог собрать деньги через залог/продажу
            if (debtor.getMoney() >= debt) {
                processPayment(debtor, creditor, debt, "погашение долга");
            }
        } else {
            // Игрок объявил банкротство
            debtor.setBankrupt(true);
            debtor.handlePlayerBankruptcy(monopolyWindow);
        }
    }

    private void interactionWithPlayerWhichInJail(Player player) {
        int result = JOptionPane.showConfirmDialog(monopolyWindow,
                "Хотите ли вы выйти из тюрьмы заплатив за это 50₩",
                "Выкуп из Тюрьмы",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            if (player.getMoney() >= 50) {
                player.decreaseMoney(50);
                SwingUtils.showInfoMessageBox(
                        player.getPlayerName() + " вышел из тюрьмы выкупив себя за 50₩"
                );
                player.setMoveBan(0);
                startPlayerTurn(player);
            } else {
                handleBankruptcy(player, 50, null);
            }
        } else if (result == JOptionPane.NO_OPTION) {
            endPlayerTurn(player);
        }
    }

    private void checkWinConditions() {
        int activePlayers = 0;
        Player lastActivePlayer = null;

        for (Player player : players) {
            if (!player.isBankrupt()) {
                activePlayers++;
                lastActivePlayer = player;
            }
        }

        if (activePlayers == 1 && lastActivePlayer != null) {
            gameStarted = false;
            SwingUtils.showInfoMessageBox("Игра окончена! Победитель: " + lastActivePlayer.getPlayerName());
            System.exit(0);
        }
    }

    public void endPlayerTurn(Player player) {
        // Переход к следующему игроку
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isBankrupt());

        checkWinConditions();
        startPlayerTurn(players.get(currentPlayerIndex));
    }
}