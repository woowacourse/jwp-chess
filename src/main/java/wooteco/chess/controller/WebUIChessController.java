package wooteco.chess.controller;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.exception.*;
import wooteco.chess.service.ChessGameService;

import java.sql.SQLException;
import java.util.Map;

import static spark.Spark.*;

public class WebUIChessController {
    private ChessGameService chessGameService;

    public WebUIChessController() throws SQLException {
        this.chessGameService = new ChessGameService();
    }

    public void run() {
        staticFiles.location("/public");

        get("/", (req, res) -> {
            return render(chessGameService.receiveLoadedBoard(), "index.html");
        });

        post("/start", (req, res) -> {
            chessGameService.initializeTurn();
            chessGameService.initializeFinish();
            return render(chessGameService.receiveInitializedBoard(), "index.html");
        });

        post("/end", (req, res) -> render(chessGameService.receiveEmptyBoard(), "index.html"));

        post("/load", (req, res) -> render(chessGameService.receiveLoadedBoard(), "index.html"));

        post("/move", (req, res) -> {
            try {
                chessGameService.receiveMovedBoard(
                        req.queryParams("fromPiece"), req.queryParams("toPiece"));
                if (chessGameService.isFinish()) {
                    return chessGameService.receiveWinner();
                }
            } catch(InvalidPositionException | PieceImpossibleMoveException | TakeTurnException e) {
                res.status(400);
                return e.getMessage();
            }
            return req.queryParams("fromPiece")+ " " +req.queryParams("toPiece");
        });

        post("/status", (req, res) -> {
            Map<String, Object> model = chessGameService.receiveScoreStatus();
            return render(model, "index.html");
        });

        get("/turn", (req, res) -> {
            return chessGameService.getCurrentTurn().toString();
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
