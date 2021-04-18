package chess.controller;

import chess.service.SpringChessService;
import chess.service.dto.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class ChessGameApiController {

    private final SpringChessService chessService;

    public ChessGameApiController(SpringChessService chessService) {
        this.chessService = chessService;
    }


    @PostMapping
    public CommonResponseDto<GameStatusDto> saveChess(@RequestBody final ChessSaveRequestDto requestDto) {
        return chessService.saveChess(requestDto);
    }


    @PutMapping
    public CommonResponseDto<Object> finishChess(@RequestBody final GameStatusRequestDto requestDto) {
        chessService.changeGameStatus(requestDto);
        return new CommonResponseDto<>(ResponseCode.OK.code(), ResponseCode.OK.message());
    }

    @GetMapping("/{name}")
    public CommonResponseDto<GameStatusDto> loadChess(@PathVariable("name") final String name) {
        return chessService.loadChess(name);
    }

    @PutMapping("/pieces")
    public CommonResponseDto<MoveResponseDto> movePieces(@RequestBody final MoveRequestDto requestDto) {
        return chessService.movePiece(requestDto);
    }
}
