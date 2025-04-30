package yutgame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BoardPanel extends JPanel {
    private final int boardSize = 400;
    private final int margin = 50;
    private final int smallRadius = 16;
    private final int bigRadius = 20;
    private final List<Point> cornerPoints = new ArrayList<>();
    private List<Point> centerLines = new ArrayList<>();
    private BoardType boardType = BoardType.SQUARE;

    public BoardPanel(BoardType type) {
        this.boardType = type;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int cx = margin + boardSize / 2;
        int cy = margin + boardSize / 2;
        Point center = new Point(cx, cy);

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);

        List<Point> points = switch (boardType) {
            case SQUARE -> generateSquareBoardPoints();
            case PENTAGON -> generatePentagonBoardPoints();
            case HEXAGON -> generateHexagonBoardPoints();
        };

        if (boardType == BoardType.SQUARE) {
            int startX = margin;
            int startY = margin;
            int endX = startX + boardSize;
            int endY = startY + boardSize;

            g2.drawRect(startX, startY, boardSize, boardSize);
            g2.drawLine(startX, startY, endX, endY);
            g2.drawLine(startX, endY, endX, startY);
        } else if (boardType == BoardType.PENTAGON || boardType == BoardType.HEXAGON) {
            int size = boardType == BoardType.PENTAGON ? 5 : 6;
            for (int i = 0; i < size; i++) {
                Point a = cornerPoints.get(i);
                Point b = cornerPoints.get((i + 1) % size);
                g2.drawLine(a.x, a.y, b.x, b.y);
            }

            Point centerPoint = cornerPoints.get(cornerPoints.size() - 1);
            for (int i = 0; i < size; i++) {
                g2.drawLine(centerPoint.x, centerPoint.y, cornerPoints.get(i).x, cornerPoints.get(i).y);
            }
        }

        for (Point p : points) {
            g2.setColor(Color.WHITE);
            g2.fillOval(p.x - smallRadius, p.y - smallRadius, smallRadius * 2, smallRadius * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - smallRadius, p.y - smallRadius, smallRadius * 2, smallRadius * 2);
        }

        for (Point p : cornerPoints) {
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - bigRadius, p.y - bigRadius, bigRadius * 2, bigRadius * 2);
        }

        if (boardType == BoardType.SQUARE) {
            Point startPoint = null;
            for (Point p : cornerPoints) {
                if (p.x == margin + boardSize && p.y == margin + boardSize) {
                    startPoint = p;
                    break;
                }
            }
            if (startPoint != null) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                g2.setColor(Color.BLACK);
                g2.drawString("출발", startPoint.x - 18, startPoint.y + 5);
            }
        }

        if (boardType == BoardType.PENTAGON) {
            Point startPoint = cornerPoints.get(1);  // 왼쪽 꼭짓점 기준
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setColor(Color.BLACK);
            g2.drawString("출발", startPoint.x - 15, startPoint.y + 5);
        }

        if (boardType == BoardType.HEXAGON) {
            Point startPoint = cornerPoints.get(3);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setColor(Color.BLACK);
            g2.drawString("출발", startPoint.x - 15, startPoint.y + 5);
        }


    }

    private List<Point> generateSquareBoardPoints() {
        List<Point> points = new ArrayList<>();
        cornerPoints.clear();

        int grid = 5;
        int gap = boardSize / (grid - 1);
        int start = margin;

        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                int x = start + i * gap;
                int y = start + j * gap;

                boolean isEdge = (i == 0 || i == 4 || j == 0 || j == 4);
                boolean isCross = (i == 2 || j == 2);
                boolean isDiagonal = (i == j || i + j == 4);
                boolean isExcluded = (i == 1 && j == 2) || (i == 3 && j == 2) || (i == 2 && j == 1) || (i == 2 && j == 3);

                if ((isEdge || isCross || isDiagonal) && !isExcluded) {
                    Point p = new Point(x, y);
                    points.add(p);
                    if ((i == 0 && j == 0) || (i == 4 && j == 0) || (i == 0 && j == 4) || (i == 4 && j == 4) || (i == 2 && j == 2)) {
                        cornerPoints.add(p);
                    }
                }
            }
        }
        return points;
    }

    private List<Point> generatePolygonBoardPoints(int sides) {
        List<Point> points = new ArrayList<>();
        cornerPoints.clear();

        int cx = margin + boardSize / 2;
        int cy = margin + boardSize / 2;
        int radius = boardSize / 2;

        for (int i = 0; i < sides; i++) {
            double angleOffset = (sides == 5) ? 90 : 0; // 오각형은 회전, 육각형은 그대로
            double angle = Math.toRadians(angleOffset + i * 360.0 / sides);
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy - radius * Math.sin(angle));
            Point p = new Point(x, y);
            points.add(p);
            cornerPoints.add(p);
        }



        int subdiv = 5;
        for (int i = 0; i < sides; i++) {
            Point start = cornerPoints.get(i);
            Point end = cornerPoints.get((i + 1) % sides);
            for (int j = 1; j < subdiv; j++) {
                int x = start.x + (end.x - start.x) * j / subdiv;
                int y = start.y + (end.y - start.y) * j / subdiv;
                points.add(new Point(x, y));
            }
        }

        Point center = new Point(cx, cy);
        points.add(center);
        cornerPoints.add(center);

        for (int i = 0; i < sides; i++) {
            Point start = cornerPoints.get(i);
            for (int j = 1; j < 3; j++) {
                int x = start.x + (center.x - start.x) * j / 3;
                int y = start.y + (center.y - start.y) * j / 3;
                points.add(new Point(x, y));
            }
        }

        return points;
    }

    private List<Point> generatePentagonBoardPoints() {
        return generatePolygonBoardPoints(5);
    }

    private List<Point> generateHexagonBoardPoints() {
        return generatePolygonBoardPoints(6);
    }

    public void setBoardType(BoardType type) {
        this.boardType = type;
    }
}
