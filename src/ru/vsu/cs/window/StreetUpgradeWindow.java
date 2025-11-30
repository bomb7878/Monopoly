package ru.vsu.cs.window;

import ru.vsu.cs.additions.WindowComponentsUtils;
import ru.vsu.cs.cards.StreetCard;
import ru.vsu.cs.game.Player;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StreetUpgradeWindow extends JDialog {
    private final Player player;
    private final StreetCard street;
    private final MonopolyWindow monopolyWindow;

    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton incrementHouseCountButton;
    private JButton incrementHotelCountButton;
    private JPanel cardPanel;
    private JTextPane streetInfoTextPane;

    public StreetUpgradeWindow(Player player, MonopolyWindow window, StreetCard street) {
        this.player = player;
        this.street = street;
        this.monopolyWindow = window;

        initializeWindow();
        createUIComponents();
        setupEventHandlers();
        updateStreetInfo();
        configureButtonsAvailability();
    }

    private void initializeWindow() {
        setContentPane(createContentPane());
        setModal(true);
        setTitle("Улучшение " + street.getStreetName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(850, 500);

        street.calcCorrectRentPrice();
    }

    private JPanel createContentPane() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cardPanel = new JPanel(new BorderLayout());

        JPanel buttonsPanel = createButtonsPanel();

        contentPane.add(cardPanel, BorderLayout.CENTER);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        return contentPane;
    }

    private void createUIComponents() {
        streetInfoTextPane = new JTextPane();
        streetInfoTextPane.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        streetInfoTextPane.setEditable(false);

        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(
                cardPanel,
                streetInfoTextPane,
                monopolyWindow.getMonopolyColorsByDescriptions().get(street.getColor())
        );
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        incrementHouseCountButton = new JButton("Увеличить кол-во домов на улице");
        incrementHotelCountButton = new JButton("Увеличить кол-во отелей на улице");
        buttonCancel = new JButton("Cancel");

        buttonsPanel.add(incrementHouseCountButton);
        buttonsPanel.add(incrementHotelCountButton);
        buttonsPanel.add(buttonCancel);

        return buttonsPanel;
    }

    private void setupEventHandlers() {
        // Cancel button
        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCancel();
            }
        });

        // Escape key handler
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        // House increment button
        incrementHouseCountButton.addActionListener(this::onHouseIncrement);

        // Hotel increment button
        incrementHotelCountButton.addActionListener(this::onHotelIncrement);
    }

    private void configureButtonsAvailability() {
        if (!street.getColorSet().isStreetsHaveSingleOwner()) {
            incrementHouseCountButton.setEnabled(false);
            incrementHouseCountButton.setToolTipText(
                    "Вы не можете увеличить число домов на улице пока не завладеете всеми улицами этого цвета"
            );
            incrementHotelCountButton.setEnabled(false);
            incrementHotelCountButton.setToolTipText(
                    "Вы также не можете увеличить и число отелей на улице по той же причине, " +
                            "однако увеличить число отелей у Вас получится только при наличии на всех улицах данного цвета по 4 домика"
            );
        } else if (!street.getColorSet().canBuildHotels()) {
            incrementHotelCountButton.setEnabled(false);
            incrementHotelCountButton.setToolTipText(
                    "Вы не можете увеличить число отелей на улице пока на всех улицах этой группы не будет стоять по 4 домика"
            );
        }
    }

    private void updateStreetInfo() {
        String info = String.format(
                "%s\nНынешняя Аренда: %d\nКол-во Домов: %d\nКол-во Отелей: %d",
                street.getStreetName(),
                street.getCurrentRentPrice(),
                street.getHouseCount(),
                street.getHotelCount()
        );
        streetInfoTextPane.setText(info);
    }

    private void turnButtons() {
        if (street.getColorSet().canBuildHouses(street)) {
            incrementHouseCountButton.setEnabled(true);
            if (street.getColorSet().canBuildHotels()) {
                incrementHotelCountButton.setEnabled(true);
            }
        }
    }

    private void disableButtons() {
        if(!street.getColorSet().canBuildHouses(street)) {
            incrementHouseCountButton.setEnabled(false);
            if (!street.getColorSet().canBuildHotels()) {
                incrementHouseCountButton.setEnabled(false);
            }
        }
    }

    private void onHouseIncrement(ActionEvent e) {
        if (player.getMoney() >= street.getHousePrice()) {
            player.decreaseMoney(street.getHousePrice());
            street.incrementHouseCount();
            street.calcCorrectRentPrice();
            updateStreetInfo();
            disableButtons();
            turnButtons();
        } else {
            SwingUtils.showInfoMessageBox("Вам не хватает денег на это");
        }
    }

    private void onHotelIncrement(ActionEvent e) {
        if (player.getMoney() >= street.getHotelPrice()) {
            player.decreaseMoney(street.getHotelPrice());
            street.incrementHotelCount();
            street.calcCorrectRentPrice();
            updateStreetInfo();
            disableButtons();
            turnButtons();
        } else {
            SwingUtils.showInfoMessageBox("Вам не хватает денег на это");
        }
    }

    private void onCancel() {
        setVisible(false);
        dispose();
    }

    public void showDialog() {
        setVisible(true);
    }
}