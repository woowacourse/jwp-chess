package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.dto.GameStatusDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.SpringGameService;

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
    public ResponseEntity<GameResponseDto> move(@RequestBody MoveRequestDto requestDTO) throws
        SQLException {
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
    public List<String> getMovablePositions(final MoveRequestDto moveRequestDto) throws
        SQLException {
        return gameService.getMovablePositions(moveRequestDto);
    }
}
