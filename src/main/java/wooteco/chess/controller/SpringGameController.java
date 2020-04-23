package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spark.Request;
import spark.Response;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.service.GameManagerDTO;
import wooteco.chess.service.GameService;

import java.sql.SQLException;

@RestController
@RequestMapping("/game")
public class SpringGameController {
//    public static final String MOVE_URL = BASIC_URL + "/move";
//    public static final String STATUS_URL = BASIC_URL + "/status";
//    public static final String GET_URL = BASIC_URL + "/get";

    @Autowired
    private GameService gameService;

    @GetMapping("/init")
    public GameManagerDTO init(@RequestParam(value = "roomId") Integer roomId, Model model) throws SQLException {
        return gameService.initialize(roomId);
    }

    public static String movePiece(Request request, Response response) throws SQLException {
        GameService gameService = GameService.getInstance();

        int roomId = Integer.parseInt(request.queryParams("roomId"));
        String sourcePosition = request.queryParams("sourcePosition");
        String targetPosition = request.queryParams("targetPosition");
        gameService.movePiece(roomId, sourcePosition, targetPosition);
        boolean kingDead = gameService.isKingDead(roomId);
        String currentColor = gameService.getCurrentColor(roomId);

        Gson gson = new Gson();
        JsonObject object = new JsonObject();
        String pieces = gson.toJson(gameService.getPiecesResponseDTO(roomId).getPieces());

        object.addProperty("pieces", pieces);
        object.addProperty("kingDead", kingDead);
        object.addProperty("currentColor", currentColor);

        return gson.toJson(object);
    }

    public static String showStatus(Request request, Response response) throws SQLException {
        GameService gameService = GameService.getInstance();
        int roomId = Integer.parseInt(request.queryParams("roomId"));

        double whiteScore = gameService.getScore(roomId, Color.WHITE);
        double blackScore = gameService.getScore(roomId, Color.BLACK);

        Gson gson = new Gson();
        JsonObject object = new JsonObject();

        object.addProperty("whiteScore", whiteScore);
        object.addProperty("blackScore", blackScore);

        return gson.toJson(object);
    }

    @GetMapping("/load")
    public String load(@RequestParam Integer roomId) throws SQLException {
        Gson gson = new Gson();
        JsonObject object = new JsonObject();
        String pieces = gson.toJson(gameService.getPiecesResponseDTO(roomId).getPieces());
        String currentColor = gameService.getCurrentColor(roomId);

        object.addProperty("pieces", pieces);
        object.addProperty("currentColor", currentColor);

        return gson.toJson(object);
    }

    public static String getMovablePositions(final Request request, final Response response) throws SQLException {
        GameService gameService = GameService.getInstance();
        int roomId = Integer.parseInt(request.queryParams("roomId"));
        String sourcePosition = request.queryParams("sourcePosition");

        Gson gson = new Gson();

        return gson.toJson(gameService.getMovablePositions(roomId, sourcePosition));
    }
}
