package chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import chess.dto.request.MovePieceDto;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;
import chess.dto.response.CommandResultDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.ScoreResultDto;
import chess.service.ChessService;
import chess.util.JsonMapper;

@RestController
public class WebSpringController {
    private static final String GAME_ID = "game-id"; // TODO: 여러 게임 방 기능 구현시 제거

    private final ChessService chessService;

    @Autowired
    public WebSpringController(ChessService chessService) {
        this.chessService = chessService;
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

    @ResponseBody
    @PostMapping("/move")
    public CommandResultDto movePiece(@RequestBody MovePieceDto movePieceDto) {
        try {
            chessService.movePiece(
                UpdatePiecePositionDto.of(GAME_ID, movePieceDto.getFromAsPosition(), movePieceDto.getToAsPosition()));
        } catch (IllegalStateException e) {
            return CommandResultDto.of(false, e.getMessage());
        }

        return CommandResultDto.of(true, "성공하였습니다.");
    }

    // TODO: Exception 으로 catch 하면 안됨
    @ResponseBody
    @PostMapping("/initialize")
    public CommandResultDto initialize() {
        try {
            chessService.initializeGame(GAME_ID);
        } catch (Exception e) {
            return CommandResultDto.of(false, e.getMessage());
        }
        return CommandResultDto.of(true, "성공하였습니다.");
    }

}
