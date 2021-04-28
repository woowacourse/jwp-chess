package chess;

import chess.controller.web.SparkController;
import chess.mysql.ChessGameManagerRepositoryImpl;
import chess.mysql.MysqlChessDao;
import chess.mysql.StaticMemoryChessDao;
import chess.service.ChessServiceImpl;

import java.util.Objects;

import static chess.mysql.ChessConnection.getConnection;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class SparkUIChessApplication {
    public static void main(String[] args) {
        port(8080);
        staticFileLocation("static");

        ChessServiceImpl chessServiceImpl = new ChessServiceImpl(new ChessGameManagerRepositoryImpl(new StaticMemoryChessDao()));

        if (!Objects.isNull(getConnection())) {
            chessServiceImpl = new ChessServiceImpl(new ChessGameManagerRepositoryImpl(new MysqlChessDao()));
        }

        SparkController sparkController = new SparkController(chessServiceImpl);
        sparkController.start();
    }
}
