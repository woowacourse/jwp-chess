package chess.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private static final String WHITE_PIECE_COLOR_NAME = "WHITE";
    private static final String BLACK_PIECE_COLOR_NAME = "BLACK";

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board")
    public ResponseEntity getBoard() {
        BoardDto boardDto = chessService.getBoard(GAME_ID);
        return ResponseEntity.ok(boardDtoToRaw(boardDto));
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

    @GetMapping("/turn")
    public ResponseEntity getTurn() {
        PieceColorDto pieceColorDto = chessService.getCurrentTurn(GAME_ID);
        Map<String, String> responseValue = new HashMap<>();
        responseValue.put("pieceColor", getColorFromPieceColorDto(pieceColorDto));
        return ResponseEntity.ok(responseValue);
    }

    private String getColorFromPieceColorDto(PieceColorDto pieceColorDto) {
        if (pieceColorDto.isWhiteTurn()) {
            return WHITE_PIECE_COLOR_NAME;
        }
        return BLACK_PIECE_COLOR_NAME;
    }

    @GetMapping("/score")
    public ResponseEntity getScore() {
        ScoreResultDto scoreResultDto = chessService.getScore(GAME_ID);
        Map<String, Double> responseValue = new HashMap<>();
        responseValue.put("white", scoreResultDto.getWhiteScore());
        responseValue.put("black", scoreResultDto.getBlackScore());
        return ResponseEntity.ok(responseValue);
    }

    @GetMapping("/winner")
    public ResponseEntity getWinner() {
        PieceColorDto pieceColorDto = chessService.getWinColor(GAME_ID);
        Map<String, String> responseValue = new HashMap<>();
        responseValue.put("pieceColor", getColorFromPieceColorDto(pieceColorDto));
        return ResponseEntity.ok(responseValue);
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

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
