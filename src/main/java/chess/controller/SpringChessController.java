package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.team.Team;
import chess.service.ChessService;
import chess.webdao.SpringChessGameDao;
import chess.webdto.ChessGameDTO;
import chess.webdto.DBConnectionDTO;
import chess.webdto.MoveRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

import static chess.controller.HTTPStatusCode.INTERNAL_SERVER_ERROR;
import static chess.controller.HTTPStatusCode.SUCCESS;

@RestController
public class SpringChessController {
    private ChessService chessService;
    private SpringChessGameDao springChessGameDao;

    public SpringChessController(ChessService chessService, SpringChessGameDao springChessGameDao){
        this.chessService = chessService;
        this.springChessGameDao = springChessGameDao;
    }

    @GetMapping(value = "/startNewGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO startNewGame() {
        springChessGameDao.deleteChessGame();
        final ChessGame chessGame = springChessGameDao.createChessGame();
        return chessService.generateChessGameDTO(chessGame);
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO move(@RequestBody MoveRequestDTO moveRequestDTO){
        final ChessGame chessGame = springChessGameDao.readChessGame();
        final String start = moveRequestDTO.getStart();
        final String destination = moveRequestDTO.getDestination();
        chessGame.move(Position.of(start), Position.of(destination));
        springChessGameDao.updateChessGame(chessGame, converter(chessGame.isWhiteTeamTurn()));
        return chessService.generateChessGameDTO(chessGame);
    }


    private String converter(boolean whiteTeam) {
        if (whiteTeam) {
            return "white";
        }
        return "black";
    }
}
