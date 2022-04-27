package chess.controller;

import chess.domain.board.Board;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.score.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.request.CreateRoomDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
    private static final String PIECE_NAME_FORMAT = "%s_%s";

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @ResponseBody
    @PostMapping("/rooms")
    public void createRoom(@RequestBody CreateRoomDto createRoomDto) {
        Room room = Room.create(createRoomDto.getTitle(), createRoomDto.getPassword());
        chessService.createGame(room);
    }

    @ResponseBody
    @GetMapping("/rooms/{id}/board")
    public Map<String, String> getBoard(@PathVariable String id) {
        Board board = chessService.getBoard(RoomId.from(id));
        return boardToRaw(board);
    }

    @ResponseBody
    @GetMapping("/rooms/{id}/turn")
    public PieceColorDto getTurn(@PathVariable String id) {
        PieceColor currentTurn = chessService.getCurrentTurn(RoomId.from(id));
        return PieceColorDto.from(currentTurn);
    }

    @ResponseBody
    @GetMapping("/rooms/{id}/score")
    public ScoreResultDto getScore(@PathVariable String id) {
        RoomId roomId = RoomId.from(id);
        Score whiteScore = chessService.getScore(roomId, PieceColor.WHITE);
        Score blackScore = chessService.getScore(roomId, PieceColor.BLACK);
        return ScoreResultDto.of(whiteScore, blackScore);
    }

    @ResponseBody
    @GetMapping("/rooms/{id}/winner")
    public PieceColorDto getWinner(@PathVariable String id) {
        PieceColor winColor = chessService.getWinColor(RoomId.from(id));
        return PieceColorDto.from(winColor);
    }


    @ResponseBody
    @PostMapping("/rooms/{id}/move")
    public CommandResultDto movePiece(@PathVariable String id, @RequestBody MovePieceDto movePieceDto) {
        try {
            chessService.movePiece(RoomId.from(id), movePieceDto.getFromAsPosition(), movePieceDto.getToAsPosition());
            return CommandResultDto.createSuccess();
        } catch (IllegalStateException e) {
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
