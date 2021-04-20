package chess.controller;

import chess.domain.position.Position;
import chess.dto.CommonResponseBody;
import chess.dto.MoveRequestBody;
import chess.dto.RunningGameDto;
import chess.exception.HandledException;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
public class SpringWebChessController {
    private ChessGameService chessGameService;

    public SpringWebChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    private ResponseEntity<CommonResponseBody<?>> handleExpectedException(Supplier<ResponseEntity<CommonResponseBody<?>>> supplier) {
        try {
            return supplier.get();
        } catch (HandledException e) {
            return ResponseEntity.badRequest().body(
                    new CommonResponseBody<>(
                            e.getMessage()));
        }
    }

    @GetMapping("/newgame")
    public ResponseEntity<CommonResponseBody<?>> newGame() {
        return handleExpectedException(() ->
                ResponseEntity.ok().body(
                        new CommonResponseBody<>(
                                "새로운 게임이 생성되었습니다.",
                                chessGameService.createNewGame()
                        )
                )
        );
    }

    @PostMapping("/move")
    public ResponseEntity<CommonResponseBody<?>> move(@RequestBody MoveRequestBody moveRequestBody) {
        return handleExpectedException(() -> {

            int gameId = moveRequestBody.getGameId();
            Position from = Position.of(moveRequestBody.getFrom());
            Position to = Position.of(moveRequestBody.getTo());

            return ResponseEntity.ok().body(
                    new CommonResponseBody<>(
                            "기물을 이동했습니다.",
                            chessGameService.move(gameId, from, to))
            );
        });
    }

    @GetMapping("/loadGames")
    public ResponseEntity<CommonResponseBody<?>> loadGames() {
        return handleExpectedException(() ->
                ResponseEntity.ok().body(
                        new CommonResponseBody<>(
                                "게임 목록을 불러왔습니다.",
                                chessGameService.loadAllGames()
                        )
                )
        );
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<CommonResponseBody<?>> loadGame(@PathVariable int id) {
        return handleExpectedException(() ->
                ResponseEntity.ok().body(
                        new CommonResponseBody<>(
                                "게임을 불러왔습니다",
                                RunningGameDto.from(chessGameService.loadChessGameByGameId(id))
                        )
                )
        );
    }
}
