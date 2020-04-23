package wooteco.chess;

import wooteco.chess.controller.SparkGameController;
import wooteco.chess.controller.SparkRoomController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static spark.Spark.*;
import static spark.Spark.exception;

public class SparkChessApplication {
    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/static");
        externalStaticFileLocation("src/main/resources/templates");

        get(SparkRoomController.BASIC_URL, SparkRoomController.getAllRoom);
        get(SparkRoomController.CREATE_ROOM_URL, SparkRoomController.createRoom);
        get(SparkRoomController.REMOVE_ROOM_URL, SparkRoomController.removeRoom);
        get(SparkRoomController.ENTER_ROOM_URL, SparkRoomController.enterRoom);

        get(SparkGameController.INIT_URL, SparkGameController::initGame);
        post(SparkGameController.MOVE_URL, SparkGameController::movePiece);
        get(SparkGameController.STATUS_URL, SparkGameController::showStatus);
        get(SparkGameController.LOAD_URL, SparkGameController::loadGame);
        get(SparkGameController.GET_URL, SparkGameController::getMovablePositions);

        exception(IllegalArgumentException.class, (e, req, res) -> {
            Gson gson = new Gson();
            JsonObject object = new JsonObject();

            object.addProperty("errorMessage", e.getMessage());
            res.body(gson.toJson(object));
        });
    }
}
