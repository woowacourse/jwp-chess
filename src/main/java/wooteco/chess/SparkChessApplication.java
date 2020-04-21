package wooteco.chess;

import wooteco.chess.controller.GameController;
import wooteco.chess.controller.RoomController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static spark.Spark.*;
import static spark.Spark.exception;

public class SparkChessApplication {
    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/static");
        externalStaticFileLocation("src/main/resources/templates");

        get(RoomController.BASIC_URL, RoomController.getAllRoom);
        get(RoomController.CREATE_ROOM_URL, RoomController.createRoom);
        get(RoomController.REMOVE_ROOM_URL, RoomController.removeRoom);
        get(RoomController.ENTER_ROOM_URL, RoomController.enterRoom);

        get(GameController.INIT_URL, GameController::initGame);
        post(GameController.MOVE_URL, GameController::movePiece);
        get(GameController.STATUS_URL, GameController::showStatus);
        get(GameController.LOAD_URL, GameController::loadGame);
        get(GameController.GET_URL, GameController::getMovablePositions);

        exception(IllegalArgumentException.class, (e, req, res) -> {
            Gson gson = new Gson();
            JsonObject object = new JsonObject();

            object.addProperty("errorMessage", e.getMessage());
            res.body(gson.toJson(object));
        });
    }
}
