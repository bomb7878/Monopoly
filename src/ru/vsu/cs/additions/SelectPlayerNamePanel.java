package ru.vsu.cs.additions;

import javax.swing.*;
import java.awt.*;

public class SelectPlayerNamePanel extends JPanel {
    private int id;

    public SelectPlayerNamePanel(int id) {
        super();
        this.id = id;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setBackground(Color.white);
        if (id == 1) {
            g.setColor(Color.red);
            g.fillRect(getWidth() - 70, getHeight() - 70, 60, 60);
        }
        else if (id == 2) {
            g.setColor(Color.green);
            int[] xPoints = {getWidth() - 40, getWidth() - 70, getWidth() - 10};
            int[] yPoints = {getHeight() - 70, getHeight() - 10, getHeight() - 10};
            g.fillPolygon(xPoints, yPoints, 3);
        }
        else if (id == 3) {
            g.setColor(Color.yellow);
            g.fillOval(getWidth() - 70, getHeight() - 70, 60, 60);
        }
        else if (id == 4) {
            g.setColor(Color.blue);
            int centerX = getWidth() - 40;
            int centerY = getHeight() - 40;
            int radius = 30;
            int[] xPoints = new int[6];
            int[] yPoints = new int[6];
            for (int i = 0; i < 6; i++) {
                double angle = 2 * Math.PI * i / 6;
                xPoints[i] = centerX + (int) (radius * Math.cos(angle));
                yPoints[i] = centerY + (int) (radius * Math.sin(angle));
            }
            g.fillPolygon(xPoints, yPoints, 6);
        }
        else if (id == 5) {
            g.setColor(Color.orange);
            int centerX = getWidth() - 40;
            int centerY = getHeight() - 40;
            int outerRadius = 30;
            int innerRadius = 12;

            int[] xPoints = new int[10];
            int[] yPoints = new int[10];

            for (int i = 0; i < 10; i++) {
                double angle = 2 * Math.PI * i / 10 - Math.PI / 2;
                int radius = (i % 2 == 0) ? outerRadius : innerRadius;
                xPoints[i] = centerX + (int) (radius * Math.cos(angle));
                yPoints[i] = centerY + (int) (radius * Math.sin(angle));
            }
            g.fillPolygon(xPoints, yPoints, 10);
        }
    }

    public int getId() {
        return id;
    }
}
