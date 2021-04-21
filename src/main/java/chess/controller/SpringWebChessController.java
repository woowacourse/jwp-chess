package chess.controller;

import chess.domain.position.Position;
import chess.dto.*;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebChessController {
    private final ChessGameService chessGameService;

    public SpringWebChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/newgame")
    public ResponseEntity<CommonResponseBody<NewGameDto>> newGame() {
        return ResponseEntity.ok().body(
                new CommonResponseBody<>(
                        "새로운 게임이 생성되었습니다.",
                        chessGameService.createNewGame()
                )
        );
    }

    @PostMapping("/move")
    public ResponseEntity<CommonResponseBody<RunningGameDto>> move(@RequestBody MoveRequestBody moveRequestBody) {
        int gameId = moveRequestBody.getGameId();
        Position from = Position.of(moveRequestBody.getFrom());
        Position to = Position.of(moveRequestBody.getTo());

        return ResponseEntity.ok().body(
                new CommonResponseBody<>(
                        "기물을 이동했습니다.",
                        chessGameService.move(gameId, from, to)));
    }

    @GetMapping("/loadGames")
    public ResponseEntity<CommonResponseBody<GameListDto>> loadGames() {
        return ResponseEntity.ok().body(
                new CommonResponseBody<>(
                        "게임 목록을 불러왔습니다.",
                        chessGameService.loadAllGames()
                ));
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<CommonResponseBody<RunningGameDto>> loadGame(@PathVariable int id) {
        return ResponseEntity.ok().body(
                new CommonResponseBody<>(
                        "게임을 불러왔습니다",
                        RunningGameDto.from(chessGameService.loadChessGameByGameId(id))
                ));
    }
}
