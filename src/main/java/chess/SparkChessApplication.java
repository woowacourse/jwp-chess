package chess;

import chess.controller.ChessWebController;
import chess.dao.CustomConnectionPool;
import chess.dao.DBChessDao;
import chess.dao.DBMovementDao;
import chess.dao.MysqlConnectionProperty;
import chess.service.ChessService;

public class SparkChessApplication {

    public static void main(String[] args) {
        ChessService chessService = new ChessService(new DBChessDao(CustomConnectionPool.create(new MysqlConnectionProperty())),
            new DBMovementDao(CustomConnectionPool.create(new MysqlConnectionProperty())));
        ChessWebController chessWebController = new ChessWebController(chessService);
        chessWebController.run();
    }
}
