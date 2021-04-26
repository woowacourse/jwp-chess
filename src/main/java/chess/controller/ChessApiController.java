package chess.controller;

import chess.dto.RoomNamesDto;
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

    @GetMapping("/room/{roomName}")
    public ResponseEntity<Integer> getRoomId(@PathVariable String roomName) {
        return ResponseEntity.ok().body(chessService.getRoomId(roomName));
    }

    @GetMapping("/rooms")
    public ResponseEntity<RoomNamesDto> getRoomNames() {
        RoomNamesDto roomNamesDto = new RoomNamesDto(chessService.getRoomNames());
        return ResponseEntity.ok().body(roomNamesDto);
    }

    @PostMapping("/rooms")
    public ResponseEntity<Integer> makeRoom(@RequestBody String roomName) {
        return new ResponseEntity<>(chessService.makeRoom(roomName), HttpStatus.CREATED);
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MoveResponseDto> move(@RequestBody MoveRequestDto moveRequestDto) {
        String source = moveRequestDto.getSource();
        String target = moveRequestDto.getTarget();
        int roomId = moveRequestDto.getRoomId();
        chessService.move(source, target, roomId);
        return ResponseEntity.ok().body(new MoveResponseDto(false));
    }

    @PostMapping(value = "/turn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> turn(@RequestBody TurnChangeRequestDto turnChangeRequestDto) {
        String nextTurn = turnChangeRequestDto.getNextTurn();
        String currentTurn = turnChangeRequestDto.getCurrentTurn();
        int roomId = turnChangeRequestDto.getRoomId();
        chessService.changeTurn(nextTurn, currentTurn, roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
