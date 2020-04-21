package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MovableRequestDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.ChessService;

@RestController
public class SpringChessController {

    private ChessService service;

    public SpringChessController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/")
    private ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/boards")
    private Map<Integer, Map<Side, Player>> getPlayerContexts() throws SQLException {
        return service.getPlayerContexts();
    }

    @PostMapping("/boards")
    private Map<Integer, Map<Side, Player>> addGameAndGetPlayers() throws
        SQLException {
        // TODO: 실제 플레이어 기능 추가
        Player white = new Player(1, "hodol", "password");
        Player black = new Player(2, "pobi", "password");
        return service.addGame(white, black);
    }

    @GetMapping("/boards/{id}")
    private BoardDto getBoard(@PathVariable int id) throws SQLException {
        return new BoardDto(service.findBoardById(id));
    }

    @PostMapping("/boards/{id}")
    private BoardDto resetBoard(@PathVariable int id) throws SQLException {
        return new BoardDto(service.resetGameById(id));
    }

    @DeleteMapping("/boards/{id}")
    private boolean finishGame(@PathVariable int id) throws SQLException {
        return service.finishGameById(id);
    }

    @GetMapping("/boards/{id}/status")
    private boolean isGameOver(@PathVariable int id) throws SQLException {
        return service.isGameOver(id);
    }

    @GetMapping("/boards/{id}/turn")
    public boolean isWhiteTurn(@PathVariable int id) throws SQLException {
        return service.isWhiteTurn(id);
    }

    @GetMapping("/boards/{id}/score")
    public Map<Side, Double> getScore(@PathVariable int id) throws SQLException {
        return service.getScoresById(id);
    }

    @PostMapping("/boards/{id}/move")
    public boolean move(@PathVariable int id, @RequestBody MoveRequestDto dto) throws SQLException {
        return service.addMoveByGameId(id, dto.getFrom(), dto.getTo());
    }

    @PostMapping("/boards/{id}/movable")
    public List<String> findAllAvailablePath(@PathVariable int id, @RequestBody MovableRequestDto dto) throws
        SQLException {
        return service.findAllAvailablePath(id, dto.getFrom());
    }

    @GetMapping("/scores")
    public Map<Integer, Map<Side, Double>> getScoreContexts() throws
        SQLException {
        return service.getScoreContexts();
    }
}

