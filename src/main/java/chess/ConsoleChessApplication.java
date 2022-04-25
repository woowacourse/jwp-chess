package chess;

import chess.controller.ConsoleChessController;

public class ConsoleChessApplication {
    public static void main(String[] args) {
        final ConsoleChessController consoleChessController = new ConsoleChessController();
        consoleChessController.run();
    }
}
