package wooteco.chess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.domain.piece.Position;
import wooteco.chess.dto.MovePositionDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.service.ChessService;

@RestController
public class WebChessRestController {
    private ChessService chessService;

    public WebChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board/{id}")
    public ResponseDto getChessGameById(@PathVariable String id) {
        return chessService.getChessGameById(Integer.parseInt(id));
    }

    @GetMapping("/games")
    public ResponseDto getGameList() {
        return chessService.getGameListId();
    }

    @PostMapping("/create")
    public ResponseDto createChessRoom() {
        return chessService.createChessRoom();
    }

    @PostMapping("/restart")
    public ResponseDto restartGame(@RequestBody String gameId) {
        return chessService.restartGame(Integer.parseInt(gameId));
    }

    @PutMapping(value = "/move/{id}")
    public ResponseDto movePiece(@PathVariable String id, @RequestBody MovePositionDto movePositionDto) {
        int chessGameId = Integer.parseInt(id);
        Position source = Position.of(movePositionDto.getSourceX(), movePositionDto.getSourceY());
        Position target = Position.of(movePositionDto.getTargetX(), movePositionDto.getTargetY());
        return chessService.movePiece(chessGameId, source, target);
    }
}
