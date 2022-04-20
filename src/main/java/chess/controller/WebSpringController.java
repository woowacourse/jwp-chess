package chess.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chess.dao.BoardDaoImpl;
import chess.dao.GameDaoImpl;
import chess.domain.position.Position;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.ScoreResultDto;
import chess.service.ChessService;
import chess.util.BodyParser;
import chess.util.JsonMapper;

@RestController
public class WebSpringController {
    private static final String GAME_ID = "game-id"; // TODO: 여러 게임 방 기능 구현시 제거

    private final ChessService chessService;

    public WebSpringController() {
        this.chessService = ChessService.of(new GameDaoImpl(), new BoardDaoImpl());
    }

    @GetMapping("/board")
    public String getBoard() {
        try {
            BoardDto boardDto = chessService.getBoard(GAME_ID);
            return JsonMapper.boardDtoToJson(boardDto);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return JsonMapper.exceptionToJson(e);
        }
    }

    @GetMapping("/turn")
    public String getTurn() {
        try {
            PieceColorDto pieceColorDto = chessService.getCurrentTurn(GAME_ID);
            return JsonMapper.turnToJson(pieceColorDto);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return JsonMapper.exceptionToJson(e);
        }
    }

    @GetMapping("/score")
    public String getScore() {
        try {
            ScoreResultDto scoreResultDto = chessService.getScore(GAME_ID);
            return JsonMapper.scoreResultDtoToJson(scoreResultDto);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return JsonMapper.exceptionToJson(e);
        }
    }

    @GetMapping("/winner")
    public String getWinner() {
        try {
            return JsonMapper.winnerToJson(chessService.getWinColor(GAME_ID));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return JsonMapper.exceptionToJson(e);
        }
    }

    @PostMapping("/move")
    public String movePiece(@RequestBody String req) {
        try {
            Map<String, String> moveRequest = BodyParser.parseToMap(req);

            Position from = Position.from(moveRequest.get("from"));
            Position to = Position.from(moveRequest.get("to"));

            chessService.movePiece(UpdatePiecePositionDto.of(GAME_ID, from, to));
        } catch (IllegalStateException e) {
            return e.getMessage();
        }

        return "success";
    }

    // TODO: Exception 으로 catch 하면 안됨
    @PostMapping("/initialize")
    public String initialize() {
        try {
            chessService.initializeGame(GAME_ID);
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

}
