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
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String sourcePosition = request.getParameter("sourcePosition");
        String targetPosition = request.getParameter("targetPosition");

        GameManagerDTO gameManagerDTO = gameService.createDTO(roomId);
        try {
            gameService.movePiece(roomId, sourcePosition, targetPosition);
            return gameService.createDTO(roomId);
        } catch (IllegalArgumentException e) {
            gameManagerDTO.setErrorMessage(e.getMessage());
            return gameManagerDTO;
        }
    }

    @GetMapping("/status")
    public Model showStatus(@RequestParam Integer roomId, Model model) throws SQLException {
        double whiteScore = gameService.getScore(roomId, Color.WHITE);
        double blackScore = gameService.getScore(roomId, Color.BLACK);

        model.addAttribute("whiteScore", whiteScore);
        model.addAttribute("blackScore", blackScore);

        return model;
    }

    @GetMapping("/load")
    public GameManagerDTO load(@RequestParam Integer roomId) throws SQLException {
        return gameService.createDTO(roomId);
    }

    @GetMapping("/get")
    public List<String> getMovablePositions(final HttpServletRequest request) throws SQLException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String sourcePosition = request.getParameter("sourcePosition");

        return gameService.getMovablePositions(roomId, sourcePosition);
    }
}
