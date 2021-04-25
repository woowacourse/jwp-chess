package chess.controller;

import chess.dto.ChessBoardDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessApiController {
    private final ChessService chessService;

    public ChessApiController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/room")
    public Long start(@RequestBody final String req) {
        return chessService.addRoom(req.split(":")[1].split("\"")[1]);
    }

    @GetMapping("/board/{roomId}")
    public ChessBoardDto chess(@PathVariable final Long roomId) {
        return chessService.chessBoardFromDB(roomId);
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public MoveResponseDto move(@RequestBody final MoveRequestDto moveRequestDto) {
        return chessService.move(moveRequestDto);
    }

    @PostMapping(value = "/turn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> turn(@RequestBody final TurnChangeRequestDto turnChangeRequestDto) {
        chessService.changeTurn(turnChangeRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
