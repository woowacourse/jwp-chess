package wooteco.chess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.Color;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.dto.GameStatusDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.SpringGameService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/game")
public class SpringGameController {

    private final SpringGameService gameService;

    public SpringGameController(final SpringGameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/init")
    public MoveResponseDto init(@RequestParam Integer roomId) throws SQLException {
        return gameService.initialize(roomId);
    }

    @PostMapping("/move")
    public ResponseEntity<MoveResponseDto> move(@RequestBody MoveRequestDto requestDTO) throws SQLException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gameService.move(requestDTO));
        } catch (IllegalArgumentException e) {
            MoveResponseDto moveResponseDTO = gameService.createMoveResponseDTO(requestDTO.getRoomId());
            moveResponseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(moveResponseDTO);
        }
    }

    @GetMapping("/status")
    public GameStatusDto showStatus(@RequestParam Integer roomId) throws SQLException {
        return new GameStatusDto(gameService.getScore(roomId, Color.WHITE), gameService.getScore(roomId, Color.BLACK));
    }

    @GetMapping("/load")
    public MoveResponseDto load(@RequestParam Integer roomId) throws SQLException {
        return gameService.createMoveResponseDTO(roomId);
    }

    @GetMapping("/get")
    public List<String> getMovablePositions(final HttpServletRequest request) throws SQLException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String sourcePosition = request.getParameter("sourcePosition");

        return gameService.getMovablePositions(roomId, sourcePosition);
    }
}
