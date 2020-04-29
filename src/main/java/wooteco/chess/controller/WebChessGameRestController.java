package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.dto.MoveOperationDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.service.ChessService;

@RestController
public class WebChessGameRestController {
    private ChessService chessService;

    public WebChessGameRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseDto createChessRoom() {
        return chessService.createChessRoom();
    }

    @PostMapping("/restart")
    public ResponseDto restartGame(@RequestBody String gameId) {
        return chessService.restartGame(Integer.parseInt(gameId));
    }

    @PostMapping("/move/{id}")
    public ResponseDto movePiece(@PathVariable String id, @RequestBody MoveOperationDto moveOperationDto) {
        int pieceId = Integer.parseInt(id);
        Position source = Position.of(moveOperationDto.getStartX(), moveOperationDto.getStartY());
        Position target = Position.of(moveOperationDto.getTargetX(), moveOperationDto.getTargetY());
        return chessService.movePiece(pieceId, source, target);
    }

    @GetMapping("/board/{id}")
    public ResponseDto getChessGameById(@PathVariable String id) {
        return chessService.getChessGameById(Integer.parseInt(id));
    }

    @GetMapping("/games")
    public ResponseDto getGameList() {
        return chessService.getGameList();
    }
}
