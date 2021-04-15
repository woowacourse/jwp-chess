package chess;

import chess.console.ConsoleChessController;

public class ConsoleApplication {

    public static void main(String[] args) {
        ConsoleChessController chessController = new ConsoleChessController();
        chessController.run();
    }

}
