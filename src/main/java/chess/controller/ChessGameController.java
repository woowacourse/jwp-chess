package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.MovePositionDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("game", HttpStatus.OK);
    }

    @PostMapping("/new-game")
    public ResponseEntity<ChessGameDto> createNewGame(@RequestParam("name") String gameName) {
        final ChessGameDto newChessGame = chessGameService.createNewChessGame(gameName);
        return ResponseEntity.ok(newChessGame);
    }

    @GetMapping("/status")
    public ResponseEntity<StatusDto> findStatusByGameName(@RequestParam("name") String gameName) {
        final StatusDto status = chessGameService.findStatus(gameName);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/finish")
    public ResponseEntity<StatusDto> finishGame(@RequestParam("name") String gameName) {
        final StatusDto status = chessGameService.finishGame(gameName);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/load")
    public ResponseEntity<ChessGameDto> loadGame(@RequestParam("name") String gameName) {
        final ChessGameDto loadChessGame = chessGameService.loadChessGame(gameName);
        return ResponseEntity.ok(loadChessGame);
    }

    @PostMapping("/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MovePositionDto movePositionDto) {
        final String chessGameName = movePositionDto.getChessGameName();
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(chessGameName, currentPosition, destinationPosition);
        return ResponseEntity.ok(chessGame);
    }
}
