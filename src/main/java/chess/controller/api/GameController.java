package chess.controller.api;

import chess.dto.ChessGameDto;
import chess.dto.GameIdDto;
import chess.dto.GameSettingDto;
import chess.dto.MoveDto;
import chess.dto.PathDto;
import chess.dto.PromotionTypeDto;
import chess.dto.SourceDto;
import chess.model.domain.piece.Team;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public GameIdDto postGame(@PathVariable Integer roomId,
        @RequestBody GameSettingDto gameSettingDto) {
        Map<Team, String> userNames = gameSettingDto.findUserNames();
        String way = gameSettingDto.getWay();
        if (way.equals("new")) {
            return createGame(roomId, userNames);
        }
        if (way.equals("load")) {
            return loadGame(roomId, userNames);
        }
        return null;
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
    public ChessGameDto getBoard(@PathVariable Integer gameId) {
        return chessGameService.loadChessGame(gameId);
    }

    // TODO GET으로 바꾸기
    @PostMapping("/{gameId}/path")
    public PathDto path(@PathVariable Integer gameId, @RequestBody SourceDto sourceDto) {
        return chessGameService.findPath(gameId, sourceDto);
    }

    @PostMapping("/{gameId}/move")
    public ChessGameDto move(@PathVariable Integer gameId, @RequestBody MoveDto MoveDto) {
        ChessGameDto chessGameDto = chessGameService.move(gameId, MoveDto);
        resultService.updateResult(chessGameDto);
        return chessGameDto;
    }

    @PostMapping("/{gameId}/promotion")
    public ChessGameDto promotion(@PathVariable Integer gameId,
        @RequestBody PromotionTypeDto promotionTypeDTO) {
        return chessGameService.promote(gameId, promotionTypeDTO);
    }

    @PostMapping("/{gameId}/end")
    public ChessGameDto end(@PathVariable Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        return new ChessGameDto(chessGameEntity.makeUserNames())
            .teamScore(chessGameEntity.makeTeamScore());
    }
}
