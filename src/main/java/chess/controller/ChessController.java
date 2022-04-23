package chess.controller;

import chess.dto.response.ErrorDto;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import chess.dto.response.PieceDto;
import chess.dto.response.PositionDto;
import chess.dto.response.ScoreResultDto;
import chess.service.ChessService;

@RestController
public class ChessController {
    private static final String GAME_ID = "game-id"; // TODO: 여러 게임 방 기능 구현시 제거
    private static final String PIECE_NAME_FORMAT = "%s_%s";

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @ResponseBody
    @GetMapping("/board")
    public Map<String, String> getBoard() {
        BoardDto boardDto = chessService.getBoard(GAME_ID);
        return boardDtoToRaw(boardDto);
    }

    @ResponseBody
    @GetMapping("/turn")
    public PieceColorDto getTurn() {
        return chessService.getCurrentTurn(GAME_ID);
    }

    @ResponseBody
    @GetMapping("/score")
    public ScoreResultDto getScore() {
        return chessService.getScore(GAME_ID);
    }

    @ResponseBody
    @GetMapping("/winner")
    public PieceColorDto getWinner() {
        return chessService.getWinColor(GAME_ID);
    }

    @ResponseBody
    @PostMapping("/move")
    public CommandResultDto movePiece(@RequestBody MovePieceDto movePieceDto) {
        try {
            chessService.movePiece(
                UpdatePiecePositionDto.of(GAME_ID, movePieceDto.getFromAsPosition(), movePieceDto.getToAsPosition()));
            return CommandResultDto.createSuccess();
        } catch (IllegalStateException e) {
            return CommandResultDto.createFail(e.getMessage());
        }
    }

    // TODO: Exception 으로 catch 하면 안됨
    @ResponseBody
    @PostMapping("/initialize")
    public CommandResultDto initialize() {
        try {
            chessService.initializeGame(GAME_ID);
            return CommandResultDto.createSuccess();
        } catch (Exception e) {
            return CommandResultDto.createFail(e.getMessage());
        }
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ErrorDto handleException(RuntimeException e) {
        return new ErrorDto(e.getMessage());
    }

    private Map<String, String> boardDtoToRaw(BoardDto boardDto) {
        Map<String, String> coordinateAndPiece = new HashMap<>();
        for (Map.Entry<PositionDto, PieceDto> entrySet : boardDto.getValue().entrySet()) {
            String coordinate = entrySet.getKey().toPosition().toCoordinate();
            String piece = generatePieceName(entrySet.getValue());
            coordinateAndPiece.put(coordinate, piece);
        }

        return coordinateAndPiece;
    }

    private String generatePieceName(PieceDto pieceDto) {
        String pieceName = pieceDto.getPieceType().name();
        String pieceColorName = pieceDto.getPieceColor().name();
        return String.format(PIECE_NAME_FORMAT, pieceName, pieceColorName);
    }
}
