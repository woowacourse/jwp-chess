package wooteco.chess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.Color;
import wooteco.chess.dto.GameRequestDto;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.dto.GameStatusDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.SpringGameService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class SpringGameController {

    private final SpringGameService gameService;

    public SpringGameController(final SpringGameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/init")
    public GameResponseDto init(@RequestParam GameRequestDto gameRequestDto) throws SQLException {
        return gameService.initialize(gameRequestDto);
    }

    @PostMapping("/move")
    public ResponseEntity<GameResponseDto> move(@RequestBody MoveRequestDto requestDTO) throws SQLException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gameService.move(requestDTO));
        } catch (IllegalArgumentException e) {
            GameResponseDto gameResponseDTO = gameService.findAllPieces(requestDTO.getId());
            gameResponseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(gameResponseDTO);
        }
    }

    @GetMapping("/status")
    public GameStatusDto showStatus(@RequestParam GameRequestDto gameRequestDto) throws SQLException {
        return new GameStatusDto(gameService.getScore(gameRequestDto), gameService.getScore(gameRequestDto));
    }

    @GetMapping("/load")
    public GameResponseDto load(@RequestParam UUID roomId) throws SQLException {
        return gameService.findAllPieces(roomId);
    }

    @GetMapping("/get")
    public List<String> getMovablePositions(final MoveRequestDto moveRequestDto) throws SQLException {
        return gameService.getMovablePositions(moveRequestDto);
    }
}
