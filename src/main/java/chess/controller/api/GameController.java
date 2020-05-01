package chess.controller.api;

import chess.dto.repository.GameIdDto;
import chess.dto.repository.MovableAreasDto;
import chess.dto.view.GameInformationDto;
import chess.dto.view.MoveDto;
import chess.dto.view.PromotionTypeDto;
import chess.dto.view.userNamesDto;
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

    @PostMapping("/new")
    public ResponseEntity<GameIdDto> newGame(@PathVariable Integer roomId,
        @RequestBody userNamesDto userNamesDto) {
        Map<Team, String> userNames = userNamesDto.findUserNames();
        chessGameService.findProceedGameId(roomId).ifPresent(chessGameService::closeGame);
        chessGameService.saveNewUserNames(userNames);
        GameIdDto gameIdDto = new GameIdDto(chessGameService.saveNewGameInfo(userNames, roomId));
        return new ResponseEntity<>(gameIdDto, HttpStatus.OK);
    }

    @PostMapping("/load")
    public ResponseEntity<GameIdDto> loadGame(@PathVariable Integer roomId,
        @RequestBody userNamesDto userNamesDto) {
        Map<Team, String> userNames = userNamesDto.findUserNames();
        GameIdDto gameIdDto = new GameIdDto(chessGameService.findProceedGameId(roomId)
            .orElseGet(() -> chessGameService
                .create(roomId, userNames)));
        return new ResponseEntity<>(gameIdDto, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/board")
    public ResponseEntity<GameInformationDto> getBoard(@PathVariable Integer gameId) {
        GameInformationDto gameInformationDto = chessGameService.loadChessGame(gameId);
        return new ResponseEntity<>(gameInformationDto, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/path")
    public ResponseEntity<MovableAreasDto> getMovableArea(@PathVariable Integer gameId,
        @RequestParam String source) {
        MovableAreasDto movableAreasDto = chessGameService.findPath(gameId, source);
        return new ResponseEntity<>(movableAreasDto, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/path")
    public ResponseEntity<GameInformationDto> move(@PathVariable Integer gameId,
        @RequestBody MoveDto MoveDto) {
        GameInformationDto gameInformationDto = chessGameService.move(gameId, MoveDto);
        resultService.updateResult(gameInformationDto);
        return new ResponseEntity<>(gameInformationDto, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/promotion")
    public ResponseEntity<GameInformationDto> promotion(@PathVariable Integer gameId,
        @RequestBody PromotionTypeDto promotionTypeDTO) {
        GameInformationDto gameInformationDto = chessGameService.promote(gameId, promotionTypeDTO);
        return new ResponseEntity<>(gameInformationDto, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/end")
    public ResponseEntity<GameInformationDto> end(@PathVariable Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        GameInformationDto gameInformationDto = new GameInformationDto(
            chessGameEntity.makeUserNames())
            .teamScore(chessGameEntity.makeTeamScore());
        return new ResponseEntity<>(gameInformationDto, HttpStatus.OK);
    }
}
