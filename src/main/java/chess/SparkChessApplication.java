package chess;

import chess.controller.ChessWebController;
import chess.dao.CustomConnectionPool;
import chess.dao.DBChessDao;
import chess.dao.DBMovementDao;
import chess.service.ChessService;

public class SparkChessApplication {

    public static void main(String[] args) {
        ChessService chessService = new ChessService(new DBChessDao(CustomConnectionPool.create()),
            new DBMovementDao(CustomConnectionPool.create()));
        ChessWebController chessWebController = new ChessWebController(chessService);
        chessWebController.run();
    }
}
