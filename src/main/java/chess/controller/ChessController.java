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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> getRooms() {
        return ResponseEntity.ok().body(chessService.findRoomList());
    }

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestBody CreateRoomDto room) {
        chessService.createRoom(room);
        return new ResponseEntity<>("방이 생성되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/rooms/{roomId}/board")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chessService.getBoard(roomId));
    }

    @PutMapping(value = "/board/{roomId}")
    public ResponseEntity<GameStateDto> move(@PathVariable Long roomId,
        @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok()
            .body(chessService.move(roomId, moveDto.getSource(), moveDto.getDestination()));
    }

    @GetMapping("/rooms/{roomId}/score")
    public ResponseEntity<ScoreDto> getScore(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chessService.getScoreBy(roomId));
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<BoardDto> reset(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chessService.resetBy(roomId));
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<GameStateDto> end(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chessService.endBy(roomId));
    }

    @GetMapping("/rooms/{roomId}/status")
    public ResponseEntity<StatusDto> getStatus(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chessService.getStatus(roomId));
    }

    @DeleteMapping("/rooms")
    public ResponseEntity<String> delete(@RequestBody DeleteRoomDto deleteDto) {
        chessService.deleteBy(deleteDto.getRoomId(), deleteDto.getPassword());
        return ResponseEntity.ok().body("방이 삭제되었습니다.");
    }
}
