package chess.controller.chess;

import chess.controller.ApiError;
import chess.controller.ApiResult;
import chess.domain.dto.RoomDto;
import chess.domain.dto.move.MoveRequestDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.exception.BusinessException;
import chess.exception.ErrorCode;
import chess.serivce.chess.ChessService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final ChessService service;

    public RoomController(final ChessService service) {
        this.service = service;
    }

    @PostMapping("/{name}")
    public ResponseEntity<ApiResult> createRoom(@PathVariable("name") String roomName) {
        RoomDto result = service.createRoom(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResult> enterRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.findPiecesByRoomName(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @PutMapping("/{name}/start")
    public ResponseEntity<ApiResult> startRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.start(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @PutMapping("/{name}/end")
    public ResponseEntity<ApiResult> endRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @PutMapping("/{name}/move")
    public ResponseEntity<ApiResult> move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto) {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
            moveRequestDto.getTarget());
        return ResponseEntity.ok().body(ApiResult.of(result));
    }
}
