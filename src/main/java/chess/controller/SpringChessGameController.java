package chess.controller;

import chess.dto.ErrorMessageDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
import chess.service.ChessService;
import chess.view.ChessMap;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessGameController {

    private final ChessService chessService;

    public SpringChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/room")
    public ResponseEntity<List<RoomDto>> getRooms() {
        return ResponseEntity.ok(chessService.getRooms());
    }

    @PostMapping("/room")
    public ResponseEntity<String> makeRoom(@RequestBody RoomDto roomDto) {
        chessService.createRoom(roomDto);
        return ResponseEntity.ok("방이 생성되었습니다.");

    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<ChessMap> startRoom(@PathVariable("roomId") int roomId, @RequestBody RoomDto roomDto) {
        if (roomDto.getId() != roomId) {
            throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        chessService.checkPassword(roomDto);
        return ResponseEntity.ok(chessService.initializeGame(roomDto.getId()));
    }

    @GetMapping("/load/{roomId}")
    public ResponseEntity<ChessMap> load(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(chessService.load(roomId));
    }

    @GetMapping("/status/{roomId}")
    public ResponseEntity<ScoreDto> status(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(chessService.getStatus(roomId));
    }

    @PostMapping("/move/{roomId}")
    public ResponseEntity<ChessMap> move(@PathVariable("roomId") int roomId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok(chessService.move(roomId, moveDto));
    }

    @PostMapping("/end/{roomId}")
    public ResponseEntity<ResultDto> end(@PathVariable("roomId") int roomId) {
        chessService.endGame(roomId);
        final ResultDto resultDto = chessService.getResult(roomId);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<String> delete(@PathVariable("roomId") int roomId, @RequestBody RoomDto roomDto) {
        if (roomDto.getId() != roomId) {
            throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        chessService.deleteRoom(roomDto);
        return ResponseEntity.ok("방이 삭제되었습니다.");
    }


    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> handle(Exception e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return ResponseEntity.badRequest().body(errorMessageDto);
    }
}
