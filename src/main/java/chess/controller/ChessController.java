package chess.controller;

import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.ScoreDto;
import chess.domain.dto.response.ApiResponseDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board/{roomName}")
    public ApiResponseDto<BoardDto> loadSavedBoard(@PathVariable String roomName) {
        return ApiResponseDto.createOK(chessService.getSavedBoard(roomName));
    }

    @PutMapping("/board/{roomName}")
    public ApiResponseDto<BoardDto> resetBoard(@PathVariable String roomName) {
        return ApiResponseDto.createOK(chessService.resetGame(roomName));
    }

    @GetMapping("/score/{roomName}")
    public ApiResponseDto<ScoreDto> scoreStatus(@PathVariable String roomName) {
        return ApiResponseDto.createOK(chessService.score(roomName));
    }

    @PostMapping("/move/{roomName}")
    public ApiResponseDto<BoardDto> move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable String roomName) {
        return ApiResponseDto.createOK(chessService.move(moveInfoDto, roomName));
    }
}
