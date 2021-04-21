package chess;

import chess.controller.web.Response;
import chess.controller.web.SparkChessController;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static chess.view.InputView.SPACE;
import static spark.Spark.*;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");

        SparkChessController sparkChessController = new SparkChessController();

        get("/", (req, res) -> {
            Response response = sparkChessController.resetGameAsReadyState();
            return render(response.getModel(), "index.hbs");
        });

        post("/game", (req, res) -> {
            String roomId = req.queryParams("room_id");
            Response response = sparkChessController.createRoom(roomId);
            if (response.isNotSuccessful()) {
                res.status(response.getHttpStatus());
                return render(response.getModel(), "index.hbs");
            }
            return render(response.getModel(), "game.hbs");
        });

        post("/game/move", (req, res) -> {
            List<String> moveCommand = getMoveCommand(req);
            Response response = sparkChessController.movePiece(moveCommand);
            if (response.isNotSuccessful()) {
                res.status(response.getHttpStatus());
            }
            return render(response.getModel(), "game.hbs");
        });

        get("/save", (req, res) -> {
            Response response = sparkChessController.getAllSavedRooms();
            if (response.isNotSuccessful()) {
                res.status(response.getHttpStatus());
            }
            return render(response.getModel(), "repository.hbs");
        });

        post("/game/load", "application/json", (req, res) -> {
            Response response = sparkChessController.loadRoom(req.body());
            if (response.isNotSuccessful()) {
                res.status(response.getHttpStatus());
            }
            return render(response.getModel(), "game.hbs");
        });

        post("/game/save", "application/json", (req, res) -> {
            Response response = sparkChessController.saveRoom(req.body());
            if (response.isNotSuccessful()) {
                res.status(response.getHttpStatus());
            }
            return !response.isNotSuccessful();
        });
    }

    private static List<String> getMoveCommand(spark.Request req) {
        return Arrays.stream(req.queryParams()
                .iterator()
                .next()
                .split(SPACE))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
