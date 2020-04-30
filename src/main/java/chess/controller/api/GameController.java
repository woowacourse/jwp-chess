package chess.controller.api;

import chess.dto.ChessGameDto;
import chess.dto.GameIdDto;
import chess.dto.GameSettingDto;
import chess.dto.MovableAreasDto;
import chess.dto.MoveDto;
import chess.dto.PromotionTypeDto;
import chess.model.domain.piece.Team;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room/{roomId}/game")
public class GameController {

    private final ChessGameService chessGameService;
    private final ResultService resultService;

    public GameController(ChessGameService chessGameService, ResultService resultService) {
        this.chessGameService = chessGameService;
        this.resultService = resultService;
    }

    @PostMapping("")
    public ResponseEntity<GameIdDto> startGame(@PathVariable Integer roomId,
        @RequestBody GameSettingDto gameSettingDto) {
        Map<Team, String> userNames = gameSettingDto.findUserNames();
        String way = gameSettingDto.getWay();
        if (way.equals("new")) {
            GameIdDto gameIdDto = createGame(roomId, userNames);
            return new ResponseEntity<>(gameIdDto, HttpStatus.OK);
        }
        if (way.equals("load")) {
            GameIdDto gameIdDto = loadGame(roomId, userNames);
            return new ResponseEntity<>(gameIdDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private GameIdDto createGame(Integer roomId, Map<Team, String> userNames) {
        chessGameService.findProceedGameId(roomId).ifPresent(chessGameService::closeGame);
        chessGameService.saveNewUserNames(userNames);
        return new GameIdDto(chessGameService.saveNewGameInfo(userNames, roomId));
    }

    private GameIdDto loadGame(Integer roomId, Map<Team, String> userNames) {
        return new GameIdDto(chessGameService.findProceedGameId(roomId)
            .orElseGet(() -> chessGameService
                .create(roomId, userNames)));
    }

    @GetMapping("/{gameId}/board")
    public ResponseEntity<ChessGameDto> getBoard(@PathVariable Integer gameId) {
        ChessGameDto chessGameDto = chessGameService.loadChessGame(gameId);
        return new ResponseEntity<>(chessGameDto, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/path")
    public ResponseEntity<MovableAreasDto> getMovableArea(@PathVariable Integer gameId,
        @RequestParam String source) {
        MovableAreasDto movableAreasDto = chessGameService.findPath(gameId, source);
        return new ResponseEntity<>(movableAreasDto, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/path")
    public ResponseEntity<ChessGameDto> move(@PathVariable Integer gameId,
        @RequestBody MoveDto MoveDto) {
        ChessGameDto chessGameDto = chessGameService.move(gameId, MoveDto);
        resultService.updateResult(chessGameDto);
        return new ResponseEntity<>(chessGameDto, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/promotion")
    public ResponseEntity<ChessGameDto> promotion(@PathVariable Integer gameId,
        @RequestBody PromotionTypeDto promotionTypeDTO) {
        ChessGameDto chessGameDto = chessGameService.promote(gameId, promotionTypeDTO);
        return new ResponseEntity<>(chessGameDto, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/end")
    public ResponseEntity<ChessGameDto> end(@PathVariable Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        ChessGameDto chessGameDto = new ChessGameDto(chessGameEntity.makeUserNames())
            .teamScore(chessGameEntity.makeTeamScore());
        return new ResponseEntity<>(chessGameDto, HttpStatus.OK);
    }
}
