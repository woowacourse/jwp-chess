package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.Color;
import wooteco.chess.dto.GameManagerDTO;
import wooteco.chess.dto.GameStatusDTO;
import wooteco.chess.dto.MoveManagerDTO;
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
    public GameManagerDTO init(@RequestParam Integer roomId) throws SQLException {
        return gameService.initialize(roomId);
    }

    @PostMapping("/move")
    public GameManagerDTO move(@RequestBody MoveManagerDTO moveDto) throws SQLException {

        GameManagerDTO gameManagerDTO = gameService.createDTO(moveDto.getRoomId());
        try {
            gameService.movePiece(moveDto.getRoomId(), moveDto.getSourcePosition(), moveDto.getTargetPosition());
            return gameService.createDTO(moveDto.getRoomId());
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
