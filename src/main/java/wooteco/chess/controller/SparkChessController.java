package wooteco.chess.controller;

import static spark.Spark.*;
import static wooteco.chess.util.JsonParser.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MovableRequestDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.ChessService;

public class SparkChessController {
    private ChessService service;
    private Gson gson = new Gson();

    public SparkChessController(ChessService service) {
        this.service = service;
        route();
    }

    public void route() {
        get("/", this::renderEntryPoint);
        get("/boards", this::getPlayerContexts, json());
        post("/boards", this::addGameAndGetPlayers, json());

        get("/boards/:id", this::getBoard, json());
        post("/boards/:id", this::resetBoard, json());
        delete("/boards/:id", this::finishGame, json());

        path("/boards/:id", () -> {
            get("/status", this::isGameOver, json());
            get("/turn", this::isWhiteTurn, json());
            get("/score", this::getScore, json());

            post("/move", this::move, json());
            post("/movable", this::findAllAvailablePath, json());
        });

        get("/scores", this::getScoreContexts, json());
    }

    private String renderEntryPoint(Request request, Response response) {
        response.type("text/html");
        return render(new HashMap<>());
    }

    private Map<Integer, Map<Side, Player>> getPlayerContexts(final Request request, final Response response) throws
        SQLException {
        return service.getPlayerContexts();
    }

    private Map<Integer, Map<Side, Player>> addGameAndGetPlayers(final Request request, final Response response) throws
        SQLException {
        // TODO: 실제 플레이어 기능 추가
        Player white = new Player(1, "hodol", "password");
        Player black = new Player(2, "pobi", "password");
        return service.addGame(white, black);
    }

    private BoardDto getBoard(final Request request, final Response response) throws SQLException {
        return new BoardDto(service.findBoardById(parseId(request)));
    }

    private BoardDto resetBoard(final Request request, final Response response) throws SQLException {
        return new BoardDto(service.resetGameById(parseId(request)));
    }

    public boolean finishGame(final Request request, final Response response) throws SQLException {
        return service.finishGameById(parseId(request));
    }

    public boolean isGameOver(final Request request, final Response response) throws SQLException {
        return service.isGameOver(parseId(request));
    }

    public boolean isWhiteTurn(final Request request, final Response response) throws SQLException {
        return service.isWhiteTurn(parseId(request));
    }

    public Map<Side, Double> getScore(final Request request, final Response response) throws SQLException {
        return service.getScoresById(parseId(request));
    }

    public boolean move(final Request request, final Response response) throws SQLException {

        MoveRequestDto dto = gson.fromJson(request.body(), MoveRequestDto.class);
        return service.moveIfMovable(parseId(request), dto.getFrom(), dto.getTo());
    }

    public List<String> findAllAvailablePath(final Request request, final Response response) throws SQLException {
        MovableRequestDto dto = gson.fromJson(request.body(), MovableRequestDto.class);
        return service.findAllAvailablePath(parseId(request), dto.getFrom());
    }

    public Map<Integer, Map<Side, Double>> getScoreContexts(final Request request, final Response response) throws
        SQLException {
        return service.getScoreContexts();
    }

    private static String render(Map<String, Object> model) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "index.html"));
    }

    private static int parseId(final Request request) {
        return Integer.parseInt(request.params(":id"));
    }
}
