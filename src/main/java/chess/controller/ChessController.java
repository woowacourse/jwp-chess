package chess.controller;

import chess.dto.BoardDto;
import chess.dto.GameStateDto;
import chess.dto.MoveDto;
import chess.dto.PieceDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
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

    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@RequestBody RoomDto roomDto) {
        chessService.createRoom(roomDto);
        return ResponseEntity.ok().body("방이 생성되었습니다.");
    }

    @GetMapping("/room")
    public ResponseEntity<List<RoomDto>> getRooms() {
        return ResponseEntity.ok().body(chessService.findRoomList());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<PieceDto>> getBoard(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.getPiecesBy(roomId));
    }

    @PostMapping(value = "/room/{roomId}")
    public ResponseEntity<GameStateDto> move(@PathVariable(value = "roomId") Long roomId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(roomId, moveDto.getSource(), moveDto.getDestination()));
    }

    @GetMapping("/room/{roomId}/status")
    public ResponseEntity<ScoreDto> getStatus(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.getScoreBy(roomId));
    }

    @PostMapping("/room/{roomId}/reset")
    public ResponseEntity<BoardDto> reset(@PathVariable(value = "roomId") Long roomId) {
        return ResponseEntity.ok().body(chessService.restartBy(roomId));
    }

    @PostMapping("/room/{roomId}/end")
    public ResponseEntity<String> end(@PathVariable(value = "roomId") Long roomId) {
        chessService.endBy(roomId);
        return ResponseEntity.ok().body("게임이 종료되었습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentExceptionHandle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> ExceptionHandle() {
        return ResponseEntity.badRequest().body("실행 중 예상치 못한 오류가 발생했습니다.");
    }
}
