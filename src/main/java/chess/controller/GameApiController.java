package chess.controller;

import chess.model.dto.ChessGameDto;
import chess.model.dto.MoveDto;
import chess.model.dto.PathDto;
import chess.model.dto.PromotionTypeDto;
import chess.model.dto.SourceDto;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameApiController {
    private static final Gson GSON = new Gson();

    private final ChessGameService chessGameService;
    private final ResultService resultService;

    public GameApiController(ChessGameService chessGameService, ResultService resultService) {
        this.chessGameService = chessGameService;
        this.resultService = resultService;
    }

    @PostMapping("/board")
    public ChessGameDto board(@RequestBody String req) {
        JsonObject body = JsonParser.parseString(req).getAsJsonObject();
        Integer gameId = GSON.fromJson(body.get("gameId"), Integer.class);

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

    @PostMapping("/end")
    public ChessGameDto end(@RequestBody String req) {
        JsonObject body = JsonParser.parseString(req).getAsJsonObject();
        Integer gameId = GSON.fromJson(body.get("gameId"), Integer.class);

        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        return new ChessGameDto(chessGameEntity.makeTeamScore(), chessGameEntity.makeUserNames());
    }
}
