package ru.vsu.cs.window;

import ru.vsu.cs.cards.StreetCard;
import ru.vsu.cs.game.Player;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BankruptcyManagementWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel debtLabel;
    private JLabel currentMoneyLabel;
    private JList<String> assetsList;
    private JButton mortgageButton;
    private JButton sellBuildingsButton;
    private JLabel statusLabel;

    private final Player player;
    private final int debt;
    private final MonopolyWindow monopolyWindow;
    private boolean resolvedSuccessfully = false;

    public BankruptcyManagementWindow(Frame parent, Player player, int debt, MonopolyWindow monopolyWindow) {
        super(parent, "Управление банкротством - " + player.getPlayerName(), true);
        this.player = player;
        this.debt = debt;
        this.monopolyWindow = monopolyWindow;

        initializeComponents();
        setupLayout();
        initialListeners();
        updateDisplay();

        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void initializeComponents() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        debtLabel = new JLabel();
        currentMoneyLabel = new JLabel();
        statusLabel = new JLabel("Выберите действие для погашения долга");

        assetsList = new JList<>();
        assetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mortgageButton = new JButton("Заложить выбранную улицу");
        sellBuildingsButton = new JButton("Продать постройки на выбранной улице");
        buttonOK = new JButton("Завершить (деньги собраны)");
        buttonCancel = new JButton("Объявить банкротство");

        buttonOK.setEnabled(false);
    }

    private void setupLayout() {
        // Верхняя панель с информацией
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(debtLabel);
        infoPanel.add(currentMoneyLabel);
        infoPanel.add(statusLabel);
        contentPane.add(infoPanel, BorderLayout.NORTH);

        // Центральная панель со списком активов
        JScrollPane scrollPane = new JScrollPane(assetsList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ваши активы"));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Панель кнопок действий
        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel assetActionsPanel = new JPanel(new FlowLayout());
        assetActionsPanel.add(mortgageButton);
        assetActionsPanel.add(sellBuildingsButton);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(buttonOK);
        controlPanel.add(buttonCancel);

        actionPanel.add(assetActionsPanel);
        actionPanel.add(controlPanel);
        contentPane.add(actionPanel, BorderLayout.SOUTH);
    }

    private void initialListeners() {
        mortgageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mortgageSelectedStreet();
            }
        });

        sellBuildingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellBuildingsOnSelectedStreet();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        assetsList.addListSelectionListener(e -> {
            updateButtonsState();
        });
    }

    private void mortgageSelectedStreet() {
        int selectedIndex = assetsList.getSelectedIndex();
        if (selectedIndex != -1) {
            StreetCard selectedStreet = getSelectedStreet(selectedIndex);
            if (selectedStreet != null && !selectedStreet.isMortgaged()) {
                if (player.mortgageCard(selectedStreet)) {
                    SwingUtils.showInfoMessageBox("Улица " + selectedStreet.getStreetName() + " заложена за " +
                            selectedStreet.getCollateralPrice() + "₩");
                    updateDisplay();
                }
            } else {
                SwingUtils.showInfoMessageBox("Эта улица уже заложена или недоступна для залога");
            }
        }
    }

    private void sellBuildingsOnSelectedStreet() {
        int selectedIndex = assetsList.getSelectedIndex();
        if (selectedIndex != -1) {
            StreetCard selectedStreet = getSelectedStreet(selectedIndex);
            if (selectedStreet != null && (selectedStreet.getHouseCount() > 0 || selectedStreet.getHotelCount() > 0)) {
                if (player.sellBuildings(selectedStreet)) {
                    SwingUtils.showInfoMessageBox("Постройки на улице " + selectedStreet.getStreetName() + " проданы");
                    updateDisplay();
                }
            } else {
                SwingUtils.showInfoMessageBox("На этой улице нет построек для продажи");
            }
        }
    }

    private StreetCard getSelectedStreet(int listIndex) {
        ArrayList<StreetCard> streets = player.getStreetsInventory();
        if (listIndex >= 0 && listIndex < streets.size()) {
            return streets.get(listIndex);
        }
        return null;
    }

    private void updateDisplay() {
        debtLabel.setText("Долг: " + debt + "₩");
        currentMoneyLabel.setText("Текущие деньги: " + player.getMoney() + "₩");

        // Обновляем список активов
        DefaultListModel<String> model = new DefaultListModel<>();
        for (StreetCard street : player.getStreetsInventory()) {
            String status = street.isMortgaged() ? " [ЗАЛОЖЕНА]" :
                    street.getHotelCount() > 0 ? " [Отель]" :
                            street.getHouseCount() > 0 ? " [Домов: " + street.getHouseCount() + "]" : "";
            model.addElement(street.getStreetName() + " - Залог: " + street.getCollateralPrice() + "₩" + status);
        }
        assetsList.setModel(model);

        // Проверяем, хватает ли теперь денег
        if (player.getMoney() >= debt) {
            buttonOK.setEnabled(true);
            statusLabel.setText("Денег достаточно для погашения долга! Нажмите 'Завершить'");
            statusLabel.setForeground(Color.GREEN);
        } else {
            buttonOK.setEnabled(false);
            statusLabel.setText("Необходимо еще: " + (debt - player.getMoney()) + "₩");
            statusLabel.setForeground(Color.RED);
        }

        updateButtonsState();
    }

    private void updateButtonsState() {
        int selectedIndex = assetsList.getSelectedIndex();
        if (selectedIndex != -1) {
            StreetCard selectedStreet = getSelectedStreet(selectedIndex);
            if (selectedStreet != null) {
                mortgageButton.setEnabled(!selectedStreet.isMortgaged() &&
                        selectedStreet.getHouseCount() == 0 && selectedStreet.getHotelCount() == 0);
                sellBuildingsButton.setEnabled(!selectedStreet.isMortgaged() &&
                        (selectedStreet.getHouseCount() > 0 || selectedStreet.getHotelCount() > 0));
                return;
            }
        }
        mortgageButton.setEnabled(false);
        sellBuildingsButton.setEnabled(false);
    }

    private void onOK() {
        resolvedSuccessfully = true;
        dispose();
    }

    private void onCancel() {
        int result = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите объявить банкротство? Это приведет к выходу из игры.",
                "Подтверждение банкротства",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            resolvedSuccessfully = false;
            dispose();
        }
    }

    public boolean isResolvedSuccessfully() {
        return resolvedSuccessfully;
    }

    public static void main(String[] args) {
        Player testPlayer = new Player(1, "Тестовый Игрок");
        testPlayer.increaseMoney(100); // Установим немного денег

        StreetCard street1 = new StreetCard(1, "Улица 1", "Красный", 100, new int[]{10, 20, 30, 40, 50, 60}, 50, 100, 50);
        StreetCard street2 = new StreetCard(2, "Улица 2", "Синий", 120, new int[]{12, 24, 36, 48, 60, 72}, 60, 120, 60);
        StreetCard street3 = new StreetCard(3, "Улица 3", "Зеленый", 140, new int[]{14, 28, 42, 56, 70, 84}, 70, 140, 70);

        testPlayer.buyCard(street1, 100);
        testPlayer.buyCard(street2, 120);
        testPlayer.buyCard(street3, 140);

        street1.setHouseCount(2);

        MonopolyWindow mockMonopolyWindow = new MonopolyWindow();

        int testDebt = 500;

        BankruptcyManagementWindow dialog = new BankruptcyManagementWindow(null, testPlayer, testDebt, mockMonopolyWindow);
        dialog.setVisible(true);

        if (dialog.isResolvedSuccessfully()) {
            System.out.println("Игрок смог избежать банкротства!");
        } else {
            System.out.println("Игрок объявил банкротство.");
        }

        System.out.println("Итоговые деньги игрока: " + testPlayer.getMoney());
        System.out.println("Итоговый инвентарь:");
        for (StreetCard street : testPlayer.getStreetsInventory()) {
            System.out.println(street.getStreetName() + " (Заложена: " + street.isMortgaged() + ", Домов: " + street.getHouseCount() + ")");
        }
    }
}