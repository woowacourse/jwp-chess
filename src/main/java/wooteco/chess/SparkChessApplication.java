package wooteco.chess;

import wooteco.chess.controller.SparkController;
import wooteco.chess.dao.SparkCommandsDao;
import wooteco.chess.service.ChessService;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class SparkChessApplication {
    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/public");

        SparkCommandsDao sparkCommandsDao = new SparkCommandsDao() {
        };

        SparkController controller = new SparkController(new ChessService(sparkCommandsDao));
        controller.play();
    }
}
