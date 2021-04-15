package chess.contoller;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import chess.dto.web.RoomDto;
import chess.service.SparkChessService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkChessController {

    private static final Gson GSON = new Gson();
    private static final HandlebarsTemplateEngine HANDLEBARS_TEMPLATE_ENGINE = new HandlebarsTemplateEngine();

    private final SparkChessService sparkChessService;

    public SparkChessController(SparkChessService sparkChessService) {
        this.sparkChessService = sparkChessService;
    }

    public void run() {
        Spark.staticFileLocation("/public");

        lobby();
        joinRoom();
        createRoom();
        usersInRoom();
        gameStatus();
        startGame();
        closeRoom();
        exitGame();
        movablePoints();
        movePiece();
        exceptionHandler();
    }

    private void lobby() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("rooms", sparkChessService.openedRooms());
            return render(model, "lobby.html");
        });
    }

    private void joinRoom() {
        get("/room/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("board", sparkChessService.latestBoard(req.params("id")));
            model.put("roomId", req.params("id"));
            model.put("userInfo", sparkChessService.usersInRoom(req.params("id")));
            return render(model, "index.html");
        });
    }

    private void createRoom() {
        post("/room", "application/json", (req, res) -> {
            RoomDto newRoom = GSON.fromJson(req.body(), RoomDto.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", "success");
            jsonObject.addProperty("roomId", sparkChessService.create(newRoom));
            return GSON.toJson(jsonObject);
        });
    }

    private void usersInRoom() {
        get("/room/:id/statistics", "application/json",
            (req, res) -> GSON.toJson(sparkChessService.usersInRoom(req.params("id"))));
    }

    private void gameStatus() {
        get("/room/:id/getGameStatus", "application/json",
            (req, res) -> GSON.toJson(sparkChessService.gameStatus(req.params("id"))));
    }

    private void startGame() {
        put("/room/:id/start", (req, res) -> GSON.toJson(sparkChessService.start(req.params(":id"))));
    }

    private void closeRoom() {
        put("/room", (req, res) -> {
            Map<String, String> body = GSON.fromJson(req.body(), HashMap.class);
            sparkChessService.close(body.get("id"));

            return GSON.toJson("success");
        });
    }

    private void exitGame() {
        put("/room/:id/exit", (req, res) -> {
            sparkChessService.exit(req.params("id"));

            return GSON.toJson("success");
        });
    }

    private void movablePoints() {
        get("/room/:id/movablePoints/:point", "application/json", (req, res) ->
            GSON.toJson(sparkChessService.movablePoints(req.params("id"), req.params("point"))));
    }

    private void movePiece() {
        put("/room/:id/move", "application/json", (req, res) -> {
            Map<String, String> body = GSON.fromJson(req.body(), HashMap.class);

            return GSON.toJson(
                sparkChessService.move(req.params("id"), body.get("source"), body.get("destination")));
        });
    }

    private void exceptionHandler() {
        exception(Exception.class, (e, req, res) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", "fail");
            jsonObject.addProperty("message", e.getMessage());
            res.status(500);
            res.body(GSON.toJson(jsonObject));
        });
    }

    private static String render(Map<String, Object> model, String viewName) {
        return HANDLEBARS_TEMPLATE_ENGINE.render(new ModelAndView(model, viewName));
    }
}
