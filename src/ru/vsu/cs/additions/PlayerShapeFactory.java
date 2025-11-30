package ru.vsu.cs.additions;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PlayerShapeFactory {
    public static Shape createShape(int id, int x, int y, int size) {
        switch (id) {
            case 1:
                return new Rectangle(x, y, size, size);
            case 2:
                int[] xPoints2 = {x + size/2, x, x + size};
                int[] yPoints2 = {y, y + size, y + size};
                return new Polygon(xPoints2, yPoints2, 3);
            case 3:
                return new Ellipse2D.Double(x, y, size, size);
            case 4:
                return createHexagon(x + size/2, y + size/2, size/2);
            case 5:
                return createStar(x + size/2, y + size/2, size/2, size/4);
            default:
                return new Rectangle(x, y, size, size);
        }
    }

    public static Color getColorById(int id) {
        switch (id) {
            case 1: return Color.RED;
            case 2: return Color.GREEN;
            case 3: return Color.YELLOW;
            case 4: return Color.BLUE;
            case 5: return Color.ORANGE;
            default: return Color.BLACK;
        }
    }

    private static Shape createHexagon(int centerX, int centerY, int radius) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI * i / 6;
            xPoints[i] = centerX + (int) (radius * Math.cos(angle));
            yPoints[i] = centerY + (int) (radius * Math.sin(angle));
        }
        return new Polygon(xPoints, yPoints, 6);
    }

    private static Shape createStar(int centerX, int centerY, int outerRadius, int innerRadius) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        for (int i = 0; i < 10; i++) {
            double angle = 2 * Math.PI * i / 10 - Math.PI / 2;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int) (radius * Math.cos(angle));
            yPoints[i] = centerY + (int) (radius * Math.sin(angle));
        }
        return new Polygon(xPoints, yPoints, 10);
    }
}
