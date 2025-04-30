package yutgame;

public class main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new YutBoardUI().setVisible(true);
        });
    }
}
