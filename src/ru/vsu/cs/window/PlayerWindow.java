package ru.vsu.cs.window;

import ru.vsu.cs.additions.PlayerInventoryItem;
import ru.vsu.cs.cards.*;
import ru.vsu.cs.game.GameLogic;
import ru.vsu.cs.game.Player;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayerWindow extends JFrame {
    private JPanel panelMain;
    private JLabel playerNameLabel;
    private JList<PlayerInventoryItem> inventoryList;
    private JButton moveButton;
    private JButton payCardButton;
    private JButton upgradeTheirCardsButton;
    private JLabel playerMoneyLabel;
    private JButton endPlayerTurnButton;

    private final Player player;
    private final MonopolyWindow monopolyWindow;
    private final GameLogic gameLogic;
    private final int currentPlayerLocation;

    public PlayerWindow(MonopolyWindow window, Player player, GameLogic gameLogic, int currentPlayerLocation) {
        this.player = player;
        this.monopolyWindow = window;
        this.currentPlayerLocation = currentPlayerLocation;
        this.gameLogic = gameLogic;

        this.setTitle(player.getPlayerName() + "'s Window");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setSize(625, 300);

        inventoryList.setVisible(false);

        payCardButton.setEnabled(false);
        payCardButton.setToolTipText("Сначала совершите ход");

        if (this.player.getStreetsInventory().isEmpty() && this.player.getSubwaysInventory().isEmpty() && this.player.getUtilitiesInventory().isEmpty()) {
            upgradeTheirCardsButton.setEnabled(false);
        }

        endPlayerTurnButton.setEnabled(false);

        playerNameLabel.setText(player.getPlayerName());
        playerMoneyLabel.setText(player.getMoney() + "₩");

        initializeListeners();
    }

    private void initializeListeners() {
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeMove();
            }
        });

        payCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payCard();
            }
        });

        upgradeTheirCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genPlayerInventoryList();
            }
        });

        inventoryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = inventoryList.locationToIndex(e.getPoint());
                upgradeThisCard(index);
            }
        });

        endPlayerTurnButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               setVisible(false);
               gameLogic.endPlayerTurn(player);
           }
        });
    }

    private void makeMove() {
        DiceDropWindow diceDropWindow = new DiceDropWindow();
        diceDropWindow.setVisible(true);

        if (diceDropWindow.isConfirmed()) {
            int diceDrop = diceDropWindow.getDiceDrop();

            // Используем GameLogic для обработки хода
            gameLogic.processPlayerMove(player, currentPlayerLocation, diceDrop);

            // Обновляем UI
            upgradeTheirCardsButton.setEnabled(false);
            inventoryList.setVisible(false);
            moveButton.setEnabled(false);

            // Проверяем, может ли игрок покупать карту
            MonopolyCard currentCard = monopolyWindow.getPanelById(player.getPosition());
            if (currentCard instanceof PurchasableCard) {
                PurchasableCard purchasableCard = (PurchasableCard) currentCard;
                if (purchasableCard.getOwner() == null && player.getMoney() >= purchasableCard.getPurchasePrice()) {
                    payCardButton.setEnabled(true);
                    endPlayerTurnButton.setEnabled(true);
                }
            }

            updatePlayerInfo();
            if (!payCardButton.isEnabled()) {
                this.setVisible(false);
                gameLogic.endPlayerTurn(player);
            }
        }
    }

    private void payCard() {
        int playerLocation = player.getPosition();
        JPanel occupiedByPlayerPanel = monopolyWindow.getPanelById(playerLocation);
        if (occupiedByPlayerPanel instanceof StreetCard) {
            buyCard((StreetCard) occupiedByPlayerPanel);
        } else if (occupiedByPlayerPanel instanceof SubwayCard) {
            buyCard((SubwayCard) occupiedByPlayerPanel);
        } else if (occupiedByPlayerPanel instanceof UtilityCard) {
            buyCard((UtilityCard) occupiedByPlayerPanel);
        } else {
            SwingUtils.showInfoMessageBox("Эта карта не может быть куплена");
        }

        updatePlayerInfo();
        this.setVisible(false);
        gameLogic.endPlayerTurn(player);
    }

    private void genPlayerInventoryList() {
        DefaultListModel<PlayerInventoryItem> playerInventoryListModel = new DefaultListModel<>();

        for (StreetCard street : player.getStreetsInventory()) {
            String displayText = street.toString();
            playerInventoryListModel.addElement(new PlayerInventoryItem(displayText, PlayerInventoryItem.STREET_TYPE, street));
        }

        for (SubwayCard subway : player.getSubwaysInventory()) {
            String displayText = subway.toString();
            playerInventoryListModel.addElement(new PlayerInventoryItem(displayText, PlayerInventoryItem.SUBWAY_TYPE, subway));
        }

        for (UtilityCard utility : player.getUtilitiesInventory()) {
            String displayText = utility.toString();
            playerInventoryListModel.addElement(new PlayerInventoryItem(displayText, PlayerInventoryItem.UTILITY_TYPE, utility));
        }

        inventoryList.setModel(playerInventoryListModel);
        inventoryList.setVisible(true);
    }

    private void upgradeThisCard(int index) {
        if (index != -1) {
            PlayerInventoryItem item = inventoryList.getModel().getElementAt(index);

            switch (item.getType()) {
                case PlayerInventoryItem.STREET_TYPE:
                    StreetCard street = (StreetCard) item.getCard();
                    if (street.isMortgaged()) { // Карта заложена - предлагаем выкуп
                        handleMortgagedStreet(street);
                    } else { // Карта не заложена - открываем окно улучшения
                        StreetUpgradeWindow upgradeWindow = new StreetUpgradeWindow(player, monopolyWindow, street);
                        upgradeWindow.showDialog();
                        updatePlayerInfo();
                    }
                    break;

                case PlayerInventoryItem.SUBWAY_TYPE:
                    SwingUtils.showInfoMessageBox(
                            "Железные дороги нельзя улучшать строительством.\n" +
                                    "Доход от ж/д зависит от количества ж/д станций у владельца."
                    );
                    break;

                case PlayerInventoryItem.UTILITY_TYPE:
                    SwingUtils.showInfoMessageBox(
                            "Коммунальные службы нельзя улучшать строительством.\n" +
                                    "Доход от коммунальных служб зависит от броска кубиков."
                    );
                    break;
            }
        }
    }

    private void updatePlayerInfo() {
        playerMoneyLabel.setText(player.getMoney() + "₩");
        if (player.isBankrupt()) {
            this.setVisible(false);
        }
    }

    private void buyCard(PurchasableCard card) {
        if (card.getPurchasePrice() <= player.getMoney()) {
            if (card.getOwner() == null) {
                if (card instanceof StreetCard) {
                    player.buyCard(((StreetCard) card), card.getPurchasePrice());
                }
                if (card instanceof SubwayCard) {
                    player.buyCard(((SubwayCard) card), card.getPurchasePrice());
                }
                if (card instanceof UtilityCard) {
                    player.buyCard(((UtilityCard) card), card.getPurchasePrice());
                }
            } else {
                SwingUtils.showInfoMessageBox("У этой карты уже есть владелец это - " + card.getOwner());
            }
        } else {
            SwingUtils.showInfoMessageBox("Вам не хватает на это денег");
        }
    }

    private void handleMortgagedStreet(StreetCard street) {
        int redemptionPrice = (int) street.getRedemptionPrice();

        int result = JOptionPane.showConfirmDialog(this,
                "Улица '" + street.getStreetName() + "' заложена.\n" +
                        "Стоимость выкупа: " + redemptionPrice + "₩\n" +
                        "Ваш баланс: " + player.getMoney() + "₩\n\n" +
                        "Хотите выкупить эту улицу?",
                "Выкуп заложенной улицы",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            if (player.withdrawFromCollateralCard(street)) {
                SwingUtils.showInfoMessageBox("Улица '" + street.getStreetName() + "' успешно выкуплена из залога!");
                updatePlayerInfo();
                genPlayerInventoryList();
            } else {
                SwingUtils.showInfoMessageBox("Не удалось выкупить улицу. Недостаточно средств.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MonopolyWindow monopolyWindow = new MonopolyWindow();
        monopolyWindow.winMain();

        Player player = new Player(1, "Глеб Sasavot");

        player.buyCard(monopolyWindow.getStreetByIndex(0), monopolyWindow.getStreetByIndex(0).getPurchasePrice());
        player.buyCard(monopolyWindow.getStreetByIndex(1), monopolyWindow.getStreetByIndex(1).getPurchasePrice());

        //player.getStreetsInventory().getFirst().setMortgaged(true);

        //player.decreaseMoney(1380);

        PlayerWindow window = new PlayerWindow(monopolyWindow, player, new GameLogic(monopolyWindow, new ArrayList<>(List.of(player))), 0);
        window.setVisible(true);
    }
}