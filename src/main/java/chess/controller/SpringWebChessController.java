package chess.controller;

import chess.dao.GameDAO;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.*;
import chess.exception.HandledException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.function.Supplier;

@RestController
public class SpringWebChessController {
    private final ChessGameManager chessGameManager = new ChessGameManager();
    private final GameDAO gameDAO = new GameDAO();

    private CommonDto<?> handleExpectedException(Supplier<CommonDto<?>> supplier, int statusCodeOnError) {
        try {
            return supplier.get();
        } catch (HandledException e) {
            return new CommonDto<>(
                    statusCodeOnError,
                    e.getMessage());
        }
    }

    @GetMapping("/newgame")
    public ResponseEntity<CommonDto<?>> newGame() {
        return ResponseEntity.ok().body(
                handleExpectedException(() -> {
                    chessGameManager.start();
                    return new CommonDto<>(StatusCode.OK,
                            "게임을 생성했습니다",
                            NewGameResponse.from(chessGameManager));
                }, StatusCode.BAD_REQUEST)
        );
    }

    @PostMapping("/move")
    public ResponseEntity<CommonDto<?>> move(@RequestBody MoveRequest moveRequest) {
        return ResponseEntity.ok().body(
                handleExpectedException(() -> {
                    String from = moveRequest.getFrom();
                    String to = moveRequest.getTo();

                    chessGameManager.move(Position.of(from), Position.of(to));

                    return new CommonDto<>(
                            StatusCode.OK,
                            "기물을 이동했습니다.",
                            RunningGameResponse.from(chessGameManager));
                }, StatusCode.BAD_REQUEST)
        );
    }

    @GetMapping("/save")
    private ResponseEntity<CommonDto<?>> saveGame() {
        return ResponseEntity.ok().body(handleExpectedException(() -> {
            try {
                gameDAO.saveGame(ChessBoardDto.from(chessGameManager.getBoard()), chessGameManager.getCurrentTurnColor());
                return new CommonDto<>(
                        StatusCode.CREATE,
                        "게임을 저장했습니다.");
            } catch (SQLException e) {
                return new CommonDto<>(
                        StatusCode.INTERNAL_SERVER_ERROR,
                        "알 수 없는 서버 내부 오류입니다. 에러 메세지: " + e.getMessage());
            }
        }, StatusCode.BAD_REQUEST));
    }

    @GetMapping("/load")
    private ResponseEntity<CommonDto<?>> loadGame() {
        return ResponseEntity.ok().body(handleExpectedException(() -> {
            try {
                SavedGameData savedGameData = gameDAO.loadGame();
                chessGameManager.load(savedGameData.getChessBoardDto().toChessBoard(), savedGameData.getCurrentTurnColor());
                return new CommonDto<>(
                        StatusCode.OK,
                        "게임을 불러왔습니다.",
                        RunningGameResponse.from(chessGameManager));
            } catch (SQLException e) {
                return new CommonDto<>(
                        StatusCode.INTERNAL_SERVER_ERROR,
                        "알 수 없는 서버 내부 오류입니다. 에러 메세지: " + e.getMessage());
            }
        }, StatusCode.BAD_REQUEST));
    }
}
