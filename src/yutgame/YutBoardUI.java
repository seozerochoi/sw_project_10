package yutgame;

import javax.swing.*;
import java.awt.*;
/*
public class YutBoardUI extends JFrame {
    public YutBoardUI() {
        setTitle("사각형 윷놀이 게임");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BoardPanel boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);
    }
}*/

// YutBoardUI.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class YutBoardUI extends JFrame {
    private final BoardPanel boardPanel;

    public YutBoardUI() {
        setTitle("윷놀이 판");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 기본은 사각형 보드
        boardPanel = new BoardPanel(BoardType.SQUARE);
        add(boardPanel, BorderLayout.CENTER);

        // 버튼 패널 하단
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton squareBtn = new JButton("사각형");
        JButton pentagonBtn = new JButton("오각형");
        JButton hexagonBtn = new JButton("육각형");

        // 버튼 액션
        squareBtn.addActionListener(e -> switchBoard(BoardType.SQUARE));
        pentagonBtn.addActionListener(e -> switchBoard(BoardType.PENTAGON));
        hexagonBtn.addActionListener(e -> switchBoard(BoardType.HEXAGON));

        buttonPanel.add(squareBtn);
        buttonPanel.add(pentagonBtn);
        buttonPanel.add(hexagonBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void switchBoard(BoardType type) {
        boardPanel.setBoardType(type);  // 보드 타입 갱신
        boardPanel.repaint();           // 다시 그리기
    }
}
