package chess.controller;

import chess.dto.ErrorMessageDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
import chess.service.ChessService;
import chess.view.ChessMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PatchMapping("/chessMap/{roomId}")
    public ResponseEntity<ChessMap> initializeGame(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(chessService.initializeGame(roomId));
    }

    @GetMapping("/chessMap/{roomId}")
    public ResponseEntity<ChessMap> loadRoom(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(chessService.load(roomId));
    }

    @PostMapping("/room")
    public ResponseEntity<String> makeRoom(@RequestBody RoomDto roomDto) {
        RoomDto newRoomDto = chessService.createRoom(roomDto);
        chessService.initializeGame(newRoomDto.getId());
        return ResponseEntity.ok("방이 생성되었습니다.");
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<String> enterRoom(@PathVariable("roomId") int roomId, @RequestBody RoomDto roomDto) {
        if (roomDto.getId() != roomId) {
            throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        chessService.checkPassword(roomDto);
        return ResponseEntity.ok("방 입장 성공");
    }

    @GetMapping("/status/{roomId}")
    public ResponseEntity<ScoreDto> status(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(chessService.getStatus(roomId));
    }

    @PostMapping("/move/{roomId}")
    public ResponseEntity<ChessMap> move(@PathVariable("roomId") int roomId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok(chessService.move(roomId, moveDto));
    }

    @GetMapping("/king/{roomId}")
    public ResponseEntity<String> hasTwoKing(@PathVariable("roomId") int roomId) {
        if (chessService.hasTwoKing(roomId)) {
            return ResponseEntity.ok("게임 진행중");
        }
        return ResponseEntity.ok("게임 끝");
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

    @GetMapping("/state/{roomId}")
    public ResponseEntity<String> getState(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(chessService.getState(roomId));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> handle(Exception e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return ResponseEntity.badRequest().body(errorMessageDto);
    }
}
