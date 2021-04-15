package chess.contoller;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import chess.dto.web.RoomDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class ChessSparkController {

    private static final Gson GSON = new Gson();
    private static final HandlebarsTemplateEngine HANDLEBARS_TEMPLATE_ENGINE = new HandlebarsTemplateEngine();

    private final ChessService chessService;

    public ChessSparkController(ChessService chessService) {
        this.chessService = chessService;
    }

    public void run() {
        Spark.staticFileLocation("/public");

        lobby();
        createRoom();
        closeRoom();
        joinRoom();
        usersInRoom();
        startGame();
        finishGame();
        movePiece();
        gameStatus();
        movablePoints();
        exceptionHandler();
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

    private void movablePoints() {
        get("/room/:id/movablePoints/:point", "application/json", (req, res) ->
            GSON.toJson(chessService.movablePoints(req.params("id"), req.params("point"))));
    }

    private void gameStatus() {
        get("/room/:id/getGameStatus", "application/json",
            (req, res) -> GSON.toJson(chessService.gameStatus(req.params("id"))));
    }

    private void movePiece() {
        put("/room/:id/move", "application/json", (req, res) -> {
            Map<String, String> body = GSON.fromJson(req.body(), HashMap.class);

            return GSON.toJson(
                chessService.move(req.params("id"), body.get("source"), body.get("destination")));
        });
    }

    private void finishGame() {
        put("/room/:id/exit", (req, res) -> {
            chessService.exit(req.params("id"));

            return GSON.toJson("success");
        });
    }

    private void startGame() {
        put("/room/:id/start", (req, res) -> GSON.toJson(chessService.start(req.params(":id"))));
    }

    private void usersInRoom() {
        get("/room/:id/statistics", "application/json",
            (req, res) -> GSON.toJson(chessService.usersInRoom(req.params("id"))));
    }

    private void joinRoom() {
        get("/room/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("board", chessService.latestBoard(req.params("id")));
            model.put("roomId", req.params("id"));
            model.put("userInfo", chessService.usersInRoom(req.params("id")));
            return render(model, "index.html");
        });
    }

    private void closeRoom() {
        put("/room", (req, res) -> {
            Map<String, String> body = GSON.fromJson(req.body(), HashMap.class);
            chessService.close(body.get("id"));

            return GSON.toJson("success");
        });
    }

    private void createRoom() {
        post("/room", "application/json", (req, res) -> {
            RoomDto newRoom = GSON.fromJson(req.body(), RoomDto.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", "success");
            jsonObject.addProperty("roomId", chessService.create(newRoom));
            return GSON.toJson(jsonObject);
        });
    }

    private void lobby() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("rooms", chessService.openedRooms());
            return render(model, "lobby.html");
        });
    }

    private static String render(Map<String, Object> model, String viewName) {
        return HANDLEBARS_TEMPLATE_ENGINE.render(new ModelAndView(model, viewName));
    }
}
