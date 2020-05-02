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
                            @RequestParam String source,
                            @RequestParam String target) throws Exception {
        return chessService.move(new Move(
                roomId, source, target));
    }

    @GetMapping("/way")
    @ResponseBody
    public ResponseDto<List<String>> way(@RequestParam Long roomId,
                                         @RequestParam String team,
                                         @RequestParam String coordinate) throws Exception{
        return chessService.getMovableWay(roomId, Team.valueOf(team), Coordinate.of(coordinate));
    }

    @GetMapping("/renew/{roomId}")
    @ResponseBody
    public ResponseDto<ChessResponseDto> renew(@PathVariable Long roomId) throws Exception {
        return chessService.renew(roomId);
    }
}

