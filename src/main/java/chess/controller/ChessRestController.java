package chess.controller;

import chess.dto.*;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessRestController {
    private final ChessGameService chessGameService;

    public ChessRestController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/games")
    public ResponseEntity<CommonResponse<NewGameDto>> newGame() {
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        "새로운 게임이 생성되었습니다.",
                        chessGameService.createNewGame()
                )
        );
    }

    @PutMapping("/games/{gameId}/piece")
    public ResponseEntity<CommonResponse<RunningGameDto>> move(@PathVariable int gameId, @RequestBody MoveRequest moveRequest) {
        String from = moveRequest.getFrom();
        String to = moveRequest.getTo();

        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        "기물을 이동했습니다.",
                        chessGameService.move(gameId, from, to)));
    }

    @GetMapping("/games")
    public ResponseEntity<CommonResponse<GameListDto>> loadGames() {
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        "게임 목록을 불러왔습니다.",
                        chessGameService.loadAllGames()
                ));
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<CommonResponse<RunningGameDto>> loadGame(@PathVariable int gameId) {
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        "게임을 불러왔습니다",
                        RunningGameDto.from(chessGameService.loadChessGameByGameId(gameId))
                ));
    }
}
