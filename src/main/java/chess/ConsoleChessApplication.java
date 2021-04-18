package chess;

import chess.controller.console.ChessController;

public class ConsoleChessApplication {
    public static void main(String[] args) {
        ChessController controller = new ChessController();
        controller.run();
    }
}
