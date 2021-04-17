package chess.controller;

import chess.dao.GameDAO;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.*;
import chess.exception.HandledException;
import chess.service.ChessGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
public class SpringWebChessController {
    private GameDAO gameDAO;
    private ChessGameService chessGameService;

    @Autowired
    public SpringWebChessController(GameDAO gameDAO, ChessGameService chessGameService) {
        this.gameDAO = gameDAO;
        this.chessGameService = chessGameService;
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
        return handleExpectedException(() ->
                ResponseEntity.ok().body(
                        new CommonDto<>(
                                "새로운 게임이 생성되었습니다.",
                                chessGameService.createNewGame()
                        )
                )
        );
    }

    @PostMapping("/move")
    public ResponseEntity<CommonDto<?>> move(@RequestBody MoveRequest moveRequest) {
        return handleExpectedException(() -> {

            int gameId = moveRequest.getGameId();
            Position from = Position.of(moveRequest.getFrom());
            Position to = Position.of(moveRequest.getTo());

            return ResponseEntity.ok().body(
                    new CommonDto<>(
                            "기물을 이동했습니다.",
                            chessGameService.move(gameId, from, to))
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
