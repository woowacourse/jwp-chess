package chess.controller;

import chess.dto.BoardDto;
import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.dto.GameStateDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/room")
    public ResponseEntity<List<RoomDto>> getRooms() {
        return ResponseEntity.ok().body(chessService.findRoomList());
    }

    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@RequestBody CreateRoomDto room) {
        chessService.createRoom(room);
        return ResponseEntity.ok().body("방이 생성되었습니다.");
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.getBoard(roomId));
    }

    @PostMapping(value = "/board/{roomId}/move")
    public ResponseEntity<GameStateDto> move(@PathVariable(value = "roomId") Long roomId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(roomId, moveDto.getSource(), moveDto.getDestination()));
    }

    @GetMapping("/room/{roomId}/score")
    public ResponseEntity<ScoreDto> getScore(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.getScoreBy(roomId));
    }

    @PostMapping("/room/{roomId}/reset")
    public ResponseEntity<BoardDto> reset(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.resetBy(roomId));
    }

    @PostMapping("/room/{roomId}/end")
    public ResponseEntity<GameStateDto> end(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.endBy(roomId));
    }

    @GetMapping("/room/{roomId}/status")
    public ResponseEntity<StatusDto> getStatus(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.getStatus(roomId));
    }

    @PostMapping("/room/delete")
    public ResponseEntity delete(@RequestBody DeleteRoomDto deleteDto) {
        chessService.deleteBy(deleteDto.getRoomId(), deleteDto.getPassword());
        return ResponseEntity.ok().body("성공");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentExceptionHandle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> ExceptionHandle(Exception exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
