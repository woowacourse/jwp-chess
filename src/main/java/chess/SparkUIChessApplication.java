package chess;

import chess.controller.web.SparkController;
import chess.dao.MysqlChessDao;
import chess.dao.StaticMemoryChessDao;
import chess.service.ChessServiceImpl;

import java.util.Objects;

import static chess.dao.ChessConnection.getConnection;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class SparkUIChessApplication {
    public static void main(String[] args) {
        port(8080);
        staticFileLocation("static");

        ChessServiceImpl chessServiceImpl = new ChessServiceImpl(new StaticMemoryChessDao());

        if (!Objects.isNull(getConnection())) {
            chessServiceImpl = new ChessServiceImpl(new MysqlChessDao());
        }

        SparkController sparkController = new SparkController(chessServiceImpl);
        sparkController.start();
    }

}
