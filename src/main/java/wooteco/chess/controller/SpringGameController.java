package wooteco.chess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.dto.*;
import wooteco.chess.service.SpringGameService;

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
    public GameResponseDto init(@RequestParam Long roomId) throws SQLException {
        return gameService.initialize(roomId);
    }

    @PostMapping("/move")
    public ResponseEntity<GameResponseDto> move(@RequestBody MoveRequestDto requestDTO) throws SQLException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gameService.move(requestDTO));
        } catch (IllegalArgumentException e) {
            GameResponseDto gameResponseDTO = gameService.findAllPieces(requestDTO.getRoomId());
            gameResponseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(gameResponseDTO);
        }
    }

    @GetMapping("/status")
    public GameStatusDto showStatus(@RequestParam Long roomId) throws SQLException {
        return gameService.getScore(roomId);
    }

    @GetMapping("/load")
    public GameResponseDto load(@RequestParam Long roomId) throws SQLException {
        return gameService.findAllPieces(roomId);
    }

    @GetMapping("/get")
    public List<String> getMovablePositions(final MoveRequestDto moveRequestDto) throws SQLException {
        return gameService.getMovablePositions(moveRequestDto);
    }
}
