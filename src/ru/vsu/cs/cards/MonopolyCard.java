package ru.vsu.cs.cards;

import ru.vsu.cs.game.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MonopolyCard extends JPanel {
    private final int cardId;
    private ArrayList<Player> playersOnCard = new ArrayList<>();

    public MonopolyCard(int cardId) {
        this.cardId = cardId;
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(80, 120));
    }

    public int getCardId() {
        return cardId;
    }

    public void addPlayer(Player player) {
        if (!playersOnCard.contains(player)) {
            playersOnCard.add(player);
            repaint(); // Перерисовываем карточку при добавлении игрока
        }
    }

    public void removePlayer(Player player) {
        playersOnCard.remove(player);
        repaint(); // Перерисовываем карточку при удалении игрока
    }

    public ArrayList<Player> getPlayersOnCard() {
        return new ArrayList<>(playersOnCard);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayers(g);
    }

    private void drawPlayers(Graphics g) {
        if (playersOnCard.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int playerCount = playersOnCard.size();
        int size = 15; // Размер фигурок
        int spacing = 3; // Отступ между фигурками

        int maxPlayersInRow = 2;

        // Начальная позиция
        int startX = getWidth() - size - 20; // Отступ от правого края
        int startY = getHeight() - size - 20; // Отступ от нижнего края

        for (int i = 0; i < playerCount; i++) {
            Player player = playersOnCard.get(i);
            int row = i / maxPlayersInRow;
            int col = i % maxPlayersInRow;

            // Позиционируем справа налево и снизу вверх
            int x = startX - col * (size + spacing);
            int y = startY - row * (size + spacing);

            player.drawShape(g2d, x, y, size);
        }
    }

    public void clearAllPlayers() {
        this.playersOnCard.clear();
    }
}
