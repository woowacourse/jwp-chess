package chess.web.controller;

import chess.board.Board;
import chess.web.service.ChessService;
import chess.web.service.dto.BoardDto;
import chess.web.service.dto.CreateRoomDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.net.URI;
import java.util.NoSuchElementException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessWebController {

    private final ChessService chessService;

    public ChessWebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rooms", chessService.getRooms());
        return "index";
    }

    @PostMapping("/chess")
    public ResponseEntity<Long> createChess(@RequestBody CreateRoomDto createRoomDto) {
        Long boardId = chessService.createGame();
        Long newId = chessService.createRoom(boardId, createRoomDto.getTitle(), createRoomDto.getPassword());
        return ResponseEntity.created(URI.create("/chess/" + boardId)).body(newId);
    }

    @GetMapping("/chess/{boardId}")
    public String chess(@PathVariable Long boardId) {
        try {
            chessService.loadGame(boardId);
        } catch (NoSuchElementException exception) {
            return "nochess";
        }
        return "chess";
    }

    @GetMapping(value = "/chess/{boardId}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long boardId) {
        Board board = chessService.loadGame(boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/chess/{boardId}/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long boardId) {
        Board board = chessService.initBoard(boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/chess/{boardId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long boardId) {
        ScoreDto status = chessService.getStatus(boardId);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping(value = "/chess/{boardId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long boardId) {
        Board board = chessService.move(moveDto, boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @DeleteMapping("/chess/{boardId}")
    public ResponseEntity<Boolean> deleteRoom(@RequestBody String password, @PathVariable Long boardId) {
        boolean result = chessService.removeRoom(boardId, password);
        return ResponseEntity.ok().body(result);
    }
}
