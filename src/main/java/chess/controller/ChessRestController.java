package chess.controller;

import chess.dto.*;
import chess.service.ChessGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessRestController {
    private final ChessGameService chessGameService;

    public ChessRestController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/games")
    public ResponseEntity<CommonResponse<NewGameDto>> newGame(@RequestBody CreateGameRequest createGameRequest) {
        return new ResponseEntity<>(
                new CommonResponse<>(
                        "새로운 게임이 생성되었습니다.",
                        chessGameService.createNewGame(createGameRequest.getTitle())
                ), HttpStatus.CREATED);
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<CommonResponse<RunningGameDto>> loadGame(@PathVariable long gameId) {
        return ResponseEntity.ok(
                new CommonResponse<>(
                        "게임을 불러왔습니다",
                        RunningGameDto.from(chessGameService.loadChessGameByGameId(gameId))
                ));
    }

    @PutMapping("/games/{gameId}/pieces")
    public ResponseEntity<CommonResponse<RunningGameDto>> move(@PathVariable long gameId, @RequestBody MoveRequest moveRequest) {
        String from = moveRequest.getFrom();
        String to = moveRequest.getTo();

        return ResponseEntity.ok(
                new CommonResponse<>(
                        "기물을 이동했습니다.",
                        chessGameService.move(gameId, from, to)));
    }

    @GetMapping("/games")
    public ResponseEntity<CommonResponse<GameListDto>> loadGames() {
        return ResponseEntity.ok(
                new CommonResponse<>(
                        "게임 목록을 불러왔습니다.",
                        chessGameService.loadAllGames()
                ));
    }
}
