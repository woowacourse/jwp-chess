package chess.controller;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import chess.domain.piece.Piece;
import chess.service.SparkChessService;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkController {

    private static final int COLUMN_INDEX = 0;
    private static final int ROW_INDEX = 1;

    public void run() {
        staticFileLocation("/static");
        SparkChessService chessService = new SparkChessService();
        getIndexPage(chessService);
        createChessGame(chessService);
        deleteChessGame(chessService);
        getChessGamePage(chessService);
        movePiece(chessService);
        resetChessGame(chessService);
        getException();
    }

    private void getIndexPage(final SparkChessService springChessService) {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("chess_games", springChessService.loadAllChessGames());
            return new ModelAndView(model, "index.html");
        }, new HandlebarsTemplateEngine());
    }

    private void createChessGame(final SparkChessService springChessService) {
        post("/create_chess_game", (req, res) -> {
            String name = req.queryParams("name");
            springChessService.createChessGame(name);
            res.redirect("/game/" + name);
            return null;
        });
    }

    private void getChessGamePage(final SparkChessService springChessService) {
        get("/game/:name", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String name = req.params(":name");
            model.put("chess_game_name", name);
            Map<String, Piece> boardForHtml = convertBoardForHtml(springChessService, name);
            model.putAll(boardForHtml);
            model.put("turn", springChessService.loadChessGame(name).getTurn());
            model.put("result", springChessService.loadChessGame(name).generateResult());
            return new ModelAndView(model, "chess_game.html");
        }, new HandlebarsTemplateEngine());
    }

    private Map<String, Piece> convertBoardForHtml(final SparkChessService springChessService, final String name) {
        return springChessService.loadChessGame(name).getCurrentBoard().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey().getColumn().getValue()) +
                                entry.getKey().getRow().getValue(),
                        Entry::getValue
                ));
    }

    private void deleteChessGame(final SparkChessService springChessService) {
        post("/delete/:name", (req, res) -> {
            springChessService.deleteChessGame(req.params(":name"));
            res.redirect("/");
            return null;
        });
    }

    private void movePiece(final SparkChessService springChessService) {
        post("/move/:chess_game_name", (req, res) -> {
            String chessGameName = req.params(":chess_game_name");
            String rawSource = req.queryParams("source").trim().toLowerCase();
            String rawTarget = req.queryParams("target").trim().toLowerCase();
            springChessService.movePiece(
                    chessGameName,
                    rawSource.charAt(COLUMN_INDEX),
                    Character.getNumericValue(rawSource.charAt(ROW_INDEX)),
                    rawTarget.charAt(COLUMN_INDEX),
                    Character.getNumericValue(rawTarget.charAt(ROW_INDEX))
            );
            res.redirect("/game/" + chessGameName);
            return null;
        });
    }

    private void resetChessGame(final SparkChessService springChessService) {
        post("/reset/:chess_game_name", (req, res) -> {
            String chessGameName = req.params(":chess_game_name");
            springChessService.createChessGame(chessGameName);
            res.redirect("/game/" + chessGameName);
            return null;
        });
    }

    private void getException() {
        exception(Exception.class, (exception, req, res) -> {
            res.status(400);
            res.body(exception.getMessage());
        });
    }
}
