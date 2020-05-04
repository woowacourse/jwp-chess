package wooteco.chess.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.dto.GameRequestDto;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.dto.GameScoreDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.SpringGameService;

@RestController
@RequestMapping("/game")
public class SpringGameController {

    private final SpringGameService gameService;

    public SpringGameController(final SpringGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/init")
    public GameResponseDto init(@RequestBody GameRequestDto gameRequestDto) {
        return gameService.initialize(gameRequestDto);
    }

    @PostMapping("/score")
    public GameScoreDto showStatus(@RequestBody GameRequestDto gameRequestDto) {
        return new GameScoreDto(gameService.getScore(gameRequestDto), gameService.getScore(gameRequestDto));
    }

    @PostMapping("/path")
    public List<String> getMovablePositions(@RequestBody MoveRequestDto moveRequestDto) {
        return gameService.getMovablePositions(moveRequestDto);
    }

    @PostMapping("/move")
    public ResponseEntity<GameResponseDto> move(@RequestBody MoveRequestDto moveRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gameService.move(moveRequestDto));
        } catch (IllegalArgumentException e) {
            GameResponseDto gameResponseDTO = gameService.findAllPieces(moveRequestDto.getId());
            gameResponseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(gameResponseDTO);
        }
    }

    @PostMapping("/load")
    public GameResponseDto load(@RequestBody GameRequestDto gameRequestDto) {
        return gameService.findAllPieces(gameRequestDto.getId());
    }
}
