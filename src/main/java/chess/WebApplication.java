package chess;

import chess.controller.WebChessController;
import chess.service.ChessService;

public class WebApplication {
    public static void main(String[] args) {
        ChessService chessService = new ChessService();
        WebChessController webChessController = new WebChessController(chessService);

        webChessController.run();
    }
}
