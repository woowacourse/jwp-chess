package chess;

import chess.controller.SparkController;
import chess.domain.dao.SparkCommandDao;
import chess.domain.dao.SparkHistoryDao;
import chess.service.SparkChessService;

import static spark.Spark.*;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");
        final SparkController sparkController = initWebController(new SparkCommandDao(), new SparkHistoryDao());

        get("/play", sparkController::moveToMainPage);
        get("/play/new", sparkController::playNewGameWithNoSave);
        get("/play/:name/new", sparkController::playNewGameWithSave);
        post("/play/move", sparkController::movePiece);
        get("/play/continue", sparkController::continueGame);
        get("/play/end", sparkController::endGame);
    }

    private static SparkController initWebController(SparkCommandDao springCommandDao, SparkHistoryDao sparkHistoryDao) {
        return new SparkController(new SparkChessService(springCommandDao, sparkHistoryDao));
    }
}