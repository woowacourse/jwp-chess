package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.domain.piece.Position;
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
    public ResponseDto getGameList() throws SQLException {
        return chessService.getGameList();
    }

    @PostMapping("/create")
    public ResponseDto createChessRoom() throws SQLException {
        return chessService.createChessRoom();
    }

    @PostMapping("/restart")
    public ResponseDto restartGame(@RequestParam String id) {
        return chessService.restartGame(Integer.parseInt(id));
    }

    @PostMapping(value = "/move/{id}")
    public ResponseDto movePiece(@PathVariable String id, @RequestBody Map<String, Integer> data) {
        int chessGameId = Integer.parseInt(id);
        Position source = Position.of(data.get("sx"), data.get("sy"));
        Position target = Position.of(data.get("tx"), data.get("ty"));
        return chessService.movePiece(chessGameId, source, target);
    }
}
