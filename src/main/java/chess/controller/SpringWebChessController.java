package chess.controller;

import chess.dao.GameDAO;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.CommonDto;
import chess.dto.MoveRequest;
import chess.dto.NewGameResponse;
import chess.dto.RunningGameResponse;
import chess.exception.HandledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
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
            try {
                ChessGameManager chessGameManager = new ChessGameManager();
                chessGameManager.start();
                int gameId = gameDAO.saveGame(chessGameManager);
                return ResponseEntity.ok().body(
                        new CommonDto<>(
                                "새로운 게임이 생성되었습니다.",
                                NewGameResponse.from(chessGameManager, gameId)
                        )
                );
            } catch (SQLException e) {  // todo try ~ catch 문 코드 중복 해결
                return ResponseEntity.badRequest().body(
                        new CommonDto<>(
                                e.getMessage())
                );
            }
        });
    }

    @PostMapping("/move")
    public ResponseEntity<CommonDto<?>> move(@RequestBody MoveRequest moveRequest) {
        return handleExpectedException(() -> {
            try {
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
            } catch (SQLException e) {
                return ResponseEntity.badRequest().body(
                        new CommonDto<>(e.getMessage())
                );
            }
        });
    }
}
