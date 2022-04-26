package chess.console;

import chess.console.controller.ConsoleChessController;
import chess.model.game.ChessGame;

public class Main {

    public static void main(final String[] args) {
        ConsoleChessController consoleChessController = new ConsoleChessController(new ChessGame());
        consoleChessController.run();
    }
}
