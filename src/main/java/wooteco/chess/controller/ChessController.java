package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.ChessResponseDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.entity.Move;
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
    public ResponseDto move(@RequestParam Long roomId,
                            @RequestParam String userPassword,
                            @RequestParam String source,
                            @RequestParam String target){
        return chessService.move(new Move(
                roomId, source, target), userPassword);
    }

    @GetMapping("/way")
    @ResponseBody
    public ResponseDto<List<String>> way(@RequestParam Long roomId,
                                         @RequestParam String userPassword,
                                         @RequestParam String coordinate){
        return chessService.getMovableWay(roomId, Coordinate.of(coordinate), userPassword);
    }

    @GetMapping("/renew/{roomId}")
    @ResponseBody
    public ResponseDto<ChessResponseDto> renew(@PathVariable Long roomId){
        return chessService.renew(roomId);
    }
}

