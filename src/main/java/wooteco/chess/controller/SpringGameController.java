package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.Color;
import wooteco.chess.dto.GameManagerDTO;
import wooteco.chess.service.SpringGameService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/game")
public class SpringGameController {

    @Autowired
    private SpringGameService gameService;

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
    public GameStatusDTO showStatus(@RequestParam Integer roomId) throws SQLException {
        return new GameStatusDTO(gameService.getScore(roomId, Color.WHITE), gameService.getScore(roomId, Color.BLACK));
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
