package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.team.Team;
import chess.service.ChessService;
import chess.webdto.ChessGameDTO;
import chess.webdto.MoveRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessController {
    private ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
    private ChessService chessService;

    public SpringChessController(ChessService chessService){
        this.chessService = chessService;
    }

    @GetMapping(value = "/startNewGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO startNewGame() {
        final ChessGameDTO chessGameDTO = chessService.generateChessGameDTO(chessGame);
        return chessGameDTO;
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO move(@RequestBody MoveRequestDTO moveRequestDTO){
        final String start = moveRequestDTO.getStart();
        final String destination = moveRequestDTO.getDestination();
        chessGame.move(Position.of(start), Position.of(destination));
        return chessService.generateChessGameDTO(chessGame);
    }

}
