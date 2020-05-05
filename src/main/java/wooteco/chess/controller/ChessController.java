package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.dto.RequestDto.ChessMoveRequestDto;
import wooteco.chess.dto.ResponseDto.ChessResponseDto;
import wooteco.chess.dto.ResponseDto.ResponseDto;
import wooteco.chess.service.ChessService;

import java.util.List;

@RestController
@RequestMapping("/chess")
public class ChessController {
    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/move")
    @ResponseBody
    public ResponseDto move(@RequestBody ChessMoveRequestDto requestDto) {
        return chessService.move(requestDto);
    }

    @GetMapping("/way/{roomId}/{userPassword}/{coordinate}")
    @ResponseBody
    public ResponseDto<List<String>> way(@PathVariable Long roomId,
                                         @PathVariable String userPassword,
                                         @PathVariable String coordinate){
        return chessService.getMovableWay(roomId, Coordinate.of(coordinate), userPassword);
    }

    @GetMapping("/renew/{roomId}")
    @ResponseBody
    public ResponseDto<ChessResponseDto> renew(@PathVariable Long roomId){
        return chessService.renew(roomId);
    }
}

