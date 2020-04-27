package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import wooteco.chess.domain.piece.Position;
import wooteco.chess.service.ChessService;

@RestController
public class WebChessRestController {
    private static final Gson GSON = new GsonBuilder().create();
    private ChessService chessService;

    public WebChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board/{id}")
    public String getChessGameById(@PathVariable String id) {
        return GSON.toJson(chessService.getChessGameById(Integer.parseInt(id)));
    }

    @GetMapping("/games")
    public String getGameList() throws SQLException {
        return GSON.toJson(chessService.getGameList());
    }

    @PostMapping("/create")
    public String createChessRoom() throws SQLException {
        return GSON.toJson(chessService.createChessRoom());
    }

    @PostMapping("/restart")
    public String restartGame(@RequestParam String id) {
        return GSON.toJson(chessService.restartGame(Integer.parseInt(id)));
    }

    @PostMapping(value = "/move/{id}")
    public String movePiece(@PathVariable String id, @RequestBody Map<String, Integer> data) {
        int chessGameId = Integer.parseInt(id);
        Position source = Position.of(data.get("sx"), data.get("sy"));
        Position target = Position.of(data.get("tx"), data.get("ty"));
        return GSON.toJson(chessService.movePiece(chessGameId, source, target));
    }
}
