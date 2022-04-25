package chess.console;

import chess.console.controller.ConsoleChessController;
import chess.model.ChessGame;

public class Main {

    public static void main(String[] args) {
        ConsoleChessController consoleChessController = new ConsoleChessController(new ChessGame());
        consoleChessController.run();
    }
}
