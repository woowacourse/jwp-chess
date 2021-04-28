package chess;

import chess.controller.console.ConsoleController;
import chess.mysql.ChessGameManagerRepositoryImpl;
import chess.mysql.MysqlChessDao;
import chess.mysql.StaticMemoryChessDao;
import chess.service.ChessServiceImpl;

import java.util.Objects;

import static chess.mysql.ChessConnection.getConnection;

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
