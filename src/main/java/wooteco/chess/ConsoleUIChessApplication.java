package wooteco.chess;

import wooteco.chess.consolView.GameManager;

public class ConsoleUIChessApplication {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.runGame();
    }
}
