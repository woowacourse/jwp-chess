package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.GameRoomDto;
import chess.dto.MoveDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class WebRestController {
    private final ChessService chessService;

    public WebRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/{gameId}/pieces")
    public ResponseEntity<ChessGameDto> startGame(@PathVariable int gameId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chessService.newGame(gameId));
    }

    @GetMapping("/{gameId}/pieces")
    public ResponseEntity<ChessGameDto> restart(@PathVariable int gameId) {
        return ResponseEntity.ok().body(chessService.loadGame(gameId));
    }

    @PatchMapping(value = "/{gameId}/pieces", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameDto> move(@PathVariable int gameId, @RequestBody MoveDto moveDto) {
        moveDto.setGameId(gameId);
        return ResponseEntity.ok(chessService.move(moveDto));
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable int gameId, @RequestBody GameRoomDto gameRoomDto) {
        gameRoomDto.setGameId(gameId);
        chessService.deleteGame(gameRoomDto);
        return ResponseEntity.noContent().build();
    }
}
