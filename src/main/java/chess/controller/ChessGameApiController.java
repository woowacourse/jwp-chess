package chess.controller;

import chess.entity.User;
import chess.service.LoginUser;
import chess.service.SpringChessService;
import chess.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class ChessGameApiController {

    private static final String USER = "user";

    private final SpringChessService chessService;

    public ChessGameApiController(SpringChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<Object> saveChess(@RequestBody final ChessSaveRequestDto requestDto, @LoginUser User user) {
         chessService.saveChess(requestDto, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> finishChess(@RequestBody final GameStatusRequestDto requestDto) {
        chessService.changeGameStatus(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<GameStatusDto> loadChess(@PathVariable final String name, @LoginUser User user) {
        GameStatusDto gameStatusDto = chessService.loadChess(name, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(gameStatusDto);
    }

    @PutMapping("/{name}/pieces")
    public ResponseEntity<MoveResponseDto> movePieces(@PathVariable("name") final String gameName,
                                                      @RequestBody final MoveRequestDto requestDto,
                                                      @LoginUser User user) {
        MoveResponseDto moveResponseDto = chessService.movePiece(gameName, requestDto, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(moveResponseDto);
    }
}
