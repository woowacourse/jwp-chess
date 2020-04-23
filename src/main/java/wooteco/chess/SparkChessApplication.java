package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.dao.BoardDAO;
import wooteco.chess.dao.TurnInfoDAO;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");
        new SparkChessController(new ChessService(new BoardDAO(), new TurnInfoDAO())).run();
    }
}
