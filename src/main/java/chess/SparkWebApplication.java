package chess;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import chess.controller.SparkWebController;
import chess.dao.ChessGameDao;
import chess.service.ChessGameService;

public class SparkWebApplication {

    public static void main(String[] args) {
        port(8080);
        staticFileLocation("/static");
        final ChessGameService chessGameService = new ChessGameService(new ChessGameDao());
        SparkWebController controller = new SparkWebController(chessGameService);
        controller.run();
    }
}
