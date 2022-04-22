package chess.controller;

import chess.dao.GameDaoImpl;
import chess.dao.PieceDaoImpl;
import chess.db.DBConnector;
import chess.dto.ChessResponseDto;
import chess.serviece.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess-game")
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController() {
        final DBConnector dbConnector = new DBConnector();
        this.chessService = new ChessService(new PieceDaoImpl(dbConnector), new GameDaoImpl(dbConnector));
    }

    @GetMapping()
    public String init() {
        return "index";
    }

    @GetMapping("/load")
    public ResponseEntity<ChessResponseDto> load() {
        return ResponseEntity.ok().body(chessService.getChess());
    }
}
