package chess.controller;

import chess.dto.ChessResponseDto;
import chess.dto.GameRequestDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

}
