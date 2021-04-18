package chess.controller;

import chess.service.SpringChessService;
import chess.service.dto.ChessSaveRequestDto;
import chess.service.dto.CommonResponseDto;
import chess.service.dto.GameStatusDto;
import chess.service.dto.GameStatusRequestDto;
import chess.service.dto.MoveRequestDto;
import chess.service.dto.MoveResponseDto;
import chess.service.dto.ResponseCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
