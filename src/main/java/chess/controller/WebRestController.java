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
        return new ResponseEntity<>(chessService.newGame(gameId), HttpStatus.CREATED);
    }

    @GetMapping("/{gameId}/pieces")
    public ResponseEntity<ChessGameDto> restart(@PathVariable int gameId) {
        return new ResponseEntity<>(chessService.loadGame(gameId), HttpStatus.OK);
    }

    @PatchMapping(value = "/{gameId}/pieces", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameDto> move(@PathVariable int gameId, @RequestBody MoveDto moveDto) {
        //rest api 적용하면서 생긴 로직
        moveDto.setGameId(gameId);
        return new ResponseEntity<>(chessService.move(moveDto), HttpStatus.OK);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable int gameId, @RequestBody GameRoomDto gameRoomDto) {
        gameRoomDto.setGameId(gameId);
        chessService.deleteGame(gameRoomDto);
        return ResponseEntity.noContent().build();
    }
}
