package chess;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import chess.dao.jdbc.DatabaseBoardDao;
import chess.dao.jdbc.DatabaseGameDao;
import chess.service.ChessService;
import chess.web.controller.SparkChessController;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFileLocation("/static");
        port(8080);
        SparkChessController controller = new SparkChessController(
                new ChessService(new DatabaseBoardDao(), new DatabaseGameDao()));
        controller.run();
    }
}
