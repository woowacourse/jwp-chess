package chess.controller;

import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public MoveResponseDto move(@RequestBody MoveRequestDto moveRequestDto) {
        return chessService.move(moveRequestDto);
    }

    @PostMapping(value = "/turn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> turn(@RequestBody TurnChangeRequestDto turnChangeRequestDto) {
        chessService.changeTurn(turnChangeRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
