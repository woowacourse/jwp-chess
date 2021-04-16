package chess.controller;

import chess.dao.GameDAO;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.*;
import chess.exception.HandledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
public class SpringWebChessController {
    private GameDAO gameDAO;

    @Autowired
    public SpringWebChessController(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    private ResponseEntity<CommonDto<?>> handleExpectedException(Supplier<ResponseEntity<CommonDto<?>>> supplier) {
        try {
            return supplier.get();
        } catch (HandledException e) {
            return ResponseEntity.badRequest().body(
                    new CommonDto<>(
                            e.getMessage()));
        }
    }

    @GetMapping("/newgame")
    public ResponseEntity<CommonDto<?>> newGame() {
        return handleExpectedException(() -> {
            ChessGameManager chessGameManager = new ChessGameManager();
            chessGameManager.start();
            int gameId = gameDAO.saveGame(chessGameManager);
            return ResponseEntity.ok().body(
                    new CommonDto<>(
                            "새로운 게임이 생성되었습니다.",
                            NewGameResponse.from(chessGameManager, gameId)
                    )
            );
        });
    }

    @PostMapping("/move")
    public ResponseEntity<CommonDto<?>> move(@RequestBody MoveRequest moveRequest) {
        return handleExpectedException(() -> {
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
        });
    }

    @GetMapping("/loadGames")
    public ResponseEntity<CommonDto<?>> loadGames() {
        return handleExpectedException(() -> {
            return ResponseEntity.ok().body(
                    new CommonDto<>(
                            "게임 목록을 불러왔습니다.",
                            new GameListDto(gameDAO.loadGames())
                    )
            );
        });
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<CommonDto<?>> loadGame(@PathVariable int id) {
        return handleExpectedException(() -> {
            ChessGameManager chessGameManager = gameDAO.loadGame(id);
            return ResponseEntity.ok().body(
                    new CommonDto<>(
                            "게임을 불러왔습니다",
                            RunningGameResponse.from(chessGameManager)
                    )
            );
        });
    }
}
