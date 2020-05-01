package wooteco.chess;

import wooteco.chess.controller.SparkController;
import wooteco.chess.repository.SparkCommandsRepository;
import wooteco.chess.service.ChessService;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class SparkChessApplication {
    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/public");

        SparkCommandsRepository sparkCommandsDao = new SparkCommandsRepository() {
        };

        SparkController controller = new SparkController(new ChessService(sparkCommandsDao));
        controller.play();
    }
}
