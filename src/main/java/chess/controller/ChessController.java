package chess.controller;

import chess.domain.board.Board;
import chess.domain.game.GameId;
import chess.domain.game.score.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.request.MovePieceDto;
import chess.dto.response.CommandResultDto;
import chess.dto.response.ErrorDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.ScoreResultDto;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
    private static final GameId GAME_ID = GameId.from("game-id"); // TODO: 여러 게임 방 기능 구현시 제거
    private static final String PIECE_NAME_FORMAT = "%s_%s";

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @ResponseBody
    @GetMapping("/board")
    public Map<String, String> getBoard() {
        Board board = chessService.getBoard(GAME_ID);
        return boardToRaw(board);
    }

    @ResponseBody
    @GetMapping("/turn")
    public PieceColorDto getTurn() {
        PieceColor currentTurn = chessService.getCurrentTurn(GAME_ID);
        return PieceColorDto.from(currentTurn);
    }

    @ResponseBody
    @GetMapping("/score")
    public ScoreResultDto getScore() {
        Score whiteScore = chessService.getScore(GAME_ID, PieceColor.WHITE);
        Score blackScore = chessService.getScore(GAME_ID, PieceColor.BLACK);
        return ScoreResultDto.of(whiteScore, blackScore);
    }

    @ResponseBody
    @GetMapping("/winner")
    public PieceColorDto getWinner() {
        PieceColor winColor = chessService.getWinColor(GAME_ID);
        return PieceColorDto.from(winColor);
    }

    @ResponseBody
    @PostMapping("/move")
    public CommandResultDto movePiece(@RequestBody MovePieceDto movePieceDto) {
        try {
            chessService.movePiece(GAME_ID, movePieceDto.getFromAsPosition(), movePieceDto.getToAsPosition());
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

    private Map<String, String> boardToRaw(Board board) {
        Map<String, String> coordinateAndPiece = new HashMap<>();
        for (Map.Entry<Position, Piece> entrySet : board.getValue().entrySet()) {
            String coordinate = entrySet.getKey().toCoordinate();
            String piece = generatePieceName(entrySet.getValue());
            coordinateAndPiece.put(coordinate, piece);
        }

        return coordinateAndPiece;
    }

    private String generatePieceName(Piece piece) {
        String pieceName = piece.getPieceType().name();
        String pieceColorName = piece.getPieceColor().name();
        return String.format(PIECE_NAME_FORMAT, pieceName, pieceColorName);
    }
}
