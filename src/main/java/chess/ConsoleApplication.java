package chess;

import chess.controller.console.ConsoleController;
import chess.mysql.ChessGameManagerRepositoryImpl;
import chess.mysql.dao.MysqlChessDao;
import chess.mysql.dao.StaticMemoryChessDao;
import chess.service.ChessServiceImpl;

import java.util.Objects;

import static chess.mysql.dao.ChessConnection.getConnection;

public class ConsoleApplication {
    public static void main(String[] args) {
        ChessServiceImpl chessServiceImpl = new ChessServiceImpl(new ChessGameManagerRepositoryImpl(new StaticMemoryChessDao()));

        if (!Objects.isNull(getConnection())) {
            chessServiceImpl = new ChessServiceImpl(new ChessGameManagerRepositoryImpl(new MysqlChessDao()));
        }

        ConsoleController chessController = new ConsoleController(chessServiceImpl);
        chessController.run();
    }
}
