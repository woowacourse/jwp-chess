package chess.controller;

import chess.dto.chess.ChessResponseDto;
import chess.dto.game.GameRequestDto;
import chess.dto.chess.MoveRequestDto;
import chess.dto.chess.MoveResponseDto;
import chess.service.ChessService;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/chess")
@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/creation")
    public String initializeChess(@RequestBody final GameRequestDto gameRequestDto) {
        final long id = chessService.initializeChess(gameRequestDto);
        return "redirect:/games/" + id;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ChessResponseDto> bringGameData(@PathVariable long gameId) {
        return ResponseEntity.ok().body(chessService.bringGameData(gameId));
    }

    @GetMapping("/{gameId}/move/check")
    public ResponseEntity<Map<String, Boolean>> checkMovement(@PathVariable long gameId,
        MoveRequestDto moveRequestDto) {
        final Map<String, Boolean> responseData = Collections
            .singletonMap("isMovable", chessService.checkMovement(gameId, moveRequestDto));
        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable long gameId,
        @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok().body(chessService.move(gameId, moveRequestDto));
    }

}
