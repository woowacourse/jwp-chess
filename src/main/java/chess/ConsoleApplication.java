package chess;

import chess.controller.console.ConsoleController;
import chess.dao.MysqlChessDao;
import chess.dao.StaticMemoryChessDao;
import chess.service.ChessServiceImpl;

import java.util.Objects;

import static chess.dao.ChessConnection.getConnection;

public class ConsoleApplication {
    public static void main(String[] args) {
        ChessServiceImpl chessServiceImpl = new ChessServiceImpl(new StaticMemoryChessDao());

        if (!Objects.isNull(getConnection())) {
            chessServiceImpl = new ChessServiceImpl(new MysqlChessDao());
        }

        ConsoleController chessController = new ConsoleController(chessServiceImpl);
        chessController.run();
    }
}
