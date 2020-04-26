package wooteco.chess.controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.DestinationPositionDto;
import wooteco.chess.dto.MovablePositionsDto;
import wooteco.chess.dto.MoveStatusDto;
import wooteco.chess.service.SparkChessService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static wooteco.chess.web.JsonTransformer.json;

public class SparkChessController {
    private SparkChessService sparkChessService;

    public SparkChessController() {
        this.sparkChessService = new SparkChessService();
    }

    public void route() {
        get("/", this::routeMainPage);

        get("/new", this::startNewGame);

        get("/loading", this::loadGame);

        get("/board", (req, res) -> sparkChessService.setBoard(), json());

        post("/board", this::postBoard);

        post("/source", this::getMovablePositions, json());

        post("/destination", this::move, json());
    }

    private String routeMainPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        model.put("normalStatus", NormalStatus.YES.isNormalStatus());

        return render(model, "index.html");
    }

    private String startNewGame(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        model.put("normalStatus", NormalStatus.YES.isNormalStatus());

        sparkChessService.clearHistory();

        return render(model, "chess.html");
    }

    private String loadGame(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        model.put("normalStatus", NormalStatus.YES.isNormalStatus());

        return render(model, "chess.html");
    }

    private String postBoard(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        try {
            MoveStatusDto moveStatusDto = sparkChessService.move(new MovingPosition(req.queryParams("source"), req.queryParams("destination")));

            model.put("normalStatus", moveStatusDto.getNormalStatus());
            model.put("winner", moveStatusDto.getWinner());

            if (moveStatusDto.getWinner().isNone()) {
                return render(model, "chess.html");
            }
            return render(model, "result.html");
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException | SQLException e) {
            model.put("normalStatus", NormalStatus.NO.isNormalStatus());
            model.put("exception", e.getMessage());
            model.put("winner", Color.NONE);
            return render(model, "chess.html");
        }
    }

    private Map<String, Object> getMovablePositions(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        try {
            MovablePositionsDto movablePositionsDto = sparkChessService.findMovablePositions(req.queryParams("source"));

            model.put("movable", movablePositionsDto.getMovablePositionNames());
            model.put("position", movablePositionsDto.getPosition());
            model.put("normalStatus", NormalStatus.YES.isNormalStatus());

            return model;
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
            model.put("normalStatus", NormalStatus.NO.isNormalStatus());
            model.put("exception", e.getMessage());

            return model;
        }
    }

    private Map<String, Object> move(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        DestinationPositionDto destinationPositionDto = sparkChessService.chooseDestinationPosition(req.queryParams("destination"));

        model.put("normalStatus", destinationPositionDto.getNormalStatus().isNormalStatus());
        model.put("position", destinationPositionDto.getPosition());

        return model;
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
