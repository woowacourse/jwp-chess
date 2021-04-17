package chess.controller.web;

import chess.chessgame.domain.manager.ChessGameManagerBundle;
import chess.controller.web.dto.RunningGameResponseDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final ChessService chessService;

    public UserController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/user")
    public RunningGameResponseDto getGames() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();
        return new RunningGameResponseDto(runningGames.getIdAndNextTurn());
    }
}
