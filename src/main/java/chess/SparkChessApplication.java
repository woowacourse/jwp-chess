package chess;

import chess.repository.ChessRepository;
import chess.service.ChessService;
import chess.web.ChessController;
import chess.web.ExceptionHandler;
import chess.web.MoveController;

import static spark.Spark.*;

public class SparkChessApplication {

    public static void main(String[] args) {
        ChessController chessController = initializeChessController();

        setConfiguration();
        get("/", MoveController::moveToGamePage);
        get("/result", MoveController::moveToResultPage);
        get("/chessgame/show", chessController::showChessBoard);
        post("/chessgame/move", chessController::move);
        get("/chessgame/show/result", chessController::showResult);
        get("/chessgame/restart", chessController::restart);
        exception(RuntimeException.class, ExceptionHandler::bindException);
    }

    private static void setConfiguration() {
        port(8080);
        staticFiles.location("/static");
    }

    private static ChessController initializeChessController() {
        ChessRepository chessRepository = new ChessRepository();
        ChessService chessService = new ChessService(chessRepository);
        return new ChessController(chessService);
    }
}
