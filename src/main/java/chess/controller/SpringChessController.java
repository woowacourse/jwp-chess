package chess.controller;

import chess.domain.ChessGame;
import chess.domain.team.Team;
import chess.service.ChessService;
import chess.webdto.ChessGameDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessController {
    @GetMapping(value = "/startNewGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO startNewGame() {
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        final ChessService chessService = new ChessService();
        final ChessGameDTO chessGameDTO = chessService.generateChessGameDTO(chessGame);
        return chessGameDTO;
    }
}
