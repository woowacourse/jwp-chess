package chess.controller;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessBoardDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomNameRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.dto.response.RoomIdResponseDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChessApiController {
    private final ChessService chessService;

    public ChessApiController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping(value = "/room")
    public RoomIdResponseDto start(@RequestBody final RoomNameRequestDto roomNameRequestDto) {
        return new RoomIdResponseDto(chessService.addRoom(roomNameRequestDto.getRoomName()));
    }

    @GetMapping("/board/{roomId}")
    public ChessBoardDto chess(@PathVariable final Long roomId) {
        Map<Position, Piece> boardFromDB = chessService.chessBoardFromDB(roomId);
        String currentTurn = chessService.currentTurn(roomId);
        return new ChessBoardDto(boardFromDB, currentTurn);
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public MoveResponseDto move(@RequestBody final MoveRequestDto moveRequestDto) {
        boolean isMovable = chessService.move(
                moveRequestDto.getSource(), moveRequestDto.getTarget(), moveRequestDto.getRoomId());
        return new MoveResponseDto(isMovable);
    }

    @PostMapping(value = "/turn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> turn(@RequestBody final TurnChangeRequestDto turnChangeRequestDto) {
        chessService.changeTurn(turnChangeRequestDto.getNextTurn(), turnChangeRequestDto.getRoomId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
