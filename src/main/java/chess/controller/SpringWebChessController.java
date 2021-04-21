package chess.controller;

import chess.dao.GameDao;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebChessController {
    private final GameDao gameDAO;

    public SpringWebChessController(GameDao gameDAO) {
        this.gameDAO = gameDAO;
    }

    @GetMapping("/newgame")
    public ResponseEntity<CommonDto<?>> newGame() { // ? extends ParentDto로 선언해볼까?
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        int gameId = gameDAO.saveGame(chessGameManager);
        return ResponseEntity.ok().body(
                new CommonDto<>(
                        "새로운 게임이 생성되었습니다.",
                        NewGameResponse.from(chessGameManager, gameId)
                )
        );
    }

    @PostMapping("/move")
    public ResponseEntity<CommonDto<?>> move(@RequestBody MoveRequest moveRequest) {
        ChessGameManager chessGameManager = gameDAO.loadGame(moveRequest.getGameId());

        int gameId = moveRequest.getGameId();
        String from = moveRequest.getFrom();
        String to = moveRequest.getTo();
        chessGameManager.move(Position.of(from), Position.of(to));

        gameDAO.updateTurnByGameId(chessGameManager, gameId);
        gameDAO.updatePiecesByGameId(chessGameManager, gameId);

        return ResponseEntity.ok().body(
                new CommonDto<>(
                        "기물을 이동했습니다.",
                        RunningGameResponse.from(chessGameManager))
        );
    }

    @GetMapping("/load/games")
    public ResponseEntity<CommonDto<?>> loadGames() {
        return ResponseEntity.ok().body(
                new CommonDto<>(
                        "게임 목록을 불러왔습니다.",
                        new GameListDto(gameDAO.loadGames())
                )
        );
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<CommonDto<?>> loadGame(@PathVariable int id) {
        ChessGameManager chessGameManager = gameDAO.loadGame(id);
        return ResponseEntity.ok().body(
                new CommonDto<>(
                        "게임을 불러왔습니다",
                        RunningGameResponse.from(chessGameManager)
                )
        );
    }
}