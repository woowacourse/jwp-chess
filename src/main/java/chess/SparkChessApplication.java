package chess;

import chess.controller.SparkController;
import chess.domain.dao.CommandDao;
import chess.domain.dao.HistoryDao;
import chess.service.SparkChessService;

import static spark.Spark.*;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");
        final SparkController sparkController = initWebController(new CommandDao(), new HistoryDao());

        get("/play", sparkController::moveToMainPage);
        get("/play/new", sparkController::playNewGameWithNoSave);
        get("/play/:name/new", sparkController::playNewGameWithSave);
        post("/play/move", sparkController::movePiece);
        get("/play/continue", sparkController::continueGame);
        get("/play/end", sparkController::endGame);
    }

    private static SparkController initWebController(CommandDao commandDao, HistoryDao historyDao) {
        return new SparkController(new SparkChessService(commandDao, historyDao));
    }
}