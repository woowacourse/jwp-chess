package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.domain.piece.Side;
import wooteco.chess.service.ChessService;

@RestController
@RequestMapping("/scores")
public class SpringChessScoreController {

    private final ChessService service;

    public SpringChessScoreController(ChessService service) {
        this.service = service;
    }

    @GetMapping
    private Map<String, Map<Side, Double>> getScoreContexts() throws SQLException {
        return service.getScoreContexts();
    }
}

