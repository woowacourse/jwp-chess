package chess.controller;

import chess.Dto.BoardDto;
import chess.Dto.MoveRequest;
import chess.Dto.PathDto;
import chess.domain.piece.PieceColor;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final ChessService service;

    public BoardController(ChessService service) {
        this.service = service;
    }

    @GetMapping(value = {"","/restart"})
    public BoardDto getNewBoard() {
        return new BoardDto(service.restartBoard());
    }

    @GetMapping("/status")
    public boolean isEnd(){
        return service.endGame();
    }

    @GetMapping("/turn")
    public PieceColor getTurn(){
        return service.findTurn();
    }

    @GetMapping("/score")
    public Map<PieceColor, Double> getScore() {
        return service.getScores();
    }

    @PostMapping(path = "/path")
    public List<String> movablePath(@RequestBody PathDto dto){
        return service.findPath(dto.getFrom());
    }

    @PostMapping(path = "/move")
    public boolean move(@RequestBody MoveRequest dto){
        return service.addMove(dto.getFrom(), dto.getTo());
    }
}
