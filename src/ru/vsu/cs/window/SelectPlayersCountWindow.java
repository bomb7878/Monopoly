package ru.vsu.cs.window;

import ru.vsu.cs.additions.SelectPlayerNamePanel;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SelectPlayersCountWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> playersCountComboBox;
    private JTextField firstPlayerTextField;
    private JTextField secondPlayerTextField;
    private JTextField thirdPlayerTextField;
    private JTextField fourthPlayerTextField;
    private JTextField fifthPlayerTextField;
    private SelectPlayerNamePanel firstPlayerPanel;
    private SelectPlayerNamePanel secondPlayerPanel;
    private SelectPlayerNamePanel thirdPlayerPanel;
    private SelectPlayerNamePanel fourthPlayerPanel;
    private SelectPlayerNamePanel fifthPlayerPanel;

    private int playersCount = 2;
    private SelectPlayerNamePanel[] playersPanels;
    private JTextField[] playersNames;

    private boolean isConfirmed = false;

    public SelectPlayersCountWindow() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindowProperties();

        updatePlayersPanelsVisibility();
    }

    private void initializeComponents() {
        contentPane = new JPanel(new BorderLayout(10, 10));

        firstPlayerPanel = new SelectPlayerNamePanel(1);
        secondPlayerPanel = new SelectPlayerNamePanel(2);
        thirdPlayerPanel = new SelectPlayerNamePanel(3);
        fourthPlayerPanel = new SelectPlayerNamePanel(4);
        fifthPlayerPanel = new SelectPlayerNamePanel(5);

        playersPanels = new SelectPlayerNamePanel[]{
                firstPlayerPanel, secondPlayerPanel, thirdPlayerPanel,
                fourthPlayerPanel, fifthPlayerPanel
        };

        firstPlayerTextField = new JTextField(15);
        secondPlayerTextField = new JTextField(15);
        thirdPlayerTextField = new JTextField(15);
        fourthPlayerTextField = new JTextField(15);
        fifthPlayerTextField = new JTextField(15);

        playersNames = new JTextField[]{
                firstPlayerTextField, secondPlayerTextField, thirdPlayerTextField,
                fourthPlayerTextField, fifthPlayerTextField
        };

        playersCountComboBox = new JComboBox<>(new String[]{"2", "3", "4", "5"});
        playersCountComboBox.setSelectedIndex(0);

        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Cancel");
    }

    private void setupLayout() {
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Выбор кол-ва игроков
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        countPanel.add(new JLabel("Количество игроков:"));
        countPanel.add(playersCountComboBox);
        mainPanel.add(countPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Панели игроков
        mainPanel.add(createPlayerPanel(1, firstPlayerPanel, firstPlayerTextField, "Игрок 1:"));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createPlayerPanel(2, secondPlayerPanel, secondPlayerTextField, "Игрок 2:"));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createPlayerPanel(3, thirdPlayerPanel, thirdPlayerTextField, "Игрок 3:"));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createPlayerPanel(4, fourthPlayerPanel, fourthPlayerTextField, "Игрок 4:"));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createPlayerPanel(5, fifthPlayerPanel, fifthPlayerTextField, "Игрок 5:"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonOK);
        buttonPanel.add(buttonCancel);

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createPlayerPanel(int playerNumber, SelectPlayerNamePanel shapePanel,
                                     JTextField textField, String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));

        // Левая сторона: панель формы (фиксированный размер)
        shapePanel.setPreferredSize(new Dimension(80, 60));
        shapePanel.setMinimumSize(new Dimension(80, 60));
        shapePanel.setMaximumSize(new Dimension(80, 60));

        // Центр: надпись и текстовое поле
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.add(new JLabel(labelText), BorderLayout.WEST);
        inputPanel.add(textField, BorderLayout.CENTER);

        panel.add(shapePanel, BorderLayout.WEST);
        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // Call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // Call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        playersCountComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    playersCount = Integer.parseInt((String) playersCountComboBox.getSelectedItem());
                    updatePlayersPanelsVisibility();
                }
            }
        });
    }

    private void setupWindowProperties() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Выбор Кол-ва игроков");
        setPreferredSize(new Dimension(625, 525));
        setResizable(true);
        setLocationRelativeTo(null);
        pack();
    }

    private void updatePlayersPanelsVisibility() {
        for (int i = 0; i < playersPanels.length; i++) {
            boolean visible = (i < playersCount);
            playersPanels[i].setVisible(visible);

            // Также заставим родительскую панель скрывать / показывать всю строку целиком
            Container parent = playersPanels[i].getParent();
            if (parent != null) {
                parent.setVisible(visible);
            }
        }
        updateWindowSize();
    }

    private void onOK() {
        for (int i = 0; i < playersCount; i++) {
            if (playersNames[i].getText().trim().isEmpty()) {
                SwingUtils.showInfoMessageBox("Прежде заполните все поля имён");
                playersNames[i].requestFocus(); // фокус на пустое поле
                return;
            }
        }
        this.isConfirmed = true;
        this.setVisible(false);
    }

    private void onCancel() {
        this.setVisible(false);
        System.exit(0);
    }

    private void updateWindowSize() {
        pack();
        setMinimumSize(new Dimension(400, 400));
        revalidate();
        repaint();
    }

    public String[] getPlayersNames() {
        String[] names = new String[playersCount];
        for (int i = 0; i < playersCount; i++) {
            names[i] = playersNames[i].getText();
        }
        return names;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public static void main(String[] args) {
        SelectPlayersCountWindow dialog = new SelectPlayersCountWindow();
        dialog.setVisible(true);
        System.exit(0);
    }
}