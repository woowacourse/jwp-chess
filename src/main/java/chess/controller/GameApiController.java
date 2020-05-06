package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.MoveDto;
import chess.dto.PathDto;
import chess.dto.PromotionTypeDto;
import chess.dto.SourceDto;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameApiController {

    private final ChessGameService chessGameService;
    private final ResultService resultService;

    public GameApiController(ChessGameService chessGameService, ResultService resultService) {
        this.chessGameService = chessGameService;
        this.resultService = resultService;
    }

    @PostMapping("/{gameId}/board")
    public ChessGameDto board(@PathVariable Integer gameId) {
        return chessGameService.loadChessGame(gameId);
    }

    @PostMapping("/move")
    public ChessGameDto move(@RequestBody MoveDto MoveDto) {
        ChessGameDto chessGameDto = chessGameService.move(MoveDto);
        resultService.updateResult(chessGameDto);
        return chessGameDto;
    }

    @PostMapping("/path")
    public PathDto path(@RequestBody SourceDto sourceDto) {
        return chessGameService.findPath(sourceDto);
    }

    @PostMapping("/promotion")
    public ChessGameDto promotion(@RequestBody PromotionTypeDto promotionTypeDTO) {
        return chessGameService.promote(promotionTypeDTO);
    }

    @PostMapping("/{gameId}/end")
    public ChessGameDto end(@PathVariable Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        return new ChessGameDto(chessGameEntity.makeTeamScore(), chessGameEntity.makeUserNames());
    }

}
