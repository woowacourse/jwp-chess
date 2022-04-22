package chess.controller;

import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public void createRoom(@PathVariable("roomName") String roomName) {
        roomService.createRoom(roomName);
    }

    @DeleteMapping("/{roomName}")
    public void deleteRoom(@PathVariable("roomName") String roomName) {
        roomService.deleteRoom(roomName);
    }

    @GetMapping("/{roomName}/pieces")
    public String findPieces(@PathVariable("roomName") String roomName) {
        return chessService.findAllPiece(roomName);
    }

    @PostMapping("/{roomName}/pieces")
    public String createPieces(@PathVariable("roomName") String roomName) {
        return chessService.initPiece(roomName);
    }
}
