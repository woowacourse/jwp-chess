package chess.controller;

import chess.service.ChessBoardService;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class SpringChessController {
    private final ChessBoardService chessBoardService;

    public SpringChessController(ChessBoardService chessBoardService) {
        this.chessBoardService = chessBoardService;
    }

    @PostMapping("/{roomId}")
    public ChessGameDto startNewGame(@PathVariable long roomId) {
        return chessBoardService.startNewGame(roomId);
    }

    @GetMapping(value = "/{roomId}/previous")
    public ChessGameDto loadPrevGame(@PathVariable long roomId) {
        return chessBoardService.loadPreviousGame(roomId);
    }

    @PostMapping(path = "/{roomId}/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable long roomId) {
        return chessBoardService.move(moveRequestDto, roomId);
    }

}
