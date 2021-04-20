package chess.controller.web;

import chess.dto.ChessGameDto;
import chess.dto.MoveRequestDto;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class ChessRoomController {
    private final SpringChessService springChessService;

    public ChessRoomController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/{roomNo}/start")
    public ChessGameDto startGame(@PathVariable("roomNo") int roomNo) {
        ChessGameDto chessGameDto = springChessService.loadRoom(roomNo);
        return chessGameDto;
    }

    @PutMapping(value = "/{roomNo}/restart")
    public ChessGameDto restartGame(@PathVariable("roomNo") int roomNo) {
        System.out.println("&&");
        System.out.println(roomNo);
        ChessGameDto chessGameDto = springChessService.resetRoom(roomNo);
        return chessGameDto;
    }

    @PutMapping(value = "/{roomNo}/move")
    public ChessGameDto movePiece(@PathVariable("roomNo") int roomNo, @RequestBody MoveRequestDto moveRequestDto) {
        ChessGameDto chessGameDto = springChessService.movePiece(roomNo, moveRequestDto);
        return chessGameDto;
    }

    @DeleteMapping("/{roomNo}/exit")
    public ResponseEntity deleteRoom(@PathVariable("roomNo") int roomNo) {
        springChessService.deleteRoom(roomNo);
        return ResponseEntity.ok().build();
    }
}
