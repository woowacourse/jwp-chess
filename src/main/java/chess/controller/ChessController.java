package chess.controller;

import chess.dto.MoveRequestDto;
import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class ChessController {

    private final RoomService roomService;
    private final ChessService chessService;

    public ChessController(final RoomService roomService, final ChessService chessService) {
        this.roomService = roomService;
        this.chessService = chessService;
    }

    @PostMapping("/{roomName}")
    public void createRoom(@PathVariable("roomName") final String roomName) {
        roomService.createRoom(roomName);
    }

    @DeleteMapping("/{roomName}")
    public void deleteRoom(@PathVariable("roomName") final String roomName) {
        roomService.deleteRoom(roomName);
    }

    @GetMapping("/{roomName}/pieces")
    public String findPieces(@PathVariable("roomName") final String roomName) {
        return chessService.findAllPiece(roomName);
    }

    @PostMapping("/{roomName}/pieces")
    public String createPieces(@PathVariable("roomName") final String roomName) {
        return chessService.initPiece(roomName);
    }

    @PutMapping("/{roomName}/pieces")
    public String movePiece(@PathVariable("roomName") final String roomName,
                            @RequestBody final MoveRequestDto moveRequestDto) {
        return chessService.move(roomName, moveRequestDto);
    }

    @GetMapping("/{roomName}/scores")
    public String findScore(@PathVariable("roomName") final String roomName) {
        return chessService.findScore(roomName);
    }

    @GetMapping("/{roomName}/turn")
    public String findTurn(@PathVariable("roomName") final String roomName) {
        return roomService.findCurrentTurn(roomName);
    }

    @GetMapping("/{roomName}/result")
    public String findResult(@PathVariable("roomName") final String roomName) {
        return chessService.result(roomName);
    }
}
