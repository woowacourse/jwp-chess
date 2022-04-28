package chess.controller.spring;

import chess.domain.ChessGame;
import chess.domain.Result;
import chess.domain.piece.Piece;
import chess.domain.square.Rank;
import chess.dto.CreateGameRequestDto;
import chess.dto.GameRoomEnterDto;
import chess.dto.MoveRequestDto;
import chess.dto.RankDto;
import chess.service.GameService;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{gameId}/play")
    public ModelAndView renderPlayGame(@PathVariable("gameId") final Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        if (chessGame.isEnd()) {
            return new ModelAndView("redirect:" + String.format("/games/%d/result", gameId));
        }

        final Map<String, Object> model = new HashMap<>();
        model.put("turn", chessGame.getTurn());
        model.put("ranks", makeRanksDto(chessGame));
        model.put("gameId", gameId);
        return new ModelAndView("play", model);
    }

    @GetMapping("/{gameId}/result")
    public ModelAndView renderGameResult(@PathVariable("gameId") final Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        final Map<String, Object> model = new HashMap<>();
        model.put("winner", result.getWinner().name());
        model.put("whiteScore", result.getWhiteScore());
        model.put("blackScore", result.getBlackScore());
        return new ModelAndView("result", model);
    }

    @PutMapping("/{gameId}/move")
    @ResponseBody
    public ResponseEntity<Long> movePiece(@PathVariable final Long gameId,
                                          @RequestBody final MoveRequestDto moveRequestDto) {
        gameService.move(gameId, moveRequestDto.getSource(), moveRequestDto.getTarget());
        return ResponseEntity.ok().body(gameId);
    }

    private List<RankDto> makeRanksDto(final ChessGame chessGame) {
        final List<RankDto> ranks = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            final List<Piece> pieces = chessGame.getBoard()
                    .getPiecesByRank(Rank.from(i));
            ranks.add(RankDto.toDto(pieces, i));
        }
        return ranks;
    }

    @GetMapping("/{gameId}/score")
    @ResponseBody
    public ResponseEntity<Result> getGameScore(@PathVariable final Long gameId) {
        final ChessGame chessGame = gameService.findByGameId(gameId);
        return ResponseEntity.ok().body(chessGame.createResult());
    }


    @PutMapping("/{gameId}/terminate")
    @ResponseBody
    public ResponseEntity<Long> terminateGame(@PathVariable final Long gameId) {
        gameService.terminate(gameId);
        return ResponseEntity.ok().body(gameId);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> createGame(@RequestBody final CreateGameRequestDto createGameRequestDto) {
        final Long whiteId = createGameRequestDto.getWhiteId();
        final Long blackId = createGameRequestDto.getBlackId();
        if (whiteId.equals(blackId)) {
            throw new IllegalArgumentException("같은 멤버를 선택해서 게임을 생성할 수 없습니다.");
        }
        final Long gameId = gameService.createGame(
                createGameRequestDto.getTitle(),
                createGameRequestDto.getPassword(),
                createGameRequestDto.getWhiteId(),
                createGameRequestDto.getBlackId()
        );
        return ResponseEntity.created(URI.create("/games/" + gameId)).body(gameId);
    }

    @PostMapping("/password")
    @ResponseBody
    public ResponseEntity<Boolean> checkPassword(@RequestBody GameRoomEnterDto gameRoomEnterDto) {
        final ChessGame game = gameService.findByGameId(gameRoomEnterDto.getGameId());
        if (game.getPassword().equals(gameRoomEnterDto.getPassword())) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @DeleteMapping("/{gameId}")
    @ResponseBody
    public ResponseEntity<Long> deleteGame(@PathVariable Long gameId) {
        final ChessGame game = gameService.findByGameId(gameId);
        if (!game.isEnd()) {
            throw new IllegalStateException("게임이 종료되지 않아서 삭제할 수 없습니다.");
        }
        final Long deletedId = gameService.deleteGame(gameId);
        return ResponseEntity.ok(deletedId);
    }
}
