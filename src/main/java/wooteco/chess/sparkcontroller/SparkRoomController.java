package wooteco.chess.sparkcontroller;

import spark.Route;
import wooteco.chess.domain.room.Room;
import wooteco.chess.service.SparkRoomService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wooteco.chess.util.HandlebarsUtil.render;

public class SparkRoomController {
    public static final String BASIC_URL = "/rooms";
    public static final String ENTER_ROOM_URL = BASIC_URL + "/enter";
    public static final String CREATE_ROOM_URL = BASIC_URL + "/create";
    public static final String REMOVE_ROOM_URL = BASIC_URL + "/remove";

    public static Route getAllRoom = (request, response) -> {
        Map<String, Object> model = new HashMap<>();

        SparkRoomService roomService = SparkRoomService.getInstance();
        List<Room> rooms = roomService.findAllRoom();
        model.put("rooms", rooms);

        return render(model, "index.html");
    };

    public static Route enterRoom = (request, response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("roomId", request.queryParams("roomId"));

        return render(model, "game.html");
    };

    public static Route createRoom = (request, response) -> {
        SparkRoomService roomService = SparkRoomService.getInstance();
        String param = request.queryParams("roomName");
        roomService.addRoom(param);

        response.redirect("/rooms");
        return null;
    };

    public static Route removeRoom = (request, response) -> {
        SparkRoomService roomService = SparkRoomService.getInstance();
        roomService.removeRoom(Integer.parseInt(request.queryParams("roomId")));

        response.redirect("/rooms");
        return null;
    };

    private SparkRoomController() {
    }
}
