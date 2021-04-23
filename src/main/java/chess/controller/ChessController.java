package chess.controller;

import chess.dto.ChessResponseDto;
import chess.dto.GameRequestDto;
import chess.dto.MoveRequestDto;
import chess.dto.MoveResponseDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RequestMapping("/chess")
@RestController
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ChessResponseDto> bringGameData(@PathVariable long gameId) {
        return ResponseEntity.ok().body(chessService.bringGameData(gameId));
    }

    @GetMapping("/{gameId}/move/check")
    public ResponseEntity<Map<String, Boolean>> checkMovement(@PathVariable long gameId, MoveRequestDto moveRequestDto) {
        final Map<String, Boolean> responseData =
                Collections.singletonMap("isMovable", chessService.checkMovement(gameId, moveRequestDto));
        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<MoveResponseDto> move(
            @PathVariable long gameId,
            @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok().body(chessService.move(gameId, moveRequestDto));
    }

}
