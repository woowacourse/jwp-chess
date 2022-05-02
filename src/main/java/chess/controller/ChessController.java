package chess.controller;

import chess.dto.request.CreateRoomDto;
import chess.dto.request.MovePieceDto;
import chess.dto.response.BoardDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.RoomDto;
import chess.dto.response.ScoreResultDto;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChessController {

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/rooms")
    public RoomDto createRoom(@RequestBody CreateRoomDto createRoomDto) {
        return chessService.createRoom(createRoomDto);
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@PathVariable String id, @RequestParam String password) {
        chessService.deleteRoom(id, password);
    }

    @GetMapping("/rooms")
    public List<RoomDto> getRooms() {
        return chessService.getRooms()
                .stream()
                .map(RoomDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/rooms/{roomId}/board")
    public Map<String, String> getBoard(@PathVariable String roomId) {
        BoardDto board = chessService.getBoard(roomId);
        return board.getValue();
    }

    @GetMapping("/rooms/{id}/turn")
    public PieceColorDto getTurn(@PathVariable String id) {
        return chessService.getCurrentTurn(id);
    }

    @GetMapping("/rooms/{id}/score")
    public ScoreResultDto getScore(@PathVariable String id) {
        return chessService.getScore(id);
    }

    @GetMapping("/rooms/{id}/winner")
    public PieceColorDto getWinner(@PathVariable String id) {
        return chessService.getWinColor(id);
    }

    @PostMapping("/rooms/{id}/move")
    public void movePiece(@PathVariable String id, @RequestBody MovePieceDto movePieceDto) {
        chessService.movePiece(id, movePieceDto);
    }
}
