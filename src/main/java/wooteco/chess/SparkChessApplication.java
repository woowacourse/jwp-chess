package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.ChessController;
import wooteco.chess.controller.SparkChessController;
import wooteco.chess.dao.BoardDAO;
import wooteco.chess.dao.TurnInfoDAO;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");

        ChessService service = new ChessService(new BoardDAO(), new TurnInfoDAO());
        ChessController controller = new SparkChessController(service);

        controller.start();
        controller.playTurn();
    }
}
