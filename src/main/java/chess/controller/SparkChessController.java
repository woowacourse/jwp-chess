package chess.controller;

import chess.dao.RoomDao;
import chess.dao.jdbcspark.BackupBoardDaoJdbc;
import chess.dao.jdbcspark.RoomDaoJdbc;
import chess.domain.Game;
import chess.domain.board.Board;
import chess.domain.piece.PieceColor;
import chess.dto.RoomNameDto;
import chess.dto.SquareDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class SparkChessController {
    private final static HandlebarsTemplateEngine HANDLEBARS_TEMPLATE_ENGINE = new HandlebarsTemplateEngine();
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final RoomDao roomDaoJdbc = new RoomDaoJdbc();
    private final BackupBoardDaoJdbc backupBoardDaoJdbc = new BackupBoardDaoJdbc();

    private static void isStart(Game game) {
        if (game.isNotStart()) {
            throw new IllegalArgumentException("게임이 시작되지 않았습니다.");
        }
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return HANDLEBARS_TEMPLATE_ENGINE.render(new ModelAndView(model, templatePath));
    }

    public void run() {
        staticFiles.location("/static");
        List<RoomNameDto> roomNames = roomDaoJdbc.findRoomNames();
        ChessService chessService = new ChessService(roomDaoJdbc, backupBoardDaoJdbc);

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("names", roomDaoJdbc.findRoomNames());
            return render(model, "index.html");
        });

        get("/start", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.queryParams("newGame").equals("yes")) {
                roomDaoJdbc.addRoom(req.queryParams("roomName"), PieceColor.WHITE);
                chessService.currentGame(req.queryParams("roomName"));
            }
            return render(model, "game.html");
        });

        post("/game", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Game currentGame = chessService.currentGame(req.queryParams("roomName"));
            currentGame.init();
            model.put("squares", squareDtos(currentGame.getBoard()));
            model.put("turn", currentGame.turnColor().getName());
            return GSON.toJson(model);
        });

        post("/continue", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Game currentGame = chessService.currentGame(req.queryParams("roomName"));
            model.put("squares", squareDtos(currentGame.getBoard()));
            model.put("turn", currentGame.turnColor().getName());
            return GSON.toJson(model);
        });

        post("/move", (req, res) -> {
            Game currentGame = chessService.currentGame(req.queryParams("roomName"));
            isStart(currentGame);
            currentGame.move(req.queryParams("source"), req.queryParams("target"));
            if (currentGame.isEnd()) {
                backupBoardDaoJdbc.deleteExistingBoard(req.queryParams("roomName"));
                roomDaoJdbc.deleteRoom(req.queryParams("roomName"));
                return req.queryParams("source") + " " +
                    req.queryParams("target") + " " + currentGame.winnerColor().getSymbol();
            }

            return req.queryParams("source") + " " +
                req.queryParams("target") + " " + currentGame.turnColor().getName();
        });

        post("/status", (req, res) -> {
            Game currentGame = chessService.currentGame(req.queryParams("roomName"));
            return currentGame.computeWhitePoint() + " " + currentGame.computeBlackPoint();
        });

        post("/end", (req, res) -> {
            Game currentGame = chessService.currentGame(req.queryParams("roomName"));
            backupBoardDaoJdbc.savePlayingBoard(req.queryParams("roomName"),
                currentGame.getBoard(),
                currentGame.turnColor()
            );
            return "";
        });

        exception(RuntimeException.class, (exception, request, response) -> {
            response.status(400);
            response.body(exception.getMessage());
        });
    }

    private List<SquareDto> squareDtos(Board board) {
        List<SquareDto> squareDtos = new ArrayList<>();
        board.positions()
            .forEach(key ->
                squareDtos.add(new SquareDto(key.toString(), board.pieceAtPosition(key).toString()))
            );

        return squareDtos;
    }
}
