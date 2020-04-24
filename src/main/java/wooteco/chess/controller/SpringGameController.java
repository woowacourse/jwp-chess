package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import spark.Request;
import spark.Response;
import wooteco.chess.domain.Color;
import wooteco.chess.domain.GameManager;
import wooteco.chess.dto.GameManagerDTO;
import wooteco.chess.dto.MovablePositionDTO;
import wooteco.chess.dto.MovePositionDTO;
import wooteco.chess.service.GameService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/game")
public class SpringGameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/init")
    public GameManagerDTO init(@RequestParam(value = "roomId") Integer roomId) throws SQLException {
        return gameService.initialize(roomId);
    }

    @PostMapping("/move")
    public GameManagerDTO move(HttpServletRequest request) throws SQLException {
        GameManagerDTO gameManagerDTO = null;


        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String sourcePosition = request.getParameter("sourcePosition");
        String targetPosition = request.getParameter("targetPosition");
//        try {
        gameService.movePiece(roomId, sourcePosition, targetPosition);
        boolean kingDead = gameService.isKingDead(roomId);
        String currentColor = gameService.getCurrentColor(roomId);
        gameManagerDTO = new GameManagerDTO(gameService.getPiecesResponseDTO(roomId).getPieces(), Color.valueOf(currentColor), kingDead);
//        } catch (Exception e) {
//            boolean kingDead = gameService.isKingDead(roomId);
//            String currentColor = gameService.getCurrentColor(roomId);
//            gameManagerDTO = new GameManagerDTO(gameService.getPiecesResponseDTO(roomId).getPieces(), Color.valueOf(currentColor), kingDead);
//            gameManagerDTO.setErrorMessage(e.getMessage());
//        }
        return gameManagerDTO;
    }


//        Gson gson = new Gson();
//        JsonObject object = new JsonObject();
//        String pieces = gson.toJson(gameService.getPiecesResponseDTO(roomId).getPieces());
//
//        object.addProperty("pieces", pieces);
//        object.addProperty("kingDead", kingDead);
//        object.addProperty("currentColor", currentColor);
//
//        return gson.toJson(object);


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

    @GetMapping("/get")
    public List<String> getMovablePositions(final HttpServletRequest request) throws SQLException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String sourcePosition = request.getParameter("sourcePosition");

        return gameService.getMovablePositions(roomId, sourcePosition);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView exceptionHandler(Exception e){
        ModelAndView model = new ModelAndView();
        model.setViewName("game");
        model.addObject("errorMessage", e.getMessage());
        return model;
    }
}
