package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MovableRequestDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.ChessService;

@RestController
@RequestMapping("/boards")
public class SpringChessBoardController {

    private final ChessService service;

    public SpringChessBoardController(ChessService service) {
        this.service = service;
    }

    @GetMapping
    private Map<Integer, Map<Side, Player>> getPlayerContexts() throws SQLException {
        return service.getPlayerContexts();
    }

    @PostMapping
    private Map<Integer, Map<Side, Player>> addGameAndGetPlayers() throws
        SQLException {
        // TODO: 실제 플레이어 기능 추가
        Player white = new Player(1, "hodol", "password");
        Player black = new Player(2, "pobi", "password");
        return service.addGame(white, black);
    }

    @GetMapping("/{id}")
    private BoardDto getBoard(@PathVariable int id) throws SQLException {
        return new BoardDto(service.findBoardById(id));
    }

    @PostMapping("/{id}")
    private BoardDto resetBoard(@PathVariable int id) throws SQLException {
        return new BoardDto(service.resetGameById(id));
    }

    @DeleteMapping("/{id}")
    private boolean finishGame(@PathVariable int id) throws SQLException {
        return service.finishGameById(id);
    }

    @GetMapping("/{id}/status")
    private boolean isGameOver(@PathVariable int id) throws SQLException {
        return service.isGameOver(id);
    }

    @GetMapping("/{id}/turn")
    private boolean isWhiteTurn(@PathVariable int id) throws SQLException {
        return service.isWhiteTurn(id);
    }

    @GetMapping("/{id}/score")
    private Map<Side, Double> getScore(@PathVariable int id) throws SQLException {
        return service.getScoresById(id);
    }

    @PostMapping("/{id}/move")
    private boolean move(@PathVariable int id, @RequestBody MoveRequestDto dto) throws SQLException {
        return service.addMoveByGameId(id, dto.getFrom(), dto.getTo());
    }

    @PostMapping("/{id}/movable")
    private List<String> findAllAvailablePath(@PathVariable int id, @RequestBody MovableRequestDto dto) throws
        SQLException {
        return service.findAllAvailablePath(id, dto.getFrom());
    }
}

