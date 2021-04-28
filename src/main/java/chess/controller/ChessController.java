package chess.controller;

import chess.dto.game.GameResponseDto;
import chess.dto.game.move.MoveRequestDto;
import chess.dto.game.move.MoveResponseDto;
import chess.dto.game.GameRequestDto;
import chess.service.GameService;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/chess")
@Controller
public class ChessController {

    private final GameService gameService;

    public ChessController(final GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/creation")
    public String initializeChess(@RequestBody GameRequestDto gameRequestDto) {
        final long id = gameService.initializeGame(gameRequestDto);
        return "redirect:/games/" + id;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDto> bringGameData(@PathVariable long gameId) {
        return ResponseEntity.ok().body(gameService.retrieveGameData(gameId));
    }

    @GetMapping("/{gameId}/move/check")
    public ResponseEntity<Map<String, Boolean>> checkMovement(@PathVariable long gameId,
        @ModelAttribute MoveRequestDto moveRequestDto) {

        final Map<String, Boolean> responseData = Collections
            .singletonMap("isMovable", gameService.checkMovement(gameId, moveRequestDto));
        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable long gameId,
        @RequestBody MoveRequestDto moveRequestDto) {

        return ResponseEntity.ok().body(gameService.move(gameId, moveRequestDto));
    }

}
