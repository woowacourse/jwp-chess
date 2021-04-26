package chess.controller;

import chess.service.SpringChessService;
import chess.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/games")
public class ChessGameApiController {

    private static final String USER = "user";

    private final SpringChessService chessService;

    public ChessGameApiController(SpringChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<Object> saveChess(@RequestBody final ChessSaveRequestDto requestDto, HttpSession session) {
        String playerName = (String) session.getAttribute(USER);
        chessService.saveChess(requestDto, playerName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> finishChess(@RequestBody final GameStatusRequestDto requestDto) {
        chessService.changeGameStatus(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<GameStatusDto> loadChess(@PathVariable final String name, HttpSession session) {
        String playerName = (String) session.getAttribute(USER);
        GameStatusDto gameStatusDto = chessService.loadChess(name, playerName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(gameStatusDto);
    }

    @PutMapping("/{name}/pieces")
    public ResponseEntity<MoveResponseDto> movePieces(@PathVariable("name") final String gameName,
                                                      @RequestBody final MoveRequestDto requestDto,
                                                      HttpSession session) {
        String playerName = (String) session.getAttribute(USER);
        MoveResponseDto moveResponseDto = chessService.movePiece(gameName, requestDto, playerName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(moveResponseDto);
    }
}
