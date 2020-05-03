package wooteco.chess.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;

@RestController
public class GameController {

    private ChessService chessService;

    public GameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/path")
    public List<String> path(@RequestParam Map<String, String> parameters) {
        try {
            return chessService.searchPath(new Room(parameters.get("roomName")),
                parameters.get("source"));
        } catch (RuntimeException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    @PostMapping("/move")
    public Map<String, Object> move(@RequestParam Map<String, String> parameters) {
        Room blackRoom = new Room(parameters.get("roomName"));

        Map<String, Object> model = new HashMap<>();
        model.put("isNotFinished", false);
        model.put("message", "");

        try {
            chessService.move(blackRoom, parameters.get("source"), parameters.get("target"));
            model.put("white", chessService.calculateWhiteScore(blackRoom));
            model.put("black", chessService.calculateBlackScore(blackRoom));
        } catch (RuntimeException e) {
            model.put("message", e.getMessage());
        }
        if (chessService.checkGameNotFinished(blackRoom)) {
            model.put("isNotFinished", true);
        }
        return model;
    }
}
