package chess.controller.chess;

import chess.controller.ApiError;
import chess.controller.ApiResult;
import chess.domain.dto.ResponseDto;
import chess.domain.dto.RoomDto;
import chess.domain.dto.move.MoveRequestDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.exception.BusinessException;
import chess.exception.ErrorCode;
import chess.serivce.chess.ChessService;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final ChessService service;

    public RoomController(final ChessService service) {
        this.service = service;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> businessExceptionHandler(BusinessException e) {
        return ResponseEntity.badRequest().body(ApiError.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> exceptionHandler(DataAccessException e) {
        return ResponseEntity.badRequest().body(ApiError.of(ErrorCode.DB_COMMON, e.getMessage()));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult> createRoom(@RequestBody Map<String, String> body) {
        RoomDto result = service.createRoom(body.get("name"));
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResult> enterRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.findPiecesByRoomName(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @GetMapping("/{name}/start")
    public ResponseEntity<ApiResult> startRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.start(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @GetMapping("/{name}/end")
    public ResponseEntity<ApiResult> endRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(ApiResult.of(result));
    }

    @PostMapping("/{name}/move")
    public ResponseEntity<ApiResult> move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto) {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
            moveRequestDto.getTarget());
        return ResponseEntity.ok().body(ApiResult.of(result));
    }
}
