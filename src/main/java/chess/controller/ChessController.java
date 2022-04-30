package chess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chess.dto.request.CreateRoomDto;
import chess.dto.request.DeleteRoomDto;
import chess.dto.request.MovePieceDto;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;
import chess.dto.response.CommandResultDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.PieceDto;
import chess.dto.response.PositionDto;
import chess.dto.response.RoomDto;
import chess.dto.response.ScoreResultDto;
import chess.service.ChessService;

@RestController
public class ChessController {
    private static final String PIECE_NAME_FORMAT = "%s_%s";
    private static final String WHITE_PIECE_COLOR_NAME = "WHITE";
    private static final String BLACK_PIECE_COLOR_NAME = "BLACK";

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board/{id}")
    public Map<String, String> getBoard(@PathVariable Integer id) {
        BoardDto boardDto = chessService.getBoard(id);
        return boardDtoToRaw(boardDto);
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

    @GetMapping("/turn/{id}")
    public Map<String, String> getTurn(@PathVariable Integer id) {
        PieceColorDto pieceColorDto = chessService.getCurrentTurn(id);
        Map<String, String> responseValue = new HashMap<>();
        responseValue.put("pieceColor", getColorFromPieceColorDto(pieceColorDto));
        return responseValue;
    }

    private String getColorFromPieceColorDto(PieceColorDto pieceColorDto) {
        if (pieceColorDto.isWhiteTurn()) {
            return WHITE_PIECE_COLOR_NAME;
        }
        return BLACK_PIECE_COLOR_NAME;
    }

    @GetMapping("/score/{id}")
    public Map<String, Double> getScore(@PathVariable Integer id) {
        ScoreResultDto scoreResultDto = chessService.getScore(id);
        Map<String, Double> responseValue = new HashMap<>();
        responseValue.put("white", scoreResultDto.getWhiteScore());
        responseValue.put("black", scoreResultDto.getBlackScore());
        return responseValue;
    }

    @GetMapping("/winner/{id}")
    public Map<String, String> getWinner(@PathVariable Integer id) {
        PieceColorDto pieceColorDto = chessService.getWinColor(id);
        Map<String, String> responseValue = new HashMap<>();
        responseValue.put("pieceColor", getColorFromPieceColorDto(pieceColorDto));
        return responseValue;
    }

    @PostMapping("/move/{id}")
    public CommandResultDto movePiece(@RequestBody MovePieceDto movePieceDto, @PathVariable Integer id) {
        try {
            chessService.movePiece(
                UpdatePiecePositionDto.of(id, movePieceDto.getFromAsPosition(), movePieceDto.getToAsPosition()));
        } catch (IllegalStateException e) {
            return CommandResultDto.of(false, e.getMessage());
        }

        return CommandResultDto.of(true, "성공하였습니다.");
    }

    @PostMapping("/room")
    public RoomDto createRoom(@RequestBody CreateRoomDto createRoomDto) {
        String gameName = createRoomDto.getName();
        String gamePassword = createRoomDto.getPassword();
        RoomDto roomDto = new RoomDto(chessService.createGameAndGetId(gameName, gamePassword), gameName);
        return roomDto;
    }

    @GetMapping("/room")
    public List<RoomDto> inquireRooms() {
        List<RoomDto> rooms = chessService.getRooms();
        return rooms;
    }

    @DeleteMapping("/room")
    public CommandResultDto deleteRoom(@RequestBody DeleteRoomDto deleteRoomDto) {
        int gameId = deleteRoomDto.getId();
        String inputPassword = deleteRoomDto.getPassword();

        try {
            chessService.deleteRoom(gameId, inputPassword);
        } catch (IllegalArgumentException e) {
            return CommandResultDto.of(false, e.getMessage());
        }
        return CommandResultDto.of(true, "삭제되었습니다!");
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String handle(RuntimeException e) {
        return e.getMessage();
    }
}
