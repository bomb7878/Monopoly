package ru.vsu.cs.additions;

import ru.vsu.cs.cards.*;

import javax.swing.*;
import java.awt.*;

public class WindowComponentsUtils {
    public static JScrollPane configuringTextPaneAndScrollPane(JTextPane textPane) {
        textPane.setEditable(false);
        textPane.setFocusable(false);
        textPane.setAlignmentX(0.5f);
        textPane.setAlignmentY(0.5f);
        textPane.setOpaque(false);

        textPane.setPreferredSize(new Dimension(100, 100));
        textPane.setMinimumSize(new Dimension(50, 50));

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        return scrollPane;
    }

    public static void addTextPaneWithScrollPaneAtPanelWithColoring(MonopolyCard card, JTextPane textPane, Color color) {
        card.setLayout(new BorderLayout());

        JScrollPane scrollPane = configuringTextPaneAndScrollPane(textPane);

        // Создаем верхнюю панель 20x10
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(10, 20));
        topPanel.setBackground(color);

        // Добавляем компоненты в основную панель
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneWithScrollPaneAtPanelWithColoring(JPanel panel, JTextPane textPane, Color color) {
        panel.setLayout(new BorderLayout());

        JScrollPane scrollPane = configuringTextPaneAndScrollPane(textPane);

        // Создаем верхнюю панель 20x10
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(10, 20));
        topPanel.setBackground(color);

        // Добавляем компоненты в основную панель
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    public static void setTextAboutStreetOnHerTextPane(StreetCard street, JTextPane textPane) {
        textPane.setText(
                String.format("%s\n   ₩%d   ", street.getStreetName(), street.getPurchasePrice())
        );
    }

    public static void addTextPaneToChanceCardAndSetTextAtHer(ChanceCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                "ШАНС"
        );

        textPane.setFont(new Font("Microsoft Sans Serif", Font.ITALIC, 24));

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToCommunityChestCardAndSetTextAtHer(CommunityChestCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                "Общественная Казна"
        );

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToTaxCardAndSetTextAtHer(TaxCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                String.format("%s\n   ₩%d   ", card.getTaxName(), card.getTax())
        );

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToSubwayCardAndSetTextAtHer(SubwayCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                String.format("%s\n   ₩%d   ", card.getSubwayName(), card.getPurchasePrice())
        );

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToUtilityCardAndSetTextAtHer(UtilityCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                String.format("%s\n   ₩%d   ", card.getCardName(), card.getPurchasePrice())
        );

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToJailCardAndSetTextAtHer(JailCard card, JTextPane textPane) {
        //card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                "ТЮРЬМА"
        );
        textPane.setFont(new Font("Microsoft Sans Serif", Font.ITALIC, 18));

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToFreeParkingCardAndSetTextAtHer(FreeParkingCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                "Бесплатная\nПарковка"
        );
        textPane.setFont(new Font("Microsoft Sans Serif", Font.ITALIC, 18));

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToGoToJailCardAndSetTextAtHer(GoToJailCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                "Отправляйся\nВ Тюрьму"
        );
        textPane.setFont(new Font("Microsoft Sans Serif", Font.ITALIC, 18));

        card.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addTextPaneToStartAndFinishCardAndSetTextAtHer(StartAndFinishCard card, JTextPane textPane) {
        card.setLayout(new BorderLayout());
        JScrollPane scrollPane = WindowComponentsUtils.configuringTextPaneAndScrollPane(textPane);

        textPane.setText(
                "ВПЕРЁД!\nПолучайте зарплату\nв размере 200₩\nИ идите дальше"
        );
        textPane.setFont(new Font("Microsoft Sans Serif", Font.ITALIC, 18));

        card.add(scrollPane, BorderLayout.CENTER);
    }
}
